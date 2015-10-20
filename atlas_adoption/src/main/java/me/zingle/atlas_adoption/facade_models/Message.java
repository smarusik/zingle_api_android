package me.zingle.atlas_adoption.facade_models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by SLAVA 09 2015.
 */
public class Message {

    public static String SEND_INTENT_MSG_ID="send_intent_message_id";
    public static String SEND_INTENT_SERVICE_ID="send_intent_service_id";
    //public static String RECEIVE_INTENT_CONTACT_ID="receive_intent_contact_id";


    private String id;
    private String body;
    private Participant sender;
    private Participant recipient;
    private String channelTypeId;
    private Date createdAt;
    private Date readAt;
    private boolean sent;
    private boolean read;
    List<Attachment> attachments=new ArrayList<>();

    private String genUuid(){
        UUID newMsgUUID=UUID.randomUUID();
        return newMsgUUID.toString();
    }

    public Message() {
        id=genUuid();
    }

    public Message(String body, Participant sender, Participant recipient, Date createdAt) {
        id=genUuid();
        this.body = body;
        this.sender = sender;
        this.recipient = recipient;
        this.createdAt = createdAt;
    }

    public Message(Message other) {
        this.id = other.id;
        this.body = other.body;
        this.sender = other.sender;
        this.recipient = other.recipient;
        this.channelTypeId = other.channelTypeId;
        this.createdAt = other.createdAt;
        this.readAt = other.readAt;
        this.sent = other.sent;
        this.read = other.read;
        this.attachments = other.attachments;
    }

    public String getChannelTypeId() {
        return channelTypeId;
    }

    public void setChannelTypeId(String channelTypeId) {
        this.channelTypeId = channelTypeId;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nMessage{");
        sb.append("\n    id=").append(id);
        sb.append("\n    body='").append(body).append('\'');
        sb.append("\n    sender=").append(sender);
        sb.append("\n    recipient=").append(recipient);
        sb.append("\n    channelTypeId='").append(channelTypeId).append('\'');
        sb.append("\n    createdAt=").append(createdAt);
        sb.append("\n    readAt=").append(readAt);
        sb.append("\n    sent=").append(sent);
        sb.append("\n    read=").append(read);
        sb.append("\n    attachments=").append(attachments);
        sb.append("}\n");
        return sb.toString();
    }
}
