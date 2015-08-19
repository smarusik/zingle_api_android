package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleAddress {
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    public ZingleAddress() {
    }

    public ZingleAddress(String address, String city, String state, String country, String postalCode) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getFullAddress(){
        return address+" \n"+
                city+" \n"+
                (state.isEmpty()?"":state+" \n")+
                postalCode+" "+country;
    }
}
