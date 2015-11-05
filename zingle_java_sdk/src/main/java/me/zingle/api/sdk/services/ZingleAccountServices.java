package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleAccount;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/accounts>ZingleAccount API</a>).
 */
public class ZingleAccountServices extends ZingleBaseService<ZingleAccount> {

    @Override
    protected String resourcePath(boolean specific) {
        if(specific)
            return "/accounts/%s";
        else
            return "/accounts";
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return modifier.equals("page")
                ||modifier.equals("page_size")
                ||modifier.equals("sort_field")
                ||modifier.equals("sort_direction")
                ;
    }

    @Override
    public ZingleAccount mapper(JSONObject source) throws MappingErrorEx {
        try {
            String id = source.getString("id");
            if (!id.isEmpty()) {
                ZingleAccount result = new ZingleAccount(id);
                result.setDisplayName(source.optString("display_name"));
                result.setTermMonths(source.optInt("term_months"));
                result.setCurrentTermStartDate(source.optLong("current_term_start_date"));
                result.setCurrentTermEndDate(source.optLong("current_term_end_date"));
                result.setCreatedAt(source.optLong("created_at"));
                result.setUpdatedAt(source.optLong("updated_at"));
                return result;
            } else
                return null;
        }catch(JSONException e){
            throw new MappingErrorEx(this.getClass().getSimpleName(),source.toString(),source.toString());
        }
    }

    @Override
    public Boolean delete(String id) {
        return false;
    }

    @Override
    public ZingleAccount create(ZingleAccount object) {
        return null;
    }

    @Override
    public ZingleAccount update(ZingleAccount object) {
        return null;
    }
}
