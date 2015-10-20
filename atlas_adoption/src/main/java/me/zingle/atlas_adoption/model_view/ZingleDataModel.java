package me.zingle.atlas_adoption.model_view;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleDataModel {

    private static ZingleDataModel item;

    public static ZingleDataModel getItem(){
        if(item==null)
            item= new ZingleDataModel();

        return item;
    }

//    private Map<String,Message> straightList=new HashMap<>();

    private Map<String,Conversation> conversations=new HashMap<>();

    public Map<String, Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(Map<String, Conversation> conversations) {
        this.conversations = conversations;
    }

    public void addConversation(String key,Conversation conversation) {
        this.conversations.put(key,conversation);
    }

    public Conversation getConversation(String serviceId) {
        return conversations.get(serviceId);
    }

}
