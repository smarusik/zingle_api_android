package me.zingle.api.sdk.model;

import java.util.Date;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleMessage {
    private int id;
    private String body;
    private ZinglePhoneNumber contactPhoneNumber;
    private ZinglePhoneNumber servicePhoneNumber;
    private Integer templateId;
    private ZingleDirerction direction;
    private ZingleContact contact;
    private String bodyLanguageCode;
    private String translatedBodyLanguageCode;
    private String translatedBody;
    private Date createdAt;

    public ZingleMessage() {
    }

    public ZingleMessage(int id, String body, ZinglePhoneNumber contactPhoneNumber,
                         ZinglePhoneNumber servicePhoneNumber, Integer templateId,
                         ZingleDirerction direction, ZingleContact contact,
                         String bodyLanguageCode, String translatedBodyLanguageCode,
                         String translatedBody, Date createdAt) {
        this.id = id;
        this.body = body;
        this.contactPhoneNumber = contactPhoneNumber;
        this.servicePhoneNumber = servicePhoneNumber;
        this.templateId = templateId;
        this.direction = direction;
        this.contact = contact;
        this.bodyLanguageCode = bodyLanguageCode;
        this.translatedBodyLanguageCode = translatedBodyLanguageCode;
        this.translatedBody = translatedBody;
        this.createdAt = createdAt;
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

    public ZinglePhoneNumber getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(ZinglePhoneNumber contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public ZinglePhoneNumber getServicePhoneNumber() {
        return servicePhoneNumber;
    }

    public void setServicePhoneNumber(ZinglePhoneNumber servicePhoneNumber) {
        this.servicePhoneNumber = servicePhoneNumber;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public ZingleDirerction getDirection() {
        return direction;
    }

    public void setDirection(ZingleDirerction direction) {
        this.direction = direction;
    }

    public ZingleContact getContact() {
        return contact;
    }

    public void setContact(ZingleContact contact) {
        this.contact = contact;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
