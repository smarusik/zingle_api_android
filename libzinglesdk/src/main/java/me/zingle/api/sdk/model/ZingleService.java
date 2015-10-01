package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleService extends ZingleBaseModel{
    private String id;
    private String displayName;
    private ZingleTimeZone timeZone;
    private ZinglePlan plan;
    private ZingleServiceAddress address;
    private ZingleAccount account;
    private Integer created_at;
    private Integer updated_at;

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

    public Integer getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Integer created_at) {
        this.created_at = created_at;
    }

    public Integer getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Integer updated_at) {
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

        JSONStringer res=new JSONStringer();

        res.object();

        res.key("account_id").value(account.getId());
        res.key("display_name").value(getDisplayName());
        res.key("time_zone").value(getTimeZone().getDisplayName());
        res.key("plan_id").value(getPlan().getId());

        //Dummy arrays
        res.key("contact_labels").array().endArray();
        res.key("contact_custom_fields").array().endArray();

        res.key("service_address").value(address.extractCreationData());


        res.endObject();

        return new JSONObject(res.toString());
    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();

        JSONStringer res=new JSONStringer();

        res.object();

        if(displayName!=null) res.key("display_name").value(getDisplayName());
        if(timeZone != null) res.key("time_zone").value(getTimeZone().getDisplayName());
        if(plan!=null) res.key("plan_id").value(getPlan().getId());
        if(address!=null) res.key("service_address").value(address.extractCreationData());

        res.endObject();

        return new JSONObject(res.toString());
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
        final StringBuilder sb = new StringBuilder("ZingleService{");
        sb.append("id='").append(id).append('\'');
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", timeZone=").append(timeZone);
        sb.append(", plan=").append(plan);
        sb.append(", address=").append(address);
        sb.append(", account=").append(account);
        sb.append(", created_at=").append(created_at);
        sb.append(", updated_at=").append(updated_at);
        sb.append(", channels=").append(channels);
        sb.append(", channelTypes=").append(channelTypes);
        sb.append(", contactLabels=").append(contactLabels);
        sb.append(", customFields=").append(customFields);
        sb.append(", settings=").append(settings);
        sb.append('}');
        return sb.toString();
    }
}
