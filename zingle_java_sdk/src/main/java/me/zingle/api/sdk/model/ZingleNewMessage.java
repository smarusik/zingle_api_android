package me.zingle.api.sdk.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Support class for creating and sending new messages. See <a href=https://github.com/Zingle/rest-api/blob/master/messages/POST_create.md#send-message>Send Message</a>
 */
public class ZingleNewMessage extends ZingleBaseModel {
    //Correspondent types
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
    private List<ZingleCorrespondent> recipients=new ArrayList<>();
    private List<ZingleChannelType> channelTypes=new ArrayList<>();
    private String body;
    private List<ZingleAttachment> attachments=new ArrayList<>();

    public ZingleNewMessage() {
    }

    public CorrespondentTypes getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType =new CorrespondentTypes(senderType);
    }

    public CorrespondentTypes getRecipientType() {
        return recipientType;
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

    public ZingleCorrespondent getSender() {
        return sender;
    }

    public void setSender(ZingleCorrespondent sender) {
        this.sender = sender;
    }


    public List<ZingleCorrespondent> getRecipient() {
        return recipients;
    }

    public void setRecipient(List<ZingleCorrespondent> recipients) {
        this.recipients = recipients;
    }

    public List<ZingleChannelType> getChannelTypes() {
        return channelTypes;
    }

    public void setChannelTypes(List<ZingleChannelType> channelTypes) {
        this.channelTypes = channelTypes;
    }

    public void addChannelTypes(List<ZingleChannelType> channelType) {
        this.channelTypes.addAll(channelType);
    }

    public void addChannelType(ZingleChannelType channelType) {
        this.channelTypes.add(channelType);
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

    public void addRecipient(ZingleCorrespondent recipient) {
        this.recipients.add(recipient);
    }



    @Override
    public JSONObject extractCreationData() {

        checkForCreate();

        JSONObject resJS=new JSONObject();

        try {
            resJS.put("sender_type",senderType);
            resJS.put("sender", sender.extractCreationData());

            resJS.put("recipient_type",recipientType);

            JSONArray resJSA=new JSONArray();
            for(ZingleCorrespondent c:recipients) {
                resJSA.put(c.extractCreationData());
            }
            resJS.put("recipients",resJSA);

            resJSA=new JSONArray();
            if(channelTypes!=null) {
                for (ZingleChannelType t : channelTypes) {
                    resJSA.put(t.getId());
                }
                resJS.put("channel_type_ids", resJSA);
            }


            resJS.put("body", body);

            if(attachments!=null && attachments.size()>0){
                resJSA=new JSONArray();
                for(ZingleAttachment a:attachments){
                    resJSA.put(a.extractCreationData());
                }
                resJS.put("attachments",resJSA);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
    }

    @Override
    public void checkForCreate() {

        if(sender==null)
            throw new RequestBodyCreationEx(RequestMethods.POST,"sender",getClass().getName()+".sender");

        if(recipients==null || recipients.isEmpty())
            throw new RequestBodyCreationEx(RequestMethods.POST,"recipients",getClass().getName()+".recipients");

        if(recipientType.equals("label") && (channelTypes==null || channelTypes.isEmpty()))
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
        sb.append("\n    recipients=").append(recipients);
        sb.append("\n    channelTypes=").append(channelTypes);
        sb.append("\n    body='").append(body).append('\'');
        sb.append("\n    attachments=").append(attachments);
        sb.append("}\n");
        return sb.toString();
    }
}
