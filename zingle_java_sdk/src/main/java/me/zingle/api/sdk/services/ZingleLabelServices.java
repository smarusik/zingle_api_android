package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleLabel;
import me.zingle.api.sdk.model.ZingleService;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/labels>ZingleLabel API</a>).
 * Supports all basic functions.
 * functional.
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

        try {
            result.setId(source.getString("id"));
            result.setDisplayName(source.getString("display_name"));
            result.setIsGlobal(source.optBoolean("is_global"));

            String bgc = source.optString("background_color");
            if (!bgc.isEmpty())
                result.setBackgroundColor(bgc);

            String tc = source.optString("text_color");
            if (!tc.isEmpty())
                result.setTextColor(tc);

            result.setService(parent);
        }catch (JSONException e) {
            e.printStackTrace();
            throw new MappingErrorEx(this.getClass().getName(),source.toString(),e.getMessage());
        }

        return result;
    }
}
