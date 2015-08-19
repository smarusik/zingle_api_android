package me.zingle.api.sdk.model;

/**
 * Created by SLAVA 08 2015.
 */
public class ZinglePlan {
    private int id;
    private int termMonths;
    private float monthlyOrUnitPrice;
    private float setupPrice;
    private String displayName;
    private boolean isPrinterPlan;

    public ZinglePlan() {
    }

    public ZinglePlan(int id, int termMonths, float monthlyOrUnitPrice, float setupPrice, String displayName, boolean isPrinterPlan) {
        this.id = id;
        this.termMonths = termMonths;
        this.monthlyOrUnitPrice = monthlyOrUnitPrice;
        this.setupPrice = setupPrice;
        this.displayName = displayName;
        this.isPrinterPlan = isPrinterPlan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(int termMonths) {
        this.termMonths = termMonths;
    }

    public float getMonthlyOrUnitPrice() {
        return monthlyOrUnitPrice;
    }

    public void setMonthlyOrUnitPrice(float monthlyOrUnitPrice) {
        this.monthlyOrUnitPrice = monthlyOrUnitPrice;
    }

    public float getSetupPrice() {
        return setupPrice;
    }

    public void setSetupPrice(float setupPrice) {
        this.setupPrice = setupPrice;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isPrinterPlan() {
        return isPrinterPlan;
    }

    public void setIsPrinterPlan(boolean isPrinterPlan) {
        this.isPrinterPlan = isPrinterPlan;
    }
}

