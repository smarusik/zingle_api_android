package me.zingle.atlas_adoption.daemons;

import android.app.IntentService;
import android.content.Intent;

import java.util.List;

import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleMessage;
import me.zingle.api.sdk.model.ZingleSortOrder;
import me.zingle.api.sdk.services.ZingleMessageServices;
import me.zingle.atlas_adoption.facade_models.Message;
import me.zingle.atlas_adoption.model_view.DataServices;
import me.zingle.atlas_adoption.utils.Client;
import me.zingle.atlas_adoption.utils.Converters;

/**
 * Created by SLAVA 10 2015.
 */
public class MessageReceiver extends IntentService {

    public MessageReceiver() {
        super("MessageReceiver");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final WorkingDataSet wds=WorkingDataSet.getItem();
        Client clnt=Client.getItem();

        while(true) {
            synchronized (this) {
                try {
                    wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (ZingleContact contact : wds.getContacts()) {
                updateMessageListForContact(contact, true);
            }
        }
    }

    private void updateMessageListForContact(ZingleContact contact,boolean all){
        DataServices dataServices=DataServices.getItem();
        ZingleMessageServices messageServices=new ZingleMessageServices(contact.getService());

        List<QueryPart> conds=messageServices.createConditions("contact_id,page_number,sort_field,sort_direction",contact.getId(),"1","created_at", ZingleSortOrder.ZINGLE_DESC.toString());

        ZingleList<ZingleMessage> messages=messageServices.list(conds);
        if(messages!=null){
            for (ZingleMessage message : messages.objects) {
                Message msg=Converters.fromZingleMessage(message);
                if(!dataServices.updateItemIfExists(contact.getService().getId(),msg))
                    dataServices.addItem(contact.getService().getId(),msg);
            }
        }

        if(all) {
            int pagesNum = messages.totalPages;
            for (int i = 2; i <= pagesNum; i++) {
                conds = messageServices.createConditions("contact_id,page_number,sort_field,sort_direction", contact.getId(), String.valueOf(i), "created_at", ZingleSortOrder.ZINGLE_DESC.toString());

                messages = messageServices.list(conds);
                if (messages != null) {
                    for (ZingleMessage message : messages.objects) {
                        Message msg=Converters.fromZingleMessage(message);
                        if(!dataServices.updateItemIfExists(contact.getService().getId(),msg))
                            dataServices.addItem(contact.getService().getId(),msg);
                    }
                }
            }
        }
    }
}
