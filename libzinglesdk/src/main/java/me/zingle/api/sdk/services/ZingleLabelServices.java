package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.RequestDTO;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZingleLabel;
import me.zingle.api.sdk.model.ZingleService;

import static me.zingle.api.sdk.dao.RequestMethods.DELETE;
import static me.zingle.api.sdk.dao.RequestMethods.GET;
import static me.zingle.api.sdk.dao.RequestMethods.POST;
import static me.zingle.api.sdk.dao.RequestMethods.PUT;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleLabelServices {
    private static final String resoursePrefixPath="/services";
    //private final String serviceID;
    private static final String resoursePath="contact-labels";
    //private static String customFieldID;

    private ServiceDelegate<List<ZingleLabel>> listDelegate;
    private ServiceDelegate<ZingleLabel> getDelegate;
    private ServiceDelegate<ZingleLabel> createDelegate;
    private ServiceDelegate<ZingleLabel> updateDelegate;
    private ServiceDelegate<Boolean> deleteDelegate;

    public void setListDelegate(ServiceDelegate<List<ZingleLabel>> listDelegate) {
        this.listDelegate = listDelegate;
    }

    public void setGetDelegate(ServiceDelegate<ZingleLabel> getDelegate) {
        this.getDelegate = getDelegate;
    }

    public void setCreateDelegate(ServiceDelegate<ZingleLabel> createDelegate) {
        this.createDelegate = createDelegate;
    }

    public void setUpdateDelegate(ServiceDelegate<ZingleLabel> updateDelegate) {
        this.updateDelegate = updateDelegate;
    }

    public void setDeleteDelegate(ServiceDelegate<Boolean> deleteDelegate) {
        this.deleteDelegate = deleteDelegate;
    }

    static ZingleLabel mapper(JSONObject source, ZingleService service) throws JSONException{
        ZingleLabel result=new ZingleLabel();

        result.setId(source.getInt("id"));
        result.setDisplayName(source.getString("display_name"));
        result.setIsGlobal(source.getInt("is_global") == 0 ? false : true);
        result.setIsAutomation(source.getInt("is_automatic") == 0 ? false : true);

        if(!source.get("background_color").equals(null))
            result.setBackgroundColor(new Color(Integer.parseInt(source.getString("background_color").substring(1), 16)));

        if(!source.get("text_color").equals(null))
            result.setTextColor(new Color(Integer.parseInt(source.getString("text_color").substring(1), 16)));

        result.setService(service);

        return result;
    }

    static List<ZingleLabel> arrayMapper(JSONArray source, ZingleService service) throws JSONException {
        int i=0;
        JSONObject temp=source.optJSONObject(i++);

        List<ZingleLabel> retList=new ArrayList<>();

        while(temp!=null){
            retList.add(mapper(temp,service));
            temp=source.optJSONObject(i++);
        }

        return retList;
    }

    public static List<ZingleLabel> listForService(ZingleService service){
        ZingleQuery query = new ZingleQuery(GET, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONArray result=response.getData().getJSONArray("result");
            return arrayMapper(result,service);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());

    }

    public boolean listForServiceAsync(final ZingleService service){
        if(listDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    listDelegate.processResult(listForService(service));
                }catch (UnsuccessfullRequestEx e){
                    listDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public static ZingleLabel getForServiceWithId(ZingleService service, int id){
        ZingleQuery query = new ZingleQuery(GET, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath+"/"+id);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result, service);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean getForServiceWithIdAsync(final ZingleService service, final int id){
        if(getDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    getDelegate.processResult(getForServiceWithId(service, id));
                }catch (UnsuccessfullRequestEx e){
                    getDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public static ZingleLabel createForService(ZingleService service, String displayName, Color backgroundColor, Color textColor){

        ZingleLabel label=new ZingleLabel();
        label.setDisplayName(displayName);
        label.setBackgroundColor(backgroundColor);
        label.setTextColor(textColor);

        ZingleQuery query = new ZingleQuery(POST, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath);

        RequestDTO payload=new RequestDTO();
        payload.fromLabel(label);

        query.setPayload(payload);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result,service);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean createForServiceAsync(final ZingleService service, final String displayName, final Color backgroundColor, final Color textColor){
        if(createDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    createDelegate.processResult(createForService(service, displayName, backgroundColor, textColor));
                }catch (UnsuccessfullRequestEx e){
                    createDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

    public static ZingleLabel update(ZingleLabel labelUpd){

        ZingleQuery query = new ZingleQuery(PUT, resoursePrefixPath+"/"+labelUpd.getService().getId()+"/"+resoursePath+"/"+labelUpd.getId());

        RequestDTO payload=new RequestDTO();
        payload.fromLabel(labelUpd);

        query.setPayload(payload);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result,labelUpd.getService());
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean updateAsync(final ZingleLabel labelUpd){
        if(updateDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    updateDelegate.processResult(update(labelUpd));
                }catch (UnsuccessfullRequestEx e){
                    updateDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

    public boolean delete(ZingleLabel label){
        ZingleQuery query = new ZingleQuery(DELETE, resoursePrefixPath+"/"+label.getService().getId()+"/"+resoursePath+"/"+label.getId());

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return true;
        }
        else
            throw new UnsuccessfullRequestEx("Error list()", response.getResponseCode(), response.getResponseStr());

    }

    public boolean deleteAsync(final ZingleLabel label){
        if(deleteDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    deleteDelegate.processResult(delete(label));
                }catch (UnsuccessfullRequestEx e){
                    deleteDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

}
