package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleServiceSetting extends ZingleBaseModel {

    private ZingleService service;

    private Object value;
    private Integer settingsFieldOptionId; //Index of option in List in object in next field
    private ZingleSettingsField settingsField;

    public ZingleServiceSetting() {
    }

    public ZingleServiceSetting(Object value, Integer settingsFieldOptionId, ZingleSettingsField settingsField) {
        this.value = value;
        this.settingsFieldOptionId = settingsFieldOptionId;
        this.settingsField = settingsField;
    }

    public ZingleService getService() {
        return service;
    }

    public void setService(ZingleService service) {
        this.service = service;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Integer getSettingsFieldOptionId() {
        return settingsFieldOptionId;
    }

    public void setSettingsFieldOptionId(Integer settingsFieldOptionId) {
        this.settingsFieldOptionId = settingsFieldOptionId;
    }

    public ZingleSettingsField getSettingsField() {
        return settingsField;
    }

    public void setSettingsField(ZingleSettingsField settingsField) {
        this.settingsField = settingsField;
    }

    @Override
    public JSONObject extractCreationData() {
        checkForCreate();

        JSONStringer res = new JSONStringer();

        res.object();

        if(settingsField.getOptions().isEmpty()){
            res.key("value").value(value.toString());
        }
        else{
            res.key("settings_field_option_id").value(settingsFieldOptionId);
        }

        res.endObject();

        return new JSONObject(res.toString());
    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
    }

    @Override
    public void checkForCreate() {
        if(settingsField.getOptions().isEmpty()){
            if(value==null){
                throw new RequestBodyCreationEx(RequestMethods.POST,"value","ZingleServiceSetting.value");
            }
        }
        else{
            if(settingsFieldOptionId==null){
                throw new RequestBodyCreationEx(RequestMethods.POST,"settingsFieldOptionId","ZingleServiceSetting.settingsFieldOptionId");
            }
        }
    }

    @Override
    public void checkForUpdate() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ZingleServiceSetting{");
        sb.append("service=").append(service);
        sb.append(", value=").append(value);
        sb.append(", settingsFieldOptionId=").append(settingsFieldOptionId);
        sb.append(", settingsField=").append(settingsField);
        sb.append('}');
        return sb.toString();
    }
}
