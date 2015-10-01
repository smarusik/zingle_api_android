package me.zingle.api.sdk.services;

import org.json.JSONObject;

import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleContactChannel;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleService;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleContactChannelServices extends ZingleBaseService<ZingleContactChannel>{

    final ZingleService parentService;
    final ZingleContact parentContact;

    public ZingleContactChannelServices(ZingleService parentService, ZingleContact parentContact) {
        this.parentService = parentService;
        this.parentContact = parentContact;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base=String.format("/services/%s/contact/%s/channels",parentService.getId(),parentContact.getId());

        if(specific)
            return base+"/%s";
        else
            return base;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return false;
    }

    @Override
    public ZingleContactChannel mapper(JSONObject source){
        ZingleContactChannel result=new ZingleContactChannel();

        result.setId(source.getString("id"));
        result.setValue(source.getString("value"));
        result.setCountry(source.getString("country"));
        result.setDisplayName(source.getString("display_name"));
        result.setFormattedValue(source.getString("formatted_value"));
        result.setIsDefault(source.getBoolean("is_default"));
        result.setIsDefaultForType(source.getBoolean("is_default_for_type"));

        ZingleChannelTypeServices channelTypeServices=new ZingleChannelTypeServices();
        result.setType(channelTypeServices.mapper(source.optJSONObject("channel_type")));

        return result;
    }

    @Override
    public ZingleList<ZingleContactChannel> list() {
        return null;
    }
}
