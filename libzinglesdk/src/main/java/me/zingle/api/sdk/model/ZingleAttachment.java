package me.zingle.api.sdk.model;

import java.net.URL;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleAttachment {
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
}
