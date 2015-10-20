package me.zingle.atlas_adoption.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import me.zingle.api.sdk.model.ZingleAttachment;
import me.zingle.api.sdk.model.ZingleChannelType;
import me.zingle.api.sdk.model.ZingleCorrespondent;
import me.zingle.api.sdk.model.ZingleMessage;
import me.zingle.api.sdk.model.ZingleNewMessage;
import me.zingle.atlas_adoption.facade_models.Attachment;
import me.zingle.atlas_adoption.facade_models.Message;
import me.zingle.atlas_adoption.facade_models.MimeTypes;
import me.zingle.atlas_adoption.facade_models.Participant;
import me.zingle.atlas_adoption.model_view.DataServices;

/**
 * Created by SLAVA 10 2015.
 */
public class Converters {

    public static ZingleCorrespondent fromParticipant(Participant p){
        ZingleCorrespondent result=null;

        if(p!=null) {
            result = new ZingleCorrespondent();
            result.setId(p.getId());
            result.setChannelTypeClass(p.getChannelType());
            result.setChannelValue(p.getChannelValue());
        }

        return result;
    }

    public static ZingleChannelType fromTypeId(String tid){
        return new ZingleChannelType(tid);
    }

    public static ZingleNewMessage fromMessage(Message msg){
        ZingleNewMessage result=null;

        if(msg!=null){
            result=new ZingleNewMessage();

            result.setSenderType(convertType(msg.getSender().getType()));
            result.setSender(Converters.fromParticipant(msg.getSender()));

            result.setRecipientType(convertType(msg.getRecipient().getType()));
            result.addRecipient(Converters.fromParticipant(msg.getRecipient()));

            result.addChannelType(fromTypeId(msg.getChannelTypeId()));

            result.setBody(msg.getBody());

        }
        return result;
    }

    public static String convertType(Participant.ParticipantType type){
        switch (type){
            case CONTACT: return "contact";
            case SERVICE: return "service";
            default: return "";
        }
    }

    public static ZingleAttachment fromAttachment(Attachment attachment, Context context) {
        ZingleAttachment result = null;

        if (attachment != null) {
            result = new ZingleAttachment();
            result.setMimeType(attachment.getMimeType().toString());

            if (attachment.getMimeType() == MimeTypes.MIME_TYPE_IMAGE_WEBP ||
                    attachment.getMimeType() == MimeTypes.MIME_TYPE_IMAGE_JPEG ||
                    attachment.getMimeType() == MimeTypes.MIME_TYPE_IMAGE_PNG) {


                byte[] data=uriToByteArray(attachment.getMimeType(),attachment.getUri(),context);


                        DataServices dataServices=DataServices.getItem();
                        dataServices.addCachedItem(attachment.getUri().toString(),data);
                        attachment.setCachePath(attachment.getUri().toString());

                        result.setData(Base64.encode(data, Base64.DEFAULT));

            }
        }

        return result;
    }

    public static byte[] uriToByteArray(MimeTypes type,Uri uri,Context context){
        ParcelFileDescriptor is = null;
        try {
            is = context.getContentResolver().openFileDescriptor(uri, "r");
            if (is != null) {
                FileDescriptor fd = is.getFileDescriptor();

                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                Bitmap.CompressFormat format;

                switch (type){
                    case MIME_TYPE_IMAGE_JPEG: format= Bitmap.CompressFormat.JPEG; break;
                    case MIME_TYPE_IMAGE_PNG: format= Bitmap.CompressFormat.PNG; break;
                    case MIME_TYPE_IMAGE_WEBP: format= Bitmap.CompressFormat.WEBP; break;
                    default: format= Bitmap.CompressFormat.JPEG; type=MimeTypes.MIME_TYPE_IMAGE_JPEG; break;
                }

                bitmap.compress(format, 100, bos);
                byte[] result=bos.toByteArray();
                is.close();
                bos.close();

                return result;
            }
            else
                return null;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static byte[] urlToByteArray(URL url){
        InputStream in = null;
        try {
            in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();

            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message fromZingleMessage(ZingleMessage msg){
        Message facadeMessage=new Message();

        facadeMessage.setCreatedAt(new Date(msg.getCreatedAt()));
        facadeMessage.setReadAt(new Date(msg.getReadAt()));
        facadeMessage.setId(msg.getId());
        facadeMessage.setRead(false);
        facadeMessage.setSent(true);
        //facadeMessage.setChannelTypeId();
        facadeMessage.setBody(msg.getBody());

        facadeMessage.setRecipient(fromCorrespondent(msg.getRecipient(), msg.getRecipientType()));
        facadeMessage.setSender(fromCorrespondent(msg.getSender(), msg.getSenderType()));

        for(ZingleAttachment a:msg.getAttachments()) {
            facadeMessage.addAttachment(fromAttachment(a));
        }

        return facadeMessage;
    }

    public static Participant fromCorrespondent(ZingleCorrespondent correspondent,String type){
        Participant participant=new Participant();

        if(type.equals("contact"))
            participant.setType(Participant.ParticipantType.CONTACT);
        else
            participant.setType(Participant.ParticipantType.SERVICE);

        participant.setId(correspondent.getId());
        participant.setChannelValue(correspondent.getChannelValue());
        participant.setChannelType(correspondent.getChannelTypeClass());

        return participant;
    }

    public static Attachment fromAttachment(ZingleAttachment attachment){
        Attachment result=new Attachment();
        result.setMimeType(attachment.getMimeType());
        result.setUrl(attachment.getUrl());

        return result;
    }
}
