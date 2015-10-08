package me.zingle.atlas_adoption.daemons;

import android.app.IntentService;
import android.content.Intent;

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

    DataServices dataServices=DataServices.getItem();


    public MessageSender() {
        super("Message sending service.");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Message msgForSend=dataServices.popUnsentMessage();


        while(msgForSend!=null){

            ZingleService service=new ZingleService(msgForSend.getRecipient().getId(),msgForSend.getRecipient().getName());
            ZingleNewMessage message = Converters.fromMessage(msgForSend);

            for(Attachment a:msgForSend.getAttachments()){
                message.addAttachment(Converters.fromAttachment(a, this));
            }

            ZingleNewMessageService messageService=new ZingleNewMessageService(service);
            messageService.sendMessage(message);

            msgForSend=dataServices.popUnsentMessage();
        }

    }
}
