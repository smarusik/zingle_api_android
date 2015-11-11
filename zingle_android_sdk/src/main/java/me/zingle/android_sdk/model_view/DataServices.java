package me.zingle.android_sdk.model_view;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.zingle.android_sdk.MessagesList;
import me.zingle.android_sdk.facade_models.Message;
import me.zingle.android_sdk.utils.Client;

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
    MessagesList listView;

    private DataServices() {

    }

    public MessagesList getListView() {
        return listView;
    }

    public void setListView(MessagesList listView) {
        this.listView = listView;
    }

    public void updateMessagesList(){
        if(listView!=null)
            listView.reloadMessagesList();
    }

    public void showEndOfMessagesList(){
        if(listView!=null)
            listView.showLastMessage();
    }

    public void initConversation(String id, MessagesList list){
        synchronized (dataModel){
            dataModel.setConversation(new Conversation());
            listView=list;

            Iterator<Map.Entry<String,Message>> i=dataModel.getStraightList().entrySet().iterator();

            while(i.hasNext()){
                addToConversation(id,i.next().getValue());
            }
        }
    }

    public DataGroup getGroupData(int index) {
        DataGroup res;
        synchronized (dataModel) {
            res =new DataGroup(dataModel.getConversation().getGroups().get(index));
        }
        return res;
    }

    public int getGroupCount() {
        int res = 0;
        synchronized (dataModel) {

            if(dataModel.getConversation()!=null &&
                    dataModel.getConversation().getGroups()!=null)

                res = dataModel.getConversation().getGroups().size();
        }
        return res;
    }

    public int getItemCount(int groupIndex){
        int res = 0;
        synchronized (dataModel) {
            if(dataModel.getConversation()!=null &&
                    dataModel.getConversation().getGroups().get(groupIndex)!=null &&
                    dataModel.getConversation().getGroups().get(groupIndex).getMessages()!=null)

                res = dataModel.getConversation().getGroups().get(groupIndex).getMessages().size();
        }
        return res;
    }

    public Message getItemData(int groupIndex, int childIndex){
        Message res = null;
        synchronized (dataModel) {
            if(dataModel.getConversation()!=null &&
                    dataModel.getConversation().getGroups().get(groupIndex)!=null &&
                    dataModel.getConversation().getGroups().get(groupIndex).getMessages()!=null) {

                String msgId = dataModel.getConversation().getGroups().get(groupIndex).getMessages().get(childIndex);

                res = new Message(dataModel.getStraightList().get(msgId));
            }
        }
        return res;
    }

    public String getItemId(int groupIndex, int childIndex){
        String res=null;

        synchronized (dataModel) {
            if(dataModel.getConversation()!=null &&
                    dataModel.getConversation().getGroups().get(groupIndex)!=null &&
                    dataModel.getConversation().getGroups().get(groupIndex).getMessages()!=null &&
                    dataModel.getConversation().getGroups().get(groupIndex).getMessages().get(childIndex)!=null)

                res = dataModel.getConversation().getGroups().get(groupIndex).getMessages().get(childIndex);
        }
        return res;

    }

    public boolean addToConversation(String conversationId,Message message){
        synchronized (dataModel) {

            Client.ConversationClient client = Client.getItem().getClient(conversationId);
            if (client.isListVisible() &&
                    (message.getSender().equals(client.getAuthContact()) || message.getRecipient().equals(client.getAuthContact()))) {

                DataGroup group = null;
                Date cntlDate = message.getCreatedAt();

                Conversation conversation = dataModel.getConversation();

                for (DataGroup g : conversation.getGroups()) {
                    if (cntlDate.after(g.getStartDate()) && cntlDate.before(g.getEndDate())) {
                        group = g;
                        break;
                    }
                }

                if (group != null) {
                    if (!group.getMessages().contains(message.getId()))
                        addMessageToGroup(group, message);
                } else {
                    group = new DataGroup(startOfDay(cntlDate), endOfDay(cntlDate));
                    conversation.addGroup(group);
                    addMessageToGroup(group, message);
                }

                return true;
            }
            else return false;
        }
    }

    public boolean conversationVisible(String serviceId){
        Client.ConversationClient client = Client.getItem().getClient(serviceId);
        return client.isListVisible();
    }

    public boolean addItem(Message message){

        synchronized (dataModel) {
            Message oldMsg=dataModel.getStraightList().get(message.getId());
            if(oldMsg!=null){
                oldMsg.setBody(message.getBody());
                oldMsg.setId(message.getId());
                oldMsg.setFailed(message.isFailed());
                oldMsg.setSent(message.isSent());
/*
                oldMsg.setRead(message.isRead());
                oldMsg.setReadAt(message.getReadAt());
*/

                return false;
            }
            else {
                dataModel.getStraightList().put(message.getId(), message);
                return true;
            }
        }
    }

    public void updateSendingResult(Message msg, boolean isSent, String receivedId){
        synchronized (dataModel){
            if(isSent){

                Conversation conversation=dataModel.getConversation();
                for(DataGroup g:conversation.getGroups()){
                    int ind=g.messages.indexOf(receivedId);
                    if(ind>0)
                        g.messages.remove(ind);

                    ind=g.messages.indexOf(msg.getId());
                    if(ind>0){
                        g.messages.set(ind,receivedId);
                        break;
                    }
                }

                dataModel.getStraightList().remove(msg.getId());
                if(!dataModel.getStraightList().containsKey(receivedId)) {
                    msg.setId(receivedId);
                    msg.setSent(true);
                    dataModel.getStraightList().put(receivedId,msg);
                }
            }
            else {
                msg.setFailed(true);
            }
        }
    }

    public void updateReadStatus(Message msg){
        synchronized (dataModel){
            msg.setRead(true);
            msg.setReadAt(new Date());
        }
    }

    private void addMessageToGroup(DataGroup group,Message message) {

        List<String> messages=group.getMessages();
        Message lastMessage=null;

        if(messages!=null && messages.size()>0) {
            lastMessage = dataModel.getStraightList().get(messages.get(messages.size() - 1));
        }

        if(lastMessage==null || lastMessage.getCreatedAt().before(message.getCreatedAt())){
            messages.add(message.getId());
            return;
        }
        else{
            for(int i=messages.size()-2;i>=0;i--){
                lastMessage=dataModel.getStraightList().get(messages.get(i));
                if(lastMessage!=null && lastMessage.getCreatedAt().before(message.getCreatedAt())){
                    messages.add(i+1,message.getId());
                    return;
                }
            }
        }
        messages.add(0,message.getId());
    }


    public Message popMessage(String id){
        synchronized (dataModel) {
            return dataModel.getStraightList().get(id);
        }
    }

/*
    public void updateItem(String oldId,Message msg){
        synchronized (dataModel) {
            Conversation conversation=dataModel.getConversation();
            for(DataGroup g:conversation.getGroups()){
                int ind=g.messages.indexOf(oldId);
                if(ind>0){
                    g.messages.set(ind,msg.getId());
                    break;
                }
            }
        }
    }
*/

    public byte[] getCachedItem(String key){
        byte[] result;

        synchronized (dataModel){
            result=dataModel.getFromMemCache(key);
        }

        return result;
    }

    public void addCachedItem(String key,byte[] item){
        synchronized (dataModel){
            dataModel.addToMemoryCache(key, item);
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