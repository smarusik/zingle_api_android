package me.zingle.api.sdk.services;

import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleAutomation;
import me.zingle.api.sdk.model.ZingleService;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleAutomationServices extends ZingleBaseService<ZingleAutomation>{

    private final ZingleService parent;

    public ZingleAutomationServices(ZingleService parent) {
        this.parent = parent;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base=String.format("/services/%s/automations",parent.getId());
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
                ;
    }

    @Override
    public ZingleAutomation mapper(JSONObject source) throws MappingErrorEx {
        ZingleAutomation result=new ZingleAutomation();

        result.setId(source.getString("id"));
        result.setDisplayName(source.getString("display_name"));
        result.setIsGlobal(source.getBoolean("is_global"));
        result.setType(source.getString("type"));
        result.setStatus(source.getString("status"));

        result.setService(parent);

        return result;
    }

    @Override
    public ZingleAutomation create(ZingleAutomation object) {
        return null;
    }
}
