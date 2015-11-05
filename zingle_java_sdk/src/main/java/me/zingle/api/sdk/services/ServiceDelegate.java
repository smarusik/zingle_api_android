package me.zingle.api.sdk.services;

import org.json.JSONObject;

import me.zingle.api.sdk.logger.Log;

/**
 *Base class for catching results of asynchronous functions in all classes, which inherits ZingleBaseService.
 * Provides basic error processing (writes to Log), which can be overloaded by user's choice. Positive result processing by void processResult() must be implemented.
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
