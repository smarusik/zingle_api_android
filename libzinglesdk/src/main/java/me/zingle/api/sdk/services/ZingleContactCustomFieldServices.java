package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.RequestDTO;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZingleContactCustomField;
import me.zingle.api.sdk.model.ZingleContactCustomFieldOption;
import me.zingle.api.sdk.model.ZingleService;

import static me.zingle.api.sdk.dao.RequestMethods.DELETE;
import static me.zingle.api.sdk.dao.RequestMethods.GET;
import static me.zingle.api.sdk.dao.RequestMethods.POST;
import static me.zingle.api.sdk.dao.RequestMethods.PUT;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleContactCustomFieldServices {
    private static final String resoursePrefixPath="/services";
    //private final String serviceID;
    private static final String resoursePath="contact-custom-fields";
    //private static String customFieldID;

    private ServiceDelegate<List<ZingleContactCustomField>> listDelegate;
    private ServiceDelegate<ZingleContactCustomField> getDelegate;
    private ServiceDelegate<ZingleContactCustomField> createDelegate;
    private ServiceDelegate<ZingleContactCustomField> updateDelegate;
    private ServiceDelegate<Boolean> deleteDelegate;

    public void setListDelegate(ServiceDelegate<List<ZingleContactCustomField>> listDelegate) {
        this.listDelegate = listDelegate;
    }

    public void setGetDelegate(ServiceDelegate<ZingleContactCustomField> getDelegate) {
        this.getDelegate = getDelegate;
    }

    public void setCreateDelegate(ServiceDelegate<ZingleContactCustomField> createDelegate) {
        this.createDelegate = createDelegate;
    }

    public void setUpdateDelegate(ServiceDelegate<ZingleContactCustomField> updateDelegate) {
        this.updateDelegate = updateDelegate;
    }

    public void setDeleteDelegate(ServiceDelegate<Boolean> deleteDelegate) {
        this.deleteDelegate = deleteDelegate;
    }

    static ZingleContactCustomField mapper(JSONObject source) throws JSONException {
        ZingleContactCustomField mapResult=new ZingleContactCustomField();

        mapResult.setId(source.getInt("id"));
        mapResult.setDisplayName(source.getString("display_name"));
        mapResult.setIsGlobal(source.getInt("is_global") == 0 ? false : true);

        JSONArray optionsJS=source.getJSONArray("options");

        int i=0;
        JSONObject optionJS=optionsJS.optJSONObject(i++);
        List<ZingleContactCustomFieldOption> options=new ArrayList<>();

        while(optionJS!=null){
            options.add(new ZingleContactCustomFieldOption(optionJS.getString("display_name"),optionJS.getString("value"),optionJS.getInt("sort_order")));
            optionJS=optionsJS.optJSONObject(i++);
        }

        mapResult.setOptions(options);

        return mapResult;
    }

    static List<ZingleContactCustomField> arrayMapper(JSONArray source) throws JSONException {
        int i=0;
        JSONObject temp=source.optJSONObject(i++);

        List<ZingleContactCustomField> retList=new ArrayList<>();

        while(temp!=null){
            retList.add(mapper(temp));
            temp=source.optJSONObject(i++);
        }

        return retList;
    }

    public static List<ZingleContactCustomField> listForService(ZingleService service){
        ZingleQuery query = new ZingleQuery(GET, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONArray result=response.getData().getJSONArray("result");
            return arrayMapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());

    }

    public boolean listForServiceAsync(final ZingleService service){
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

    public static ZingleContactCustomField getForServiceWithId(ZingleService service, int id){
        ZingleQuery query = new ZingleQuery(GET, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath+"/"+id);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean getForServiceWithIdAsync(final ZingleService service, final int id){
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

    public static ZingleContactCustomField createForService(ZingleService service, String displayName,
                                                    List<ZingleContactCustomFieldOption> options){

        ZingleContactCustomField newCCField=new ZingleContactCustomField();
        newCCField.setDisplayName(displayName);
        newCCField.setOptions(options);

        ZingleQuery query = new ZingleQuery(POST, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath);

        RequestDTO ccField=new RequestDTO();
        ccField.fromContactCustomField(newCCField);

        query.setPayload(ccField);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean createForServiceAsync(final ZingleService service, final String displayName,
                                                            final List<ZingleContactCustomFieldOption> options){
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    createDelegate.processResult(createForService(service,displayName,options));
                }catch (UnsuccessfullRequestEx e){
                    createDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

    public static ZingleContactCustomField updateForService(ZingleService service,ZingleContactCustomField ccFieldUpd){

        ZingleQuery query = new ZingleQuery(PUT, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath+"/"+ccFieldUpd.getId());

        RequestDTO payload=new RequestDTO();
        payload.fromContactCustomField(ccFieldUpd);

        query.setPayload(payload);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean updateForServiceAsync(final ZingleService service, final ZingleContactCustomField ccFieldUpd){
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    updateDelegate.processResult(updateForService(service, ccFieldUpd));
                }catch (UnsuccessfullRequestEx e){
                    updateDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

    public boolean deleteForService(ZingleService service,ZingleContactCustomField ccField){
        ZingleQuery query = new ZingleQuery(DELETE, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath+"/"+ccField.getId());

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return true;
        }
        else
            throw new UnsuccessfullRequestEx("Error list()", response.getResponseCode(), response.getResponseStr());

    }

    public boolean deleteForServiceAsync(final ZingleService service, final ZingleContactCustomField ccField){
        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    deleteDelegate.processResult(deleteForService(service,ccField));
                }catch (UnsuccessfullRequestEx e){
                    deleteDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }
}
