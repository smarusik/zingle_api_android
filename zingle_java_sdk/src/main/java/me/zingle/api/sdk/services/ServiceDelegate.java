package me.zingle.api.sdk.services;

import org.json.JSONObject;

import me.zingle.api.sdk.logger.Log;

/**
 * Created by SLAVA 08 2015.
 */
public abstract class ServiceDelegate <ResultType> {

    public final String asyncFunction;

    public ServiceDelegate(String asyncFunction) {
        this.asyncFunction = asyncFunction;
    }

    public abstract void processResult(ResultType res);

    public void processError(JSONObject data,int responseCode,String responseStr){
        Log.err(getClass(), "processResult()",
                String.format("Error:\nCode=%d\nDescription=%s\nServer answer:%s",
                        responseCode, responseStr, data));
    }

}
