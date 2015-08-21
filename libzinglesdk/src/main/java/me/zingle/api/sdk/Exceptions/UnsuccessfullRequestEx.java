package me.zingle.api.sdk.Exceptions;

/**
 * Created by SLAVA 08 2015.
 */
public class UnsuccessfullRequestEx extends RuntimeException{
    private int responceCode;
    private String responceStr;

    public UnsuccessfullRequestEx(String message, int responceCode, String responceStr) {
        super(message);
        this.responceCode = responceCode;
        this.responceStr = responceStr;
    }

    public int getResponceCode() {
        return responceCode;
    }

    public String getResponceStr() {
        return responceStr;
    }
}
