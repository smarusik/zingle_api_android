package me.zingle.api.sdk.services;

import org.json.JSONObject;

import java.awt.Color;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleLabel;
import me.zingle.api.sdk.model.ZingleService;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleLabelServices extends ZingleBaseService<ZingleLabel> {

    final ZingleService parent;

    public ZingleLabelServices(ZingleService parent) {
        this.parent = parent;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base=String.format("/services/%s/contact-labels",parent.getId());

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
                ||modifier.equals("display_name")
                ||modifier.equals("is_global")
                ;
    }

    @Override
    public ZingleLabel mapper(JSONObject source) throws MappingErrorEx {
        ZingleLabel result=new ZingleLabel();

        result.setId(source.getString("id"));
        result.setDisplayName(source.getString("display_name"));
        result.setIsGlobal(source.optBoolean("is_global"));

        if(!source.get("background_color").equals(null))
            result.setBackgroundColor(new Color(Integer.parseInt(source.getString("background_color").substring(1), 16)));

        if(!source.get("text_color").equals(null))
            result.setTextColor(new Color(Integer.parseInt(source.getString("text_color").substring(1), 16)));

        result.setService(parent);

        return result;
    }
}
