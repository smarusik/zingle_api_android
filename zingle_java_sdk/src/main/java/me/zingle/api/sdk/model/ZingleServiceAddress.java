package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/service_addresses#service-address-object>Service Address Object</a>
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
        JSONObject resJS=new JSONObject();

        try {
            resJS.put("address",address);
            resJS.put("city",city);
            resJS.put("state",state);
            resJS.put("country",country);
            resJS.put("postalCode",postalCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resJS;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ZingleServiceAddress that = (ZingleServiceAddress) o;

        if (!address.equals(that.address)) return false;
        if (!city.equals(that.city)) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (!country.equals(that.country)) return false;
        return !(postalCode != null ? !postalCode.equals(that.postalCode) : that.postalCode != null);

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleServiceAddress{");
        sb.append("\n    address='").append(address).append('\'');
        sb.append("\n    city='").append(city).append('\'');
        sb.append("\n    state='").append(state).append('\'');
        sb.append("\n    country='").append(country).append('\'');
        sb.append("\n    postalCode='").append(postalCode).append('\'');
        sb.append("}\n");
        return sb.toString();
    }
}
