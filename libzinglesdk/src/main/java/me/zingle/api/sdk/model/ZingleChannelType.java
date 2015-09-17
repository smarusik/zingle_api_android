package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 09 2015.
 */
public enum ZingleChannelType {
    CHANNEL_TYPE_PHONE_NUMBER("Phone Number"),
    CHANNEL_TYPE_E_MAIL("Email Address")
    ;

    private String typeName;

    ZingleChannelType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }


    @Override
    public String toString() {
        return typeName;
    }
}
