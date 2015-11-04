package me.zingle.api.sdk.dto;

import org.json.JSONObject;



/**
 * Created by SLAVA 08 2015.
 */
public class RequestDTO {
    JSONObject data;

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

}