package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 09 2015.
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

    public ZingleServiceChannel(String id, ZingleChannelType type) {
        this.id = id;
        this.type = type;
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

        JSONStringer res = new JSONStringer();

        res.object();

        res.key("channel_type_id").value(getType().getId());
        res.key("value").value(getValue());
        res.key("country").value(getCountry());


        if (!getDisplayName().isEmpty())
            res.key("display_name").value(getDisplayName());

        if (getIsDefaultForType() != null)
            res.key("is_default_for_type").value(getIsDefaultForType());

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
        if ((getType() == null || getType().getId().isEmpty()) && getIsDefaultForType()==null) {
            throw new RequestBodyCreationEx(RequestMethods.PUT, "type,is_default_for_type", "ZingleServiceChannel.type,isDefaultForType");
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ZingleServiceChannel{");
        sb.append("service=").append(service);
        sb.append(", id='").append(id).append('\'');
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", type=").append(type);
        sb.append(", isDefaultForType=").append(isDefaultForType);
        sb.append(", value='").append(value).append('\'');
        sb.append(", formattedValue='").append(formattedValue).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
