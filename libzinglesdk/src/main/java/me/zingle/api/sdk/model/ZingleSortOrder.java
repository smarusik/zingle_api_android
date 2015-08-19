package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 08 2015.
 */
public enum ZingleSortOrder {
    ZINGLE_ASC(0),
    ZINGLE_DESC(1);

    private final int sortOrder;

    ZingleSortOrder(int i) {
        sortOrder = i;
    }

    public int toInt(){
        return sortOrder;
    }

}
