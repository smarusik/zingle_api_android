package me.zingle.atlas_adoption.model_view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SLAVA 09 2015.
 */
public class DataGroup {
    Date startDate;
    Date endDate;
    List<String> messages;

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

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

}
