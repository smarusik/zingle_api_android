package me.zingle.api.sdk.services;

/**
 * Created by SLAVA 08 2015.
 */
public abstract class ServiceDelegate <ResultType> {

    public final String asyncFunction;

    public ServiceDelegate(String asyncFunction) {
        this.asyncFunction = asyncFunction;
    }

    public abstract void processResult(ResultType res);

    public abstract void processError(int errorCode,String errorDescr);

}
