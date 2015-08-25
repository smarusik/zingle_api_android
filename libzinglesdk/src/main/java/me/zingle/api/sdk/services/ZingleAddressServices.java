package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.model.ZingleAddress;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleAddressServices {
    static ZingleAddress mapper(JSONObject source) throws JSONException {
        return new ZingleAddress(source.getString("address"), source.getString("city"), source.getString("state"),
                source.getString("country"), source.getString("postal_code"));
    }
}
