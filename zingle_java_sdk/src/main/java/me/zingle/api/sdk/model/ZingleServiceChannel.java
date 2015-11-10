package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/service_channels#service-channels-object>Service Channels Object</a>
 */
public class ZingleServiceChannel extends ZingleBaseModel{

    ZingleService service;

    private String id;
    private String displayName;
    private ZingleChannelType type;
    private Boolean isDefaultForType;
    private String value;
    private String formattedValue;
    private String country;

    public ZingleServiceChannel() {
    }

    public ZingleServiceChannel(ZingleChannelType type, String value, String country) {
        this.type = type;
        this.value = value;
        this.country = country;
    }

    public ZingleServiceChannel(String displayName, ZingleChannelType type, Boolean isDefaultForType, String value,String country) {
        this.displayName = displayName;
        this.type = type;
        this.isDefaultForType = isDefaultForType;
        this.value = value;
        this.country = country;
    }

    public ZingleService getService() {
        return service;
    }

    public void setService(ZingleService service) {
        this.service = service;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ZingleChannelType getType() {
        return type;
    }

    public void setType(ZingleChannelType type) {
        this.type = type;
    }

    public Boolean getIsDefaultForType() {
        return isDefaultForType;
    }

    public void setIsDefaultForType(Boolean isDefaultForType) {
        this.isDefaultForType = isDefaultForType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFormattedValue() {
        return formattedValue;
    }

    public void setFormattedValue(String formattedValue) {
        this.formattedValue = formattedValue;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public JSONObject extractCreationData() {

        checkForCreate();

        JSONObject resJS=new JSONObject();

        try {
            resJS.put("channel_type_id", getType().getId());
            resJS.put("value", getValue());
            resJS.put("country", getCountry());


            if (getDisplayName()!=null && !getDisplayName().isEmpty())
                resJS.put("display_name", getDisplayName());

            if (getIsDefaultForType() != null)
                resJS.put("is_default_for_type", getIsDefaultForType());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();

        JSONObject resJS=new JSONObject();

        try {
            if (!getDisplayName().isEmpty())
                resJS.put("display_name",getDisplayName());

            if (getIsDefaultForType() != null)
                resJS.put("is_default_for_type",getIsDefaultForType());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public void checkForCreate() {
        if (getType() == null || getType().getId().isEmpty()) {
            throw new RequestBodyCreationEx(RequestMethods.POST, "channel_type_id", "ZingleServiceChannel.type.id");
        }

        if (getValue().isEmpty()) {
            throw new RequestBodyCreationEx(RequestMethods.POST, "value", "ZingleServiceChannel.value");
        }

        if (getCountry().isEmpty()) {
            throw new RequestBodyCreationEx(RequestMethods.POST, "country", "ZingleServiceChannel.country");
        }
    }

    @Override
    public void checkForUpdate() {
        if ((getType() == null || getType().getId().isEmpty()) && getIsDefaultForType()==null) {
            throw new RequestBodyCreationEx(RequestMethods.PUT, "type,is_default_for_type", "ZingleServiceChannel.type,isDefaultForType");
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleServiceChannel{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("\n    type=").append(type);
        sb.append("\n    isDefaultForType=").append(isDefaultForType);
        sb.append("\n    value='").append(value).append('\'');
        sb.append("\n    formattedValue='").append(formattedValue).append('\'');
        sb.append("\n    country='").append(country).append('\'');
        sb.append("}\n");
        return sb.toString();
    }
}
