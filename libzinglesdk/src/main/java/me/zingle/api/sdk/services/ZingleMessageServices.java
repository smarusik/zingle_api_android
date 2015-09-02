package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.RequestDTO;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleMessage;
import me.zingle.api.sdk.model.ZinglePhoneNumber;
import me.zingle.api.sdk.model.ZingleService;

import static me.zingle.api.sdk.dao.RequestMethods.GET;
import static me.zingle.api.sdk.dao.RequestMethods.POST;
import static me.zingle.api.sdk.model.ZingleDirerction.ZINGLE_DIRERCTION_INBOUND;
import static me.zingle.api.sdk.model.ZingleDirerction.ZINGLE_DIRERCTION_OUTBOUND;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleMessageServices {
    private static final String resoursePrefixPath="/services";
    private static final String resoursePath="messages";

    private ServiceDelegate<List<ZingleMessage>> listDelegate;
    private ServiceDelegate<ZingleMessage> getDelegate;
    private ServiceDelegate<ZingleMessage> sendDelegate;

    public void setListDelegate(ServiceDelegate<List<ZingleMessage>> listDelegate) {
        this.listDelegate = listDelegate;
    }

    public void setGetDelegate(ServiceDelegate<ZingleMessage> getDelegate) {
        this.getDelegate = getDelegate;
    }

    public void setSendDelegate(ServiceDelegate<ZingleMessage> sendDelegate) {
        this.sendDelegate = sendDelegate;
    }

    static ZingleMessage mapper(JSONObject source, ZingleService service) throws JSONException {
        ZingleMessage result=new ZingleMessage();

        result.setId(source.getInt("id"));
        result.setBody(source.getString("body"));
        result.setContactPhoneNumber(new ZinglePhoneNumber(source.getString("contact_phone_number")));
        result.setServicePhoneNumber(new ZinglePhoneNumber(source.getString("service_phone_number")));
        //result.setTemplateId(source.getInt("template_id"));
        result.setDirection(source.getString("communication_direction").equals("inbound") ? ZINGLE_DIRERCTION_INBOUND : ZINGLE_DIRERCTION_OUTBOUND);
        result.setContact(new ZingleContact(source.getInt("contact_id"),service));
        //communication_details
        //result.setBodyLanguageCode(source.getString("body_language_code"));
        result.setCreatedAt(new Date(source.getLong("created_at")));
        //result.setBodyLanguageCode(source.getString("translated_body_language_code"));
        //triggered_by_user_id
        //result.setTranslatedBody("translated_body");

        return result;
    }

    static List<ZingleMessage> arrayMapper(JSONArray source, ZingleService service) throws JSONException {
        int i=0;
        JSONObject temp=source.optJSONObject(i++);

        List<ZingleMessage> retList=new ArrayList<>();

        while(temp!=null){
            retList.add(mapper(temp,service));
            temp=source.optJSONObject(i++);
        }

        return retList;
    }

    public static List<ZingleMessage> getConversation(ZingleService service, List<QueryPart> filters) throws UnsuccessfullRequestEx{
        ZingleQuery query = new ZingleQuery(GET, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath);

        if(filters!=null){
            for (QueryPart p:filters) {
                query.addParam(p);
            }
        }

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONArray result=response.getData().getJSONArray("result");
            return arrayMapper(result,service);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean getConversationAsync(final ZingleService service, final List<QueryPart> filters){
        if(listDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    listDelegate.processResult(getConversation(service, filters));
                }catch (UnsuccessfullRequestEx e){
                    listDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

    public static ZingleMessage get(ZingleService service, int id){
        ZingleQuery query = new ZingleQuery(GET, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath+"/"+id);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result, service);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean getAsync(final ZingleService service, final int id){
        if(getDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    getDelegate.processResult(get(service, id));
                }catch (UnsuccessfullRequestEx e){
                    getDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public static ZingleMessage send(ZingleService service, ZingleMessage message){

        ZingleQuery query = new ZingleQuery(POST, resoursePrefixPath+"/"+service.getId()+"/"+resoursePath);

        RequestDTO contactDTO=new RequestDTO();
        contactDTO.fromMessage(message);

        query.setPayload(contactDTO);

        System.out.println(contactDTO.getData().toString());

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result,service);
        }
        else
            throw new UnsuccessfullRequestEx("Error list()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean sendAsync(final ZingleService service, final ZingleMessage message){
        if(sendDelegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    sendDelegate.processResult(send(service, message));
                }catch (UnsuccessfullRequestEx e){
                    sendDelegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

    /*
    * public static ZingleMessage sendSMS(ZingleService service, ZingleMessage message){
    *
    *
    * }
    * */
}
