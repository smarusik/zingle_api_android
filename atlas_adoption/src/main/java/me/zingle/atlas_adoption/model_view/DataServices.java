package me.zingle.atlas_adoption.model_view;

import java.util.Calendar;
import java.util.Date;

import me.zingle.atlas_adoption.MessagesList;
import me.zingle.atlas_adoption.facade_models.Message;

/**
 * Created by SLAVA 09 2015.
 */
public class DataServices {

    private static DataServices item;

    public static DataServices getItem(){
        if(item==null) item=new DataServices();

        return item;
    }

    final ZingleDataModel dataModel = ZingleDataModel.getItem();

    private DataServices() {
    }


    public DataGroup getGroupData(int index) {
        DataGroup res = null;
        synchronized (dataModel) {
            res = dataModel.getGroups().get(index);
        }
        return res;
    }

    public int getGroupCount() {
        int res = 0;
        synchronized (dataModel) {
            res = dataModel.getGroups().size();
        }
        return res;
    }

    public int getItemCount(int groupIndex){
        int res = 0;
        synchronized (dataModel) {
            res = dataModel.getGroups().get(groupIndex).getMessages().size();
        }
        return res;
    }

    public Message getItemData(int groupIndex, int childIndex){
        Message res = null;
        synchronized (dataModel) {
            res = dataModel.getGroups().get(groupIndex).getMessages().get(childIndex);
        }
        return res;
    }

    public Long getItemId(int groupIndex, int childIndex){
        Long res=null;

        synchronized (dataModel) {
            res = dataModel.getGroups().get(groupIndex).getMessages().get(childIndex).getId();
        }
        return res;

    }

    public void addItem(Message message){

        synchronized (dataModel) {
            DataGroup group = null;
            Date cntlDate = message.getCreatedAt();

            for (DataGroup g : dataModel.getGroups()) {
                if (cntlDate.after(g.getStartDate()) && cntlDate.before(g.getEndDate())) {
                    group = g;
                    break;
                }
            }

            if (group == null) {
                group = new DataGroup(startOfDay(cntlDate), endOfDay(cntlDate));
                group.addMessage(message);
                dataModel.addGroup(group);
            } else {
                dataModel.addMessage(group, message);
            }

        }
    }

    public void addItem(Message message, MessagesList messagesList){
        addItem(message);

        if(messagesList!=null){
            messagesList.reloadMessagesList();
            messagesList.showLastMessage();
        }
    }

    public void addUnsentMessage(Message msg){
        synchronized (dataModel) {
            dataModel.pushUnsentMessage(msg);
        }
    }

    public Message popUnsentMessage(){
        synchronized (dataModel) {
            return dataModel.popUnsentMessage();
        }
    }

    private Date startOfDay(Date date){
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 0, 0, 0);

        return c.getTime();
    }

    private Date endOfDay(Date date){
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 23, 59, 59);

        return c.getTime();
    }

}