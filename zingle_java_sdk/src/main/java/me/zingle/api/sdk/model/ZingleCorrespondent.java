package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleCorrespondent extends ZingleBaseModel{

    //Correspondent's channel
    public static class Channel{
        String typeClass;
        String value;

        public String getTypeClass() {
            return typeClass;
        }

        public void setTypeClass(String typeClass) {
            this.typeClass = typeClass;
        }


        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("\nChannel{");
            sb.append("\n    typeClass='").append(typeClass).append('\'');
            sb.append("\n    value='").append(value).append('\'');
            sb.append("}\n");
            return sb.toString();
        }
    }

    //Correspondent itself
    private String id;
    private Channel channel=new Channel();

    public ZingleCorrespondent() {
    }

    public ZingleCorrespondent(String id, String chTypeClass, String chValue) {
        this.id = id;
        this.setChannelTypeClass(chTypeClass);
        this.setChannelValue(chValue);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelTypeClass() {
        return channel.getTypeClass();
    }

    public void setChannelTypeClass(String channelTypeClass) {
        this.channel.setTypeClass(channelTypeClass);
    }


    public String getChannelValue() {
        return channel.getValue();
    }

    public void setChannelValue(String value) {
        channel.setValue(value);
    }


    @Override
    public JSONObject extractCreationData() {
        checkForCreate();

        JSONObject resJS=new JSONObject();

        try {
            if(id!=null)
                resJS.put("id",id);

            if(!(channel==null || channel.getValue()==null || channel.getValue().isEmpty())) {
                resJS.put("channel_value", channel.getValue());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
    }

    @Override
    public void checkForCreate() {

        if(id==null && channel.getValue()==null)
            throw new RequestBodyCreationEx(RequestMethods.POST,"id,channel_value",getClass().getName()+".id,channel.value");
    }

    @Override
    public void checkForUpdate() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleCorrespondent{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    channel=").append(channel);
        sb.append("}\n");
        return sb.toString();
    }
}
