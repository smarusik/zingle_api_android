package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.RequestDTO;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZingleAutomation;
import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleContactCustomField;
import me.zingle.api.sdk.model.ZingleLabel;
import me.zingle.api.sdk.model.ZinglePhoneNumber;
import me.zingle.api.sdk.model.ZingleService;

import static me.zingle.api.sdk.dao.RequestMethods.DELETE;
import static me.zingle.api.sdk.dao.RequestMethods.GET;
import static me.zingle.api.sdk.dao.RequestMethods.POST;
import static me.zingle.api.sdk.dao.RequestMethods.PUT;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleContactServices {
    private static final String resoursePrefixPath="/services";
    private static final String resoursePath="contacts";

    private ServiceDelegate<List<ZingleContact>> searchDelegate;
    private ServiceDelegate<ZingleContact> getDelegate;
    private ServiceDelegate<Boolean> deleteDelegate;
    private ServiceDelegate<ZingleContact> createDelegate;
    private ServiceDelegate<ZingleContact> updateDelegate;
    private ServiceDelegate<Boolean> labelDelegate;
    private ServiceDelegate<Boolean> automationDelegate;


    public void setSearchDelegate(ServiceDelegate<List<ZingleContact>> searchDelegate) {
        this.searchDelegate = searchDelegate;
    }

    public void setGetDelegate(ServiceDelegate<ZingleContact> getDelegate) {
        this.getDelegate = getDelegate;
    }

    public void setDeleteDelegate(ServiceDelegate<Boolean> deleteDelegate) {
        this.deleteDelegate = deleteDelegate;
    }

    public void setCreateDelegate(ServiceDelegate<ZingleContact> createDelegate) {
        this.createDelegate = createDelegate;
    }

    public void setUpdateDelegate(ServiceDelegate<ZingleContact> updateDelegate) {
        this.updateDelegate = updateDelegate;
    }


    public void setLabelDelegate(ServiceDelegate<Boolean> labelDelegate) {
        this.labelDelegate = labelDelegate;
    }

    public void setAutomationDelegate(ServiceDelegate<Boolean> automationDelegate) {
        this.automationDelegate = automationDelegate;
    }

    static ZingleContact mapper(JSONObject source, ZingleService service){
        ZingleContact result=new ZingleContact();

        result.setId(source.getInt("id"));
        result.setPhoneNumber(ZinglePhoneNumberServices.mapper(source));
        result.setIsConfirmed(source.getInt("is_confirmed") == 0 ? false : true);
        result.setIsConfirmed(source.getInt("is_starred")==0?false:true);

        JSONArray customFieldsJS=source.optJSONArray("custom_field_values");
        int i = 0;
        if(customFieldsJS!=null) {
            JSONObject customFieldJS = customFieldsJS.optJSONObject(i++);
            Map<ZingleContactCustomField, String> customFields = new HashMap<>();

            while (customFieldJS != null) {
                ZingleContactCustomField key = new ZingleContactCustomField();

                key.setService(service);
                key.setId(customFieldJS.getInt("custom_field_id"));
                key.setDisplayName(customFieldJS.getString("custom_field_display_name"));

                customFields.put(key, customFieldJS.getString("value"));

                customFieldJS = customFieldsJS.optJSONObject(i++);
            }

            result.setCustomFieldValues(customFields);
        }

        JSONArray labelsJS=source.getJSONArray("labels");
        i=0;
        JSONObject labelJS=labelsJS.optJSONObject(i++);
        List<ZingleLabel> labels=new ArrayList<>();

        while(labelJS!=null){
            ZingleLabel temp=new ZingleLabel();
            temp.setId(labelJS.getInt("id"));
            temp.setDisplayName(labelJS.getString("display_name"));

            labels.add(temp);

            labelJS=labelsJS.optJSONObject(i++);
        }

        result.setLabels(labels);

        return result;
    }

    static List<ZingleContact> arrayMapper(JSONArray source, ZingleService service) throws JSONException {
        int i=0;
        JSONObject temp=source.optJSONObject(i++);

        List<ZingleContact> retList=new ArrayList<>();

        while(temp!=null){
            retList.add(mapper(temp,service));
            temp=source.optJSONObject(i++);
        }

        return retList;
    }

    public static List<ZingleContact> search(ZingleService service,List<QueryPart> filters) throws UnsuccessfullRequestEx {

        ZingleQuery query = new ZingleQuery(GET, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath);

        if(filters!=null){
            for (QueryPart p:filters) {
                query.addParam(p);
            }
        }

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONArray result=response.getData().getJSONArray("result");
            return arrayMapper(result,service);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());

    }

    public boolean searchAsync(final ZingleService service,final List<QueryPart> filters){
        if(searchDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    searchDelegate.processResult(search(service,filters));
                }catch (UnsuccessfullRequestEx e){
                    searchDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public static ZingleContact get(ZingleService service, int id){
        ZingleQuery query = new ZingleQuery(GET, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath+"/"+id);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result, service);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean getAsync(final ZingleService service, final int id){
        if(getDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
            try{
                getDelegate.processResult(get(service, id));
            }catch (UnsuccessfullRequestEx e){
                getDelegate.processError(e.getResponceCode(),e.getResponceStr());
            }
            }
        });

        th.start();

        return true;
    }

    public boolean delete(ZingleService service,int id){
        ZingleQuery query = new ZingleQuery(DELETE, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath+"/"+id);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return true;
        }
        else
            throw new UnsuccessfullRequestEx("Error list()", response.getResponseCode(), response.getResponseStr());

    }

    public boolean deleteAsync(final ZingleService service,final int id){
        if(deleteDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    deleteDelegate.processResult(delete(service, id));
                }catch (UnsuccessfullRequestEx e){
                    deleteDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }


    public boolean delete(ZingleContact contact){
        return delete(contact.getService(),contact.getId());
    }

    public boolean deleteAsync(final ZingleContact contact){
        return deleteAsync(contact.getService(), contact.getId());
    }

    public static ZingleContact create(ZingleService service, /*UUID uuid,*/ ZinglePhoneNumber phoneNumber,
                                                            Map<ZingleContactCustomField,String> customFieldValues){

        ZingleContact contact=new ZingleContact();

        contact.setPhoneNumber(phoneNumber);
        contact.setCustomFieldValues(customFieldValues);

        ZingleQuery query = new ZingleQuery(POST, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath);

        RequestDTO contactDTO=new RequestDTO();
        contactDTO.fromContact(contact);

        query.setPayload(contactDTO);

        System.out.println(contactDTO.getData().toString());

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result,service);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean createForServiceAsync(final ZingleService service, /*final UUID uuid,*/ final ZinglePhoneNumber phoneNumber,
                                         final Map<ZingleContactCustomField,String> customFieldValues){
        if(createDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
            try{
                createDelegate.processResult(create(service, phoneNumber, customFieldValues));
            }catch (UnsuccessfullRequestEx e){
                createDelegate.processError(e.getResponceCode(),e.getResponceStr());
            }
            }
        });

        th.start();

        return true;
    }

    public static ZingleContact update(ZingleContact contact){

        ZingleQuery query = new ZingleQuery(PUT, resoursePrefixPath+"/"+contact.getService().getId()+"/"+resoursePath+"/"+contact.getId());

        RequestDTO payload=new RequestDTO();
        payload.fromContact(contact);

        query.setPayload(payload);

        System.out.println(payload.getData().toString());

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result,contact.getService());
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean updateAsync(final ZingleContact contact){
        if(updateDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    updateDelegate.processResult(update(contact));
                }catch (UnsuccessfullRequestEx e){
                    updateDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public static boolean applyLabel(ZingleContact contact, ZingleLabel label){
        ZingleQuery query = new ZingleQuery(POST, resoursePrefixPath+"/"+contact.getService().getId()+"/"+resoursePath+"/"+contact.getId()+
                                                "/labels/"+label.getId());

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return true;
        }
        else
            throw new UnsuccessfullRequestEx("Error applyLabel()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean applyLabelAsync(final ZingleContact contact, final ZingleLabel label){
        if(labelDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    labelDelegate.processResult(applyLabel(contact, label));
                }catch (UnsuccessfullRequestEx e){
                    labelDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }



    public static boolean removeLabel(ZingleContact contact, ZingleLabel label){
        ZingleQuery query = new ZingleQuery(DELETE, resoursePrefixPath+"/"+contact.getService().getId()+"/"+resoursePath+"/"+contact.getId()+
                "/labels/"+label.getId());

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return true;
        }
        else
            throw new UnsuccessfullRequestEx("Error removeLabel()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean removeLabelAsync(final ZingleContact contact, final ZingleLabel label){
        if(labelDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    labelDelegate.processResult(removeLabelAsync(contact, label));
                }catch (UnsuccessfullRequestEx e){
                    labelDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }


    public static boolean runAutomation(ZingleContact contact, ZingleAutomation automation){
        ZingleQuery query = new ZingleQuery(POST, resoursePrefixPath+"/"+contact.getService().getId()+"/"+resoursePath+"/"+contact.getId()+
                "/automations/"+automation.getId());

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return true;
        }
        else
            throw new UnsuccessfullRequestEx("Error runAutomation()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean runAutomationAsync(final ZingleContact contact, final ZingleAutomation automation){
        if(labelDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    automationDelegate.processResult(runAutomation(contact, automation));
                }catch (UnsuccessfullRequestEx e){
                    automationDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }
}