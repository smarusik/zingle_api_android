package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfulRequestEx;
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
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/blob/master/messages/POST_create.md>ZingleMessage API</a> create function.
 * Supports none of basic functions, except creating (sending), which wrapped in separate function.

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

        if(idsJSON!=null) {
            for (int i = 0; i < idsJSON.length(); i++) {
                ids.add(idsJSON.optString(i));
            }
        }

        return ids;
    }


    /**
     * Sends request to write to database (send) message from sender to recipient(s).
     * @param msg new message
     * @return list of newly created messages' ids.
     */
    public List<String> sendMessage(ZingleNewMessage msg){
        ZingleQuery query = new ZingleQuery(POST, resourcePath(false));

        RequestDTO payload=new RequestDTO();

        payload.setData(msg.extractCreationData());
        query.setPayload(payload);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            try {
                JSONObject result = response.getData().optJSONObject("result");
                if(result!=null) {
                    JSONArray resultArr = result.getJSONArray("message_ids");
                    return mapper(resultArr);
                }
                else return new ArrayList<>();

            }catch (JSONException e) {
                e.printStackTrace();
                throw new MappingErrorEx(this.getClass().getName(),response.getData().toString(),e.getMessage());
            }
        }
        else
            throw new UnsuccessfulRequestEx(response.getData(),response.getResponseCode(),response.getResponseStr());
    }

    /**
     * Same as <b>List<String> sendMessage(ZingleNewMessage msg)</b>, but runs request in separate thread. Result is received by proper implementation
     * of <i>ServiceDelegate</i>, provided as function parameter.
     * @param msg new message
     * @param delegate - implementation of ServiceDelegate
     * @return true if request starts successfully
     */
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
                }catch (UnsuccessfulRequestEx e){
                    delegate.processError(e.getErrMessage(),e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;
    }

    /**
     * Same as <b>boolean sendMessageAsync(final ZingleNewMessage msg, final ServiceDelegate<List<String>> delegate)</b>, but implementation
     * of <i>ServiceDelegate</i> is taken from <b>sendDelegate</b> property.
     * @param msg new message
     * @return true if request starts successfully
     */
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
