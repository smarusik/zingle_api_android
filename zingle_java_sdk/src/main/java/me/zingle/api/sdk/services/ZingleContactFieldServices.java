package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleContactField;
import me.zingle.api.sdk.model.ZingleService;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/contact_custom_fields>ZingleContactField API</a>).
 * Supports all base functions.
 */
public class ZingleContactFieldServices extends ZingleBaseService<ZingleContactField> {

    final ZingleService parent;

    public ZingleContactFieldServices(ZingleService parent) {
        this.parent = parent;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base=String.format("/services/%s/contact-custom-fields",parent.getId());
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
    public ZingleContactField mapper(JSONObject source) throws MappingErrorEx {
        ZingleContactField mapResult=new ZingleContactField();

        try {
            mapResult.setId(source.getString("id"));
            mapResult.setDisplayName(source.getString("display_name"));
            mapResult.setDataType(source.optString("data_type"));
            mapResult.setIsGlobal(source.optBoolean("is_global"));

            mapResult.setService(parent);

            JSONArray optionsJS = source.getJSONArray("options");

            if(optionsJS!=null) {
                ZingleFieldOptionServices optionServices = new ZingleFieldOptionServices();
                mapResult.setOptions(optionServices.arrayMapper(optionsJS));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw new MappingErrorEx(this.getClass().getName(),source.toString(),e.getMessage());
        }

        return mapResult;
    }

}
