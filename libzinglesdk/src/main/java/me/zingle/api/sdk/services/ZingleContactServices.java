package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.RequestDTO;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleContactFieldValue;
import me.zingle.api.sdk.model.ZingleService;

import static me.zingle.api.sdk.dao.RequestMethods.DELETE;
import static me.zingle.api.sdk.dao.RequestMethods.POST;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleContactServices extends ZingleBaseService<ZingleContact> {

    final ZingleService parent;

    private ServiceDelegate<ZingleContact> labelDelegate;
    private ServiceDelegate<Boolean> automationDelegate;
    private ServiceDelegate<ZingleContact> fieldDelegate;

    public void setLabelDelegate(ServiceDelegate<ZingleContact> labelDelegate) {
        this.labelDelegate = labelDelegate;
    }

    public void setAutomationDelegate(ServiceDelegate<Boolean> automationDelegate) {
        this.automationDelegate = automationDelegate;
    }

    public void setFieldDelegate(ServiceDelegate<ZingleContact> fieldDelegate) {
        this.fieldDelegate = fieldDelegate;
    }

    public ZingleContactServices(ZingleService parent) {
        this.parent = parent;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base=String.format("/services/%s/contacts",parent.getId());
        if(specific)
            return base+"/%s";
        else
            return base;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return modifier.equals("page")
                ||modifier.equals("page_size")
                ||modifier.equals("sort_field")
                ||modifier.equals("sort_direction")
                ||modifier.equals("channel_value")
                ||modifier.equals("label_id")
                ||modifier.equals("is_confirmed")
                ||modifier.equals("is_starred")
                ;
    }

    @Override
    public ZingleContact mapper(JSONObject source) throws MappingErrorEx {
        ZingleContact result=new ZingleContact();

        try {
            result.setId(source.getString("id"));
            result.setIsConfirmed(source.optBoolean("is_confirmed"));
            result.setIsStarred(source.optBoolean("is_starred"));
            result.setCreatedAt(source.optInt("created_at"));
            result.setUpdatedAt(source.optInt("updated_at"));

            JSONObject lastMessageJSON = source.optJSONObject("last_message");
            if (lastMessageJSON != null) {
                result.setLastMessage(new ZingleContact.LastMessageDigest(lastMessageJSON.optString("id"),
                        lastMessageJSON.optString("body"),
                        lastMessageJSON.optInt("created_at")
                ));
            }

            JSONArray channelsJS = source.optJSONArray("channels");
            if (channelsJS != null) {
                ZingleContactChannelServices contactChannelServices = new ZingleContactChannelServices(parent, result);
                result.setChannels(contactChannelServices.arrayMapper(channelsJS));
            }

            JSONArray customFieldsJS = source.optJSONArray("custom_field_values");
            if (customFieldsJS != null) {
                ZingleContactFieldValueService contactFieldValueService = new ZingleContactFieldValueService(parent);
                result.setCustomFieldValues(contactFieldValueService.arrayMapper(customFieldsJS));
            }

            JSONArray labelsJS = source.getJSONArray("labels");
            if (labelsJS != null) {
                ZingleLabelServices labelServices = new ZingleLabelServices(parent);
                result.setLabels(labelServices.arrayMapper(labelsJS));
            }

            result.setService(parent);
        }catch (JSONException e) {
            e.printStackTrace();
            throw new MappingErrorEx(this.getClass().getName(),source.toString(),e.getMessage());
        }

        return result;
    }

//Label manipulations
    private String labelResourcePath(){
        return resourcePath(true) + "/labels/%s";
    }
    //detach
    public ZingleContact detachLabel(ZingleContact contact, String labelId) {
        ZingleQuery query = new ZingleQuery(DELETE, String.format(labelResourcePath(), contact.getId(), labelId));

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if (response.getResponseCode() == 200) {
            JSONObject result = null;
            try {
                result = response.getData().getJSONObject("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapper(result);
        } else
            throw new UnsuccessfullRequestEx("Error create()", response.getResponseCode(), response.getResponseStr());
    }

    public boolean detachLabelAsync(final ZingleContact contact, final String labelId,final ServiceDelegate<ZingleContact> delegate) {
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ZingleContact result=detachLabel(contact, labelId);
                    delegate.processResult(result);
                }catch (UnsuccessfullRequestEx e){
                    delegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public boolean detachLabelAsync(final ZingleContact contact, final String labelId) {
        synchronized (labelDelegate) {
            if (labelDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return detachLabelAsync(contact, labelId, labelDelegate);
        }
    }

    //attach
    public ZingleContact attachLabel(ZingleContact contact, String labelId) {
        ZingleQuery query = new ZingleQuery(POST, String.format(labelResourcePath(), contact.getId(), labelId));

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if (response.getResponseCode() == 200) {
            JSONObject result = null;
            try {
                result = response.getData().getJSONObject("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapper(result);
        } else
            throw new UnsuccessfullRequestEx("Error attachLabel()", response.getResponseCode(), response.getResponseStr());
    }

    public boolean attachLabelAsync(final ZingleContact contact, final String labelId,final ServiceDelegate<ZingleContact> delegate) {
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ZingleContact result=attachLabel(contact, labelId);
                    delegate.processResult(result);
                }catch (UnsuccessfullRequestEx e){
                    delegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public boolean attachLabelAsync(final ZingleContact contact, final String labelId) {
        synchronized (labelDelegate) {
            if (labelDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return attachLabelAsync(contact, labelId, labelDelegate);
        }
    }
//Trigger Automation
    private String automationResourcePath(){
        return resourcePath(true) + "/automations/%s";
    }

    public Boolean triggerAutomation(ZingleContact contact, String automationId) {
        ZingleQuery query = new ZingleQuery(POST, String.format(labelResourcePath(), contact.getId(), automationId));

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if (response.getResponseCode() == 200) {
            return true;
        } else
            throw new UnsuccessfullRequestEx("Error triggerAutomation()", response.getResponseCode(), response.getResponseStr());
    }

    public boolean triggerAutomationAsync(final ZingleContact contact, final String automationId,final ServiceDelegate<Boolean> delegate) {
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Boolean result=triggerAutomation(contact, automationId);
                    delegate.processResult(result);
                }catch (UnsuccessfullRequestEx e){
                    delegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public boolean triggerAutomationAsync(final ZingleContact contact, final String automationId) {
        synchronized (automationDelegate) {
            if (automationDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return triggerAutomationAsync(contact, automationId, automationDelegate);
        }
    }

//Set custom field value
    private String fieldValueResourcePath(){
        return resourcePath(true) + "/custom-field-values/%s";
    }

    public ZingleContact setFieldValue(ZingleContact contact, ZingleContactFieldValue fieldValue) {
        ZingleQuery query = new ZingleQuery(POST, String.format(fieldValueResourcePath(), contact.getId(), fieldValue.getContactField().getId()));

        RequestDTO payload=new RequestDTO();
        fieldValue.checkForUpdate();
        payload.setData(fieldValue.extractUpdateData());
        query.setPayload(payload);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if (response.getResponseCode() == 200) {
            JSONObject result = null;
            try {
                result = response.getData().getJSONObject("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapper(result);
        } else
            throw new UnsuccessfullRequestEx("Error setFieldValue()", response.getResponseCode(), response.getResponseStr());
    }

    public boolean setFieldValueAsync(final ZingleContact contact, final ZingleContactFieldValue fieldValue,final ServiceDelegate<ZingleContact> delegate) {
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ZingleContact result=setFieldValue(contact, fieldValue);
                    delegate.processResult(result);
                }catch (UnsuccessfullRequestEx e){
                    delegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public boolean setFieldValueAsync(final ZingleContact contact, final ZingleContactFieldValue fieldValue) {
        synchronized (fieldDelegate) {
            if (fieldDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return setFieldValueAsync(contact, fieldValue, fieldDelegate);
        }
    }
}