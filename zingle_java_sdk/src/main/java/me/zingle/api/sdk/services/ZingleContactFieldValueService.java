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
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/custom_field_values>ZingleContactFieldValue API</a>).
 * Supports only creation functions.
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

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleList<ZingleContactFieldValue> list(List<QueryPart> conditions) {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleList<ZingleContactFieldValue> list() {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public Boolean delete(String id) {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleContactFieldValue update(ZingleContactFieldValue object) {
        return null;
    }
}
