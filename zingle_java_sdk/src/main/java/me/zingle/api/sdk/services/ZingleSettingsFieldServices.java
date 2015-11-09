package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleSettingsField;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/settings_fields>ZingleSettingsField API</a>.
 * Supports none of basic functions.
 * */
public class ZingleSettingsFieldServices extends ZingleBaseService<ZingleSettingsField> {
    @Override
    protected String resourcePath(boolean specific) {
        return null;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return false;
    }

    @Override
    public ZingleSettingsField mapper(JSONObject source) throws MappingErrorEx {
        try {
            String id = source.getString("id");
            if (!id.isEmpty()) {
                ZingleSettingsField result = new ZingleSettingsField();
                result.setId(id);
                result.setDisplayName(source.optString("display_name"));
                result.setDataType(source.optString("data_type"));

                JSONArray optionsJSON=source.optJSONArray("options");
                ZingleFieldOptionServices optionServices=new ZingleFieldOptionServices();
                result.setOptions(optionServices.arrayMapper(optionsJSON));

                return result;
            } else
                return null;
        }catch(JSONException e){
            throw new MappingErrorEx(this.getClass().getSimpleName(),source.toString(),source.toString());
        }
    }

    @Override
    public ZingleSettingsField get(String id) {
        return null;
    }

    @Override
    public ZingleList<ZingleSettingsField> list() {
        return null;
    }

    @Override
    public ZingleList<ZingleSettingsField> list(List<QueryPart> conditions) {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }

    @Override
    public ZingleSettingsField create(ZingleSettingsField object) {
        return null;
    }

    @Override
    public ZingleSettingsField update(ZingleSettingsField object) {
        return null;
    }
}
