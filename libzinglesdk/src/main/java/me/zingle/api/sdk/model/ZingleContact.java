package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;

/**
 * Created by SLAVA 08 2015.
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
    }

    private ZingleService service;

    private String id;
    private Boolean isConfirmed;
    private Boolean isStarred;
    private LastMessageDigest lastMessage;
    private List<ZingleContactChannel> channels;
    private List<ZingleContactFieldValue> customFieldValues;
    private List<ZingleLabel> labels;
    private Integer createdAt;
    private Integer updatedAt;

    public ZingleContact() {
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

    public List<ZingleLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ZingleLabel> labels) {
        this.labels = labels;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer created_at) {
        this.createdAt = created_at;
    }

    public Integer getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Integer updated_at) {
        this.updatedAt = updated_at;
    }

    @Override
    public JSONObject extractCreationData() {
        JSONStringer res = new JSONStringer();

        res.object();

        if (customFieldValues!=null) {
            res.key("custom_field_values");
            res.array();
            for(ZingleContactFieldValue fv:customFieldValues){
                res.value(fv.extractCreationData());
            }
            res.endArray();
        }

        if(isStarred!=null){
            res.key("is_starred").value(isStarred);
        }
        if(isConfirmed!=null){
            res.key("is_confirmed").value(isConfirmed);
        }

        res.endObject();

        return new JSONObject(res.toString());
    }

    @Override
    public JSONObject extractUpdateData() {
        return extractCreationData();
    }

    @Override
    public void checkForCreate() {

    }

    @Override
    public void checkForUpdate() {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ZingleContact{");
        sb.append("service=").append(service);
        sb.append(", id='").append(id).append('\'');
        sb.append(", isConfirmed=").append(isConfirmed);
        sb.append(", isStarred=").append(isStarred);
        sb.append(", lastMessage=").append(lastMessage);
        sb.append(", channels=").append(channels);
        sb.append(", customFieldValues=").append(customFieldValues);
        sb.append(", labels=").append(labels);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append('}');
        return sb.toString();
    }
}
