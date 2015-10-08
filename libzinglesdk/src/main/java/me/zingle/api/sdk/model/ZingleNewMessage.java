package me.zingle.api.sdk.model;


import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 10 2015.
 */
public class ZingleNewMessage extends ZingleBaseModel {

    private ZingleCorrespondent sender;
    private ZingleCorrespondent recipient;
    private List<ZingleChannelType> channelTypes;
    private String body;
    private List<ZingleAttachment> attachments;

    public ZingleNewMessage() {
    }


    public ZingleCorrespondent getSender() {
        return sender;
    }

    public void setSender(ZingleCorrespondent sender) {
        this.sender = sender;
    }


    public ZingleCorrespondent getRecipient() {
        return recipient;
    }

    public void setRecipient(ZingleCorrespondent recipient) {
        this.recipient = recipient;
    }

    public List<ZingleChannelType> getChannelTypes() {
        return channelTypes;
    }

    public void setChannelTypes(List<ZingleChannelType> channelTypes) {
        this.channelTypes = channelTypes;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<ZingleAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ZingleAttachment> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(ZingleAttachment att){
        if(att!=null)
            attachments.add(att);
    }

    @Override
    public JSONObject extractCreationData() {

        checkForCreate();

        JSONStringer res=new JSONStringer();

        res.object();

        res.key("sender").value(sender.extractCreationData());
        res.key("recipient").value(recipient.extractCreationData());

        res.key("channel_type_ids");
        res.array();
        for(ZingleChannelType t:channelTypes){
            res.value(t.getId());
        }
        res.endArray();

        res.key("body").value(body);

        res.key("attachments");
        res.array();
        if(attachments!=null){
            for(ZingleAttachment a:attachments){
                a.extractCreationData();
            }
        }
        res.endArray();

        res.endObject();

        return new JSONObject(res.toString());

    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
    }

    @Override
    public void checkForCreate() {

        if(sender==null)
            throw new RequestBodyCreationEx(RequestMethods.POST,"sender",getClass().getName()+".sender");

        if(recipient==null)
            throw new RequestBodyCreationEx(RequestMethods.POST,"recipients",getClass().getName()+".recipients");

        if(recipient.getType().equals("label") && (channelTypes==null || channelTypes.isEmpty()))
            throw new RequestBodyCreationEx(RequestMethods.POST,"channel_type_ids",getClass().getName()+".channelTypes");

        if((body==null || body.isEmpty()) && (attachments==null || attachments.isEmpty()))
            throw new RequestBodyCreationEx(RequestMethods.POST,"body,attachments",getClass().getName()+".body,attachments");

    }

    @Override
    public void checkForUpdate() {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleNewMessage{");
        sb.append("\n    sender=").append(sender);
        sb.append("\n    recipients=").append(recipient);
        sb.append("\n    channelTypes=").append(channelTypes);
        sb.append("\n    body='").append(body).append('\'');
        sb.append("\n    attachments=").append(attachments);
        sb.append("}\n");
        return sb.toString();
    }
}
