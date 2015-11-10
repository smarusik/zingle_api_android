package me.zingle.api.sdk.Exceptions;

/**
 * Runtime exception. Indicates, that some asynchronous method from Zingle services was triggered without delegate.
 */
public class UndefinedServiceDelegateEx extends RuntimeException{

    @Override
    public String getMessage() {
        return "Async method without proper delegate.";
    }
}
