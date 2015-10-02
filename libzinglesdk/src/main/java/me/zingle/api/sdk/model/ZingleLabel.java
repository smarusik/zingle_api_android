package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.awt.Color;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleLabel extends ZingleBaseModel{
    private String id;
    private ZingleService service;
    private String displayName;
    private Color backgroundColor;
    private Color textColor;
    private Boolean isGlobal;

    public ZingleLabel() {
    }

    public ZingleLabel(String displayName, Color backgroundColor, Color textColor) {
        this.displayName = displayName;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public ZingleLabel(String id, ZingleService service, String displayName, Color backgroundColor, Color textColor) {
        this.id = id;
        this.service = service;
        this.displayName = displayName;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }


    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    @Override
    public JSONObject extractCreationData() {
        checkForCreate();
        return extractData();

    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();
        return extractData();
    }

    public JSONObject extractData() {
        checkForCreate();
        JSONStringer res=new JSONStringer();

        res.object();

        res.key("display_name").value(getDisplayName());
        if(getBackgroundColor()!=null)
            res.key("background_color").value("#" + Integer.toHexString(getBackgroundColor().getRGB()).substring(2));
        if(getTextColor()!=null)
            res.key("text_color").value("#" + Integer.toHexString(getTextColor().getRGB()).substring(2));

        res.endObject();

        return new JSONObject(res.toString());
    }

    @Override
    public void checkForCreate() {
        if(displayName==null) throw new RequestBodyCreationEx(RequestMethods.POST,"display_name",getClass().getName()+".displayName");
        if(backgroundColor==null) throw new RequestBodyCreationEx(RequestMethods.POST,"background_color",getClass().getName()+".backgroundColor");
        if(textColor==null) throw new RequestBodyCreationEx(RequestMethods.POST,"text_color",getClass().getName()+".textColor");
    }

    @Override
    public void checkForUpdate() {
        if(displayName==null && backgroundColor==null && textColor==null)
            throw new RequestBodyCreationEx(RequestMethods.PUT,"text_color,background_color,display_name",getClass().getName()+".textColor,backgroundColor,displayName");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleLabel{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("\n    backgroundColor=").append(backgroundColor);
        sb.append("\n    textColor=").append(textColor);
        sb.append("\n    isGlobal=").append(isGlobal);
        sb.append("}\n");
        return sb.toString();
    }
}
