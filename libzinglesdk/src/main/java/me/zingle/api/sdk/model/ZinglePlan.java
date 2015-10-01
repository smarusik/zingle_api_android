package me.zingle.api.sdk.model;

import org.json.JSONObject;

/**
 * Created by SLAVA 09 2015.
 */
public class ZinglePlan extends ZingleBaseModel{

    ZingleAccount account;

    String id;	//Unique identifier
    String code;
    String displayName;
    Double monthlyOrUnitPrice; //Price (USD) per-month (or per room per month, when using per room pricing).
    Double setupPrice; //Price (USD) charged one time when starting a new service using this plan
    Integer termMonths;	//Number of months per billing term
    Boolean isPrinterPlan; //Whether this plan is used when provisioning new printer-based service

    public ZinglePlan() {
    }

    public ZinglePlan(String id) {
        this.id = id;
    }

    public ZinglePlan(ZingleAccount account, String id) {
        this.account = account;
        this.id = id;
    }

    public ZingleAccount getAccount() {
        return account;
    }

    public void setAccount(ZingleAccount account) {
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Double getMonthlyOrUnitPrice() {
        return monthlyOrUnitPrice;
    }

    public void setMonthlyOrUnitPrice(Double monthlyOrUnitPrice) {
        this.monthlyOrUnitPrice = monthlyOrUnitPrice;
    }

    public Double getSetupPrice() {
        return setupPrice;
    }

    public void setSetupPrice(Double setupPrice) {
        this.setupPrice = setupPrice;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public Boolean getIsPrinterPlan() {
        return isPrinterPlan;
    }

    public void setIsPrinterPlan(Boolean isPrinterPlan) {
        this.isPrinterPlan = isPrinterPlan;
    }

    @Override
    public String toString() {
        StringBuilder result=new StringBuilder();

        result.append("Account:\n").append(account.toString()).append("\n");

        result.append("\nid=").append(id);
        result.append("\ncode=").append(code);
        result.append("\ndisplayName=").append(displayName);
        result.append("\nmonthlyOrUnitPrice=").append(monthlyOrUnitPrice);
        result.append("\nsetupPrice=").append(setupPrice);
        result.append("\ntermMonths=").append(termMonths);
        result.append("\nisPrinterPlan=").append(isPrinterPlan);

        return result.toString();
    }

    @Override
    public JSONObject extractCreationData() {
        return null;
    }

    @Override
    public JSONObject extractUpdateData() {
        return null;
    }

    @Override
    public void checkForCreate() {

    }

    @Override
    public void checkForUpdate() {

    }
}