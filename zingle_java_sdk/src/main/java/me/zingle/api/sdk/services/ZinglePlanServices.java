package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.model.ZingleAccount;
import me.zingle.api.sdk.model.ZinglePlan;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/plans>ZinglePlan API</a>.
 * Supports only getting by id and listing.
 */
public class ZinglePlanServices extends ZingleBaseService<ZinglePlan> {

    private final ZingleAccount parent;

    public ZinglePlanServices(ZingleAccount parent) {
        this.parent = parent;
    }

    @Override
    protected String resourcePath(boolean specific) {
        String base=String.format("/accounts/%s/plans",parent.getId());

        if(specific)
            return base+"/%s";
        else
            return base;
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
    public ZinglePlan mapper(JSONObject source) throws MappingErrorEx {
        try {
            String id = source.getString("id");
            if (!id.isEmpty()) {
                ZinglePlan result = new ZinglePlan(id);
                result.setCode(source.optString("code"));
                result.setDisplayName(source.optString("display_name"));
                result.setTermMonths(source.optInt("term_months"));
                result.setMonthlyOrUnitPrice(source.optDouble("monthly_or_unit_price"));
                result.setSetupPrice(source.optDouble("setup_price"));
                result.setIsPrinterPlan(source.optBoolean("is_printer_plan"));

                result.setAccount(parent);

                return result;
            } else
                return null;
        }catch(JSONException e){
            throw new MappingErrorEx(this.getClass().getSimpleName(),source.toString(),source.toString());
        }
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public Boolean delete(String id) {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZinglePlan create(ZinglePlan object) {
        return null;
    }

    /**
     * Unsupported method from base class. Overloaded to return <i>null</i>.
     */
    @Override
    public ZinglePlan update(ZinglePlan object) {
        return null;
    }
}
