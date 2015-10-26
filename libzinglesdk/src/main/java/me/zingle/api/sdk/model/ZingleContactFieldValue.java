package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleContactFieldValue extends ZingleBaseModel {

    private ZingleContactField contactField;
    private Object value;
    private String selectedFieldOptionId;

    public ZingleContactFieldValue() {
    }

    public ZingleContactFieldValue(ZingleContactField contactField, Object value, String selectedFieldOptionId) {
        this.contactField = contactField;
        this.value = value;
        this.selectedFieldOptionId = selectedFieldOptionId;
    }

    public ZingleContactFieldValue(ZingleContactField contactField, Object value) {
        this.contactField = contactField;
        this.value=value;
    }

    public ZingleContactField getContactField() {
        return contactField;
    }

    public void setContactField(ZingleContactField contactField) {
        this.contactField = contactField;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getSelectedFieldOptionId() {
        return selectedFieldOptionId;
    }

    public void setSelectedFieldOptionId(String selectedFieldOptionId) {
        this.selectedFieldOptionId = selectedFieldOptionId;
    }


    @Override
    public JSONObject extractCreationData() {
        checkForCreate();

        JSONObject resJS=new JSONObject();

        try {

            resJS.put("custom_field_id",contactField.getId());

            if(value!=null)
                resJS.put("value",value);
            else if (selectedFieldOptionId !=null)
                resJS.put("selected_custom_field_option_id",selectedFieldOptionId);

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

            if(value!=null)
                resJS.put("value",value);
            else if (selectedFieldOptionId !=null)
                resJS.put("selected_custom_field_option_id",selectedFieldOptionId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resJS;
    }

    @Override
    public void checkForCreate() {
        if(contactField==null){
            throw new RequestBodyCreationEx(RequestMethods.POST,"custom_field",
                    getClass().getName()+".custom_field");
        }
        if(value==null && selectedFieldOptionId ==null){
            throw new RequestBodyCreationEx(RequestMethods.POST,"value,selected_custom_field_option_id",
                    getClass().getName()+".value,selectedSettingsFieldOptionId");
        }
    }

    @Override
    public void checkForUpdate() {
        if(contactField==null){
            throw new RequestBodyCreationEx(RequestMethods.POST,"custom_field",
                    getClass().getName()+".custom_field");
        }
        if(value==null && selectedFieldOptionId ==null){
            throw new RequestBodyCreationEx(RequestMethods.POST,"value,selected_custom_field_option_id",
                    getClass().getName()+".value,selectedSettingsFieldOptionId");
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleContactFieldValue{");
        sb.append("\n    contactField=").append(contactField);
        sb.append("\n    value=").append(value);
        sb.append("\n    selectedFieldOptionId=").append(selectedFieldOptionId);
        sb.append("}\n");
        return sb.toString();
    }
}
