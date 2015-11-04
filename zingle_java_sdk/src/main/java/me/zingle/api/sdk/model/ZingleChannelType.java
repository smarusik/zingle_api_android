package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

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
    private Boolean isGlobal;
    private Integer priority;

    public ZingleChannelType() {
    }

    public ZingleChannelType(String id) {
        this.id = id;
    }

    public ZingleChannelType(String typeclass, String displayName) {
        this.typeclass = typeclass;
        this.displayName = displayName;
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

    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public JSONObject extractCreationData() {
        checkForCreate();

        JSONObject resJS=new JSONObject();

        try {

            resJS.put("display_name", getDisplayName());

            if (getInboundNotificationUrl() != null)
                resJS.put("inbound_notification_url", getInboundNotificationUrl());

            if (getOutboundNotificationUrl() != null)
                resJS.put("outbound_notification_url", getOutboundNotificationUrl());

            if (getAllowCommunications() != null)
                resJS.put("allow_communications", getAllowCommunications());

            if (getPriority() != null)
                resJS.put("priority", getPriority());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();

        JSONObject resJS=new JSONObject();

        try {

            resJS.put("display_name", getDisplayName());

            if (getInboundNotificationUrl() != null)
                resJS.put("inbound_notification_url", getInboundNotificationUrl());

            if (getOutboundNotificationUrl() != null)
                resJS.put("outbound_notification_url", getOutboundNotificationUrl());

            if (getAllowCommunications() != null)
                resJS.put("allow_communications", getAllowCommunications());

            if (getPriority() != null)
                resJS.put("priority", getPriority());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;

    }

    @Override
    public void checkForCreate() {
        if (getDisplayName() == null) {
            throw new RequestBodyCreationEx(RequestMethods.POST, "display_name", "ZingleChannelType.displayName");
        }
    }

    @Override
    public void checkForUpdate() {
        if (getDisplayName() == null) {
            throw new RequestBodyCreationEx(RequestMethods.POST, "display_name", "ZingleChannelType.displayName");
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleChannelType{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    typeclass='").append(typeclass).append('\'');
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("\n    inboundNotificationUrl='").append(inboundNotificationUrl).append('\'');
        sb.append("\n    outboundNotificationUrl='").append(outboundNotificationUrl).append('\'');
        sb.append("\n    allowCommunications=").append(allowCommunications);
        sb.append("\n    isGlobal=").append(isGlobal);
        sb.append("\n    priority=").append(priority);
        sb.append("}\n");
        return sb.toString();
    }
}
