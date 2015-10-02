package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * Created by SLAVA 09 2015.
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

        JSONStringer res = new JSONStringer();

        res.object();

        res.key("display_name").value(displayName);
        res.key("value").value(value);
        res.key("sort_order").value(sortOrder);


        res.endObject();
        return new JSONObject(res.toString());
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
