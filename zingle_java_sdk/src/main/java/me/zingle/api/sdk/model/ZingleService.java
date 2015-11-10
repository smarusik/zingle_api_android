package me.zingle.api.sdk.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/services#service-object>Service Object</a>
 */
public class ZingleService extends ZingleBaseModel{
    private String id;
    private String displayName;
    private ZingleTimeZone timeZone;
    private ZinglePlan plan;
    private ZingleServiceAddress address;
    private ZingleAccount account;
    private Long created_at;
    private Long updated_at;

    private List<ZingleServiceChannel> channels;
    private List<ZingleChannelType> channelTypes;
    private List<ZingleLabel> contactLabels;
    private List<ZingleContactField> customFields;
    private List<ZingleServiceSetting> settings;


    public ZingleService() {
    }

    public ZingleService(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ZingleTimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZingleTimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public ZinglePlan getPlan() {
        return plan;
    }

    public void setPlan(ZinglePlan plan) {
        this.plan = plan;
    }

    public ZingleServiceAddress getAddress() {
        return address;
    }

    public void setAddress(ZingleServiceAddress address) {
        this.address = address;
    }

    public ZingleAccount getAccount() {
        return account;
    }

    public void setAccount(ZingleAccount account) {
        this.account = account;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public Long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Long updated_at) {
        this.updated_at = updated_at;
    }

    public List<ZingleServiceChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<ZingleServiceChannel> channels) {
        this.channels = channels;
    }

    public List<ZingleChannelType> getChannelTypes() {
        return channelTypes;
    }

    public void setChannelTypes(List<ZingleChannelType> channelTypes) {
        this.channelTypes = channelTypes;
    }

    public List<ZingleLabel> getContactLabels() {
        return contactLabels;
    }

    public void setContactLabels(List<ZingleLabel> contactLabels) {
        this.contactLabels = contactLabels;
    }

    public List<ZingleContactField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<ZingleContactField> customFields) {
        this.customFields = customFields;
    }

    public List<ZingleServiceSetting> getSettings() {
        return settings;
    }

    public void setSettings(List<ZingleServiceSetting> settings) {
        this.settings = settings;
    }

    @Override
    public JSONObject extractCreationData() {
        checkForCreate();

        JSONObject resJS=new JSONObject();

        try {

            resJS.put("account_id",account.getId());
            resJS.put("display_name", getDisplayName());
            resJS.put("time_zone",getTimeZone().getDisplayName());
            resJS.put("plan_id", getPlan().getId());

            //Dummy arrays
            resJS.put("contact_labels", new JSONArray());
            resJS.put("contact_custom_fields", new JSONArray());

            resJS.put("service_address",address.extractCreationData());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();

        JSONObject resJS=new JSONObject();

        try {

            if(displayName!=null) resJS.put("display_name",getDisplayName());
            if(timeZone != null) resJS.put("time_zone",getTimeZone().getDisplayName());
            if(plan!=null) resJS.put("plan_id",getPlan().getId());
            if(address!=null) resJS.put("service_address",address.extractCreationData());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public void checkForCreate() {
        if(account==null) throw new RequestBodyCreationEx(RequestMethods.POST,"account_id",getClass().getName()+".account");
        if(displayName==null) throw new RequestBodyCreationEx(RequestMethods.POST,"display_name",getClass().getName()+".displayName");
        if(timeZone==null) throw new RequestBodyCreationEx(RequestMethods.POST,"time_zone",getClass().getName()+".timeZone");
        if(plan==null) throw new RequestBodyCreationEx(RequestMethods.POST,"plan_code",getClass().getName()+".plan");
        if(address==null) throw new RequestBodyCreationEx(RequestMethods.POST,"service_address",getClass().getName()+".address");
    }

    @Override
    public void checkForUpdate() {
        if(displayName==null && timeZone==null && plan==null && address==null)
            throw new RequestBodyCreationEx(RequestMethods.POST,"service_address,display_name,time_zone,plan_code"
                    ,getClass().getName()+".address,plan,timeZone,displayName");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleService{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("\n    timeZone=").append(timeZone);
        sb.append("\n    plan=").append(plan);
        sb.append("\n    address=").append(address);
        sb.append("\n    account=").append(account);
        sb.append("\n    created_at=").append(created_at);
        sb.append("\n    updated_at=").append(updated_at);
        sb.append("\n    channels=").append(channels);
        sb.append("\n    channelTypes=").append(channelTypes);
        sb.append("\n    contactLabels=").append(contactLabels);
        sb.append("\n    customFields=").append(customFields);
        sb.append("\n    settings=").append(settings);
        sb.append("}\n");
        return sb.toString();
    }
}
