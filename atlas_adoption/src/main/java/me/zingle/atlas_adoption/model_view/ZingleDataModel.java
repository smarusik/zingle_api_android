package me.zingle.atlas_adoption.model_view;

import java.util.LinkedList;
import java.util.List;

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


    private List<DataGroup> groups;
    private LinkedList<Message> unsentMessages;

    private ZingleDataModel() {
        groups=new LinkedList<>();
        unsentMessages=new LinkedList<>();
    }

    public List<Message> getUnsentMessages() {
        return unsentMessages;
    }


    public List<DataGroup> getGroups () {
        return groups;
    }

    public void setGroups(List<DataGroup> groups) {
        this.groups = groups;
    }

    public void addGroup(DataGroup group) {
        if(groups.isEmpty() || groups.get(groups.size()-1).getEndDate().before(group.getStartDate())){
            groups.add(group);
            return;
        }
        else{
            for(int i=groups.size()-2;i>=0;i--){
                if(groups.get(i).getEndDate().before(group.getStartDate())){
                    groups.add(i,group);
                    return;
                }
            }
        }
        groups.add(0,group);
    }

    public void addMessage(DataGroup group, Message message) {
        int index=groups.indexOf(group);
        if(index!=-1){
            groups.get(index).addMessage(message);
        }
    }

    public void pushUnsentMessage(Message msg){
        unsentMessages.add(msg);
    }

    public Message popUnsentMessage(){
        return unsentMessages.pollFirst();
    }

}
