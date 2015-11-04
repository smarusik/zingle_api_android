package me.zingle.android_sdk.facade_models;

/**
 * Created by SLAVA 09 2015.
 */
public enum MimeTypes {
    MIME_TYPE_TEXT("text/plain"),
    MIME_TYPE_IMAGE_PNG("image/png"),
    MIME_TYPE_IMAGE_JPEG("image/jpeg"),
    MIME_TYPE_IMAGE_WEBP("image/webp"),
    MIME_TYPE_IMAGE_GIF("image/gif"),
    MIME_TYPE_UNSUPPORTED("unsupported"),
    ;

    private final String representation;

    MimeTypes(String representation) {
        this.representation = representation;
    }

    public boolean equals(String type){
        return this.representation.equals(type);
    }

    public String toString(){
        return representation;
    }
}
