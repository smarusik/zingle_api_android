package me.zingle.api.sdk.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleContact {
    private int id;
    private ZingleService service;
    private ZinglePhoneNumber phoneNumber;
    private boolean isConfirmed;
    private boolean isStarred;
    private Map<ZingleContactCustomField,String> customFieldValues;
    private List<ZingleLabel> labels;

    public ZingleContact() {
    }

    public ZingleContact(int id, ZingleService service, ZinglePhoneNumber phoneNumber,
                         boolean isConfirmed, boolean isStarred,
                         Map<ZingleContactCustomField,String> customFieldValues, List<ZingleLabel> labels) {
        this.id = id;
        this.service = service;
        this.phoneNumber = phoneNumber;
        this.isConfirmed = isConfirmed;
        this.isStarred = isStarred;
        this.customFieldValues = customFieldValues;
        this.labels = labels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ZingleService getService() {
        return service;
    }

    public void setService(ZingleService service) {
        this.service = service;
    }

    public ZinglePhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(ZinglePhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public Map<ZingleContactCustomField,String> getCustomFieldValues() {
        return customFieldValues;
    }

    public void setCustomFieldValues(Map<ZingleContactCustomField,String> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    public void addCustomFieldValues(Map<ZingleContactCustomField,String> customFieldValues){
        if(this.customFieldValues==null)
            this.customFieldValues=customFieldValues;
        else
            this.customFieldValues.putAll(customFieldValues);
    }

    public void addCustomFieldValue(ZingleContactCustomField customField,String value){
        if(customFieldValues==null)
            customFieldValues=new HashMap<ZingleContactCustomField,String>();

        customFieldValues.put(customField,value);
    }

    public List<ZingleLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<ZingleLabel> labels) {
        this.labels = labels;
    }

    public void addLabels(List<ZingleLabel> labels){
        if(this.labels==null)
            this.labels=labels;
        else
            this.labels.addAll(labels);
    }

    public void addLabel(ZingleLabel label){
        if(labels==null)
            labels=new ArrayList<ZingleLabel>();

        labels.add(label);

    }
}
