package me.zingle.api.sdk.services;

import org.json.JSONObject;

import java.util.List;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.model.ZingleCorrespondent;
import me.zingle.api.sdk.model.ZingleList;

/**
 * Created by SLAVA 10 2015.
 */
public class ZingleCorrespondentServices extends ZingleBaseService<ZingleCorrespondent> {
    @Override
    protected String resourcePath(boolean specific) {
        return null;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return false;
    }

    @Override
    public ZingleCorrespondent mapper(JSONObject source) throws MappingErrorEx {
        ZingleCorrespondent result=new ZingleCorrespondent();

        result.setId(source.optString("id"));

        JSONObject channelJSON = source.optJSONObject("channel");

        if(channelJSON!=null) {
            result.setChannelTypeClass(channelJSON.optString("type"));
            result.setChannelValue(channelJSON.optString("value"));
        }

        return result;

    }

    @Override
    public ZingleCorrespondent get(String id) {
        return null;
    }

    @Override
    public ZingleList<ZingleCorrespondent> list() {
        return null;
    }

    @Override
    public ZingleList<ZingleCorrespondent> list(List<QueryPart> conditions) {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return false;
    }

    @Override
    public ZingleCorrespondent create(ZingleCorrespondent object) {
        return null;
    }

    @Override
    public ZingleCorrespondent update(ZingleCorrespondent object) {
        return null;
    }
}