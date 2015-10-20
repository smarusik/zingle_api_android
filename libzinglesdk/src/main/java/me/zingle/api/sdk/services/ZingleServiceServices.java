package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.Exceptions.UndefinedServiceDelegateEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.dao.ZingleQuery;
import me.zingle.api.sdk.dto.RequestDTO;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.model.ZingleAccount;
import me.zingle.api.sdk.model.ZingleService;
import me.zingle.api.sdk.model.ZingleServiceSetting;
import me.zingle.api.sdk.model.ZingleTimeZone;

import static me.zingle.api.sdk.dao.RequestMethods.POST;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleServiceServices extends ZingleBaseService<ZingleService>{

    private ServiceDelegate<ZingleService> settingsDelegate;

    public void setSettingsDelegate(ServiceDelegate<ZingleService> settingsDelegate) {
        this.settingsDelegate = settingsDelegate;
    }

    @Override
    protected String resourcePath(boolean specific) {
        final String base="/services";
        if(specific)
            return base+"/%s";
        else
            return base;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return modifier.equals("page")
            ||modifier.equals("page_size")
            ||modifier.equals("sort_field")
            ||modifier.equals("sort_direction")
            ||modifier.equals("account_id") //	N	only return Services from this Account
            ||modifier.equals("channel_value") //	Y	Filter by service channel values
            ||modifier.equals("display_name") //	Y	Filter by Service display name
            ||modifier.equals("address") //	Y	Filter by Service street address
            ||modifier.equals("city") //	N	Filter by Service city
            ||modifier.equals("state") //	N	Filter by Service state
            ||modifier.equals("country") //	N	Filter by Service country
            ||modifier.equals("postal_code") //
            ;
    }

    @Override
    public ZingleService mapper(JSONObject source) throws MappingErrorEx {
        ZingleService mapResult=new ZingleService();

        try {
            mapResult.setId(source.getString("id"));
            mapResult.setDisplayName(source.getString("display_name"));
            mapResult.setTimeZone(new ZingleTimeZone(source.getString("time_zone")));
            mapResult.setCreated_at(source.getInt("created_at"));
            mapResult.setUpdated_at(source.getInt("updated_at"));

            ZingleAccountServices accountServices = new ZingleAccountServices();
            ZingleAccount account = accountServices.mapper(source.getJSONObject("account"));
            mapResult.setAccount(account);

            ZinglePlanServices planServices = new ZinglePlanServices(account);
            mapResult.setPlan(planServices.mapper(source.getJSONObject("plan")));

            ZingleServiceChannelServices channelServices = new ZingleServiceChannelServices(mapResult);
            mapResult.setChannels(channelServices.arrayMapper(source.getJSONArray("channels")));

            ZingleChannelTypeServices channelTypeServices = new ZingleChannelTypeServices();
            mapResult.setChannelTypes(channelTypeServices.arrayMapper(source.getJSONArray("channel_types")));

            ZingleLabelServices labelServices = new ZingleLabelServices(mapResult);
            mapResult.setContactLabels(labelServices.arrayMapper(source.getJSONArray("contact_labels")));

            ZingleContactFieldServices customFieldServices = new ZingleContactFieldServices(mapResult);
            mapResult.setCustomFields(customFieldServices.arrayMapper(source.getJSONArray("contact_custom_fields")));

            ZingleServiceAddressServices addressServices = new ZingleServiceAddressServices();
            mapResult.setAddress(addressServices.mapper(source.getJSONObject("service_address")));

            ZingleServiceSettingService settingService = new ZingleServiceSettingService(mapResult);
            mapResult.setSettings(settingService.arrayMapper(source.getJSONArray("settings")));

            //"customFieldValues": [] ?????
        }catch (JSONException e) {
            e.printStackTrace();
            throw new MappingErrorEx(this.getClass().getName(),source.toString(),e.getMessage());
        }

        return mapResult;
    }

//Update settings
    public ZingleService updateSetting(ZingleServiceSetting object){

        ZingleQuery query = new ZingleQuery(POST, String.format(resourcePath(true) + "/settings/%s", object.getService().getId(), object.getSettingsField().getId()));

        RequestDTO payload=new RequestDTO();
        payload.setData(object.extractCreationData());
        query.setPayload(payload);

        ResponseDTO response = ZingleConnection.getInstance().send(query);

        if(response.getResponseCode()==200){
            JSONObject result= null;
            try {
                result = response.getData().getJSONObject("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapper(result);
        }
        else
            throw new UnsuccessfullRequestEx("Error create()",response.getResponseCode(),response.getResponseStr());
    }

    public boolean updateSettingAsync(final ZingleServiceSetting object,final ServiceDelegate<ZingleService> delegate){
        if(delegate==null){
            throw new UndefinedServiceDelegateEx();
        }

        Thread th=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    ZingleService result=updateSetting(object);
                    delegate.processResult(result);
                }catch (UnsuccessfullRequestEx e){
                    delegate.processError(e.getResponceCode(),e.getResponceStr());
                }
            }
        });

        th.start();

        return true;

    }

    public boolean updateSettingAsync(final ZingleServiceSetting object){
        synchronized (settingsDelegate) {
            if (settingsDelegate == null) {
                throw new UndefinedServiceDelegateEx();
            }
            return updateSettingAsync(object, settingsDelegate);
        }
    }

}
