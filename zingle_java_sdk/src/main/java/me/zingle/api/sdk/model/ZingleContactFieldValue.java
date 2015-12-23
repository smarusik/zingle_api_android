package me.zingle.api.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/custom_field_values#contact-custom-field-value-object>Contact Custom Field Value Object</a>
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ZingleContactFieldValue that = (ZingleContactFieldValue) o;

        if (!contactField.equals(that.contactField)) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return !(selectedFieldOptionId != null ? !selectedFieldOptionId.equals(that.selectedFieldOptionId) : that.selectedFieldOptionId != null);

    }

    @Override
    public int hashCode() {
        int result = contactField.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (selectedFieldOptionId != null ? selectedFieldOptionId.hashCode() : 0);
        return result;
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
