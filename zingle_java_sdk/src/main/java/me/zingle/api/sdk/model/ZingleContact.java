package me.zingle.api.sdk.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/contacts#contact-object>Contact Object</a>
 */
public class ZingleContact extends ZingleBaseModel{

    public static class LastMessageDigest{
        String id;
        String body;
        Integer created_at;

        public LastMessageDigest() {
        }

        public LastMessageDigest(String id, String body, Integer created_at) {
            this.id = id;
            this.body = body;
            this.created_at = created_at;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("\nLastMessageDigest{");
            sb.append("\n    id='").append(id).append('\'');
            sb.append("\n    body='").append(body).append('\'');
            sb.append("\n    created_at=").append(created_at);
            sb.append("}\n");
            return sb.toString();
        }
    }

    private ZingleService service;

    private String id;
    private Boolean isConfirmed;
    private Boolean isStarred;
    private LastMessageDigest lastMessage;
    private List<ZingleContactChannel> channels;
    private List<ZingleContactFieldValue> customFieldValues;
    private List<ZingleLabel> labels;
    private Long createdAt;
    private Long updatedAt;

    /*public ZingleContact() {
    }*/

    public ZingleContact(ZingleService parent) {
        this.service = parent;
    }

    public ZingleContact(ZingleService service, String id) {
        this.service = service;
        this.id = id;
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

    public Boolean getIsConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public Boolean getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(Boolean isStarred) {
        this.isStarred = isStarred;
    }

    public LastMessageDigest getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessageDigest lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<ZingleContactChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<ZingleContactChannel> channels) {
        this.channels = channels;
    }

    public List<ZingleContactFieldValue> getCustomFieldValues() {
        return customFieldValues;
    }

    public void setCustomFieldValues(List<ZingleContactFieldValue> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    public void addCustomFieldValue(ZingleContactFieldValue contactFieldValue){
        if(customFieldValues!=null) {
            for (ZingleContactFieldValue v : customFieldValues) {
                if (v.getContactField().equals(contactFieldValue.getContactField())) {
                    v.setValue(contactFieldValue.getValue());
                    v.setSelectedFieldOptionId(contactFieldValue.getSelectedFieldOptionId());
                    return;
                }
            }
            customFieldValues.add(contactFieldValue);
        }
        else{
            customFieldValues=new ArrayList<>();
            customFieldValues.add(contactFieldValue);
        }
    }

    public void addCustomFieldValue(String id, Object value){
        if(service==null){
            throw (new RuntimeException("Undefined service."));
        }

        addCustomFieldValue(new ZingleContactFieldValue(new ZingleContactField(service,id),value));
    }

    public List<ZingleLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ZingleLabel> labels) {
        this.labels = labels;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long created_at) {
        this.createdAt = created_at;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updated_at) {
        this.updatedAt = updated_at;
    }

    @Override
    public JSONObject extractCreationData() {
        checkForCreate();
        return extractData();
    }

    private JSONObject extractData(){
        JSONObject resJS=new JSONObject();

        try {

            if (customFieldValues!=null) {
                JSONArray arrJS=new JSONArray();
                for(ZingleContactFieldValue fv:customFieldValues){
                    arrJS.put(fv.extractCreationData());
                }
                resJS.put("custom_field_values",arrJS);
            }

            if(isStarred!=null){
                resJS.put("is_starred",isStarred);
            }
            if(isConfirmed!=null){
                resJS.put("is_confirmed",isConfirmed);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();
        return extractData();
    }

    @Override
    public void checkForCreate() {

    }

    @Override
    public void checkForUpdate() {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleContact{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    isConfirmed=").append(isConfirmed);
        sb.append("\n    isStarred=").append(isStarred);
        sb.append("\n    lastMessage=").append(lastMessage);
        sb.append("\n    channels=").append(channels);
        sb.append("\n    customFieldValues=").append(customFieldValues);
        sb.append("\n    labels=").append(labels);
        sb.append("\n    createdAt=").append(createdAt);
        sb.append("\n    updatedAt=").append(updatedAt);
        sb.append("}\n");
        return sb.toString();
    }
}
