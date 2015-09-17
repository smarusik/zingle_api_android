package me.zingle.api.sdk.services;

/**
 * Created by SLAVA 08 2015.
 */
public abstract class ServiceDelegate <Model> {

    public final String asyncFunction;

    public ServiceDelegate(String asyncFunction) {
        this.asyncFunction = asyncFunction;
    }

    public abstract void processResult(Model res);

    public abstract void processError(int errorCode,String errorDescr);
}
