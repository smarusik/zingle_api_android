package me.zingle.api.sdk.model;

import org.json.JSONObject;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleTimeZone extends ZingleBaseModel{
    private String displayName;

    public ZingleTimeZone() {
    }

    public ZingleTimeZone(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleTimeZone{");
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("}\n");
        return sb.toString();
    }

    public boolean equals(ZingleTimeZone obj) {
        return this.displayName.equals(obj.displayName);
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
}