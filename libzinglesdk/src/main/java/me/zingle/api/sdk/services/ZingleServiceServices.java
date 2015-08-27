package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.RequestDTO;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZingleAddress;
import me.zingle.api.sdk.model.ZinglePhoneNumber;
import me.zingle.api.sdk.model.ZinglePlan;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.model.ZingleTimeZone;

import static me.zingle.api.sdk.dao.RequestMethods.DELETE;
import static me.zingle.api.sdk.dao.RequestMethods.GET;
import static me.zingle.api.sdk.dao.RequestMethods.POST;
import static me.zingle.api.sdk.dao.RequestMethods.PUT;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleServiceServices {
    private static final String resoursePath="/services";
    //static String serviceID;

    private ServiceDelegate<List<ZingleService>> searchDelegate;
    private ServiceDelegate<ZingleService> getDelegate;
    private ServiceDelegate<Boolean> cancelDelegate;
    private ServiceDelegate<ZingleService> provisionDelegate;
    private ServiceDelegate<ZingleService> updateDelegate;

   /* public static String getServiceID() {
        return serviceID;
    }

    public static void setServiceID(String serviceID) {
        ZingleServiceServices.serviceID = serviceID;
    }*/

    public void setSearchDelegate(ServiceDelegate<List<ZingleService>> searchDelegate) {
        this.searchDelegate = searchDelegate;
    }

    public void setGetDelegate(ServiceDelegate<ZingleService> getDelegate) {
        this.getDelegate = getDelegate;
    }

    public void setCancelDelegate(ServiceDelegate<Boolean> cancelDelegate) {
        this.cancelDelegate = cancelDelegate;
    }

    public void setProvisionDelegate(ServiceDelegate<ZingleService> provisionDelegate) {
        this.provisionDelegate = provisionDelegate;
    }

    public void setUpdateDelegate(ServiceDelegate<ZingleService> updateDelegate) {
        this.updateDelegate = updateDelegate;
    }

    static ZingleService mapper(JSONObject source) throws JSONException {
        ZingleService mapResult=new ZingleService();

        mapResult.setId(source.getInt("id"));
        mapResult.setDisplayName(source.getString("display_name"));
        mapResult.setPhoneNumber(ZinglePhoneNumberServices.mapper(source.getJSONObject("phone_number")));
        mapResult.setTimeZone(new ZingleTimeZone(source.getString("time_zone")));

        mapResult.setPlan(new ZinglePlan());
        mapResult.getPlan().setId(source.getInt("plan_id"));

        mapResult.setAddress(ZingleAddressServices.mapper(source.getJSONObject("service_address")));

        return mapResult;
    }

    static List<ZingleService> arrayMapper(JSONArray source) throws JSONException {
        int i=0;
        JSONObject temp=source.optJSONObject(i++);

        List<ZingleService> retList=new ArrayList<>();

        while(temp!=null){
            retList.add(mapper(temp));
            temp=source.optJSONObject(i++);
        }

        return retList;
    }

    public static List<ZingleService> search(List<QueryPart> filters) throws UnsuccessfullRequestEx{

        ZingleQuery query = new ZingleQuery(GET, resoursePath);

        if(filters!=null) {
            for (QueryPart p : filters) {
                query.addParam(p);
            }
        }

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONArray result=response.getData().getJSONArray("result");
            return arrayMapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());

    }

    public boolean searchAsync(final List<QueryPart> filters){
        if(searchDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    searchDelegate.processResult(search(filters));
                }catch (UnsuccessfullRequestEx e){
                    searchDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public static ZingleService get(String id) throws UnsuccessfullRequestEx{
        ZingleQuery query = new ZingleQuery(GET, resoursePath+"/"+id);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());

    }
    
    public boolean getAsync(final String id){
        if(getDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    getDelegate.processResult(get(id));
                }catch (UnsuccessfullRequestEx e){
                    getDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public static boolean cancel(String id){
        ZingleQuery query = new ZingleQuery(DELETE, resoursePath+"/"+id);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            return true;
        }
        else
            throw new UnsuccessfullRequestEx("Error list()", response.getResponseCode(), response.getResponseStr());

    }

    public boolean cancelAsync(final String id){
        if(cancelDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    cancelDelegate.processResult(cancel(id));
                }catch (UnsuccessfullRequestEx e){
                    cancelDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public static ZingleService provisionNewService(ZinglePlan plan, ZingleTimeZone timeZone, ZinglePhoneNumber phoneNumber,
                                                    ZingleAddress address, String serviceDisplayName){

        ZingleService newService=new ZingleService();
        newService.setAddress(address);
        newService.setPlan(plan);
        newService.setPhoneNumber(phoneNumber);
        newService.setTimeZone(timeZone);
        newService.setDisplayName(serviceDisplayName);

        ZingleQuery query = new ZingleQuery(POST, resoursePath);

        RequestDTO service=new RequestDTO();
        service.fromService(newService);

        query.setPayload(service);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean provisionNewServiceAsync(final ZinglePlan plan, final ZingleTimeZone timeZone, final ZinglePhoneNumber phoneNumber,
                                       final ZingleAddress address, final String serviceDisplayName){
        if(provisionDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }


        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    provisionDelegate.processResult(provisionNewService(plan,timeZone,phoneNumber,address,serviceDisplayName));
                }catch (UnsuccessfullRequestEx e){
                    provisionDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public static ZingleService update(ZingleService serviceUpd){

        ZingleQuery query = new ZingleQuery(PUT, resoursePath+"/"+serviceUpd.getId());

        RequestDTO service=new RequestDTO();
        service.fromService(serviceUpd);

        query.setPayload(service);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean updateAsync(final ZingleService serviceUpd){
        if(updateDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    updateDelegate.processResult(update(serviceUpd));
                }catch (UnsuccessfullRequestEx e){
                    updateDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

}
