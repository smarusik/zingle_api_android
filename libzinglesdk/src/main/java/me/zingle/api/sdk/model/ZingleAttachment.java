package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleAttachment extends ZingleBaseModel{
    private String mimeType;
    private URL url;
    private byte[] data; //base64 encoded data

    public ZingleAttachment() {
    }

    public ZingleAttachment(byte[] data, String mimeType) {
        this.data = data;
        this.mimeType = mimeType;
    }

    public ZingleAttachment(String mimeType, URL url) {
        this.mimeType = mimeType;
        this.url = url;
    }

    public ZingleAttachment(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public JSONObject extractCreationData() {
        checkForCreate();

        JSONObject resJS=new JSONObject();

        try {
            resJS.put("content_type", mimeType);
            resJS.put("base64", new String(data));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
    }

    @Override
    public void checkForCreate() {
        if(mimeType==null || mimeType.isEmpty()){
            throw new RequestBodyCreationEx(RequestMethods.POST,"content_type",getClass().getName()+".mimeType");
        }
        if(data==null){
            throw new RequestBodyCreationEx(RequestMethods.POST,"base64",getClass().getName()+".data");
        }
    }

    @Override
    public void checkForUpdate() {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleAttachment{");
        sb.append("\n    mimeType='").append(mimeType).append('\'');
        sb.append("\n    url=").append(url);
        sb.append("\n    data=").append(Arrays.toString(data));
        sb.append("}\n");
        return sb.toString();
    }
}
