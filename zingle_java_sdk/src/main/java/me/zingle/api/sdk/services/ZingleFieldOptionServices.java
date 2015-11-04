package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleFieldOption;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleFieldOptionServices extends ZingleBaseService<ZingleFieldOption> {
    @Override
    protected String resourcePath(boolean specific) {
        return null;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return false;
    }

    @Override
    public ZingleFieldOption mapper(JSONObject source) throws MappingErrorEx {
        try {
            String id = source.getString("id");
            if (!id.isEmpty()) {
                ZingleFieldOption result = new ZingleFieldOption();
                result.setId(id);
                result.setDisplayName(source.optString("display_name"));
                result.setValue(source.optString("value"));
                result.setSortOrder(source.optInt("sort_order"));

                return result;
            } else
                return null;
        }catch(JSONException e){
            throw new MappingErrorEx(this.getClass().getSimpleName(),source.toString(),source.toString());
        }
    }
}
