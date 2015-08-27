package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 08 2015.
 */
public class ZinglePhoneNumber {

    private String phoneNumber;

    public ZinglePhoneNumber() {
    }

    public ZinglePhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
