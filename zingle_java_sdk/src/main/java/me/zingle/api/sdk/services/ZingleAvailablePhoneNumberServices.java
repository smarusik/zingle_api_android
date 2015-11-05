package me.zingle.api.sdk.services;

import org.json.JSONException;
import org.json.JSONObject;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.Exceptions.UnsuccessfullRequestEx;
import me.zingle.api.sdk.model.ZingleAvailablePhoneNumber;
import me.zingle.api.sdk.model.ZingleList;

/**
 * ZingleBaseService derivation for working with <a href=https://github.com/Zingle/rest-api/tree/master/available_phone_numbers>ZingleAvailablePhoneNumber API</a>).
 * Supports only list() base function and provides some convenience methods.
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

    /**
     * Convenience list() modification. Allows searching by country code without manual adding of key-value query pairs
     *
     * @param countryCode String ISO 3166-1 alpha-2 country code
     * @return ZingleList of available phone numbers.
     * @throws UnsuccessfullRequestEx if http response differs from success.
     */
    public ZingleList<ZingleAvailablePhoneNumber> searchForCountryCode(String countryCode) throws UnsuccessfullRequestEx{
        return list(createConditions("country",countryCode));
    }


    /**
     * Same as searchForCountryCode(String countryCode), but runs in separate thread. Proper ServiceDelegate must be implemented and
     * assigned with setListDelegate()
     */
    public boolean searchForCountryCodeAsync(final String countryCode){
        return listAsync(createConditions("country", countryCode));
    }

    /**
     * Same as searchForCountryCodeAsync(String countryCode), but ServiceDelegate must be implemented and
     * provided as argument.
     * @param countryCode String ISO 3166-1 alpha-2 country code
     * @param delegate implementation of ServiceDelegate<ZingleList<ZingleAvailablePhoneNumber>>
     * @return ZingleList of available phone numbers.
     * @throws UnsuccessfullRequestEx if http response differs from success.
     */
    public boolean searchForCountryCodeAsync(final String countryCode, ServiceDelegate<ZingleList<ZingleAvailablePhoneNumber>> delegate){
        return listAsync(createConditions("country",countryCode),delegate);
    }


    /**
     * Convenience list() modification. Allows searching by country and area code without manual adding of key-value query pairs
     *
     * @param countryCode String ISO 3166-1 alpha-2 country code
     * @param areaCode country area code (usually 3 digits)
     * @return ZingleList of available phone numbers.
     * @throws UnsuccessfullRequestEx if http response differs from success.
     */
    public ZingleList<ZingleAvailablePhoneNumber> searchForCountryCodeAreaCode(String countryCode, String areaCode) throws UnsuccessfullRequestEx{
        return list(createConditions("country,search", countryCode, String.format(AREA_SEARCH_PATTERN, areaCode)));
    }

    /**
     * Same as searchForCountryCodeAreaCode(String countryCode, String areaCode), but runs in separate thread. Proper ServiceDelegate must be implemented and
     * assigned with setListDelegate()
     */
    public boolean searchForCountryCodeAreaCodeAsync(final String countryCode,final String areaCode){
        return listAsync(createConditions("country,search", countryCode, String.format(AREA_SEARCH_PATTERN, areaCode)));
    }

    /**
     * Same as searchForCountryCodeAsync(String countryCode,String areaCode), but ServiceDelegate must be implemented and
     * provided as argument.
     * @param countryCode String ISO 3166-1 alpha-2 country code
     * @param areaCode country area code (usually 3 digits)
     * @param delegate implementation of ServiceDelegate<ZingleList<ZingleAvailablePhoneNumber>>
     * @return ZingleList of available phone numbers.
     * @throws UnsuccessfullRequestEx if http response differs from success.
     */
    public boolean searchForCountryCodeAreaCodeAsync(final String countryCode,final String areaCode, ServiceDelegate<ZingleList<ZingleAvailablePhoneNumber>> delegate){
        return listAsync(createConditions("country,search", countryCode, String.format(AREA_SEARCH_PATTERN, areaCode)),delegate);
    }

}
