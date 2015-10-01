package me.zingle.api.sdk.Exceptions;

import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 09 2015.
 */
public class RequestBodyCreationEx extends RuntimeException{
    private RequestMethods method;
    private String requiredField;
    private String objectField;

    public RequestBodyCreationEx(RequestMethods method, String requiredField, String objectField) {
        this.method = method;
        this.requiredField = requiredField;
        this.objectField = objectField;
    }

    public String getRequiredField() {
        return requiredField;
    }

    public void setRequiredField(String requiredField) {
        this.requiredField = requiredField;
    }

    @Override
    public String getMessage() {
        return String.format("Request body for %s message must contain valid data in %s field (%s)", method.method(),requiredField,objectField);
    }
}
