package me.zingle.api.sdk.model;

/**
 * Represents communication direction in ZingleMessage. Partially lost it's meaning when correspondents were divided on sender and receiver.
 */
public enum ZingleDirerction {
    ZINGLE_DIRERCTION_INBOUND("inbound"),
    ZINGLE_DIRERCTION_OUTBOUND("outbound");

    public String representation;

    ZingleDirerction(String representation) {
        this.representation = representation;
    }
}
