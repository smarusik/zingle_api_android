package me.zingle.api.sdk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleContactCustomField {
    private int id;
    private ZingleService service;
    private String displayName;
    private boolean isGlobal;
    private List<ZingleContactCustomFieldOption> options;

    public ZingleContactCustomField() {
    }

    public ZingleContactCustomField(int id, ZingleService service, String displayName, boolean isGlobal, List<ZingleContactCustomFieldOption> options) {
        this.id = id;
        this.service = service;
        this.displayName = displayName;
        this.isGlobal = isGlobal;
        this.options = options;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public List<ZingleContactCustomFieldOption> getOptions() {
        return options;
    }

    public void setOptions(List<ZingleContactCustomFieldOption> options) {
        this.options = options;
    }

    public void addOption(ZingleContactCustomFieldOption option){

        if(options==null)
            options = new ArrayList<ZingleContactCustomFieldOption>();

            options.add(option);
    }

    public void addOptions(List<ZingleContactCustomFieldOption> options){
        if(this.options==null)
            this.options=options;
        else
            this.options.addAll(options);
    }
}
