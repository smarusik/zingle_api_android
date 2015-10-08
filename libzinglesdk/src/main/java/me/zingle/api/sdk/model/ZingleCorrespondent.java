package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Arrays;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleCorrespondent extends ZingleBaseModel{
    //Correspondent types
    private class CorrespondentTypes{
        final String[] correspondentTypes ={"contact", "service","label"};
        public CorrespondentTypes(String name) {
            if(Arrays.asList(correspondentTypes).contains(name)) {
                this.name = name;
            }
            else
                throw new RuntimeException("Unsupported Correspondent type.");
        }

        private String name;

        @Override
        public String toString() {
            return name;
        }
    }

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
    private CorrespondentTypes type;
    private String id;
    private Channel channel=new Channel();

    public ZingleCorrespondent() {
    }

    public String getType() {
        return type.name;
    }

    public void setType(String senderType) {
        this.type = new CorrespondentTypes(senderType);
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

        JSONStringer res=new JSONStringer();

        res.object();

        res.key("id").value(id);
        res.key("type").value(type.name);

        if(!(channel==null || channel.getValue().isEmpty())) {
            res.key("channel");
            res.object();
            res.key("type").value(channel.getTypeClass());
            res.key("value").value(channel.getValue());
            res.endObject();
        }

        res.endObject();

        return new JSONObject(res.toString());

    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
    }

    @Override
    public void checkForCreate() {
        if(type==null)
            throw new RequestBodyCreationEx(RequestMethods.POST,"type",getClass().getName()+".type");

        if(id==null)
            throw new RequestBodyCreationEx(RequestMethods.POST,"id",getClass().getName()+".id");

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
