package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/automations#automation-object>Automation Object</a>
 */
public class ZingleAutomation extends ZingleBaseModel {

    private class Types{
        final String[] types={"Escalation","Keyword","Self-Registration","Survey","Phone Call","Custom Automation"};

        public Types(String name) {
            if(Arrays.asList(types).contains(name)) {
                this.name = name;
            }
            else
                throw new RuntimeException("Unsupported Automation type.");
        }

        private String name;

        @Override
        public String toString() {
            return name;
        }
    }

    private class Statuses{
        final String[] statuses={"active","inactive"};
        public Statuses(String name) {
            if(Arrays.asList(statuses).contains(name)) {
                this.name = name;
            }
            else
                throw new RuntimeException("Unsupported Automation status.");
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
    private Types type;
    private Statuses status;
    private Boolean isGlobal;

    public ZingleAutomation() {
    }

    public ZingleAutomation(ZingleService service, String id, String displayName) {
        this.service = service;
        this.id = id;
        this.displayName = displayName;
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

    public Boolean isGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public String getType() {
        return type.name;
    }

    public void setType(String type) {
        this.type = new Types(type);
    }

    public String getStatus() {
        return status.name;
    }

    public void setStatus(String status) {
        this.status = new Statuses(status);
    }

    @Override
    public JSONObject extractCreationData() {
        return null;
    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();

        JSONObject resJS=new JSONObject();

        try {

            resJS.put("status",status.name);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return resJS;
    }

    @Override
    public void checkForCreate() {

    }

    @Override
    public void checkForUpdate() {
        if(status==null) throw new RequestBodyCreationEx(RequestMethods.PUT, "status", getClass().getName() + ".status");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleAutomation{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("\n    type=").append(type);
        sb.append("\n    status=").append(status);
        sb.append("\n    isGlobal=").append(isGlobal);
        sb.append("}\n");
        return sb.toString();
    }
}
