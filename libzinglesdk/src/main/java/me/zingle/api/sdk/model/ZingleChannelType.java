package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleChannelType {
    public static final ZingleChannelType CHANNEL_TYPE_PHONE_NUMBER = new ZingleChannelType("Phone Number");
    public static final ZingleChannelType CHANNEL_TYPE_E_MAIL = new ZingleChannelType("Email Address");

    private String typeName;

    public ZingleChannelType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }

    public boolean equals(String other){
        return typeName.equals(other);
    }
}
