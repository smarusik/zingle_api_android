package me.zingle.api.sdk.services;

/**
 * Created by SLAVA 08 2015.
 */
public class ServiceDelegate <Model> {

    public final String asyncFunction;

    public ServiceDelegate(String asyncFunction) {
        this.asyncFunction = asyncFunction;
    }

    public void processResult(Model res){
        return;
    }

    public void processError(int errorCode,String errorDescr){
        return;
    }
}
