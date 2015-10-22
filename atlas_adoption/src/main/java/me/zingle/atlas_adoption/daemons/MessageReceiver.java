package me.zingle.atlas_adoption.daemons;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

import java.util.List;

import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleMessage;
import me.zingle.api.sdk.model.ZingleSortOrder;
import me.zingle.api.sdk.services.ZingleMessageServices;
import me.zingle.atlas_adoption.facade_models.Attachment;
import me.zingle.atlas_adoption.facade_models.Message;
import me.zingle.atlas_adoption.model_view.DataServices;
import me.zingle.atlas_adoption.utils.Converters;

/**
 * Created by SLAVA 10 2015.
 */
public class MessageReceiver extends IntentService {

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
        handler=new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final WorkingDataSet wds = WorkingDataSet.getItem();

        while (true) {
            synchronized (this) {
                try {
                    wait(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i=0; i<wds.getContacts().size();i++) {
                ZingleContact contact=wds.getContact(i);
                updateMessageListForContact(contact, true);
            }
            updateListView();
        }
    }

    private void updateMessageListForContact(ZingleContact contact, boolean all) {
        ZingleMessageServices messageServices = new ZingleMessageServices(contact.getService());

        List<QueryPart> conds = messageServices.createConditions("contact_id,page,sort_field,sort_direction", contact.getId(), "1", "created_at", ZingleSortOrder.ZINGLE_DESC.toString());

        ZingleList<ZingleMessage> messages = messageServices.list(conds);
        if (messages != null) {
            for (ZingleMessage message : messages.objects) {
                Message msg = Converters.fromZingleMessage(message);
                dataServices.addItem(msg);
                processAttachments(msg);
            }

            if (all) {
                int pagesNum = messages.totalPages;
                for (int i = 2; i <= pagesNum; i++) {
                    conds = messageServices.createConditions("contact_id,page,sort_field,sort_direction", contact.getId(), String.valueOf(i), "created_at", ZingleSortOrder.ZINGLE_DESC.toString());

                    messages = messageServices.list(conds);
                    if (messages != null) {
                        for (ZingleMessage message : messages.objects) {
                            Message msg = Converters.fromZingleMessage(message);
                            dataServices.addItem(msg);
                            processAttachments(msg);
                        }
                    }
                }
            }
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