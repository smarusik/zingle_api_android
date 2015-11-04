package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.RequestDTO;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZingleBaseModel;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleSortOrder;

import static me.zingle.api.sdk.dao.RequestMethods.DELETE;
import static me.zingle.api.sdk.dao.RequestMethods.GET;
import static me.zingle.api.sdk.dao.RequestMethods.POST;
import static me.zingle.api.sdk.dao.RequestMethods.PUT;

/**
 * Created by SLAVA 09 2015.
 * Base class for Zingle services
 */
public abstract class ZingleBaseService<Model extends ZingleBaseModel> {

    protected ServiceDelegate<ZingleList<Model>> listDelegate;
    protected ServiceDelegate<Model> getDelegate;
    protected ServiceDelegate<Boolean> deleteDelegate;
    protected ServiceDelegate<Model> createDelegate;
    protected ServiceDelegate<Model> updateDelegate;

    public void setListDelegate(ServiceDelegate<ZingleList<Model>> listDelegate) {
        this.listDelegate = listDelegate;
    }

    public void setGetDelegate(ServiceDelegate<Model> getDelegate) {
        this.getDelegate = getDelegate;
    }

    public void setDeleteDelegate(ServiceDelegate<Boolean> deleteDelegate) {
        this.deleteDelegate = deleteDelegate;
    }

    public void setCreateDelegate(ServiceDelegate<Model> createDelegate) {
        this.createDelegate = createDelegate;
    }

    public void setUpdateDelegate(ServiceDelegate<Model> updateDelegate) {
        this.updateDelegate = updateDelegate;
    }

    /**
     * @param specific if true, must return path with specific id
     * @return part of the path to resourse after API version
     */
    protected abstract String resourcePath(boolean specific);

    protected abstract boolean checkModifier(String modifier);

    /**
     * @param keys coma separated list of filter keys
     * @param values array of filter expressions
     * @return list of conditions to use in ZingleQuery
     */
    public List<QueryPart> createConditions(String keys,String ...values){

        String[] keyList=keys.split(",");

        List<QueryPart> result=new ArrayList<>();

        int length=keyList.length>=values.length?values.length:keyList.length;

        for(int i=0;i<length;i++){
            if(checkModifier(keyList[i]))
                result.add(new QueryPart(keyList[i].trim(),values[i]));
        }
        return result;
    }

//Mappers
    public abstract Model mapper(JSONObject source) throws MappingErrorEx;

