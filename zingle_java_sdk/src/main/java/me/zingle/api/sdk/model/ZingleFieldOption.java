package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/field_options#field-option-object>Field Option Object</a>
 */
public class ZingleFieldOption extends ZingleBaseModel {

    private String id;
    private String displayName;
    private String value;
    private Integer sortOrder;

    public ZingleFieldOption() {
    }

    public ZingleFieldOption(String id, String displayName, String value, Integer sortOrder) {
        this.id = id;
        this.displayName = displayName;
        this.value = value;
        this.sortOrder = sortOrder;
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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public JSONObject extractCreationData() {
        checkForCreate();

        JSONObject resJS=new JSONObject();

        try {
            resJS.put("display_name",displayName);
            resJS.put("value",value);
            resJS.put("sort_order",sortOrder);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public JSONObject extractUpdateData() {
        return extractCreationData();
    }

    @Override
    public void checkForCreate() {

    }

    @Override
    public void checkForUpdate() {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleFieldOption{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("\n    value='").append(value).append('\'');
        sb.append("\n    sortOrder=").append(sortOrder);
        sb.append("}\n");
        return sb.toString();
    }
}
