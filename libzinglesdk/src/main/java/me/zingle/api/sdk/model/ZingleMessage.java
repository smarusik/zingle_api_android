package me.zingle.api.sdk.model;

import java.util.List;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleMessage {
    private int id;
    private String body;
    private ZingleParticipant sender;
    private ZingleParticipant recipient;
    private Integer templateId=null;
    private String bodyLanguageCode=null;
    private String translatedBodyLanguageCode=null;
    private String translatedBody=null;
    private Long createdAt;
    private Long readAt;
    private Integer triggered_by_user_id=null;
    private List<ZingleAttachment> attachments;

    public ZingleMessage() {
    }

    public ZingleMessage(int id, String body, ZingleParticipant sender, ZingleParticipant recipient, Integer templateId, String bodyLanguageCode,
                         String translatedBodyLanguageCode, String translatedBody, Long createdAt, Long readAt, Integer triggered_by_user_id,
                         List<ZingleAttachment> attachments) {
        this.id = id;
        this.body = body;
        this.sender = sender;
        this.recipient = recipient;
        this.templateId = templateId;
        this.bodyLanguageCode = bodyLanguageCode;
        this.translatedBodyLanguageCode = translatedBodyLanguageCode;
        this.translatedBody = translatedBody;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.triggered_by_user_id = triggered_by_user_id;
        this.attachments = attachments;
    }

    public ZingleMessage(String body, ZingleParticipant sender, ZingleParticipant recipient) {
        this.body = body;
        this.sender = sender;
        this.recipient = recipient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZingleParticipant getSender() {
        return sender;
    }

    public void setSender(ZingleParticipant sender) {
        this.sender = sender;
    }

    public ZingleParticipant getRecipient() {
        return recipient;
    }

    public void setRecipient(ZingleParticipant recipient) {
        this.recipient = recipient;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getBodyLanguageCode() {
        return bodyLanguageCode;
    }

    public void setBodyLanguageCode(String bodyLanguageCode) {
        this.bodyLanguageCode = bodyLanguageCode;
    }

    public String getTranslatedBodyLanguageCode() {
        return translatedBodyLanguageCode;
    }

    public void setTranslatedBodyLanguageCode(String translatedBodyLanguageCode) {
        this.translatedBodyLanguageCode = translatedBodyLanguageCode;
    }

    public String getTranslatedBody() {
        return translatedBody;
    }

    public void setTranslatedBody(String translatedBody) {
        this.translatedBody = translatedBody;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getReadAt() {
        return readAt;
    }

    public void setReadAt(Long readAt) {
        this.readAt = readAt;
    }

    public Integer getTriggered_by_user_id() {
        return triggered_by_user_id;
    }

    public void setTriggered_by_user_id(Integer triggered_by_user_id) {
        this.triggered_by_user_id = triggered_by_user_id;
    }

    public List<ZingleAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ZingleAttachment> attachments) {
        this.attachments = attachments;
    }
}
