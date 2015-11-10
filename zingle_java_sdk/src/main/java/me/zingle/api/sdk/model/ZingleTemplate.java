package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Arrays;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/templates#template-object>Template Object</a>
 */
public class ZingleTemplate extends ZingleBaseModel {

    private class Types{
        final String[] types={"automated_welcome", "welcome", "general"};
        public Types(String name) {
            if(Arrays.asList(types).contains(name)) {
                this.name = name;
            }
            else
                throw new RuntimeException("Unsupported Template type.");
        }

        private String name;

        @Override
        public String toString() {
            return name;
        }
    }

    private ZingleService service;

    private String id;
    private String displayName;
    private String subject;
    private Types type;
    private String body;
    private Boolean isGlobal;

    public ZingleTemplate() {
    }

    public ZingleTemplate(Types type, String displayName, String subject, String body) {
        this.type = type;
        this.displayName = displayName;
        this.subject = subject;
        this.body = body;
    }

    public ZingleService getService() {
        return service;
    }

    public void setService(ZingleService service) {
        this.service = service;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type.name;
    }

    public void setType(String type) {
        this.type = new Types(type);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    @Override
    public JSONObject extractCreationData() {
        checkForCreate();

        JSONObject resJS=new JSONObject();

        try {
            resJS.put("display_name",displayName);
            resJS.put("body",body);
            resJS.put("type",type.name);

            if(subject!=null)
                resJS.put("subject",subject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();

        JSONStringer res=new JSONStringer();
        JSONObject resJS=null;

        try {
            res.object();

            if(displayName!=null)
                res.key("display_name").value(displayName);

            if(body!=null)
                res.key("body").value(body);

            if(type!=null)
                res.key("type").value(type.name);

            if(subject!=null)
                res.key("subject").value(subject);

            res.endObject();

            resJS= new JSONObject(res.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public void checkForCreate() {
        if(displayName==null) throw new RequestBodyCreationEx(RequestMethods.POST,"display_name",getClass().getName()+".displayName");
        if(type==null) throw new RequestBodyCreationEx(RequestMethods.POST,"type",getClass().getName()+".type");
        if(body==null) throw new RequestBodyCreationEx(RequestMethods.POST,"body",getClass().getName()+".body");
    }

    @Override
    public void checkForUpdate() {
        if(displayName==null && type==null && body==null && subject==null)
            throw new RequestBodyCreationEx(RequestMethods.PUT,"display_name,type,body,subject",
                    getClass().getName()+".displayName,type,body,subject");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleTemplate{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("\n    subject='").append(subject).append('\'');
        sb.append("\n    type=").append(type);
        sb.append("\n    body='").append(body).append('\'');
        sb.append("\n    isGlobal=").append(isGlobal);
        sb.append("}\n");
        return sb.toString();
    }
}
