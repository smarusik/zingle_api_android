package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfulRequestEx;
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
 * Base class for Zingle services
 */
public abstract class ZingleBaseService<Model extends ZingleBaseModel> {

    /**
     * Properties for storing predefined delegates, which are used in Asynchronous (...Async) functions
     * when delegate wasn't provided as parameter.
     */
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
     * Abstract function. Must be implemented for returning proper API path for derived object (for example for ZingleServiceServices
     * it is \/services or \/services\/{serviceId} if specific=true)
     * @param specific if true, must return path with specific id
     * @return part of the path to resource after API version
     */
    protected abstract String resourcePath(boolean specific);


    /**
     * Abstract function. Proper implementation must check if HTTP request modifier, provided as function parameter is supported by API for derived class.
     * (see <a href=https://github.com/Zingle/rest-api#request-modifiers>Request modifiers</a> and specific classes documentation.)
     * @param modifier
     * @return
     */
    protected abstract boolean checkModifier(String modifier);

    /**
     * Convenience function for creating a List of HTTP request modifiers (List<QueryParts>)
     * @param keys String, containing coma separated list of filter keys
     * @param values Strings with filter expressions
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
    /**
     *Abstract function. Proper implementation must convert JSON data from HTTP response into Zingle object or objects hierarchy.
     * @param source JSON object from HTTP responce
     * @return ZingleObject of type Model
     * @throws MappingErrorEx
     */
    public abstract Model mapper(JSONObject source) throws MappingErrorEx;

    /**
     * Converts JSON data from HTTP response into ZingleList (see <a href=https://github.com/Zingle/rest-api#http-responses>HTTP Response</a> in Zingle API docs).
     * <i>status</i> part forms the properties of ZingleList and <i>result</i> forms array of objects of Model type.
     * @param source Successfull HTTP response in form of JSON object
     * @return ZingleList with Model objects and response status.
     * @throws MappingErrorEx
     */
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


    /**
     *Same as ZingleList<Model> arrayMapper(JSONObject source), but returns only standard List of Zingle objects, ignoring status data.
     * May be used, when <i>status</i> parameters are not needed.
     */
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