    public ZingleList<Model> arrayMapper(JSONObject source) throws MappingErrorEx{
        try {
            ZingleList<Model> retList = new ZingleList<>();

            JSONObject status= null;
            try {
                status = source.getJSONObject("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            retList.sortField=status.optString("sort_field");

            String sortDir=status.optString("sort_direction");
            if(sortDir!=null)
                if(sortDir.equals(ZingleSortOrder.ZINGLE_ASC.toString()))
                    retList.sortDirection=ZingleSortOrder.ZINGLE_ASC;
                else if(sortDir.equals(ZingleSortOrder.ZINGLE_DESC.toString()))
                    retList.sortDirection=ZingleSortOrder.ZINGLE_DESC;
                else
                    retList.sortDirection=null;
            else
                retList.sortDirection=null;

            retList.page=status.optInt("page");
            retList.pageSize=status.optInt("page_size");
            retList.totalPages=status.optInt("total_pages");
            retList.totalRecords=status.optInt("total_records");

            JSONArray result= null;
            try {
                result = source.getJSONArray("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int i = 0;
            JSONObject temp = result.optJSONObject(i++);

            while (temp != null) {
                retList.objects.add(mapper(temp));
                temp = result.optJSONObject(i++);
            }

            return retList;
        }catch(MappingErrorEx e){
            e.setMappedSource(source.toString());
            throw e;
        }
    }

    public List<Model> arrayMapper(JSONArray source) throws MappingErrorEx{
        try {
            List<Model> retList = new ArrayList<>();

            for (int i=0; i<source.length(); i++) {
                JSONObject temp = source.optJSONObject(i);
                retList.add(mapper(temp));
            }

            return retList;
        }catch(MappingErrorEx e){
            e.setMappedSource(source.toString());
            throw e;
        }
    }
//Getting by ID methods
    public Model get(String id){
        ZingleQuery query = new ZingleQuery(GET, String.format(resourcePath(true),id));

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result= null;
            try {
                result = response.getData().getJSONObject("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapper(result);
        }
        else
            throw new UnsuccessfullRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
    }

    public boolean getAsync(final String id,final ServiceDelegate<Model> delegate){
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Model result = get(id);
                    delegate.processResult(result);
                } catch (UnsuccessfullRequestEx e) {
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();
        return true;
    }

    public boolean getAsync(final String id){
        synchronized (getDelegate) {
            if (getDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return getAsync(id, getDelegate);
        }
    }

//Listing methods
    public ZingleList<Model> list(){
        ZingleQuery query = new ZingleQuery(GET, resourcePath(false));

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return arrayMapper(response.getData());
        }
        else
            throw new UnsuccessfullRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
    }

    public boolean listAsync(final ServiceDelegate<ZingleList<Model>> delegate){
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ZingleList<Model> result = list();
                    delegate.processResult(result);
                } catch (UnsuccessfullRequestEx e) {
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();
        return true;
    }

    public boolean listAsync(){
        synchronized (listDelegate) {
            if (listDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return listAsync(listDelegate);
        }
    }

    public ZingleList<Model> list(List<QueryPart> conditions){
        ZingleQuery query = new ZingleQuery(GET, resourcePath(false));
        query.setParams(conditions);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return arrayMapper(response.getData());
        }
        else
            throw new UnsuccessfullRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
    }

    public boolean listAsync(final List<QueryPart> conditions,final ServiceDelegate<ZingleList<Model>> delegate){
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ZingleList<Model> result=list(conditions);
                    delegate.processResult(result);
                }catch (UnsuccessfullRequestEx e){
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public boolean listAsync(final List<QueryPart> conditions){
        synchronized (listDelegate){
            if(listDelegate==null){
                throw new UndefinedServiceDelegateEx();
            }

            return listAsync(conditions,listDelegate);
        }
    }

//Deleting by ID methods
    public Boolean delete(String id){
        ZingleQuery query = new ZingleQuery(DELETE, String.format(resourcePath(true), id));

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return true;
        }
        else
            throw new UnsuccessfullRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
    }

    public boolean deleteAsync(final String id,final ServiceDelegate<Boolean> delegate){
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    delegate.processResult(delete(id));
                } catch (UnsuccessfullRequestEx e) {
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();
        return true;
    }

    public boolean deleteAsync(final String id){
        synchronized (deleteDelegate) {
            if (deleteDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return deleteAsync(id, deleteDelegate);
        }
    }



//Creating methods
    public Model create(Model object){

        ZingleQuery query = new ZingleQuery(POST, resourcePath(false));

        RequestDTO payload=new RequestDTO();
        object.checkForCreate();
        payload.setData(object.extractCreationData());
        query.setPayload(payload);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result= null;
            try {
                result = response.getData().getJSONObject("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapper(result);
        }
        else
            throw new UnsuccessfullRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
    }

    public boolean createAsync(final Model object,final ServiceDelegate<Model> delegate){
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Model result=create(object);
                    delegate.processResult(result);
                }catch (UnsuccessfullRequestEx e){
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

    public boolean createAsync(final Model object){
        synchronized (createDelegate) {
            if (createDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return createAsync(object, createDelegate);
        }
    }

//Updating methods
public Model update(Model object){

    ZingleQuery query = new ZingleQuery(PUT, String.format(resourcePath(true),object.getId()));

    RequestDTO payload=new RequestDTO();
    object.checkForUpdate();
    payload.setData(object.extractUpdateData());
    query.setPayload(payload);

    ResponseDTO response = ZingleConnection.getInstance().send(query);

    if(response.getResponseCode()==200){
        JSONObject result= null;
        try {
            result = response.getData().getJSONObject("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mapper(result);
    }
    else
        throw new UnsuccessfullRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
}

    public boolean updateAsync(final Model object,final ServiceDelegate<Model> delegate){
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Model result=update(object);
                    delegate.processResult(result);
                }catch (UnsuccessfullRequestEx e){
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

    public boolean updateAsync(final Model object){
        synchronized (updateDelegate) {
            if (updateDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return updateAsync(object, updateDelegate);
        }
    }
}


