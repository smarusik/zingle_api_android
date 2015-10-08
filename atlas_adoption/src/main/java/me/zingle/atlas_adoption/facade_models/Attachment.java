package me.zingle.atlas_adoption.facade_models;


import android.net.Uri;

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
    private String localPath;
    private String textContent;

    private Long bytesDownloaded;
    private boolean downloadCompleted;

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
        String unifiedType=mimeType.toLowerCase();

        if (unifiedType.equals(MIME_TYPE_TEXT.toString())) {
            this.mimeType = MIME_TYPE_TEXT;
        }
        else if (unifiedType.equals(MIME_TYPE_IMAGE_PNG.toString())) {
            this.mimeType = MIME_TYPE_IMAGE_PNG;
        }
        else if (unifiedType.equals(MIME_TYPE_IMAGE_JPEG.toString())) {
            this.mimeType = MIME_TYPE_IMAGE_JPEG;
        }
        else if (unifiedType.equals(MIME_TYPE_IMAGE_GIF.toString())) {
            this.mimeType = MIME_TYPE_IMAGE_GIF;
        }
        else {
            this.mimeType = MIME_TYPE_UNSUPPORTED;
        }
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public Long getBytesDownloaded() {
        return bytesDownloaded;
    }

    public void setBytesDownloaded(Long bytesDownloaded) {
        this.bytesDownloaded = bytesDownloaded;
    }

    public boolean isDownloadCompleted() {
        return downloadCompleted;
    }

    public void setDownloadCompleted(boolean downloadCompleted) {
        this.downloadCompleted = downloadCompleted;
    }
}
