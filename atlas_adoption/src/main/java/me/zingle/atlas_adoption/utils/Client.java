package me.zingle.atlas_adoption.utils;

import java.util.HashSet;
import java.util.Set;

import me.zingle.atlas_adoption.facade_models.Participant;

/**
 * Created by SLAVA 09 2015.
 */
public class Client {
    public static final String CONTACT_ID="contact_id";
    public static final String CONTACT_CH_VALUE="contact_channel_value";
    public static final String SERVICE_ID="service_id";
    public static final String SERVICE_NAME="service_name";
    public static final String SERVICE_CH_VALUE="service_channel_value";
    public static final String CH_TYPE_ID="channel_type_id";

    private static Client item;

    public static Client getItem(){
        if(item==null){
            item=new Client();
        }

        return item;
    }

    private Client() {
    }

    private Participant authContact;
    private Participant connectedService;
    private Set<String> channelTypeId=new HashSet<String>();
    synchronized public Set<String> getChannelTypeId() {
        return channelTypeId;
    }

    synchronized public void setChannelTypeId(Set<String> channelTypeId) {
        this.channelTypeId = channelTypeId;
    }

    synchronized public void addChannelTypeId(String channelTypeId) {
        this.channelTypeId.add(channelTypeId);
    }

    synchronized public Participant getAuthContact() {
        return authContact;
    }

    synchronized public void setAuthContact(Participant authContact) {
        this.authContact = authContact;
    }

    synchronized public Participant getConnectedService() {
        return connectedService;
    }

    synchronized public void setConnectedService(Participant connectedService) {
        this.connectedService = connectedService;
    }

}
