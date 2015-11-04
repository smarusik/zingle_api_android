package me.zingle.api.sdk.Exceptions;

/**
 * Created by SLAVA 08 2015.
 */
public class UndefinedServiceDelegateEx extends RuntimeException{

    @Override
    public String getMessage() {
        return "Asynch method without proper delegate.";
    }
}
