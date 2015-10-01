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
        return displayName;
    }

    @Override
    public boolean equals(Object obj) {
        return this.displayName.equals(obj.toString());
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