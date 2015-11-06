package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfulRequestEx;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.RequestDTO;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZingleMessage;
import me.zingle.api.sdk.model.ZingleService;

import static me.zingle.api.sdk.dao.RequestMethods.POST;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleMessageServices extends ZingleBaseService<ZingleMessage>{

    private final ZingleService parent;

    private ServiceDelegate<ZingleMessage> markReadDelegate;

    public void setMarkReadDelegate(ServiceDelegate<ZingleMessage> markReadDelegate) {
        this.markReadDelegate = markReadDelegate;
    }

    public ZingleMessageServices(ZingleService parent) {
        this.parent = parent;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base=String.format("/services/%s/messages",parent.getId());

        if(specific)
            return base+"/%s/read";
        else
            return base;

    }

    @Override
    protected boolean checkModifier(String modifier) {
        return modifier.equals("page")
                ||modifier.equals("page_size")
                ||modifier.equals("sort_field")
                ||modifier.equals("sort_direction")
                ||modifier.equals("contact_id")
                ||modifier.equals("channel_type")
                ||modifier.equals("channel_value")
                ||modifier.equals("communication_id")
                ||modifier.equals("communication_direction")
                ||modifier.equals("created_at")
                ;
    }

    @Override
    public ZingleMessage mapper(JSONObject source) throws MappingErrorEx {
        ZingleMessage result=new ZingleMessage();

        try {
            result.setId(source.getString("id"));
            result.setBody(source.optString("body"));
            result.setCreatedAt(source.getLong("created_at")*1000);
            result.setReadAt(source.optLong("read_at")*1000);
            result.setSenderType(source.getString("sender_type"));

            ZingleCorrespondentServices correspondentServices = new ZingleCorrespondentServices();
            result.setSender(correspondentServices.mapper(source.getJSONObject("sender")));

            result.setRecipientType(source.getString("recipient_type"));
            result.setRecipient(correspondentServices.mapper(source.getJSONObject("recipient")));

            result.setCommunicationDirection(source.optString("communication_direction"));
            result.setBodyLanguageCode(source.optString("body_language_code"));
            result.setTriggeredByUserId(source.optString("triggered_by_user_id"));
            result.setTranslatedBody(source.optString("translated_body"));
            result.setTemplateId(source.optString("template_id"));
            result.setTranslatedBodyLanguageCode(source.optString("translated_body_language_code"));

            ZingleAttachmentService attachmentService = new ZingleAttachmentService();
            result.setAttachments(attachmentService.arrayMapper(source.getJSONArray("attachments")));
        }catch (JSONException e) {
            e.printStackTrace();
            throw new MappingErrorEx(this.getClass().getName(),source.toString(),e.getMessage());
        }

        return result;
    }

    public ZingleMessage markRead(ZingleMessage msg){
        ZingleQuery query = new ZingleQuery(POST, String.format(resourcePath(true),msg.getId()));

        RequestDTO payload=new RequestDTO();

        payload.setData(msg.extractCreationData());
        query.setPayload(payload);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            try {
                JSONObject result = response.getData().optJSONObject("result");
                if(result!=null) {
                    return mapper(result);
                }
                else return null;

            }catch (JSONException e) {
                e.printStackTrace();
                throw new MappingErrorEx(this.getClass().getName(),response.getData().toString(),e.getMessage());
            }
        }
        else
            throw new UnsuccessfulRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());

    }

    public boolean markReadAsync(ZingleMessage message,ServiceDelegate<ZingleMessage> delegate){
        return updateAsync(message, delegate);
    }

    public boolean markReadAsync(ZingleMessage message){
        synchronized (markReadDelegate) {
            if (markReadDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return updateAsync(message, markReadDelegate);
        }
    }
}
