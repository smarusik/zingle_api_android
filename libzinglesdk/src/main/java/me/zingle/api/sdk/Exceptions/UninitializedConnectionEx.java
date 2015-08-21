package me.zingle.api.sdk.Exceptions;

/**
 * Created by SLAVA 08 2015.
 */
public class UninitializedConnectionEx extends RuntimeException {

    @Override
    public String getMessage() {
        return "Zingle connection must be initialized with init()";
    }
}
