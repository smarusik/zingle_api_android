package me.zingle.api.sdk.Exceptions;

/**
 * Runtime exception. Indicates that HTTP connection to API was not initialized with proper URL, version number etc.
 */
public class UninitializedConnectionEx extends RuntimeException {

    @Override
    public String getMessage() {
        return "Zingle connection must be initialized with init()";
    }
}
