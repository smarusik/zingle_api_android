package me.zingle.api.sdk.dto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SLAVA 08 2015.
 */
public class ResponseDTO {
    private int responseCode;
    private String responseStr;
    private String errorString;
    private String errorStackTrace;

    private JSONObject data;


    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseStr() {
        return responseStr;
    }

    public void setResponseStr(String responseStr) {
        this.responseStr = responseStr;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorStackTrace() {
        return errorStackTrace;
    }

    public void setErrorStackTrace(String errorStackTrace) {
        this.errorStackTrace = errorStackTrace;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public void setDataWithStr(String dataStr){
        try {
            this.data = new JSONObject(dataStr);
        }catch (JSONException e){
            try {
                this.data= new JSONObject("{\"status\":null,\"result\":null}");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            errorString.concat("\n"+e.getMessage());
            errorStackTrace.concat("\n"+e.getStackTrace());
        }
    }
}
