package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.model.ZingleServiceChannel;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleServiceChannelServices extends ZingleBaseService<ZingleServiceChannel> {

    private final ZingleService parent;

    public ZingleServiceChannelServices(ZingleService parent) {
        this.parent = parent;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base=String.format("/services/%s/channels",parent.getId());

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
    public ZingleServiceChannel mapper(JSONObject source) throws MappingErrorEx {
        try {
            String id = source.getString("id");
            if (!id.isEmpty()) {
                ZingleServiceChannel result = new ZingleServiceChannel();
                result.setId(id);
                result.setDisplayName(source.optString("display_name"));
                result.setValue(source.optString("value"));
                result.setFormattedValue(source.optString("formatted_value"));
                result.setCountry(source.optString("country"));
                result.setIsDefaultForType(source.optBoolean("is_default_for_type"));

                ZingleChannelTypeServices channelTypeServices=new ZingleChannelTypeServices();
                result.setType(channelTypeServices.mapper(source.optJSONObject("channel_type")));

                result.setService(parent);

                return result;
            } else
                return null;
        }catch(JSONException e){
            throw new MappingErrorEx(this.getClass().getSimpleName(),source.toString(),source.toString());
        }
    }
}
