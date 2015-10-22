package me.zingle.api.sdk.Exceptions;

import org.json.JSONObject;

/**
 * Created by SLAVA 08 2015.
 */
public class UnsuccessfullRequestEx extends RuntimeException{
    private JSONObject errMessage;
    private int responceCode;
    private String responceStr;

    public UnsuccessfullRequestEx(JSONObject message, int responceCode, String responceStr) {
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
