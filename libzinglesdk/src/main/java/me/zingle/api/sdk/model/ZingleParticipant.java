package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleParticipant {
    private ZingleParticipantType type;
    private Integer participantId;
    private ZingleChannelType channelType;
    private String channelValue;

    public ZingleParticipant() {
    }

    public ZingleParticipant(ZingleParticipantType type, Integer participantId, ZingleChannelType channelType, String channelValue) {
        this.type = type;
        this.participantId = participantId;
        this.channelType = channelType;
        this.channelValue = channelValue;
    }

    public ZingleParticipantType getType() {
        return type;
    }

    public void setType(ZingleParticipantType type) {
        this.type = type;
    }

    public Integer getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Integer participantId) {
        this.participantId = participantId;
    }

    public ZingleChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ZingleChannelType channelType) {
        this.channelType = channelType;
    }

    public String getChannelValue() {
        return channelValue;
    }

    public void setChannelValue(String channelValue) {
        this.channelValue = channelValue;
    }
}
