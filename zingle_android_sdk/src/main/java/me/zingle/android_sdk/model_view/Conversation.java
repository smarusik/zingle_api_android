package me.zingle.android_sdk.model_view;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by SLAVA 10 2015.
 */
public class Conversation {

    private List<DataGroup> groups;

    public Conversation() {
        groups=new LinkedList<>();
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
                    groups.add(i+1,group);
                    return;
                }
            }
        }
        groups.add(0,group);
    }
}
