package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleSettingsField;

/**
 * Created by SLAVA 09 2015.
 */
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
}
