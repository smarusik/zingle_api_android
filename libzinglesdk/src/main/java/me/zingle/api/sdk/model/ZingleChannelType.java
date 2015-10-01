package me.zingle.api.sdk.model;

import org.json.JSONObject;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleChannelType extends ZingleBaseModel{

    private String id;
    private String typeclass;
    private String displayName;
    private String inboundNotificationUrl;
    private String outboundNotificationUrl;
    private Boolean allowCommunications;

    public ZingleChannelType() {
    }

    public ZingleChannelType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeclass() {
        return typeclass;
    }

    public void setTypeclass(String typeclass) {
        this.typeclass = typeclass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getInboundNotificationUrl() {
        return inboundNotificationUrl;
    }

    public void setInboundNotificationUrl(String inboundNotificationUrl) {
        this.inboundNotificationUrl = inboundNotificationUrl;
    }

    public String getOutboundNotificationUrl() {
        return outboundNotificationUrl;
    }

    public void setOutboundNotificationUrl(String outboundNotificationUrl) {
        this.outboundNotificationUrl = outboundNotificationUrl;
    }

    public Boolean getAllowCommunications() {
        return allowCommunications;
    }

    public void setAllowCommunications(Boolean allowCommunications) {
        this.allowCommunications = allowCommunications;
    }

    @Override
    public JSONObject extractCreationData() {
        return null;
    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
    }

    @Override
    public void checkForCreate() {

    }

    @Override
    public void checkForUpdate() {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ZingleChannelType{");
        sb.append("id='").append(id).append('\'');
        sb.append(", typeclass='").append(typeclass).append('\'');
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", inboundNotificationUrl='").append(inboundNotificationUrl).append('\'');
        sb.append(", outboundNotificationUrl='").append(outboundNotificationUrl).append('\'');
        sb.append(", allowCommunications=").append(allowCommunications);
        sb.append('}');
        return sb.toString();
    }
}
