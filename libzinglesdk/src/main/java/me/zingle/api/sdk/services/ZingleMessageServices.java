package me.zingle.api.sdk.services;

import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.model.ZingleMessage;
import me.zingle.api.sdk.model.ZingleService;

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

        result.setId(source.getString("id"));
        result.setBody(source.optString("body"));
        result.setCreatedAt(source.getInt("created_at"));
        result.setReadAt(source.optInt("read_at"));
        result.setSenderType(source.getString("sender_type"));

        ZingleCorrespondentServices correspondentServices=new ZingleCorrespondentServices();
        result.setSender(correspondentServices.mapper(source.getJSONObject("sender")));

        result.setRecipientType(source.getString("recipient_type"));
        result.setRecipient(correspondentServices.mapper(source.getJSONObject("recipient")));

        result.setCommunicationDirection(source.optString("communication_direction"));
        result.setBodyLanguageCode(source.optString("body_language_code"));
        result.setTriggeredByUserId(source.optString("triggered_by_user_id"));
        result.setTranslatedBody(source.optString("translated_body"));
        result.setTemplateId(source.optString("template_id"));
        result.setTranslatedBodyLanguageCode(source.optString("translated_body_language_code"));

        ZingleAttachmentService attachmentService=new ZingleAttachmentService();
        result.setAttachments(attachmentService.arrayMapper(source.getJSONArray("attachments")));

        return result;
    }

    public ZingleMessage markRead(ZingleMessage message){
        return update(message);
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
