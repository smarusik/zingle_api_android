package me.zingle.android_sdk.model_view;

import android.graphics.Typeface;

import me.zingle.android_sdk.R;

/**
 * Created by SLAVA 09 2015.
 */
public class MessageListStyleSettings {

    public final float CELL_CONTAINER_ALPHA_UNSENT  = 0.5f;
    public final float CELL_CONTAINER_ALPHA_SENT    = 1.0f;

    /*Settings from atlas_message_list styles*/
    private int myBubbleColor=R.color.atlas_bubble_blue;
    private int contBubbleColor=R.color.atlas_background_gray;
    private int myTextColor=R.color.atlas_text_black;
    private Typeface myTextTypeface=Typeface.create("", Typeface.NORMAL);
    private int myTextStyle=Typeface.NORMAL;

    private int otherBubbleColor=R.color.atlas_background_gray;
    private int otherTextColor=R.color.atlas_text_gray;
    private Typeface otherTextTypeface=Typeface.create("",Typeface.NORMAL);
    private int otherTextStyle=Typeface.NORMAL;

    private int dateTextColor=R.color.atlas_text_black;
    private int avatarTextColor=R.color.atlas_text_black;
    private int avatarBackgroundColor=R.color.atlas_text_black;

    public int getMyBubbleColor() {
        return myBubbleColor;
    }

    public void setMyBubbleColor(int myBubbleColor) {
        this.myBubbleColor = myBubbleColor;
    }

    public int getContBubbleColor() {
        return contBubbleColor;
    }

    public void setContBubbleColor(int contBubbleColor) {
        this.contBubbleColor = contBubbleColor;
    }

    public int getMyTextColor() {
        return myTextColor;
    }

    public void setMyTextColor(int myTextColor) {
        this.myTextColor = myTextColor;
    }

    public Typeface getMyTextTypeface() {
        return myTextTypeface;
    }

    public void setMyTextTypeface(Typeface myTextTypeface) {
        this.myTextTypeface = myTextTypeface;
    }

    public int getMyTextStyle() {
        return myTextStyle;
    }

    public void setMyTextStyle(int myTextStyle) {
        this.myTextStyle = myTextStyle;
    }

    public int getOtherBubbleColor() {
        return otherBubbleColor;
    }

    public void setOtherBubbleColor(int otherBubbleColor) {
        this.otherBubbleColor = otherBubbleColor;
    }

    public int getOtherTextColor() {
        return otherTextColor;
    }

    public void setOtherTextColor(int otherTextColor) {
        this.otherTextColor = otherTextColor;
    }

    public Typeface getOtherTextTypeface() {
        return otherTextTypeface;
    }

    public void setOtherTextTypeface(Typeface otherTextTypeface) {
        this.otherTextTypeface = otherTextTypeface;
    }

    public int getOtherTextStyle() {
        return otherTextStyle;
    }

    public void setOtherTextStyle(int otherTextStyle) {
        this.otherTextStyle = otherTextStyle;
    }

    public int getDateTextColor() {
        return dateTextColor;
    }

    public void setDateTextColor(int dateTextColor) {
        this.dateTextColor = dateTextColor;
    }

    public int getAvatarTextColor() {
        return avatarTextColor;
    }

    public void setAvatarTextColor(int avatarTextColor) {
        this.avatarTextColor = avatarTextColor;
    }

    public int getAvatarBackgroundColor() {
        return avatarBackgroundColor;
    }

    public void setAvatarBackgroundColor(int avatarBackgroundColor) {
        this.avatarBackgroundColor = avatarBackgroundColor;
    }
}
