package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

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

        JSONStringer res = new JSONStringer();

        res.object();

        res.key("channel_type_id").value(getType().getId());
        res.key("value").value(getValue());
        res.key("country").value(getCountry());


        if (!getDisplayName().isEmpty())
            res.key("display_name").value(getDisplayName());

        if (getIsDefaultForType() != null)
            res.key("is_default_for_type").value(getIsDefaultForType());

        if (getIsDefault() != null)
            res.key("is_default").value(getIsDefault());

        res.endObject();

        return new JSONObject(res.toString());
    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();

        JSONStringer res = new JSONStringer();

        res.object();

        if (!getDisplayName().isEmpty())
            res.key("display_name").value(getDisplayName());

        if (getIsDefault() != null)
            res.key("is_default").value(getIsDefault());

        if (getIsDefaultForType() != null)
            res.key("is_default_for_type").value(getIsDefaultForType());

        res.endObject();

        return new JSONObject(res.toString());

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
