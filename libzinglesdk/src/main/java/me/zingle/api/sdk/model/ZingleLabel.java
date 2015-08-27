package me.zingle.api.sdk.model;

import java.awt.Color;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleLabel {
    private int id;
    private ZingleService service;
    private String displayName;
    private Color backgroundColor;
    private Color textColor;
    private Boolean isAutomation;
    private Boolean isGlobal;
    private Boolean active;

    public ZingleLabel() {
    }

    public ZingleLabel(int id, ZingleService service, String displayName, Color backgroundColor, Color textColor, boolean isAutomation, boolean isGlobal, boolean active) {
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

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Boolean isAutomation() {
        return isAutomation;
    }

    public void setIsAutomation(boolean isAutomation) {
        this.isAutomation = isAutomation;
    }

    public Boolean isGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
