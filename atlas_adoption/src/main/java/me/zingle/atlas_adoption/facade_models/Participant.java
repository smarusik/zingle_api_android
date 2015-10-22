package me.zingle.atlas_adoption.facade_models;

/**
 * Created by SLAVA 09 2015.
 */

public class Participant {
    public enum ParticipantType{
        CONTACT,
        SERVICE
    }

    private ParticipantType type;
    private String id;
    private String channelType;
    private String channelValue;
    private String name;


    public Participant() {

    }

    public Participant(String name, ParticipantType type) {
        this.name = name;
        this.type = type;
    }

    public Participant(String name,ParticipantType type, String id, String channelType, String channelValue) {
        this.name=name;
        this.type = type;
        this.id = id;
        this.channelType = channelType;
        this.channelValue = channelValue;
    }

    public ParticipantType getType() {
        return type;
    }

    public void setType(ParticipantType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelValue() {
        return channelValue;
    }

    public void setChannelValue(String channelValue) {
        this.channelValue = channelValue;
    }

    public boolean equals(Participant o) {
        return this.id.equals(o.getId()) && this.type.equals(o.getType());
    }
}
