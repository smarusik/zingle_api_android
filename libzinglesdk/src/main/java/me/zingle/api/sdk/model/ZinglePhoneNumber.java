package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 08 2015.
 */
public class ZinglePhoneNumber {
    private String countryCode;
    private String areaCode;
    private String phoneNumber;

    public ZinglePhoneNumber() {
    }

    public ZinglePhoneNumber(String countryCode, String areaCode, String phoneNumber) {
        this.countryCode = countryCode;
        this.areaCode = areaCode;
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullNumber(){
        return "+"+countryCode+areaCode+phoneNumber;
    }
}
