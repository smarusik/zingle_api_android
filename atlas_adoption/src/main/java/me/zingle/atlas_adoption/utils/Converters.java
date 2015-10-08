package me.zingle.atlas_adoption.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import me.zingle.api.sdk.model.ZingleAttachment;
import me.zingle.api.sdk.model.ZingleCorrespondent;
import me.zingle.api.sdk.model.ZingleNewMessage;
import me.zingle.atlas_adoption.facade_models.Attachment;
import me.zingle.atlas_adoption.facade_models.Message;
import me.zingle.atlas_adoption.facade_models.Participant;

/**
 * Created by SLAVA 10 2015.
 */
public class Converters {

    public static ZingleCorrespondent fromParticipant(Participant p){
        ZingleCorrespondent result=null;

        if(p!=null) {
            result = new ZingleCorrespondent();
            result.setType(p.getType().name().toLowerCase());
            result.setId(p.getId());
            result.setChannelTypeClass(p.getChannelType());
            result.setChannelValue(p.getChannelValue());
        }

        return result;
    }

    public static ZingleNewMessage fromMessage(Message msg){
        ZingleNewMessage result=null;

        if(msg!=null){
            result=new ZingleNewMessage();
            result.setSender(Converters.fromParticipant(msg.getSender()));
            result.setRecipient(Converters.fromParticipant(msg.getRecipient()));
            result.setBody(msg.getBody());

        }
        return result;
    }

    public static ZingleAttachment fromAttachment(Attachment attachment, Context context){
        ZingleAttachment result=null;

        if(attachment!=null) {
            result = new ZingleAttachment();
            result.setMimeType(attachment.getMimeType().toString());

            InputStream is = null;
            try {
                is = context.getContentResolver().openInputStream(attachment.getUri());
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.reset();
                ByteArrayOutputStream bos=new ByteArrayOutputStream();
                byte[] buff=new byte[1024];

                while(is.read(buff)>=0) bos.write(buff);

                result.setData(Base64.encode(bos.toByteArray(),Base64.DEFAULT));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
}
