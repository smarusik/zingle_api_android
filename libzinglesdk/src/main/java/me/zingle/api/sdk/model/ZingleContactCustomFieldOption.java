package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleContactCustomFieldOption {
    private String displayName;
    private String value;
    private Integer sortOrder;

    public ZingleContactCustomFieldOption() {
    }

    public ZingleContactCustomFieldOption(String displayName, String value, int sortOrder) {
        this.displayName = displayName;
        this.value = value;
        this.sortOrder = sortOrder;
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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
