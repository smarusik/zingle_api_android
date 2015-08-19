package me.zingle.api.sdk.dto;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Map;

import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleContactCustomField;
import me.zingle.api.sdk.model.ZingleContactCustomFieldOption;
import me.zingle.api.sdk.model.ZingleLabel;
import me.zingle.api.sdk.model.ZingleMessage;
import me.zingle.api.sdk.model.ZingleService;

/**
 * Created by SLAVA 08 2015.
 */
public class RequestDTO {
    JSONObject data;

    public JSONObject getData() {
        return data;
    }

    public void fromService(ZingleService service){
        JSONStringer res=new JSONStringer();

        res.object();
        res.key("display_name").value(service.getDisplayName());
        if(service.getTimeZone()!=null)
            res.key("time_zone").value(service.getTimeZone().getDisplayName());
        if(service.getPlan()!=null)
            res.key("plan_id").value(service.getPlan().getId());
        if(service.getPhoneNumber()!=null)
            res.key("phone_number").value(service.getPhoneNumber().getFullNumber());
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

    public void fromContact(ZingleContact contact){
        JSONStringer res=new JSONStringer();

        res.object();

        if(contact.getPhoneNumber()!=null)
            res.key("phone_number").value(contact.getPhoneNumber().getFullNumber());

        if(contact.getCustomFieldValues()!=null) {
            res.key("custom_field_values");
            res.array();
            for (Map.Entry<ZingleContactCustomField, String> entry : contact.getCustomFieldValues().entrySet()) {
                res.object().key(String.valueOf(entry.getKey().getId())).value(entry.getValue()).endObject();
            }
            res.endArray();
        }

        res.endObject();

        data=new JSONObject(res.toString());
    }

    public void fromContactCustomField(ZingleContactCustomField contactCustomField){
        JSONStringer res=new JSONStringer();

        res.object();

        res.key("display_name").value(contactCustomField.getDisplayName());

        if(contactCustomField.getOptions()!=null) {
            res.key("options");
            res.array();
            for (ZingleContactCustomFieldOption option : contactCustomField.getOptions()) {
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

        res.key("type").value("sms");

        if(message.getServicePhoneNumber()!=null)
            res.key("recipient").value(message.getServicePhoneNumber().getFullNumber());

        res.key("body").value(message.getBody());

        res.endObject();

        data=new JSONObject(res.toString());
    }
}
