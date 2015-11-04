package me.zingle.api.sdk.dao;

/**
 * Created by SLAVA 08 2015.
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
