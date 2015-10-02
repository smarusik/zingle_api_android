package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Arrays;
import java.util.List;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 10 2015.
 */
public class ZingleNewMessage extends ZingleBaseModel {

    private class CorrespondentTypes{
        final String[] correspondentTypes ={"contact", "service","label"};
        public CorrespondentTypes(String name) {
            if(Arrays.asList(correspondentTypes).contains(name)) {
                this.name = name;
            }
            else
                throw new RuntimeException("Unsupported Correspondent type.");
        }

        private String name;

        @Override
        public String toString() {
            return name;
        }
    }

    private CorrespondentTypes senderType;
    private ZingleCorrespondent sender;
    private CorrespondentTypes recipientType;
    private List<ZingleCorrespondent> recipients;
    private List<ZingleChannelType> channelTypes;
    private String body;
    private List<ZingleAttachment> attachments;

    public ZingleNewMessage() {
    }

    public String getSenderType() {
        return senderType.name;
    }

    public void setSenderType(String senderType) {
        this.senderType = new CorrespondentTypes(senderType);
    }

    public ZingleCorrespondent getSender() {
        return sender;
    }

    public void setSender(ZingleCorrespondent sender) {
        this.sender = sender;
    }

    public String getRecipientType() {
        return recipientType.name;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = new CorrespondentTypes(recipientType);
    }

    public List<ZingleCorrespondent> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<ZingleCorrespondent> recipients) {
        this.recipients = recipients;
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

    @Override
    public JSONObject extractCreationData() {

        checkForCreate();

        JSONStringer res=new JSONStringer();

        res.object();

        res.key("sender_type").value(senderType.name);
        res.key("sender").value(sender.extractCreationData());

        res.key("recipient_type").value(recipientType.name);

        res.key("recipients");
        res.array();
        for(ZingleCorrespondent c:recipients){
            c.extractCreationData();
        }
        res.endArray();

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
        if(senderType==null||senderType.name.isEmpty())
            throw new RequestBodyCreationEx(RequestMethods.POST,"sender_type",getClass().getName()+".senderType");

        if(sender==null)
            throw new RequestBodyCreationEx(RequestMethods.POST,"sender",getClass().getName()+".sender");

        if(recipientType==null)
            throw new RequestBodyCreationEx(RequestMethods.POST,"recipient_type",getClass().getName()+".recipientType");

        if(recipients==null || recipients.isEmpty())
            throw new RequestBodyCreationEx(RequestMethods.POST,"recipients",getClass().getName()+".recipients");

        if(channelTypes==null || channelTypes.isEmpty())
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
        sb.append("\n    senderType=").append(senderType);
        sb.append("\n    sender=").append(sender);
        sb.append("\n    recipientType=").append(recipientType);
        sb.append("\n    recipients=").append(recipients);
        sb.append("\n    channelTypes=").append(channelTypes);
        sb.append("\n    body='").append(body).append('\'');
        sb.append("\n    attachments=").append(attachments);
        sb.append("}\n");
        return sb.toString();
    }
}
