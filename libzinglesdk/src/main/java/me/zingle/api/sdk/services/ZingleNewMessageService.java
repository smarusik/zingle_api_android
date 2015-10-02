package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.RequestDTO;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleNewMessage;
import me.zingle.api.sdk.model.ZingleService;

import static me.zingle.api.sdk.dao.RequestMethods.POST;

/**
 * Created by SLAVA 10 2015.
 */
public class ZingleNewMessageService extends ZingleBaseService<ZingleNewMessage> {

    private final ZingleService parent;

    public ZingleNewMessageService(ZingleService parent) {
        this.parent = parent;
    }

    private ServiceDelegate<List<String>> sendDelegate;

    public void setSendDelegate(ServiceDelegate<List<String>> sendDelegate) {
        this.sendDelegate = sendDelegate;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base=String.format("/services/%s/messages",parent.getId());

        if(specific)
            return null;
        else
            return base;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return false;
    }

    public List<String> mapper(JSONArray idsJSON){
        List<String> ids=new ArrayList<>();

        for(int i=0; i<idsJSON.length(); i++){
            ids.add(idsJSON.getString(i));
        }

        return ids;
    }

    public List<String> sendMessage(ZingleNewMessage msg){
        ZingleQuery query = new ZingleQuery(POST, resourcePath(false));

        RequestDTO payload=new RequestDTO();

        payload.setData(msg.extractCreationData());
        query.setPayload(payload);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result=response.getData().getJSONObject("result");
            return mapper(result.getJSONArray("message_ids"));
        }
        else
            throw new UnsuccessfullRequestEx("Error sendMessage()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean sendMessageAsync(final ZingleNewMessage msg, final ServiceDelegate<List<String>> delegate){
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    List<String> result=sendMessage(msg);
                    delegate.processResult(result);
                }catch (UnsuccessfullRequestEx e){
                    delegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    public boolean sendMessageAsync(final ZingleNewMessage msg){
        synchronized (sendDelegate) {
            if (sendDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return sendMessageAsync(msg, sendDelegate);
        }
    }

    public ZingleNewMessage mapper(JSONObject source) throws MappingErrorEx {
        return null;
    }

    @Override
    public ZingleNewMessage get(String id) {
        return null;
    }

    @Override
    public ZingleList<ZingleNewMessage> list() {
        return null;
    }

    @Override
    public ZingleList<ZingleNewMessage> list(List<QueryPart> conditions) {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return false;
    }

    @Override
    public ZingleNewMessage create(ZingleNewMessage object) {
        return null;
    }

    @Override
    public ZingleNewMessage update(ZingleNewMessage object) {
        return null;
    }
}
