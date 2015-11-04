package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.model.ZingleTemplate;

/**
 * Created by SLAVA 10 2015.
 */
public class ZingleTemplateServices extends ZingleBaseService<ZingleTemplate> {
    final ZingleService parent;

    public ZingleTemplateServices(ZingleService parent) {
        this.parent = parent;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base=String.format("/services/%s/templates",parent.getId());
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
    public ZingleTemplate mapper(JSONObject source) throws MappingErrorEx {
        ZingleTemplate result=new ZingleTemplate();

        try {
            result.setId(source.getString("id"));
            result.setDisplayName(source.getString("display_name"));
            result.setIsGlobal(source.getBoolean("is_global"));
            result.setType(source.getString("type"));
            result.setSubject(source.getString("subject"));
            result.setBody(source.getString("body"));
        }catch (JSONException e) {
            e.printStackTrace();
            throw new MappingErrorEx(this.getClass().getName(),source.toString(),e.getMessage());
        }

        result.setService(parent);

        return result;

    }
}
