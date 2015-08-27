package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleService {
    private int id;
    private String displayName;
    private ZinglePhoneNumber phoneNumber;
    private ZingleTimeZone timeZone;
    private ZinglePlan plan;
    private ZingleAddress address;
    private Boolean active;

    public ZingleService() {
    }

    public ZingleService(int id, String displayName, ZinglePhoneNumber phoneNumber, ZingleTimeZone timeZone,
                         ZinglePlan plan, ZingleAddress address, boolean active) {
        this.id = id;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.timeZone = timeZone;
        this.plan = plan;
        this.address = address;
        this.active = active;
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

    public ZinglePhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(ZinglePhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public Boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
