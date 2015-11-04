package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.model.ZingleAvailablePhoneNumber;
import me.zingle.api.sdk.model.ZingleList;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleAvailablePhoneNumberServices extends ZingleBaseService<ZingleAvailablePhoneNumber> {
    static final String AREA_SEARCH_PATTERN="%s*******";

    @Override
    protected String resourcePath(boolean specific) {
        return "/available-phone-numbers";
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return modifier.equals("country")
                ||modifier.equals("search");
    }

    public ZingleAvailablePhoneNumber mapper(JSONObject source) throws MappingErrorEx {
        try {
            String numberStr = source.getString("phone_number");
            if (!numberStr.isEmpty()) {
                ZingleAvailablePhoneNumber result = new ZingleAvailablePhoneNumber(numberStr);
                result.setCountry(source.optString("country"));
                result.setFormattedPhoneNumber(source.optString("formatted_phone_number"));
                return result;
            } else
                return null;
        }catch(JSONException e){
            throw new MappingErrorEx(this.getClass().getSimpleName(),source.toString(),source.toString());
        }
    }

    public ZingleList<ZingleAvailablePhoneNumber> searchForCountryCode(String countryCode) throws UnsuccessfullRequestEx{
        return list(createConditions("country",countryCode));
    }

    public boolean searchForCountryCodeAsync(final String countryCode){
        return listAsync(createConditions("country", countryCode));
    }

    public boolean searchForCountryCodeAsync(final String countryCode, ServiceDelegate<ZingleList<ZingleAvailablePhoneNumber>> delegate){
        return listAsync(createConditions("country",countryCode),delegate);
    }

    public ZingleList<ZingleAvailablePhoneNumber> searchForCountryCodeAreaCode(String countryCode, String areaCode) throws UnsuccessfullRequestEx{
        return list(createConditions("country,search", countryCode, String.format(AREA_SEARCH_PATTERN, areaCode)));
    }

    public boolean searchForCountryCodeAreaCodeAsync(final String countryCode,final String areaCode){
        return listAsync(createConditions("country,search", countryCode, String.format(AREA_SEARCH_PATTERN, areaCode)));
    }

    public boolean searchForCountryCodeAreaCodeAsync(final String countryCode,final String areaCode, ServiceDelegate<ZingleList<ZingleAvailablePhoneNumber>> delegate){
        return listAsync(createConditions("country,search", countryCode, String.format(AREA_SEARCH_PATTERN, areaCode)),delegate);
    }

}
