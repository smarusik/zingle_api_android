package me.zingle.api.sdk.Exceptions;

import org.json.JSONObject;

/**
 * Runtime exception. Indicates, that HTTP request to API response returns error (code differs from 200).
 */
public class UnsuccessfulRequestEx extends RuntimeException{
    private JSONObject errMessage;
    private int responceCode;
    private String responceStr;

    public UnsuccessfulRequestEx(JSONObject message, int responceCode, String responceStr) {
        super(message.toString());
        this.errMessage=message;
        this.responceCode = responceCode;
        this.responceStr = responceStr;
    }

    public int getResponceCode() {
        return responceCode;
    }

    public String getResponceStr() {
        return responceStr;
    }

    public JSONObject getErrMessage() {
        return errMessage;
    }

    @Override
    public String getMessage() {
        return String.format("HTTP request failed:\nError code: %d\nError description: %s\nError message:%s",responceCode,responceStr,errMessage);
    }
}
