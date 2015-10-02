package me.zingle.api.sdk.services;

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

    public void processError(int errorCode,String errorDescr){
        Log.info(getClass(),"processResult()","Error:\nCode="+errorCode+"\nDescription="+errorDescr);
    }

}
