package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleServiceAddress extends ZingleBaseModel{
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    public ZingleServiceAddress() {
    }

    public ZingleServiceAddress(String address, String city, String state, String country, String postalCode) {
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

    @Override
    public JSONObject extractCreationData() {
        checkForCreate();
        return extractData();
    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();
        return extractData();
    }

    private JSONObject extractData(){
        JSONStringer res = new JSONStringer();

        res.object();

        res.key("address").value(address);
        res.key("city").value(city);
        res.key("state").value(state);
        res.key("country").value(country);
        res.key("postalCode").value(postalCode);

        res.endObject();

        return new JSONObject(res.toString());
    }

    @Override
    public void checkForCreate() {
        if(address==null) throw new RequestBodyCreationEx(RequestMethods.POST,"address",this.getClass().getName()+"address");
        if(city==null) throw new RequestBodyCreationEx(RequestMethods.POST,"city",this.getClass().getName()+"city");
        if(state==null) throw new RequestBodyCreationEx(RequestMethods.POST,"state",this.getClass().getName()+"state");
        if(country==null) throw new RequestBodyCreationEx(RequestMethods.POST,"country",this.getClass().getName()+"country");
        if(postalCode==null) throw new RequestBodyCreationEx(RequestMethods.POST,"postal_code",this.getClass().getName()+"postalCode");
    }

    @Override
    public void checkForUpdate() {
        if(address==null) throw new RequestBodyCreationEx(RequestMethods.PUT,"address",this.getClass().getName()+"address");
        if(city==null) throw new RequestBodyCreationEx(RequestMethods.PUT,"city",this.getClass().getName()+"city");
        if(state==null) throw new RequestBodyCreationEx(RequestMethods.PUT,"state",this.getClass().getName()+"state");
        if(country==null) throw new RequestBodyCreationEx(RequestMethods.PUT,"country",this.getClass().getName()+"country");
        if(postalCode==null) throw new RequestBodyCreationEx(RequestMethods.PUT,"postal_code",this.getClass().getName()+"postalCode");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ZingleServiceAddress{");
        sb.append("address='").append(address).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", postalCode='").append(postalCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
