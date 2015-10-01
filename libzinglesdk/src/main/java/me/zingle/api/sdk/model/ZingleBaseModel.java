package me.zingle.api.sdk.model;

import org.json.JSONObject;

/**
 * Created by SLAVA 09 2015.
 */
public abstract class ZingleBaseModel {
    public abstract JSONObject extractCreationData();
    public abstract JSONObject extractUpdateData();
    public abstract void checkForCreate();
    public abstract void checkForUpdate();
}
