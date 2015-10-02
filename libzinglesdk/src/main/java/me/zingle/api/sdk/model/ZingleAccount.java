package me.zingle.api.sdk.model;

import org.json.JSONObject;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleAccount extends ZingleBaseModel {
    String id;
    String displayName;
    Integer termMonths;
    Long currentTermStartDate;
    Long currentTermEndDate;
    Long createdAt;
    Long updatedAt;

    public ZingleAccount() {
    }

    public ZingleAccount(String id) {
        this.id = id;
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

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public Long getCurrentTermStartDate() {
        return currentTermStartDate;
    }

    public void setCurrentTermStartDate(Long currentTermStartDate) {
        this.currentTermStartDate = currentTermStartDate;
    }

    public Long getCurrentTermEndDate() {
        return currentTermEndDate;
    }

    public void setCurrentTermEndDate(Long currentTermEndDate) {
        this.currentTermEndDate = currentTermEndDate;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean equals(ZingleAccount obj) {
        return id.equals(obj.id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleAccount{");
        sb.append("\n    id='").append(id).append('\'');
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("\n    termMonths=").append(termMonths);
        sb.append("\n    currentTermStartDate=").append(currentTermStartDate);
        sb.append("\n    currentTermEndDate=").append(currentTermEndDate);
        sb.append("\n    createdAt=").append(createdAt);
        sb.append("\n    updatedAt=").append(updatedAt);
        sb.append("}\n");
        return sb.toString();
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
