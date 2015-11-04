package me.zingle.android_sdk.daemons;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import me.zingle.android_sdk.R;
import me.zingle.android_sdk.ZingleMessagingActivity;
import me.zingle.android_sdk.facade_models.Attachment;
import me.zingle.android_sdk.facade_models.Message;
import me.zingle.android_sdk.facade_models.Participant;
import me.zingle.android_sdk.model_view.DataServices;
import me.zingle.android_sdk.utils.Converters;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleMessage;
import me.zingle.api.sdk.model.ZingleSortOrder;
import me.zingle.api.sdk.services.ZingleMessageServices;

//import android.app.Notification;

/**
 * Created by SLAVA 10 2015.
 */
public class MessageReceiver extends IntentService {

    private String MESSAGES_QUERY_PAGE_SIZE;
    private int NOTIFICATION_STRING_LENGTH;
    private int RECEIVE_FREQUENCY;
    private final int NOTIFICATION_ID=5060;
    private Handler handler;

    private void updateListView(){
        handler.post(new MessageListUpdater());
    }

    DataServices dataServices=DataServices.getItem();

    public MessageReceiver() {
        super("MessageReceiver");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MESSAGES_QUERY_PAGE_SIZE=getResources().getString(R.string.message_query_page_size);
        NOTIFICATION_STRING_LENGTH=getResources().getInteger(R.integer.notification_string_length);
        RECEIVE_FREQUENCY=getResources().getInteger(R.integer.message_monitoring_frequency);

        handler=new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final WorkingDataSet wds = WorkingDataSet.getItem();

        while (true) {

            for (int i=0; i<wds.getContacts().size();i++) {
                ZingleContact contact=wds.getContact(i);
                updateMessageListForContact(contact, true);
            }
            updateListView();

            synchronized (this) {
                try {
                    wait(RECEIVE_FREQUENCY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseMsgList(ZingleList<ZingleMessage> messages){
        for (ZingleMessage message : messages.objects) {
            Message msg = Converters.fromZingleMessage(message);
            boolean isNewMessage=dataServices.addItem(msg);
            processAttachments(msg);

            String conversationId;
            if(msg.getSender().getType()== Participant.ParticipantType.SERVICE) conversationId=message.getSender().getId();
            else conversationId=message.getRecipient().getId();

            boolean isAddedToConversation=dataServices.addToConversation(conversationId,msg);
            if(!msg.isRead() && isNewMessage) {
                if (!(isAddedToConversation && dataServices.conversationVisible(conversationId)) && msg.getSender().getType() == Participant.ParticipantType.SERVICE) {
                    //Use notification if conversation inactive.
                    Intent intent = new Intent(this, ZingleMessagingActivity.class);
                    intent.putExtra(ZingleMessagingActivity.BASE_SERVICE_ID,conversationId);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    String contentText=msg.getBody();
                    if(contentText.length()>NOTIFICATION_STRING_LENGTH)
                        contentText=msg.getBody().substring(0, NOTIFICATION_STRING_LENGTH-1) + "...";

                    Notification notification = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_zingle_notify)
                            .setContentTitle(msg.getSender().getName())
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setContentIntent(pendingIntent)
                            .setContentText(contentText)
                            .build();
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICATION_ID, notification);
                }
            }
        }
    }

    private void updateMessageListForContact(ZingleContact contact, boolean all) {
        ZingleMessageServices messageServices = new ZingleMessageServices(contact.getService());

        List<QueryPart> conds = messageServices.createConditions("page_size,contact_id,page,sort_field,sort_direction",
                MESSAGES_QUERY_PAGE_SIZE,contact.getId(),"1", "created_at", ZingleSortOrder.ZINGLE_DESC.toString());

        ZingleList<ZingleMessage> messages = messageServices.list(conds);
        if (messages != null) {
            parseMsgList(messages);

            if (all) {
                int pagesNum = messages.totalPages;
                for (int i = 2; i <= pagesNum; i++) {
                    conds = messageServices.createConditions("contact_id,page,sort_field,sort_direction", contact.getId(), String.valueOf(i), "created_at", ZingleSortOrder.ZINGLE_DESC.toString());

                    messages = messageServices.list(conds);
                    if (messages != null) {
                        parseMsgList(messages);
                    }
                }
            }
            handler.post(new MessageListUpdater(false));
        }
    }

    private void processAttachments(Message msg){
        List<Attachment> attachments=msg.getAttachments();

        if(attachments!=null && attachments.size()>0){
            for(int i=0;i<attachments.size();i++) {
                String key=attachments.get(i).getCachePath();
                if(!key.isEmpty()) {
                    if (dataServices.getCachedItem(key)==null) {
                        Intent intent = new Intent(this, AttachmentDownloader.class);
                        intent.putExtra("key",msg.getId());
                        intent.putExtra("i",i);
                        startService(intent);
                    }
                }
            }
        }
    }
}