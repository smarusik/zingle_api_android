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
import me.zingle.api.sdk.model.ZingleTimeZone;

import static me.zingle.api.sdk.dao.RequestMethods.GET;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleTimeZoneServices {
    static final String resoursePath="/time-zones";

    private ServiceDelegate<List<ZingleTimeZone>> listDelegate;

    public ZingleTimeZoneServices(ServiceDelegate<List<ZingleTimeZone>> listDelegate) {
        this.listDelegate = listDelegate;
    }

    public void setDelegate(ServiceDelegate<List<ZingleTimeZone>> listDelegate) {
        this.listDelegate = listDelegate;
    }

    static ZingleTimeZone mapper(JSONObject source) throws JSONException {
        return new ZingleTimeZone(source.getString("display_name"));
    }

    static List<ZingleTimeZone> arrayMapper(JSONArray source) throws JSONException {
        int i=0;
        JSONObject temp=source.optJSONObject(i++);

        List<ZingleTimeZone> retList=new ArrayList<>();

        while(temp!=null){
            retList.add(mapper(temp));
            temp=source.optJSONObject(i++);
        }

        return retList;
    }

    public static List<ZingleTimeZone> list() throws UnsuccessfullRequestEx {

        ZingleQuery query = new ZingleQuery(GET, resoursePath);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONArray result=response.getData().getJSONArray("result");
            return arrayMapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean listAsync(){
        if(listDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    listDelegate.processResult(list());
                }catch (UnsuccessfullRequestEx e){
                    listDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }
}
