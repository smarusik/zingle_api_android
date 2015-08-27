package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZinglePhoneNumber;

import static me.zingle.api.sdk.dao.RequestMethods.GET;

/**
 * Created by SLAVA 08 2015.
 */
public class ZinglePhoneNumberServices {

    static final String resoursePath="/available-phone-numbers";

    private ServiceDelegate<List<ZinglePhoneNumber>> searchDelegate;

    public ZinglePhoneNumberServices(ServiceDelegate<List<ZinglePhoneNumber>> delegate) {
        this.searchDelegate = delegate;
    }

    public void setDelegate(ServiceDelegate<List<ZinglePhoneNumber>> delegate) {
        this.searchDelegate = delegate;
    }

    static ZinglePhoneNumber mapper(JSONObject source) throws JSONException {
        String numberStr = source.getString("phone_number");
        if(!numberStr.isEmpty()) {
            return new ZinglePhoneNumber(numberStr);
        }
        else
            return new ZinglePhoneNumber("");
    }

    static List<ZinglePhoneNumber> arrayMapper(JSONArray source) throws JSONException {
        int i=0;
        JSONObject temp=source.optJSONObject(i++);

        List<ZinglePhoneNumber> retList=new ArrayList<>();

        while(temp!=null){
            retList.add(mapper(temp));
            temp=source.optJSONObject(i++);
        }

        return retList;
    }

    public static List<ZinglePhoneNumber> searchAvailableForCountryCode(String countryCode) throws UnsuccessfullRequestEx{

        ZingleQuery query = new ZingleQuery(GET, resoursePath);
        query.addParam("country",countryCode);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONArray result=response.getData().getJSONArray("result");
            return arrayMapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error searchAvailableForCountryCode()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean searchAvailableForCountryCodeAsync(final String countryCode){
        if(searchDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    searchDelegate.processResult(searchAvailableForCountryCode(countryCode));
                }catch (UnsuccessfullRequestEx e){
                    searchDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public static List<ZinglePhoneNumber> searchAvailableForCountryCodeAreaCode(String countryCode, String areaCode) throws UnsuccessfullRequestEx{
        ZingleQuery query = new ZingleQuery(GET, resoursePath);
        query.addParam("country", countryCode);
        query.addParam("search",areaCode);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONArray result=response.getData().getJSONArray("result");
            return arrayMapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error searchAvailableForCountryCodeAreaCode()",response.getResponseCode(),response.getResponseStr());

    }

    public boolean searchAvailableForCountryCodeAreaCodeAsync(final String countryCode,final String areaCode){
        if(searchDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    searchDelegate.processResult(searchAvailableForCountryCodeAreaCode(countryCode, areaCode));
                }catch (UnsuccessfullRequestEx e){
                    searchDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

}
