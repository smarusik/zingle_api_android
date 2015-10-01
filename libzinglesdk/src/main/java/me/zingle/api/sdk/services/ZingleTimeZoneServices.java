package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleTimeZone;

/**
 * Created by SLAVA 08 2015.
 */
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
        return new ZingleTimeZone(source.getString("display_name"));
    }

    @Override
    public ZingleList<ZingleTimeZone> arrayMapper(JSONObject source) throws MappingErrorEx {
        ZingleList<ZingleTimeZone> result=new ZingleList<>();

        JSONArray timeZonesJSON=source.getJSONArray("result");
        int i=0;
        String temp=timeZonesJSON.optString(i++);

        while(!temp.isEmpty()){
            result.objects.add(new ZingleTimeZone(temp));
            temp=timeZonesJSON.optString(i++);
        }
        return result;
    }
}
