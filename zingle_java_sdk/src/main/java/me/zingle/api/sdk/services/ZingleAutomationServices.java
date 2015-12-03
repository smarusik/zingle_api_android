package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleAutomation;
import me.zingle.api.sdk.model.ZingleService;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/automations>ZingleAutomation API</a>).
 * Supports all base functions, except creation.
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

        try {
            result.setId(source.getString("id"));
            result.setDisplayName(source.getString("display_name"));
            result.setIsGlobal(source.getBoolean("is_global"));
            result.setType(source.getString("type"));
            result.setStatus(source.getString("status"));

        } catch (JSONException e) {
            e.printStackTrace();
            throw new MappingErrorEx(this.getClass().getName(),source.toString(),e.getMessage());
        }
        result.setService(parent);

        return result;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleAutomation create(ZingleAutomation object) {
        return null;
    }
}
