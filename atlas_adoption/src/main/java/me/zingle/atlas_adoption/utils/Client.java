package me.zingle.atlas_adoption.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import me.zingle.atlas_adoption.facade_models.Participant;

/**
 * Created by SLAVA 09 2015.
 */

public class Client {

    public static class ConversationClient{
        private boolean listVisible;
        private String token;
        private String key;
        private Participant authContact;
        private Participant connectedService;
        private Set<String> channelTypeId=new HashSet<String>();
        synchronized public Set<String> getChannelTypeId() {
            return channelTypeId;
        }

        public void setChannelTypeId(Set<String> channelTypeId) {
            this.channelTypeId = channelTypeId;
        }

        public void addChannelTypeId(String channelTypeId) {
            this.channelTypeId.add(channelTypeId);
        }

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

        public boolean isListVisible() {
            return listVisible;
        }

        public void setListVisible(boolean listVisible) {
            this.listVisible = listVisible;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }


    private static Client item;

    public static Client getItem(){
        if(item==null){
            item=new Client();
        }

        return item;
    }

    private Map<String,ConversationClient> clients= new HashMap<>();

    synchronized public void addClient(ConversationClient client){
        clients.put(client.connectedService.getId(),client);
    }

    synchronized public ConversationClient getClient(String id){
        return clients.get(id);
    }
}
