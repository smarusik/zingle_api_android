package me.zingle.api.sdk.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/contact_custom_fields#contact-field-object>Contact Field Object</a>
 */
public class ZingleContactField extends ZingleBaseModel{
    private ZingleService service;

    private String id;
    private String displayName;
    private String dataType;
    private Boolean isGlobal;
    private List<ZingleFieldOption> options=new ArrayList<>();

    public ZingleContactField() {
    }

    public ZingleContactField(ZingleService service, String id) {
        this.service = service;
        this.id = id;
    }


    public ZingleContactField(ZingleService service, String id, String displayName, String dataType, Boolean isGlobal, List<ZingleFieldOption> options) {
        this.service = service;
        this.id = id;
        this.displayName = displayName;
        this.dataType = dataType;
        this.isGlobal = isGlobal;
        this.options = options;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Boolean getIsGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(Boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZingleService getService() {
        return service;
    }

    public void setService(ZingleService service) {
        this.service = service;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Boolean isGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public List<ZingleFieldOption> getOptions() {
        return options;
    }

    public void setOptions(List<ZingleFieldOption> options) {
        this.options = options;
    }

    public void addOption(ZingleFieldOption option){

        if(options==null)
            options = new ArrayList<ZingleFieldOption>();

            options.add(option);
    }

    public void addOptions(List<ZingleFieldOption> options){
        if(this.options==null)
            this.options=options;
        else
            this.options.addAll(options);
    }

    @Override
    public JSONObject extractCreationData() {
        checkForCreate();
        return extractData();
    }

    @Override
    public JSONObject extractUpdateData() {
        checkForUpdate();
        return extractData();
    }

    private JSONObject extractData(){

        JSONObject resJS=new JSONObject();

        try {

            resJS.put("display_name", displayName);
            JSONArray arrJS=new JSONArray();

            if(!(options==null || options.isEmpty())){
                for(ZingleFieldOption fo:options) arrJS.put(fo.extractCreationData());
            }
            resJS.put("options",arrJS);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public void checkForCreate() {
        if(displayName==null || displayName.isEmpty()) throw new RequestBodyCreationEx(RequestMethods.POST,"display_name",getClass().getName()+"displayName");
    }

    @Override
    public void checkForUpdate() {
        if(displayName==null || displayName.isEmpty()) throw new RequestBodyCreationEx(RequestMethods.PUT,"display_name",getClass().getName()+"displayName");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleContactField{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("\n    dataType='").append(dataType).append('\'');
        sb.append("\n    isGlobal=").append(isGlobal);
        sb.append("\n    options=").append(options);
        sb.append("}\n");
        return sb.toString();
    }
}