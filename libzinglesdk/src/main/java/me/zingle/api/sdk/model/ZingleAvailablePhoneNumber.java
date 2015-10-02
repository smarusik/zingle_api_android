package me.zingle.api.sdk.model;

import org.json.JSONObject;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleAvailablePhoneNumber extends ZingleBaseModel {

    private String phoneNumber;
    private String formattedPhoneNumber;
    private String country;

    public ZingleAvailablePhoneNumber() {
    }

    public ZingleAvailablePhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean equals(ZingleAvailablePhoneNumber obj) {
        return obj.phoneNumber.equals(phoneNumber);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleAvailablePhoneNumber{");
        sb.append("\n    phoneNumber='").append(phoneNumber).append('\'');
        sb.append("\n    formattedPhoneNumber='").append(formattedPhoneNumber).append('\'');
        sb.append("\n    country='").append(country).append('\'');
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public JSONObject extractCreationData() {
        return null;
    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
    }

    @Override
    public void checkForCreate() {

    }

    @Override
    public void checkForUpdate() {

    }
}
