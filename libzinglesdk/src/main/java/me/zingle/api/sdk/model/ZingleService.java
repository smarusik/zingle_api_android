package me.zingle.api.sdk.model;

import java.util.List;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleService {
    private int id;
    private String displayName;
    private List<ZingleServiceChannel> channels;
    private ZingleTimeZone timeZone;
    private ZinglePlan plan;
    private ZingleAddress address;

    public ZingleService() {
    }

    public ZingleService(int id, String displayName, List<ZingleServiceChannel> channels, ZingleTimeZone timeZone, ZinglePlan plan, ZingleAddress address) {
        this.id = id;
        this.displayName = displayName;
        this.channels = channels;
        this.timeZone = timeZone;
        this.plan = plan;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<ZingleServiceChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<ZingleServiceChannel> channels) {
        this.channels = channels;
    }

    public ZingleTimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZingleTimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public ZinglePlan getPlan() {
        return plan;
    }

    public void setPlan(ZinglePlan plan) {
        this.plan = plan;
    }

    public ZingleAddress getAddress() {
        return address;
    }

    public void setAddress(ZingleAddress address) {
        this.address = address;
    }
}
