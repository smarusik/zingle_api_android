package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfulRequestEx;
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
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/contacts>ZingleContact API</a>).
 * Supports all basic functions. Also complemented by ZingleLabel manipulation (attach, detach), ZingleAutomation triggering and setting ZingleContactFieldValues
 * functional.
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
            result.setCreatedAt(source.optLong("created_at")*1000);
            result.setUpdatedAt(source.optLong("updated_at")*1000);

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

    /**
     * Sends request for detaching ZingleLabel with provided id from provided ZingleContact
     * @param contact - ZingleContact to detach label from
     * @param labelId - ID of detaching label
     * @return updated ZingleContact (without specified label)
     */
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
            throw new UnsuccessfulRequestEx(response.getData(), response.getResponseCode(), response.getResponseStr());
    }


    /**
     * Same as <b>ZingleContact detachLabel(ZingleContact contact, String labelId)</b>, but runs request in separate thread. Result of request is received by proper implementation
     * of <i>ServiceDelegate</i>, provided as function parameter.
     * @param contact - ZingleContact to detach label from
     * @param labelId - ID of detaching label
     * @param delegate  - implementation of <i>ServiceDelegate</i> to get request results
     * @return true if request successfully starts
     */
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
                }catch (UnsuccessfulRequestEx e){
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    /**
     * Same as <b>boolean detachLabelAsync(final ZingleContact contact, final String labelId,final ServiceDelegate<ZingleContact> delegate)</b>, but implementation
     * of <i>ServiceDelegate</i> is taken from <b>labelDelegate</b> property.
     * @param contact - ZingleContact to detach label from
     * @param labelId - ID of detaching label
     * @return true if request successfully starts
     */
    public boolean detachLabelAsync(final ZingleContact contact, final String labelId) {
        synchronized (labelDelegate) {
            if (labelDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return detachLabelAsync(contact, labelId, labelDelegate);
        }
    }

    //attach

    /**
     * Sends request for attaching ZingleLabel with provided id to provided ZingleContact
     * @param contact - ZingleContact to attach label to
     * @param labelId - ID of attaching label
     * @return updated ZingleContact (with specified label)
     */
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
            throw new UnsuccessfulRequestEx(response.getData(), response.getResponseCode(), response.getResponseStr());
    }

    /**
     * Same as <b>ZingleContact attachLabel(ZingleContact contact, String labelId)</b>, but runs request in separate thread. Result of request is received by proper implementation
     * of <i>ServiceDelegate</i>, provided as function parameter.
     * @param contact - ZingleContact to attach label to
     * @param labelId - ID of attaching label
     * @param delegate  - implementation of <i>ServiceDelegate</i> to get request results
     * @return true if request successfully starts
     */
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
                }catch (UnsuccessfulRequestEx e){
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    /**
     * Same as <b>boolean attachLabelAsync(final ZingleContact contact, final String labelId,final ServiceDelegate<ZingleContact> delegate)</b>, but implementation
     * of <i>ServiceDelegate</i> is taken from <b>labelDelegate</b> property.
     * @param contact - ZingleContact to attach label to
     * @param labelId - ID of attaching label
     * @return true if request successfully starts
     */
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

    /**
     * Sends request for triggering ZingleAutomation with provided id for provided ZingleContact
     * @param contact - ZingleContact to trigger automation on
     * @param automationId - ID of automation
     * @return true if request return successful response
     */
    public Boolean triggerAutomation(ZingleContact contact, String automationId) {
        ZingleQuery query = new ZingleQuery(POST, String.format(labelResourcePath(), contact.getId(), automationId));

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if (response.getResponseCode() == 200) {
            return true;
        } else
            throw new UnsuccessfulRequestEx(response.getData(), response.getResponseCode(), response.getResponseStr());
    }

    /**
     * Same as <b>Boolean triggerAutomation(ZingleContact contact, String automationId)</b>, but runs request in separate thread. Result of is received by proper implementation
     * of <i>ServiceDelegate</i>, provided as function parameter.
     * @param contact - ZingleContact to trigger automation on
     * @param automationId - ID of automation
     * @param delegate - implementation of ServiceDelegate
     * @return true if request return successful response
     */
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
                }catch (UnsuccessfulRequestEx e){
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    /**
     * Same as boolean triggerAutomationAsync(final ZingleContact contact, final String automationId,final ServiceDelegate<Boolean> delegate), but implementation
     * of <i>ServiceDelegate</i> is taken from <b>automationDelegate</b> property.
     * @param contact
     * @param automationId
     * @return true if request starts successfully
     */
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


    /**
     * Sends request for setting specified value to specified contact field (ZingleContactFieldValue) for provided ZingleContact
     * (see <a href=https://github.com/Zingle/rest-api/blob/master/custom_field_values/POST_create_id.md>Update Contact Custom Field Value</a> in API docs)
     * @param contact - ZingleContact to set field value on
     * @param fieldValue - ZingleContactFieldValue object.
     * @return Updated ZingleContact
     */
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
            throw new UnsuccessfulRequestEx(response.getData(), response.getResponseCode(), response.getResponseStr());
    }

    /**
     * Same as <b>ZingleContact setFieldValue(ZingleContact contact, ZingleContactFieldValue fieldValue)</b>, but runs request in separate thread. Result is received by proper implementation
     * of <i>ServiceDelegate</i>, provided as function parameter.
     * @param contact - ZingleContact to set field value on
     * @param fieldValue - ZingleContactFieldValue object.
     * @param delegate - implementation of ServiceDelegate
     * @return true if request starts successfully
     */
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
                }catch (UnsuccessfulRequestEx e){
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    /**
     *Same as <b>boolean setFieldValueAsync(final ZingleContact contact, final ZingleContactFieldValue fieldValue,final ServiceDelegate<ZingleContact> delegate)</b>, but implementation
     * of <i>ServiceDelegate</i> is taken from <b>fieldDelegate</b> property.
     * @param contact - ZingleContact to set field value on
     * @param fieldValue - ZingleContactFieldValue object.
     * @return
     */
    public boolean setFieldValueAsync(final ZingleContact contact, final ZingleContactFieldValue fieldValue) {
        synchronized (fieldDelegate) {
            if (fieldDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return setFieldValueAsync(contact, fieldValue, fieldDelegate);
        }
    }
}