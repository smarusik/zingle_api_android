package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Arrays;
import java.util.List;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleMessage extends ZingleBaseModel{

    private class Directions{
        final String[] direrctions ={"inbound", "outbound"};
        public Directions(String name) {
            if(Arrays.asList(direrctions).contains(name)) {
                this.name = name;
            }
            else
                throw new RuntimeException("Unsupported Message direction.");
        }

        private String name;

        @Override
        public String toString() {
            return name;
        }
    }

    private class CorrespondentTypes{
        final String[] correspondentTypes ={"contact", "service"};
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

    private String id;
    private String body;
    private Directions communicationDirection;
    private String bodyLanguageCode=null;
    private String translatedBody=null;
    private String translatedBodyLanguageCode=null;
    private String triggeredByUserId;
    private String templateId=null;
    private CorrespondentTypes senderType;
    private ZingleCorrespondent sender;
    private CorrespondentTypes recipientType;
    private ZingleCorrespondent recipient;
    private List<ZingleAttachment> attachments;
    private Integer createdAt;
    private Integer readAt;

    public ZingleMessage() {
    }

    public ZingleMessage(String body, CorrespondentTypes senderType, ZingleCorrespondent sender, CorrespondentTypes recipientType, ZingleCorrespondent recipient) {
        this.body = body;
        this.senderType = senderType;
        this.sender = sender;
        this.recipientType = recipientType;
        this.recipient = recipient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCommunicationDirection() {
        return communicationDirection.name;
    }

    public void setCommunicationDirection(String communicationDirection) {
        this.communicationDirection = new Directions(communicationDirection);
    }

    public String getBodyLanguageCode() {
        return bodyLanguageCode;
    }

    public void setBodyLanguageCode(String bodyLanguageCode) {
        this.bodyLanguageCode = bodyLanguageCode;
    }

    public String getTranslatedBody() {
        return translatedBody;
    }

    public void setTranslatedBody(String translatedBody) {
        this.translatedBody = translatedBody;
    }

    public String getTranslatedBodyLanguageCode() {
        return translatedBodyLanguageCode;
    }

    public void setTranslatedBodyLanguageCode(String translatedBodyLanguageCode) {
        this.translatedBodyLanguageCode = translatedBodyLanguageCode;
    }

    public String getTriggeredByUserId() {
        return triggeredByUserId;
    }

    public void setTriggeredByUserId(String triggeredByUserId) {
        this.triggeredByUserId = triggeredByUserId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public ZingleCorrespondent getRecipient() {
        return recipient;
    }

    public void setRecipient(ZingleCorrespondent recipient) {
        this.recipient = recipient;
    }

    public List<ZingleAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ZingleAttachment> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(ZingleAttachment attachment){
        attachments.add(attachment);
    }

    public void addAttachment(List<ZingleAttachment> attachments){
        attachments.addAll(attachments);
    }


    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getReadAt() {
        return readAt;
    }

    public void setReadAt(Integer readAt) {
        this.readAt = readAt;
    }

    @Override
    public JSONObject extractCreationData() {
        return null;
    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();

        JSONStringer res=new JSONStringer();

        res.object();

        res.key("read_at").value(getReadAt());

        res.endObject();

        return new JSONObject(res.toString());

    }

    @Override
    public void checkForCreate() {

    }

    @Override
    public void checkForUpdate() {
        if(readAt==null) throw new RequestBodyCreationEx(RequestMethods.POST,"read_at",getClass().getName()+".readAt");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleMessage{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    body='").append(body).append('\'');
        sb.append("\n    communicationDirection=").append(communicationDirection);
        sb.append("\n    bodyLanguageCode='").append(bodyLanguageCode).append('\'');
        sb.append("\n    translatedBody='").append(translatedBody).append('\'');
        sb.append("\n    translatedBodyLanguageCode='").append(translatedBodyLanguageCode).append('\'');
        sb.append("\n    triggeredByUserId='").append(triggeredByUserId).append('\'');
        sb.append("\n    templateId='").append(templateId).append('\'');
        sb.append("\n    senderType=").append(senderType);
        sb.append("\n    sender=").append(sender);
        sb.append("\n    recipientType=").append(recipientType);
        sb.append("\n    recipient=").append(recipient);
        sb.append("\n    attachments=").append(attachments);
        sb.append("\n    createdAt=").append(createdAt);
        sb.append("\n    readAt=").append(readAt);
        sb.append("}\n");
        return sb.toString();
    }
}
