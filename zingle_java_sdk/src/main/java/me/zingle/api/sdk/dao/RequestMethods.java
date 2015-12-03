package me.zingle.api.sdk.dao;

/**
 * Enumeration for working with HTTP request methods.
 */
public enum RequestMethods {
    PUT("PUT"),
    GET("GET"),
    POST("POST"),
    DELETE("DELETE");

    private final String methodStr;

    RequestMethods(String str) {
        methodStr=str;
    }

    public String method(){
        return methodStr;
    }
}
