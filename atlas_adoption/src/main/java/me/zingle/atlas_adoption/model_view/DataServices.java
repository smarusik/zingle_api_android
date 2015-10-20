package me.zingle.atlas_adoption.model_view;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


    public void initConversation(String serviceId){
        synchronized (dataModel){
            dataModel.addConversation(serviceId,new Conversation());
        }
    }

    public DataGroup getGroupData(String serviceId,int index) {
        DataGroup res;
        synchronized (dataModel) {
            res =new DataGroup(dataModel.getConversation(serviceId).getGroups().get(index));
        }
        return res;
    }

    public int getGroupCount(String serviceId) {
        int res = 0;
        synchronized (dataModel) {

            if(dataModel.getConversation(serviceId)!=null &&
                    dataModel.getConversation(serviceId).getGroups()!=null)

                res = dataModel.getConversation(serviceId).getGroups().size();
        }
        return res;
    }

    public int getItemCount(String serviceId,int groupIndex){
        int res = 0;
        synchronized (dataModel) {
            if(dataModel.getConversation(serviceId)!=null &&
                    dataModel.getConversation(serviceId).getGroups().get(groupIndex)!=null &&
                    dataModel.getConversation(serviceId).getGroups().get(groupIndex).getMessages()!=null)

                res = dataModel.getConversation(serviceId).getGroups().get(groupIndex).getMessages().size();
        }
        return res;
    }

    public Message getItemData(String serviceId,int groupIndex, int childIndex){
        Message res = null;
        synchronized (dataModel) {
            if(dataModel.getConversation(serviceId)!=null &&
                    dataModel.getConversation(serviceId).getGroups().get(groupIndex)!=null &&
                    dataModel.getConversation(serviceId).getGroups().get(groupIndex).getMessages()!=null)

                res = new Message(dataModel.getConversation(serviceId).getGroups().get(groupIndex).getMessages().get(childIndex));
        }
        return res;
    }

    public String getItemId(String serviceId,int groupIndex, int childIndex){
        String res=null;

        synchronized (dataModel) {
            if(dataModel.getConversation(serviceId)!=null &&
                    dataModel.getConversation(serviceId).getGroups().get(groupIndex)!=null &&
                    dataModel.getConversation(serviceId).getGroups().get(groupIndex).getMessages()!=null &&
                    dataModel.getConversation(serviceId).getGroups().get(groupIndex).getMessages().get(childIndex)!=null)

                res = dataModel.getConversation(serviceId).getGroups().get(groupIndex).getMessages().get(childIndex).getId();
        }
        return res;

    }

    public void addItem(String serviceId,Message message){

        synchronized (dataModel) {
            DataGroup group = null;
            Date cntlDate = message.getCreatedAt();

            Conversation conversation=dataModel.getConversations().get(serviceId);
            if(conversation==null){
                conversation=new Conversation();
                dataModel.addConversation(serviceId,conversation);
            }

            for (DataGroup g : conversation.getGroups()) {
                if (cntlDate.after(g.getStartDate()) && cntlDate.before(g.getEndDate())) {
                    group = g;
                    break;
                }
            }

            if (group == null) {
                group = new DataGroup(startOfDay(cntlDate), endOfDay(cntlDate));
                group.addMessage(message);
                conversation.addGroup(group);
            } else {
                conversation.addMessage(group, message);
            }

        }
    }

    public Message popUnsentMessage(String serviceId,String id){
        synchronized (dataModel) {
            Conversation conversation=dataModel.getConversations().get(serviceId);

            Message result=null;

            for(int i=conversation.getGroups().size()-1;i>=0;--i){
                List<Message> grp=conversation.getGroups().get(i).getMessages();
                for(int j=grp.size()-1;j>=0;--j){
                    Message cm=grp.get(j);
                    if(!cm.isSent() && cm.getId().equals(id)){
                        result=new Message(cm);
                        break;
                    }
                }
            }
            return result;
        }
    }

    public void updateItem(String serviceId,String oldId,Message msg){
        synchronized (dataModel) {
            Conversation conversation=dataModel.getConversations().get(serviceId);

            for(int i=conversation.getGroups().size()-1;i>=0;--i){
                List<Message> grp=conversation.getGroups().get(i).getMessages();
                for(int j=grp.size()-1;j>=0;--j){
                    Message cm=grp.get(j);
                    if(!cm.isSent() && cm.getId().equals(oldId)){
                        grp.set(j,msg);
                        break;
                    }
                }
            }
        }
    }


    public boolean updateItemIfExists(String serviceId,Message msg){
        synchronized (dataModel) {
            Conversation conversation=dataModel.getConversations().get(serviceId);

            if(conversation==null) return false;

            for(int i=conversation.getGroups().size()-1;i>=0;--i){
                List<Message> grp=conversation.getGroups().get(i).getMessages();
                for(int j=grp.size()-1;j>=0;--j){
                    Message cm=grp.get(j);
                    if(!cm.isSent() && cm.getId().equals(msg.getId())){
                        grp.set(j,msg);
                        return true;
                    }
                }
            }
            return false;
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