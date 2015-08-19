package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleLabel {
    private int id;
    private ZingleService service;
    private String displayName;
    private int backgroundColor;
    private int textColor;
    private boolean isAutomation;
    private boolean isGlobal;
    private boolean active;

    public ZingleLabel() {
    }

    public ZingleLabel(int id, ZingleService service, String displayName, int backgroundColor, int textColor, boolean isAutomation, boolean isGlobal, boolean active) {
        this.id = id;
        this.service = service;
        this.displayName = displayName;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.isAutomation = isAutomation;
        this.isGlobal = isGlobal;
        this.active = active;
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

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public boolean isAutomation() {
        return isAutomation;
    }

    public void setIsAutomation(boolean isAutomation) {
        this.isAutomation = isAutomation;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
