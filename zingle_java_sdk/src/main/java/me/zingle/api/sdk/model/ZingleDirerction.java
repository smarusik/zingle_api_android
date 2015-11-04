package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 08 2015.
 */
public enum ZingleDirerction {
    ZINGLE_DIRERCTION_INBOUND("inbound"),
    ZINGLE_DIRERCTION_OUTBOUND("outbound");

    public String representation;

    ZingleDirerction(String representation) {
        this.representation = representation;
    }
}
