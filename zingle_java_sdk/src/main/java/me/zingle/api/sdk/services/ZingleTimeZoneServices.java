package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleTimeZone;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/time_zones>ZingleTimeZone API</a>.
 * Supports only listing.
 * */
public class ZingleTimeZoneServices extends ZingleBaseService<ZingleTimeZone> {

    @Override
    protected String resourcePath(boolean specific) {
        return "/time-zones";
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return false;
    }

    @Override
    public ZingleTimeZone mapper(JSONObject source) throws MappingErrorEx {
        return new ZingleTimeZone(source.optString("display_name"));
    }

    @Override
    public ZingleList<ZingleTimeZone> arrayMapper(JSONObject source) throws MappingErrorEx {
        ZingleList<ZingleTimeZone> result=new ZingleList<>();

        try {
            JSONArray timeZonesJSON = source.getJSONArray("result");
            int i = 0;
            String temp = timeZonesJSON.optString(i++);

            while (!temp.isEmpty()) {
                result.objects.add(new ZingleTimeZone(temp));
                temp = timeZonesJSON.optString(i++);
            }
        }catch (JSONException e) {
            e.printStackTrace();
            throw new MappingErrorEx(this.getClass().getName(),source.toString(),e.getMessage());
        }

        return result;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleTimeZone get(String id) {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>false</i>.
     */
    @Override
    public Boolean delete(String id) {
        return false;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleTimeZone create(ZingleTimeZone object) {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleTimeZone update(ZingleTimeZone object) {
        return null;
    }
}
