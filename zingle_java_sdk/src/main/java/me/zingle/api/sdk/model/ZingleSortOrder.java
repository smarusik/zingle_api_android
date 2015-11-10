package me.zingle.api.sdk.model;

/**
 * Represents sorting order. Used in list-like requests and ZingleList responses to describe sorting order of required or received lists of objects.
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
