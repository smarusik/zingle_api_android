package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleCorrespondent extends ZingleBaseModel{
    private class Channel{
        String typeClass;
        String displayName;
        String value;
        String formattedValue;

        public String getTypeClass() {
            return typeClass;
        }

        public void setTypeClass(String typeClass) {
            this.typeClass = typeClass;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getFormattedValue() {
            return formattedValue;
        }

        public void setFormattedValue(String formattedValue) {
            this.formattedValue = formattedValue;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("\nChannel{");
            sb.append("\n    typeClass='").append(typeClass).append('\'');
            sb.append("\n    displayName='").append(displayName).append('\'');
            sb.append("\n    value='").append(value).append('\'');
            sb.append("\n    formattedValue='").append(formattedValue).append('\'');
            sb.append("}\n");
            return sb.toString();
        }
    }

    private String id;
    private Channel channel=new Channel();

    public ZingleCorrespondent() {
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

    public String getChannelDisplayName() {
        return channel.getDisplayName();
    }

    public void setChannelDisplayName(String displayName) {
        channel.setDisplayName(displayName);
    }

    public String getChannelValue() {
        return channel.getValue();
    }

    public void setChannelValue(String value) {
        channel.setValue(value);
    }

    public String getChannelFormattedValue() {
        return channel.getFormattedValue();
    }

    public void setChannelFormattedValue(String formattedValue) {
        channel.setFormattedValue(formattedValue);
    }

    @Override
    public JSONObject extractCreationData() {
        checkForCreate();

        JSONStringer res=new JSONStringer();

        res.object();

        if(!(id==null || !id.isEmpty()))
            res.key("id").value(id);

        if(!(channel==null || channel.getValue().isEmpty()))
            res.key("channel_value").value(channel.getValue());

        res.endObject();

        return new JSONObject(res.toString());

    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
    }

    @Override
    public void checkForCreate() {

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
