package me.zingle.api.sdk.Exceptions;

/**
 * Created by SLAVA 08 2015.
 */
public class UndefinedServiceDelegateEx extends RuntimeException{

    @Override
    public String getMessage() {
        return "It's redundancy to call asynch method without proper delegate.";
    }
}
