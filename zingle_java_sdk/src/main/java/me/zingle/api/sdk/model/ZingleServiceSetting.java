package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/service_settings#service-settings-object>Service Settings Object</a>
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

    public String getId() {
        return settingsField.getId();
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

        JSONObject resJS=new JSONObject();

        try {

            if(settingsField.getOptions().isEmpty()){
                resJS.put("value",value.toString());
            }
            else{
                resJS.put("settings_field_option_id",settingsFieldOptionId);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ZingleServiceSetting that = (ZingleServiceSetting) o;

        if (!service.equals(that.service)) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (settingsFieldOptionId != null ? !settingsFieldOptionId.equals(that.settingsFieldOptionId) : that.settingsFieldOptionId != null)
            return false;
        return settingsField.equals(that.settingsField);

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleServiceSetting{");
        sb.append("\n    value=").append(value);
        sb.append("\n    settingsFieldOptionId=").append(settingsFieldOptionId);
        sb.append("\n    settingsField=").append(settingsField);
        sb.append("}\n");
        return sb.toString();
    }
}
