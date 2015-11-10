package me.zingle.api.sdk.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/settings_fields#settings-field-object>Settings Field Object</a>
 */
public class ZingleSettingsField extends ZingleBaseModel{

    private String id;
    private String displayName;
    private String dataType;
    private List<ZingleFieldOption> options= new ArrayList<>();

    public ZingleSettingsField() {
    }

    public ZingleSettingsField(String id, String displayName, String dataType) {
        this.id = id;
        this.displayName = displayName;
        this.dataType = dataType;
    }

    public void addOption(ZingleFieldOption option){
       options.add(option);
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public List<ZingleFieldOption> getOptions() {
        return options;
    }

    public void setOptions(List<ZingleFieldOption> options) {
        this.options = options;
    }

    @Override
    public JSONObject extractCreationData() {
        return null;
    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
    }

    @Override
    public void checkForCreate() {

    }

    @Override
    public void checkForUpdate() {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleSettingsField{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("\n    dataType='").append(dataType).append('\'');
        sb.append("\n    options=").append(options);
        sb.append("}\n");
        return sb.toString();
    }
}
