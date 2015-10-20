package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.model.ZingleContactFieldValue;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleService;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleContactFieldValueService extends ZingleBaseService<ZingleContactFieldValue> {

    final ZingleService parent;

    public ZingleContactFieldValueService(ZingleService parent) {
        this.parent = parent;
    }

    @Override
    protected String resourcePath(boolean specific) {
        return null;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return false;
    }

    @Override
    public ZingleContactFieldValue mapper(JSONObject source) throws MappingErrorEx {
        ZingleContactFieldValue result=new ZingleContactFieldValue();

        try {
            result.setValue(source.get("value"));
            result.setSelectedFieldOptionId(source.optString("selected_custom_field_option_id"));

            ZingleContactFieldServices contactFieldServices = new ZingleContactFieldServices(parent);
            result.setContactField(contactFieldServices.mapper(source.getJSONObject("custom_field")));

        }catch (JSONException e) {
            e.printStackTrace();
            throw new MappingErrorEx(this.getClass().getName(),source.toString(),e.getMessage());
        }

        return result;
    }

    @Override
    public ZingleList<ZingleContactFieldValue> list(List<QueryPart> conditions) {
        return super.list(conditions);
    }

    @Override
    public ZingleList<ZingleContactFieldValue> list() {
        return super.list();
    }

    @Override
    public Boolean delete(String id) {
        return super.delete(id);
    }

    @Override
    public ZingleContactFieldValue create(ZingleContactFieldValue object) {
        return super.create(object);
    }

    @Override
    public ZingleContactFieldValue update(ZingleContactFieldValue object) {
        return super.update(object);
    }
}
