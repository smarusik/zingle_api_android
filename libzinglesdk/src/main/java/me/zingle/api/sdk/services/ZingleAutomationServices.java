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
import me.zingle.api.sdk.model.ZingleAutomation;
import me.zingle.api.sdk.model.ZingleService;

import static me.zingle.api.sdk.dao.RequestMethods.GET;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleAutomationServices {
    private static final String resoursePrefixPath="/services";
    private static final String resoursePath="automations";

    private ServiceDelegate<List<ZingleAutomation>> listDelegate;

    public ZingleAutomationServices(ServiceDelegate<List<ZingleAutomation>> listDelegate) {
        this.listDelegate = listDelegate;
    }

    public void setListDelegate(ServiceDelegate<List<ZingleAutomation>> listDelegate) {
        this.listDelegate = listDelegate;
    }

    static ZingleAutomation mapper(JSONObject source, ZingleService service) throws JSONException {
        ZingleAutomation result=new ZingleAutomation();

        result.setId(source.getInt("id"));
        result.setDisplayName(source.getString("display_name"));
        result.setIsGlobal(source.getInt("is_global") == 0 ? false : true);

        result.setService(service);

        return result;
    }

    static List<ZingleAutomation> arrayMapper(JSONArray source, ZingleService service) throws JSONException {
        int i=0;
        JSONObject temp=source.optJSONObject(i++);

        List<ZingleAutomation> retList=new ArrayList<>();

        while(temp!=null){
            retList.add(mapper(temp,service));
            temp=source.optJSONObject(i++);
        }

        return retList;
    }

    public static List<ZingleAutomation> listForService(ZingleService service){
        ZingleQuery query = new ZingleQuery(GET, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONArray result=response.getData().getJSONArray("result");
            return arrayMapper(result,service);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());

    }

    public boolean listForServiceAsync(final ZingleService service){
        if(listDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listDelegate.processResult(listForService(service));
                }catch (UnsuccessfullRequestEx e){
                    listDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

}
