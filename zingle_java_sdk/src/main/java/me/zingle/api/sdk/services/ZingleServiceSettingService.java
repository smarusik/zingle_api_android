package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.model.ZingleServiceSetting;

/**
 * Created by SLAVA 09 2015.
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

    @Override
    public ZingleList<ZingleServiceSetting> list() {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return false;
    }

    @Override
    public ZingleServiceSetting create(ZingleServiceSetting object) {
        return null;
    }

    @Override
    public ZingleServiceSetting get(String id) {
        return null;
    }

    @Override
    public ZingleServiceSetting update(ZingleServiceSetting object) {
        return null;
    }
}
