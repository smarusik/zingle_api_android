package me.zingle.api.sdk.model;

import java.net.URL;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleServiceChannel {
    private int id;
    private int serviceId;
    private ZingleChannelType type;
    private Boolean default_for_type;
    private String value;
    private URL inbound_notification_url=null;
    private String inbound_notification_request_type=null;
    private String inbound_notification_data_format=null;
    private URL outbound_notification_url=null;
    private String outbound_notification_request_type=null;
    private String outbound_notification_data_format=null;

    public ZingleServiceChannel() {
    }

    public ZingleServiceChannel(int id, int serviceId, ZingleChannelType type, Boolean default_for_type, String value) {
        this.id = id;
        this.serviceId = serviceId;
        this.type = type;
        this.default_for_type = default_for_type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public ZingleChannelType getType() {
        return type;
    }

    public void setType(ZingleChannelType type) {
        this.type = type;
    }

    public Boolean getDefault_for_type() {
        return default_for_type;
    }

    public void setDefault_for_type(Boolean default_for_type) {
        this.default_for_type = default_for_type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public URL getInbound_notification_url() {
        return inbound_notification_url;
    }

    public void setInbound_notification_url(URL inbound_notification_url) {
        this.inbound_notification_url = inbound_notification_url;
    }

    public String getInbound_notification_request_type() {
        return inbound_notification_request_type;
    }

    public void setInbound_notification_request_type(String inbound_notification_request_type) {
        this.inbound_notification_request_type = inbound_notification_request_type;
    }

    public String getInbound_notification_data_format() {
        return inbound_notification_data_format;
    }

    public void setInbound_notification_data_format(String inbound_notification_data_format) {
        this.inbound_notification_data_format = inbound_notification_data_format;
    }

    public URL getOutbound_notification_url() {
        return outbound_notification_url;
    }

    public void setOutbound_notification_url(URL outbound_notification_url) {
        this.outbound_notification_url = outbound_notification_url;
    }

    public String getOutbound_notification_request_type() {
        return outbound_notification_request_type;
    }

    public void setOutbound_notification_request_type(String outbound_notification_request_type) {
        this.outbound_notification_request_type = outbound_notification_request_type;
    }

    public String getOutbound_notification_data_format() {
        return outbound_notification_data_format;
    }

    public void setOutbound_notification_data_format(String outbound_notification_data_format) {
        this.outbound_notification_data_format = outbound_notification_data_format;
    }
}
