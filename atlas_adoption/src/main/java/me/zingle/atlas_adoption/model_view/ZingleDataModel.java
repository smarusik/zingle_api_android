package me.zingle.atlas_adoption.model_view;

import android.util.LruCache;

import java.util.HashMap;
import java.util.Map;

import me.zingle.atlas_adoption.facade_models.Message;

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

    private ZingleDataModel() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        attachmentsCache = new LruCache<String, byte[]>(cacheSize) {
            @Override
            protected int sizeOf(String key, byte[] bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.length / 1024;
            }
        };
    }

    private Map<String,Message> straightList=new HashMap<>();

    public Map<String, Message> getStraightList() {
        return straightList;
    }

    public void setStraightList(Map<String, Message> straightList) {
        this.straightList = straightList;
    }

    private Conversation conversation=new Conversation();

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    private LruCache<String,byte[]> attachmentsCache;

    public void addToMemoryCache(String key, byte[] bitmap) {
        if (getFromMemCache(key) == null) {
            attachmentsCache.put(key, bitmap);
        }
    }

    public byte[] getFromMemCache(String key) {
        return attachmentsCache.get(key);
    }

}

