package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 09 2015.
 */
public enum ZingleParticipantType {
    ZINGLE_SERVICE("service"),
    ZINGLE_CONTACT("contact")
    ;

    private final String displayName;

    ZingleParticipantType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }


    @Override
    public String toString() {
        return displayName;
    }
}