    /**
     * Returns Zingle object of Model type with provided ID, if it exists in database, otherwise throws UnsuccessfulRequestEx
     * @param id - object's ID
     * @return Zingle object
     */
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
            throw new UnsuccessfulRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
    }


    /**
     * Same as <b>Model get(String id)</b>, but runs request in separate thread. Result of request is sent to proper implementation
     * of <i>ServiceDelegate</i>, provided as function parameter.
     *
     * @param id - object's id
     * @param delegate - implementation of <i>ServiceDelegate</i> to get request results
     * @return true if request successfully starts
     */
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
                } catch (UnsuccessfulRequestEx e) {
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();
        return true;
    }

    /**
     * Same as <b>Model getAsync(String id,ServiceDelegate<Model> delegate)</b>, but implementation
     * of <i>ServiceDelegate</i> is taken from <b>getDelegate</b> property.
     *
     * @param id - object's id
     * @return true if request successfully starts
     */
    public boolean getAsync(final String id){
        synchronized (getDelegate) {
            if (getDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return getAsync(id, getDelegate);
        }
    }

//Listing methods
    /**
     * Returns ZingleList of Model objects from database according to request string without request modifiers (uses default pagination options and sorting),
     * otherwise throws UnsuccessfulRequestEx.
     * @return ZingleList of <i>Model</i> objects
     */

    public ZingleList<Model> list(){
        ZingleQuery query = new ZingleQuery(GET, resourcePath(false));

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return arrayMapper(response.getData());
        }
        else
            throw new UnsuccessfulRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
    }

    /**
     * Same as <b>ZingleList<Model> list()</b>, but runs request in separate thread. Result of request is sent to proper implementation
     * of <i>ServiceDelegate</i>, provided as function parameter.
     *
     * @param delegate - implementation of <i>ServiceDelegate</i> to get request results
     * @return true if request successfully starts
     */
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
                } catch (UnsuccessfulRequestEx e) {
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();
        return true;
    }

    /**
     * Same as <b>boolean listAsync(ServiceDelegate<ZingleList<Model>> delegate)</b>, but implementation
     * of <i>ServiceDelegate</i> is taken from <b>listDelegate</b> property.
     * @return true if request successfully starts
     */
    public boolean listAsync(){
        synchronized (listDelegate) {
            if (listDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return listAsync(listDelegate);
        }
    }


    /**
     * Same as <b>ZingleList<Model> list()</b>, but can add filters and sorting options to request and override default pagination through <i>conditions</i>
     * parameter list. For list of supported parameters refer to respective class section in <a href=https://github.com/Zingle/rest-api>API docs</a>
     * @param conditions
     * @return
     */
    public ZingleList<Model> list(List<QueryPart> conditions){
        ZingleQuery query = new ZingleQuery(GET, resourcePath(false));
        query.setParams(conditions);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return arrayMapper(response.getData());
        }
        else
            throw new UnsuccessfulRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
    }

    /**
     * Same as <b>ZingleList<Model> list(List<QueryPart> conditions)</b>, but runs request in separate thread. Result of request is sent to proper implementation
     * of <i>ServiceDelegate</i>, provided as function parameter.
     *
     * @param delegate - implementation of <i>ServiceDelegate</i> to get request results
     * @return true if request successfully starts
     */
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
                }catch (UnsuccessfulRequestEx e){
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    /**
     * Same as <b>boolean listAsync(List<QueryPart> conditions,ServiceDelegate<ZingleList<Model>> delegate)</b>, but implementation
     * of <i>ServiceDelegate</i> is taken from <b>listDelegate</b> property.
     * @return true if request successfully starts
     */
    public boolean listAsync(final List<QueryPart> conditions){
        synchronized (listDelegate){
            if(listDelegate==null){
                throw new UndefinedServiceDelegateEx();
            }

            return listAsync(conditions,listDelegate);
        }
    }

//Deleting by ID methods

    /**
     * Sends request for deleting proper Zingle object with provided id from database (if exists)
     * @param id - ID of deleting object
     * @return true if deletes successfully
     */
    public Boolean delete(String id){
        ZingleQuery query = new ZingleQuery(DELETE, String.format(resourcePath(true), id));

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return true;
        }
        else
            throw new UnsuccessfulRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
    }

    /**
     * Same as <b>Boolean delete(String id)</b>, but runs request in separate thread. Result of request is sent to proper implementation
     * of <i>ServiceDelegate</i>, provided as function parameter.
     *
     * @param id - object's id
     * @param delegate - implementation of <i>ServiceDelegate</i> to get request results
     * @return true if request successfully starts
     */
    public boolean deleteAsync(final String id,final ServiceDelegate<Boolean> delegate){
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    delegate.processResult(delete(id));
                } catch (UnsuccessfulRequestEx e) {
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();
        return true;
    }

    /**
     * Same as <b>Model deleteAsync(String id,ServiceDelegate<Model> delegate)</b>, but implementation
     * of <i>ServiceDelegate</i> is taken from <b>deleteDelegate</b> property.
     *
     * @param id - object's id
     * @return true if request successfully starts
     */
    public boolean deleteAsync(final String id){
        synchronized (deleteDelegate) {
            if (deleteDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return deleteAsync(id, deleteDelegate);
        }
    }



//Creating methods

    /**
     * Sends request to create a new record in database with data and type, provided by <i>object</i>. Throws if request fails.
     * @param object - Zingle object. For details about required fields, etc. refer to <a href=https://github.com/Zingle/rest-api>API docs</a>
     * @return newly created Zingle object
     */
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
            throw new UnsuccessfulRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
    }

    /**
     * Same as <b>Model create(Model object)</b>, but runs request in separate thread. Result of request is sent to proper implementation
     * of <i>ServiceDelegate</i>, provided as function parameter.
     * @param object - Zingle object. For details about required fields, etc. refer to <a href=https://github.com/Zingle/rest-api>API docs</a>
     * @param delegate - implementation of <i>ServiceDelegate</i> to get request results
     * @return true if request successfully starts
     */
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
                }catch (UnsuccessfulRequestEx e){
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

    /**
     * Same as <b>createAsync(Model object,ServiceDelegate<Model> delegate)</b>, but implementation
     * of <i>ServiceDelegate</i> is taken from <b>createDelegate</b> property.
     * @param object - Zingle object. For details about required fields, etc. refer to <a href=https://github.com/Zingle/rest-api>API docs</a>
     * @return true if request successfully starts
     */
    public boolean createAsync(final Model object){
        synchronized (createDelegate) {
            if (createDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return createAsync(object, createDelegate);
        }
    }

    //Updating methods
    /**
     * Sends request to update record in database with data and type, provided by <i>object</i>. Throws if request fails.
     * @param object - Zingle object. For details about required fields, etc. refer to <a href=https://github.com/Zingle/rest-api>API docs</a>
     * @return updated Zingle object
     */
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
        throw new UnsuccessfulRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
}
    /**
     * Same as <b>Model update(Model object)</b>, but runs request in separate thread. Result of request is sent to proper implementation
     * of <i>ServiceDelegate</i>, provided as function parameter.
     * @param object - Zingle object. For details about required fields, etc. refer to <a href=https://github.com/Zingle/rest-api>API docs</a>
     * @param delegate - implementation of <i>ServiceDelegate</i> to get request results
     * @return true if request successfully starts
     */
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
                }catch (UnsuccessfulRequestEx e){
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

    /**
     * Same as <b>updateAsync(Model object,ServiceDelegate<Model> delegate)</b>, but implementation
     * of <i>ServiceDelegate</i> is taken from <b>updateDelegate</b> property.
     * @param object - Zingle object. For details about required fields, etc. refer to <a href=https://github.com/Zingle/rest-api>API docs</a>
     * @return true if request successfully starts
     */
    public boolean updateAsync(final Model object){
        synchronized (updateDelegate) {
            if (updateDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return updateAsync(object, updateDelegate);
        }
    }
}


