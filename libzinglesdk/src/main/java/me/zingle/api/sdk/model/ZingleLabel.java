package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

//import java.awt.Color;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleLabel extends ZingleBaseModel{
    private String id;
    private ZingleService service;
    private String displayName;
    private String backgroundColor;
    private String textColor;
    private Boolean isGlobal;

    public ZingleLabel() {
    }

    public ZingleLabel(String displayName, String backgroundColor, String textColor) {
        this.displayName = displayName;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public ZingleLabel(String id, ZingleService service, String displayName, String backgroundColor, String textColor) {
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

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
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
        JSONObject resJS=new JSONObject();

        try {

            resJS.put("display_name",getDisplayName());
            if(getBackgroundColor()!=null)
                resJS.put("background_color",getBackgroundColor());
            if(getTextColor()!=null)
                resJS.put("text_color",getTextColor());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
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
