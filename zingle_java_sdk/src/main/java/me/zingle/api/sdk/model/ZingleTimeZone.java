package me.zingle.api.sdk.model;

import org.json.JSONObject;

/**
 * <a href=https://github.com/Zingle/rest-api/tree/master/time_zones>Time Zone</a>
 */
public class ZingleTimeZone extends ZingleBaseModel{
    private String displayName;

    public ZingleTimeZone() {
    }

    public ZingleTimeZone(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nZingleTimeZone{");
        sb.append("\n    displayName='").append(displayName).append('\'');
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ZingleTimeZone that = (ZingleTimeZone) o;

        return displayName.equals(that.displayName);

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