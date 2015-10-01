package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.model.ZingleChannelType;
import me.zingle.api.sdk.model.ZingleList;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleChannelTypeServices extends ZingleBaseService<ZingleChannelType> {
    @Override
    protected String resourcePath(boolean specific) {
        return null;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return false;
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

                return result;
            } else
                return null;
        }catch(JSONException e){
            throw new MappingErrorEx(this.getClass().getSimpleName(),source.toString(),source.toString());
        }
    }

    @Override
    public ZingleChannelType get(String id) {
        return null;
    }

    @Override
    public boolean getAsync(String id, ServiceDelegate<ZingleChannelType> delegate) {
        return false;
    }

    @Override
    public boolean getAsync(String id) {
        return false;
    }

    @Override
    public ZingleList<ZingleChannelType> list() {
        return null;
    }

    @Override
    public boolean listAsync(ServiceDelegate<ZingleList<ZingleChannelType>> delegate) {
        return false;
    }

    @Override
    public boolean listAsync() {
        return false;
    }

    @Override
    protected ZingleList<ZingleChannelType> list(List<QueryPart> conditions) {
        return null;
    }

    @Override
    protected boolean listAsync(List<QueryPart> conditions, ServiceDelegate<ZingleList<ZingleChannelType>> delegate) {
        return false;
    }

    @Override
    protected boolean listAsync(List<QueryPart> conditions) {
        return false;
    }
}
