package me.zingle.api.sdk.dto;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;
import me.zingle.api.sdk.model.ZingleAttachment;
import me.zingle.api.sdk.model.ZingleContactChannel;
import me.zingle.api.sdk.model.ZingleContactField;
import me.zingle.api.sdk.model.ZingleCorrespondent;
import me.zingle.api.sdk.model.ZingleFieldOption;
import me.zingle.api.sdk.model.ZingleLabel;
import me.zingle.api.sdk.model.ZingleMessage;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.model.ZingleServiceChannel;



/**
 * Created by SLAVA 08 2015.
 */
public class RequestDTO {
    JSONObject data;

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public void fromService(ZingleService service){
        JSONStringer res=new JSONStringer();

        res.object();



        res.key("display_name").value(service.getDisplayName());
        if(service.getTimeZone()!=null)
            res.key("time_zone").value(service.getTimeZone().getDisplayName());
        if(service.getPlan()!=null)
            res.key("plan_id").value(service.getPlan().getId());

        List<ZingleServiceChannel> channels=service.getChannels();
        if(channels!=null) {
            for(ZingleServiceChannel ch:channels){
/*
                if(ch.getType().equals(CHANNEL_TYPE_PHONE_NUMBER))
                    res.key("phone_number").value(ch.getValue());
*/
            }
        }

        //res.key("inbound_sms_webhook_url").value("https://my-inbound-sms-handler");
        if(service.getAddress()!=null) {
            res.key("service_address");
            res.object();
            res.key("address").value(service.getAddress().getAddress());
            res.key("city").value(service.getAddress().getCity());
            res.key("state").value(service.getAddress().getState());
            res.key("country").value(service.getAddress().getCountry());
            res.key("postal_code").value(service.getAddress().getPostalCode());
            res.endObject();
        }
        res.endObject();

        data=new JSONObject(res.toString());
    }

    public void fromContactChannel(ZingleContactChannel channel){
        JSONStringer res=new JSONStringer();

        res.object();
                res.key("type").value(channel.getType());
                res.key("value").value(channel.getFormattedValue());
        res.endObject();

        data=new JSONObject(res.toString());

    }

   /* public void fromContact(ZingleContact contact){
        JSONStringer res=new JSONStringer();

        res.object();

        res.key("channels");
        res.array();
        List<ZingleContactChannel> channels=contact.getChannels();
        if(channels!=null) {
            for(ZingleContactChannel ch:channels){
                res.object();
                res.key("type").value(ch.getType());
                res.key("value").value(ch.getFormattedValue());
                res.endObject();
            }
        }
        res.endArray();

        if(contact.isConfirmed()!=null)
            res.key("is_confirmed").value(contact.isConfirmed());
        if(contact.isStarred()!=null)
            res.key("is_starred").value(contact.isStarred());


        if(contact.getCustomFieldValues()!=null) {
            res.key("custom_field_values");
            res.array();
            for (Map.Entry<ZingleContactField, String> entry : contact.getCustomFieldValues().entrySet()) {
                res.object();
                res.key("customFieldId");
                res.value(entry.getKey().getId());
                res.key("value");
                res.value(entry.getValue());
                res.endObject();
            }
            res.endArray();
        }

        if(contact.getLabels()!=null) {
            res.key("labels");
            res.array();
            for (ZingleLabel entry : contact.getLabels()) {
                res.object();
                res.key("id").value(entry.getId());
                res.key("display_name").value(entry.getDisplayName());
                res.endObject();
            }
            res.endArray();
        }

        res.endObject();

        data=new JSONObject(res.toString());
    }*/

