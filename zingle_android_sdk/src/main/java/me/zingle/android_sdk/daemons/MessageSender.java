package me.zingle.android_sdk.daemons;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

import java.util.Date;
import java.util.List;

import me.zingle.android_sdk.facade_models.Attachment;
import me.zingle.android_sdk.facade_models.Message;
import me.zingle.android_sdk.facade_models.Participant;
import me.zingle.android_sdk.model_view.DataServices;
import me.zingle.android_sdk.utils.Converters;
import me.zingle.api.sdk.Exceptions.UnsuccessfulRequestEx;
import me.zingle.api.sdk.logger.Log;
import me.zingle.api.sdk.model.ZingleMessage;
import me.zingle.api.sdk.model.ZingleNewMessage;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.services.ZingleMessageServices;
import me.zingle.api.sdk.services.ZingleNewMessageService;

/**
 * Created by SLAVA 10 2015.
 */
public class MessageSender extends IntentService{

    private Handler handler;

    final int MAX_TRIES=3;

    DataServices dataServices=DataServices.getItem();

    public MessageSender() {
        super("Message sending service.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler=new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateListView(){
        handler.post(new MessageListUpdater());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String msgLocalId=intent.getStringExtra(Message.SEND_INTENT_MSG_ID);

        Message msgForSend=dataServices.popMessage(msgLocalId);

        if(msgForSend.getSender().getType()== Participant.ParticipantType.CONTACT) {
            ZingleService service = new ZingleService(msgForSend.getRecipient().getId(), msgForSend.getRecipient().getName());
            ZingleNewMessage message = Converters.fromMessage(msgForSend);

            for (Attachment a : msgForSend.getAttachments()) {
                message.addAttachment(Converters.fromAttachment(a, this));
            }

            ZingleNewMessageService messageService = new ZingleNewMessageService(service);
            int triesCount = 0;
            while (!msgForSend.isSent() && triesCount < MAX_TRIES) {
                try {
                    List<String> ids = messageService.sendMessage(message);
                    if (!ids.isEmpty()) {
                        msgForSend.setSent(true);
                        msgForSend.setId(ids.get(0));
                        dataServices.updateItem(msgLocalId, msgForSend);
                    }
                    break;

                } catch (UnsuccessfulRequestEx e) {
                    Log.err("Error sending message\n" + message + "\n" + e.getResponceCode() + "\n" + e.getResponceStr());
                }
                triesCount++;
            }
            if (triesCount >= MAX_TRIES) {
                msgForSend.setFailed(true);
            }
            updateListView();
        }
        else{

            msgForSend.setRead(true);
            msgForSend.setReadAt(new Date());

            ZingleMessageServices messageServices=new ZingleMessageServices(new ZingleService(msgForSend.getSender().getId(),msgForSend.getSender().getName()));
            try {
                messageServices.markRead(new ZingleMessage(msgForSend.getId(), msgForSend.getReadAt().getTime() / 1000));
            }catch (UnsuccessfulRequestEx e) {
                Log.err("Error putting reaing timestamp on message\n" + msgForSend.getId() + "\n" + e.getResponceCode() + "\n" + e.getResponceStr());
            }
        }
    }
}
