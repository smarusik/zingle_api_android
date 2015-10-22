package me.zingle.atlas_adoption.daemons;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;

import me.zingle.atlas_adoption.facade_models.Attachment;
import me.zingle.atlas_adoption.facade_models.Message;
import me.zingle.atlas_adoption.facade_models.MimeTypes;
import me.zingle.atlas_adoption.model_view.DataServices;
import me.zingle.atlas_adoption.utils.Converters;

/**
 * Created by SLAVA 10 2015.
 */
public class AttachmentDownloader extends IntentService {
    private Handler handler;

    private void updateListView(){
        handler.post(new MessageListUpdater());
    }

    DataServices dataServices=DataServices.getItem();

    public AttachmentDownloader() {
        super("DataDownloader");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String msgId=intent.getStringExtra("key");
        int i=intent.getIntExtra("i",-1);

        if(!msgId.isEmpty() && i>0){
            Message message=new Message(dataServices.popMessage(msgId));
            Attachment attachment=message.getAttachments().get(i);

            if (attachment.getMimeType() == MimeTypes.MIME_TYPE_IMAGE_WEBP ||
                    attachment.getMimeType() == MimeTypes.MIME_TYPE_IMAGE_JPEG ||
                    attachment.getMimeType() == MimeTypes.MIME_TYPE_IMAGE_PNG) {

                if(attachment.getUri()!=null){
                    byte[] data=Converters.uriToByteArray(attachment.getMimeType(), attachment.getUri(), this);
                            dataServices.addCachedItem(attachment.getUri().toString(), data);
                }
                else if (attachment.getUrl()!=null){
                    byte[] data=Converters.urlToByteArray(attachment.getUrl());
                    dataServices.addCachedItem(attachment.getUri().toString(),data);
                }
            }
            updateListView();
        }
    }
}
