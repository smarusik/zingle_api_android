package me.zingle.api.sdk.model;

import org.json.JSONObject;
import org.json.JSONStringer;

import me.zingle.api.sdk.Exceptions.RequestBodyCreationEx;
import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleContactFieldValue extends ZingleBaseModel {

    private ZingleContactField contactField;
    private Object value;
    private Integer selectedFieldOptionId;

    public ZingleContactFieldValue() {
    }

    public ZingleContactFieldValue(ZingleContactField contactField) {
        this.contactField = contactField;
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

    public Integer getSelectedFieldOptionId() {
        return selectedFieldOptionId;
    }

    public void setSelectedFieldOptionId(Integer selectedFieldOptionId) {
        this.selectedFieldOptionId = selectedFieldOptionId;
    }


    @Override
    public JSONObject extractCreationData() {
        checkForCreate();

        JSONStringer res = new JSONStringer();

        res.object();

        if(value!=null)
            res.key("value").value(value);
        else if (selectedFieldOptionId !=null)
            res.key("selected_custom_field_option_id").value(selectedFieldOptionId);

        res.endObject();
        return new JSONObject(res.toString());
    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
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

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ZingleContactFieldValue{");
        sb.append("contactField=").append(contactField);
        sb.append(", value=").append(value);
        sb.append(", selectedFieldOptionId=").append(selectedFieldOptionId);
        sb.append('}');
        return sb.toString();
    }
}
