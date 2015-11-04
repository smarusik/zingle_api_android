package me.zingle.atlas_adoption.facade_models;


import android.net.Uri;

import java.net.URL;

import static me.zingle.atlas_adoption.facade_models.MimeTypes.MIME_TYPE_IMAGE_GIF;
import static me.zingle.atlas_adoption.facade_models.MimeTypes.MIME_TYPE_IMAGE_JPEG;
import static me.zingle.atlas_adoption.facade_models.MimeTypes.MIME_TYPE_IMAGE_PNG;
import static me.zingle.atlas_adoption.facade_models.MimeTypes.MIME_TYPE_TEXT;
import static me.zingle.atlas_adoption.facade_models.MimeTypes.MIME_TYPE_UNSUPPORTED;

/**
 * Created by SLAVA 09 2015.
 */
public class Attachment {
    private MimeTypes mimeType;
    private Uri uri;
    private URL url;
    private String cachePath;
    private String textContent;

    public Attachment() {
    }

    public MimeTypes getMimeType() {
        return mimeType;
    }

    public void setMimeType(MimeTypes mimeType) {
        this.mimeType = mimeType;
    }

    public void setMimeType(String mimeType)
    {
        if(mimeType!=null) {
            String unifiedType = "";
            unifiedType = mimeType.toLowerCase();

            if (unifiedType.equals(MIME_TYPE_TEXT.toString())) {
                this.mimeType = MIME_TYPE_TEXT;
            } else if (unifiedType.equals(MIME_TYPE_IMAGE_PNG.toString())) {
                this.mimeType = MIME_TYPE_IMAGE_PNG;
            } else if (unifiedType.equals(MIME_TYPE_IMAGE_JPEG.toString())) {
                this.mimeType = MIME_TYPE_IMAGE_JPEG;
            } else if (unifiedType.equals(MIME_TYPE_IMAGE_GIF.toString())) {
                this.mimeType = MIME_TYPE_IMAGE_GIF;
            } else {
                this.mimeType = MIME_TYPE_UNSUPPORTED;
            }
        }
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

}
