package me.zingle.atlas_adoption.model_view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.zingle.atlas_adoption.facade_models.Message;

/**
 * Created by SLAVA 09 2015.
 */
public class DataGroup {
    Date startDate;
    Date endDate;
    List<Message> messages;

    public DataGroup(Date startDate, Date endDate) {
        this.messages=new ArrayList<>();
        this.startDate = startDate;
        this.endDate=endDate;
    }

    public DataGroup(DataGroup other) {
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.messages = other.messages;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        if(messages.isEmpty() || messages.get(messages.size()-1).getCreatedAt().before(message.getCreatedAt())){
            messages.add(message);
            return;
        }
        else{
            for(int i=messages.size()-2;i>=0;i--){
                if(messages.get(i).getCreatedAt().before(message.getCreatedAt())){
                    messages.add(i+1,message);
                    return;
                }
            }
        }
        messages.add(0,message);
    }
}
