package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleAutomation {
    private int id;
    private ZingleService service;
    private String displayName;
    private Boolean isGlobal;

    public ZingleAutomation() {
    }

    public ZingleAutomation(int id, ZingleService service, String displayName, boolean isGlobal) {
        this.id = id;
        this.service = service;
        this.displayName = displayName;
        this.isGlobal = isGlobal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ZingleService getService() {
        return service;
    }

    public void setService(ZingleService service) {
        this.service = service;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean isGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }
}
