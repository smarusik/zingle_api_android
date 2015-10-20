package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleContactChannel extends ZingleBaseModel{
    private String id;
    private String displayName;
    private String value;
    private String formattedValue;
    private String country;
    private Boolean isDefault;
    private Boolean isDefaultForType;
    private ZingleChannelType type;

    public ZingleContactChannel() {
    }

    public ZingleContactChannel(String value, ZingleChannelType type, String country) {
        this.value = value;
        this.type = type;
        this.country = country;
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

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsDefaultForType() {
        return isDefaultForType;
    }

    public void setIsDefaultForType(Boolean isDefaultForType) {
        this.isDefaultForType = isDefaultForType;
    }

    public ZingleChannelType getType() {
        return type;
    }

    public void setType(ZingleChannelType type) {
        this.type = type;
    }

    @Override
    public JSONObject extractCreationData() {

        checkForCreate();

        JSONObject resJS=new JSONObject();

        try {

            resJS.put("channel_type_id", getType().getId());
            resJS.put("value", getValue());
            resJS.put("country", getCountry());


            if (!(getDisplayName()==null || getDisplayName().isEmpty()))
                resJS.put("display_name", getDisplayName());

            if (getIsDefaultForType() != null)
                resJS.put("is_default_for_type", getIsDefaultForType());

            if (getIsDefault() != null)
                resJS.put("is_default", getIsDefault());

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

            if (!(getDisplayName()==null || getDisplayName().isEmpty()))
                resJS.put("display_name",getDisplayName());

            if (getIsDefault() != null)
                resJS.put("is_default",getIsDefault());

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
        if(displayName==null && isDefault==null && isDefaultForType==null){
            throw new RequestBodyCreationEx(RequestMethods.PUT, "displayName,isDefault,isDefaultForType",
                    "ZingleServiceChannel.display_name,is_default,is_default_for_type");
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleContactChannel{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("\n    value='").append(value).append('\'');
        sb.append("\n    formattedValue='").append(formattedValue).append('\'');
        sb.append("\n    country='").append(country).append('\'');
        sb.append("\n    isDefault=").append(isDefault);
        sb.append("\n    isDefaultForType=").append(isDefaultForType);
        sb.append("\n    type=").append(type);
        sb.append("}\n");
        return sb.toString();
    }
}
