package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.model.ZingleFieldOption;
import me.zingle.api.sdk.model.ZingleList;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/field_options>ZingleFieldOption API</a>).
 * Supports none of basic functions. Used for mapping internal objects.
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

    @Override
    public ZingleFieldOption get(String id) {
        return null;
    }

    @Override
    public ZingleList<ZingleFieldOption> list() {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }

    @Override
    public ZingleFieldOption create(ZingleFieldOption object) {
        return null;
    }

    @Override
    public ZingleFieldOption update(ZingleFieldOption object) {
        return null;
    }

    @Override
    public ZingleList<ZingleFieldOption> list(List<QueryPart> conditions) {
        return null;
    }
}