    public void fromContactCustomField(ZingleContactField contactCustomField){
        JSONStringer res=new JSONStringer();

        res.object();

        res.key("display_name").value(contactCustomField.getDisplayName());

        if(contactCustomField.getOptions()!=null) {
            res.key("options");
            res.array();
            for (ZingleFieldOption option : contactCustomField.getOptions()) {
                res.object();
                res.key("display_name").value(option.getDisplayName());
                res.key("value").value(option.getValue());
                res.key("sort_order").value(option.getSortOrder());
                res.endObject();
            }
            res.endArray();
        }

        res.endObject();

        data=new JSONObject(res.toString());
    }

    public void fromLabel(ZingleLabel label){
        JSONStringer res=new JSONStringer();

        res.object();

        res.key("display_name").value(label.getDisplayName());
        if(label.getBackgroundColor()!=null)
            res.key("background_color").value("#"+Integer.toHexString(label.getBackgroundColor().getRGB()).substring(2));
        if(label.getTextColor()!=null)
            res.key("text_color").value("#"+Integer.toHexString(label.getTextColor().getRGB()).substring(2));

        res.endObject();

        data=new JSONObject(res.toString());
    }

    public void fromMessage(ZingleMessage message){
        JSONStringer res=new JSONStringer();

        res.object();

        res.key("sender");//Sender object
        res.object();
            ZingleCorrespondent sender=message.getSender();
            if(sender!=null){
                res.key("type").value(sender.getType());
                res.key("id").value(sender.getParticipantId());
                res.key("channel");
                    res.object();
                    res.key("type").value(sender.getChannelType());
                    res.key("value").value(sender.getChannelValue());
                    res.endObject();
            }
        res.endObject();

        res.key("recipient");//recipient object
        res.object();
        ZingleCorrespondent recipient=message.getSender();
        if(sender!=null){
            res.key("type").value(recipient.getType());
            res.key("id").value(recipient.getParticipantId());
            res.key("channel");
            res.object();
            res.key("type").value(recipient.getChannelType());
            res.key("value").value(recipient.getChannelValue());
            res.endObject();
        }
        res.endObject();

        res.key("body").value(message.getBody());

        res.key("attachments");
        res.array();
        for(ZingleAttachment att:message.getAttachments()){
            res.object();
            res.key("mime-type").value(att.getMimeType());
            res.key("base64").value(att.getData());
            res.endObject();
        }
        res.endArray();
        res.endObject();

        data=new JSONObject(res.toString());
    }

    public void fromServiceChannel(ZingleServiceChannel serviceChannel, RequestMethods requestMethod){

        if(requestMethod==RequestMethods.POST) {
            if (serviceChannel.getType() == null || serviceChannel.getType().getId().isEmpty()) {
                throw new RequestBodyCreationEx(requestMethod, "channel_type_id", "ZingleServiceChannel.type.id");
            }

            if (serviceChannel.getValue().isEmpty()) {
                throw new RequestBodyCreationEx(requestMethod, "value", "ZingleServiceChannel.value");
            }

            if (serviceChannel.getCountry().isEmpty()) {
                throw new RequestBodyCreationEx(requestMethod, "country", "ZingleServiceChannel.country");
            }

            JSONStringer res = new JSONStringer();

            res.object();

            res.key("channel_type_id").value(serviceChannel.getType().getId());
            res.key("value").value(serviceChannel.getValue());
            res.key("country").value(serviceChannel.getCountry());


            if (!serviceChannel.getDisplayName().isEmpty())
                res.key("display_name").value(serviceChannel.getDisplayName());

            if (serviceChannel.getIsDefaultForType() != null)
                res.key("is_default_for_type").value(serviceChannel.getIsDefaultForType());

            res.endObject();
        }
        else if(requestMethod==RequestMethods.PUT){

            JSONStringer res = new JSONStringer();

            res.object();

            if (!serviceChannel.getDisplayName().isEmpty())
                res.key("display_name").value(serviceChannel.getDisplayName());

            if (serviceChannel.getIsDefaultForType() != null)
                res.key("is_default_for_type").value(serviceChannel.getIsDefaultForType());

            res.endObject();
        }

    }
}
