package me.zingle.atlas_adoption.daemons;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.model.ZingleContact;
import me.zingle.api.sdk.model.ZingleService;

/**
 * Created by SLAVA 10 2015.
 */
public class WorkingDataSet {

    private static WorkingDataSet item;

    public static WorkingDataSet getItem(){
        if(item==null){
            item=new WorkingDataSet();
        }

        return item;
    }

    private WorkingDataSet() {
    }

    String login;
    String password;
    private List<ZingleService> allowedServices=new ArrayList<>();
    private List<ZingleContact> contacts=new ArrayList<>();

    synchronized public List<ZingleService> getAllowedServices() {
        return allowedServices;
    }

    synchronized public void setAllowedServices(List<ZingleService> allowedServices) {
        this.allowedServices = allowedServices;
    }

    synchronized public List<ZingleContact> getContacts() {
        return contacts;
    }

    synchronized public ZingleContact getContact(int index) {
        return contacts.get(index);
    }

    synchronized public void setContacts(List<ZingleContact> contacts) {
        this.contacts = contacts;
    }

    synchronized public void addContacts(List<ZingleContact> contacts) {
        this.contacts.addAll(contacts);
    }

    synchronized public String getLogin() {
        return login;
    }

    synchronized public void setLogin(String login) {
        this.login = login;
    }

    synchronized public String getPassword() {
        return password;
    }

    synchronized public void setPassword(String password) {
        this.password = password;
    }
}
