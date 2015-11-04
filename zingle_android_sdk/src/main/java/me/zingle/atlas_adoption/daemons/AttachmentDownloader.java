package me.zingle.atlas_adoption.daemons;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;

import me.zingle.atlas_adoption.R;
import me.zingle.atlas_adoption.facade_models.Attachment;
import me.zingle.atlas_adoption.facade_models.Message;
import me.zingle.atlas_adoption.model_view.DataServices;
import me.zingle.atlas_adoption.utils.Converters;
import me.zingle.atlas_adoption.utils.ScalingUtilities;

import static me.zingle.atlas_adoption.facade_models.MimeTypes.MIME_TYPE_IMAGE_JPEG;
import static me.zingle.atlas_adoption.facade_models.MimeTypes.MIME_TYPE_IMAGE_PNG;
import static me.zingle.atlas_adoption.facade_models.MimeTypes.MIME_TYPE_IMAGE_WEBP;

/**
 * Created by SLAVA 10 2015.
 */
public class AttachmentDownloader extends IntentService {
    private Handler handler;
/*
    private int screenWidthDp;
    private int smallestScreenWidthDp;
*/

    private void updateListView(){
        handler.post(new MessageListUpdater());
    }

    DataServices dataServices=DataServices.getItem();

    public AttachmentDownloader() {
        super("DataDownloader");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler=new Handler();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String msgId=intent.getStringExtra("key");
        int i=intent.getIntExtra("i",-1);

        if(!msgId.isEmpty() && i>=0){
            Message message=dataServices.popMessage(msgId);
            Attachment attachment=message.getAttachments().get(i);

            //Configuration configuration = dataServices.getListView().getResources().getConfiguration();
            //screenWidthDp = configuration.screenWidthDp;
            //smallestScreenWidthDp = configuration.smallestScreenWidthDp;

            if(attachment.getUri()!=null){
                if (attachment.getMimeType() == MIME_TYPE_IMAGE_WEBP ||
                        attachment.getMimeType() == MIME_TYPE_IMAGE_JPEG ||
                        attachment.getMimeType() == MIME_TYPE_IMAGE_PNG) {

                    byte[] data = Converters.uriToByteArray(attachment.getMimeType(), attachment.getUri(), this);
                    dataServices.addCachedItem(attachment.getUri().toString(), data);


                }
            }
            else if (attachment.getUrl()!=null){
                URLConnection conn= null;
                try {
                    conn = attachment.getUrl().openConnection();
                    String mimeType = conn.getContentType();

                    if(mimeType!=null){
                        attachment.setMimeType(mimeType);
                    }

                    if (attachment.getMimeType() == MIME_TYPE_IMAGE_WEBP ||
                            attachment.getMimeType() == MIME_TYPE_IMAGE_JPEG ||
                            attachment.getMimeType() == MIME_TYPE_IMAGE_PNG) {

                        int mDstWidth = getResources().getDimensionPixelSize(R.dimen.attachment_image_width);
                        int mDstHeight = getResources().getDimensionPixelSize(R.dimen.attachment_image_height);

                        Bitmap unscaledBitmap = ScalingUtilities.decodeResource(attachment.getUrl(),
                                mDstWidth, mDstHeight, ScalingUtilities.ScalingLogic.FIT);

                        if(unscaledBitmap!=null) {
                            Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, mDstWidth,
                                    mDstHeight, ScalingUtilities.ScalingLogic.FIT);
                            unscaledBitmap.recycle();

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            Bitmap.CompressFormat format;

                            switch (attachment.getMimeType()) {
                                case MIME_TYPE_IMAGE_JPEG:
                                    format = Bitmap.CompressFormat.JPEG;
                                    break;
                                case MIME_TYPE_IMAGE_PNG:
                                    format = Bitmap.CompressFormat.PNG;
                                    break;
                                case MIME_TYPE_IMAGE_WEBP:
                                    format = Bitmap.CompressFormat.WEBP;
                                    break;
                                default:
                                    format = Bitmap.CompressFormat.JPEG;
                            }

                            scaledBitmap.compress(format, 100, bos);
                            byte[] data = bos.toByteArray();
                            bos.close();
                            dataServices.addCachedItem(attachment.getUrl().toString(), data);
                        }
                    }
                    else {
                        byte[] data = Converters.urlToByteArray(attachment.getUrl());
                        dataServices.addCachedItem(attachment.getUrl().toString(), data);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            updateListView();
        }
    }

/*
    private void defineImgDimentions(){
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
    }
*/
}
