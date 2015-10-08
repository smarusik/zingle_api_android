package me.zingle.atlas_adoption.facade_models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SLAVA 09 2015.
 */
public class Message {
    private Long id;
    private String body;
    private Participant sender;
    private Participant recipient;
    private Date createdAt;
    private Date readAt;
    private boolean sent;
    private boolean read;
    List<Attachment> attachments=new ArrayList<>();

    public Message() {

    }

    public Message(String body, Participant sender, Participant recipient, Date createdAt) {
        this.body = body;
        this.sender = sender;
        this.recipient = recipient;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Participant getSender() {
        return sender;
    }

    public void setSender(Participant sender) {
        this.sender = sender;
    }

    public Participant getRecipient() {
        return recipient;
    }

    public void setRecipient(Participant recipient) {
        this.recipient = recipient;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getReadAt() {
        return readAt;
    }

    public void setReadAt(Date readAt) {
        this.readAt = readAt;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }


    public void markAsRead(){
        setRead(true);
        setReadAt(new Date());
    }

    public void addAttachment(Attachment a){
        attachments.add(a);
    }
}
