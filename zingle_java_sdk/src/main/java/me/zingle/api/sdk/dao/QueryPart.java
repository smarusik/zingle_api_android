package me.zingle.api.sdk.dao;

import me.zingle.api.sdk.model.ZingleDirerction;
import me.zingle.api.sdk.model.ZingleSortOrder;

/**
 * Convenience class for request modifiers. Represents key-value pair of one modifier, which is translated to request modifier.
 * For example <i>key=page, value=2</i> will add <i>page=2</i> modifier to HTTP request.
 */
public class QueryPart {
    public String key;
    public String value;

    public QueryPart(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public QueryPart(RequestModifiers key, String value) {
        this.key = key.representation;
        this.value = value;
    }

    public QueryPart(String key, int value) {
        this.key = key;
        this.value = String.valueOf(value);
    }

    public QueryPart(RequestModifiers key, int value) {
        this.key = key.representation;
        this.value = String.valueOf(value);
    }

    public QueryPart(String key, ZingleSortOrder value) {
        this.key = key;
        this.value = String.valueOf(value.toString());
    }

    public QueryPart(RequestModifiers key, ZingleSortOrder value) {
        this.key = key.representation;
        this.value = String.valueOf(value.toString());
    }

    public QueryPart(String key, ZingleDirerction value) {
        this.key = key;
        this.value = value.representation;
    }

    public QueryPart(RequestModifiers key, ZingleDirerction value) {
        this.key = key.representation;
        this.value = value.representation;
    }

}
