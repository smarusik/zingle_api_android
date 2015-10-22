package me.zingle.atlas_adoption.daemons;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

import java.util.List;

import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.logger.Log;
import me.zingle.api.sdk.model.ZingleNewMessage;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.services.ZingleNewMessageService;
import me.zingle.atlas_adoption.facade_models.Attachment;
import me.zingle.atlas_adoption.facade_models.Message;
import me.zingle.atlas_adoption.model_view.DataServices;
import me.zingle.atlas_adoption.utils.Converters;

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
        String serviceId=intent.getStringExtra(Message.SEND_INTENT_SERVICE_ID);

        Message msgForSend=dataServices.popMessage(msgLocalId);

        ZingleService service=new ZingleService(msgForSend.getRecipient().getId(),msgForSend.getRecipient().getName());
        ZingleNewMessage message = Converters.fromMessage(msgForSend);

        for(Attachment a:msgForSend.getAttachments()){
            message.addAttachment(Converters.fromAttachment(a, this));
        }

        ZingleNewMessageService messageService=new ZingleNewMessageService(service);
        int triesCount=0;
        while(!msgForSend.isSent() && triesCount<MAX_TRIES) {
            try {
                List<String> ids=messageService.sendMessage(message);
                if(!ids.isEmpty()){
                    msgForSend.setSent(true);
                    msgForSend.setId(ids.get(0));
                    dataServices.updateItem(msgLocalId,msgForSend);
                }

            } catch (UnsuccessfullRequestEx e) {
                Log.err("Error sending message\n" + message + "\n" + e.getResponceCode() + "\n" + e.getResponceStr());
            }
            triesCount++;
        }
        if(triesCount>=MAX_TRIES){
            msgForSend.setFailed(true);
        }
        updateListView();
    }
}
