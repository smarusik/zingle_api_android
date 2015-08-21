package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZinglePlan;

import static me.zingle.api.sdk.dao.RequestMethods.GET;

/**
 * Created by SLAVA 08 2015.
 */
public class ZinglePlanServices {
    static final String resoursePath="/plans";

    private ServiceDelegate<List<ZinglePlan>> delegate;

    public ZinglePlanServices(ServiceDelegate<List<ZinglePlan>> delegate) {
        this.delegate = delegate;
    }

    public void setDelegate(ServiceDelegate<List<ZinglePlan>> delegate) {
        this.delegate = delegate;
    }

    static ZinglePlan mapper(JSONObject source) throws JSONException {
        return new ZinglePlan(source.getInt("id"),source.getInt("term_months"), (float) source.getDouble("monthly_or_unit_price"),
                (float)source.getDouble("setup_price"),source.getString("display_name"),
                source.getInt("is_printer_plan")==0?false:true);
    }

    static List<ZinglePlan> arrayMapper(JSONArray source) throws JSONException {
        int i=0;
        JSONObject temp=source.optJSONObject(i++);

        List<ZinglePlan> retList=new ArrayList<>();

        while(temp!=null){
            retList.add(mapper(temp));
            temp=source.optJSONObject(i++);
        }

        return retList;
    }

    public static List<ZinglePlan> list() throws UnsuccessfullRequestEx {

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

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    delegate.processResult(list());
                }catch (UnsuccessfullRequestEx e){
                    delegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }


}
