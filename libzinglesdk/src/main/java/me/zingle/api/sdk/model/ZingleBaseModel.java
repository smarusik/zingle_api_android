package me.zingle.api.sdk.model;

import org.json.JSONObject;

/**
 * Created by SLAVA 09 2015.
 */
public abstract class ZingleBaseModel {
    private String id=null;
    public String getId(){
        return id;
    }
    public abstract JSONObject extractCreationData();
    public abstract JSONObject extractUpdateData();
    public abstract void checkForCreate();
    public abstract void checkForUpdate();
}
