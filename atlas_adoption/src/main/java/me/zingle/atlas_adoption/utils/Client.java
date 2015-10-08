package me.zingle.atlas_adoption.utils;

import me.zingle.atlas_adoption.facade_models.Participant;

/**
 * Created by SLAVA 09 2015.
 */
public class Client {

    public static final String CONTACT_ID="contact_id";
    public static final String CONTACT_NAME="contact_name";
    public static final String CONTACT_CH_TYPE="contact_channel_type";
    public static final String CONTACT_CH_VALUE="contact_channel_value";

    public static final String SERVICE_ID="service_id";
    public static final String SERVICE_NAME="service_name";
    public static final String SERVICE_CH_TYPE="service_channel_type";
    public static final String SERVICE_CH_VALUE="service_channel_value";


    private Participant authContact;
    private Participant connectedService;

    public Participant getAuthContact() {
        return authContact;
    }

    public void setAuthContact(Participant authContact) {
        this.authContact = authContact;
    }

    public Participant getConnectedService() {
        return connectedService;
    }

    public void setConnectedService(Participant connectedService) {
        this.connectedService = connectedService;
    }

}
