package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleChannelType;
import me.zingle.api.sdk.model.ZingleService;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/channel_types>ZingleChannelType API</a>).
 * Supports all base functions.
 */
public class ZingleChannelTypeServices extends ZingleBaseService<ZingleChannelType> {

    final ZingleService parentService;

    public ZingleChannelTypeServices(ZingleService parentService) {
        this.parentService = parentService;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base = String.format("/services/%s/channel-types", parentService.getId());

        if (specific)
            return base + "/%s";
        else
            return base;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return modifier.equals("page")
                ||modifier.equals("page_size")
                ||modifier.equals("sort_field")
                ||modifier.equals("sort_direction")
                ;
    }


    @Override
    public ZingleChannelType mapper(JSONObject source) throws MappingErrorEx {
        try {
            String id = source.getString("id");
            if (!id.isEmpty()) {
                ZingleChannelType result = new ZingleChannelType();
                result.setId(id);
                result.setDisplayName(source.optString("display_name"));
                result.setTypeclass(source.optString("type_class"));
                result.setInboundNotificationUrl(source.optString("inbound_notification_url"));
                result.setOutboundNotificationUrl(source.optString("outbound_notification_url"));
                result.setAllowCommunications(source.optBoolean("allow_communications"));
                result.setIsGlobal(source.optBoolean("is_global"));
                result.setPriority(source.optInt("priority"));

                return result;
            } else
                return null;
        } catch (JSONException e) {
            throw new MappingErrorEx(this.getClass().getSimpleName(), source.toString(), source.toString());
        }
    }
}