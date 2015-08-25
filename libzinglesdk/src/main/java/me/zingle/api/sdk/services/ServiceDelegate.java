package me.zingle.api.sdk.services;

import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;

/**
 * Created by SLAVA 08 2015.
 */
public class ServiceDelegate <Model> {

    public final String asyncFunction;

    public ServiceDelegate(String asyncFunction) {
        this.asyncFunction = asyncFunction;
    }

    public void processResult(Model res) throws UndefinedServiceDelegateEx{
        throw new UndefinedServiceDelegateEx();
    }

    public void processError(int errorCode,String errorDescr) throws UndefinedServiceDelegateEx{
        throw new UndefinedServiceDelegateEx();
    }
}
