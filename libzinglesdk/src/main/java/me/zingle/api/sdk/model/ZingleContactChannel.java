package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleContactChannel {
    private int id;
    private ZingleChannelType type;
    private String displayName;
    private String value;
    private Boolean is_default;
    private Boolean is_default_for_type;

    public ZingleContactChannel() {
    }

    public ZingleContactChannel(int id, ZingleChannelType type, String displayName, String value, Boolean is_default, Boolean is_default_for_type) {
        this.id = id;
        this.type = type;
        this.displayName = displayName;
        this.value = value;
        this.is_default = is_default;
        this.is_default_for_type = is_default_for_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ZingleChannelType getType() {
        return type;
    }

    public void setType(ZingleChannelType type) {
        this.type = type;
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

    public Boolean getIs_default() {
        return is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }

    public Boolean getIs_default_for_type() {
        return is_default_for_type;
    }

    public void setIs_default_for_type(Boolean is_default_for_type) {
        this.is_default_for_type = is_default_for_type;
    }
}
