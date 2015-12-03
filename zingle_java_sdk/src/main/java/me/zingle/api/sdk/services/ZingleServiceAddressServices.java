package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleList;
import me.zingle.api.sdk.model.ZingleServiceAddress;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/service_addresses>ZingleServiceAddress API</a>.
 * Supports none of basic functions.
 */
public class ZingleServiceAddressServices extends ZingleBaseService <ZingleServiceAddress>{
    @Override
    protected String resourcePath(boolean specific) {
        return null;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return false;
    }

    public ZingleServiceAddress mapper(JSONObject source) throws MappingErrorEx {
        try {
            return new ZingleServiceAddress(source.getString("address"), source.getString("city"), source.getString("state"),
                    source.getString("country"), source.getString("postal_code"));
        }catch (JSONException e) {
            e.printStackTrace();
            throw new MappingErrorEx(this.getClass().getName(),source.toString(),e.getMessage());
        }
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleServiceAddress get(String id) {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleList<ZingleServiceAddress> list() {
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
    public ZingleServiceAddress create(ZingleServiceAddress object) {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZingleServiceAddress update(ZingleServiceAddress object) {
        return null;
    }
}
