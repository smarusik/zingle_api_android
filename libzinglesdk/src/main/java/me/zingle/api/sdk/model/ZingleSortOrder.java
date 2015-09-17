package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 08 2015.
 */
public enum ZingleSortOrder {
    ZINGLE_ASC("asc"),
    ZINGLE_DESC("desc");

    private final String sortOrder;

    ZingleSortOrder(String i) {
        sortOrder = i;
    }

    public String toString(){
        return sortOrder;
    }

}
