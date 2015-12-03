package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.model.ZingleServiceSetting;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/service_settings>ZingleServiceSetting API</a>.
 * Supports only create function.
 */
public class ZingleServiceSettingService extends ZingleBaseService<ZingleServiceSetting> {

    private final ZingleService parent;

    public ZingleServiceSettingService(ZingleService parent) {
        this.parent = parent;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base=String.format("/services/%s/settings",parent.getId());

        if(specific)
            return base+"/%s";
        else
            return null;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return false;
    }

    @Override
    public ZingleServiceSetting mapper(JSONObject source) throws MappingErrorEx {
        try {
            ZingleServiceSetting result = new ZingleServiceSetting();
            result.setValue(source.get("value"));//NEED to be thoroughly checked
            result.setSettingsFieldOptionId(source.optInt("settings_field_option_id"));

            ZingleSettingsFieldServices settingsFieldServices=new ZingleSettingsFieldServices();
            result.setSettingsField(settingsFieldServices.mapper(source.optJSONObject("settings_field")));

            result.setService(parent);

            return result;
        }catch(JSONException e){
            throw new MappingErrorEx(this.getClass().getSimpleName(),source.toString(),source.toString());
        }
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleList<ZingleServiceSetting> list() {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleList<ZingleServiceSetting> list(List<QueryPart> conditions) {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>false</i>.
     */
    @Override
    public Boolean delete(String id) {
        return false;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleServiceSetting get(String id) {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleServiceSetting update(ZingleServiceSetting object) {
        return null;
    }
}
