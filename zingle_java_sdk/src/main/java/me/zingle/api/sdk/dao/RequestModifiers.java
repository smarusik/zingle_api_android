package me.zingle.api.sdk.dao;

/**
 * Created by SLAVA 09 2015.
 */
public enum RequestModifiers {
    //general
    PAGE("page"),
    PAGE_SIZE("page_size"),
    SORT_FIELD("sort_field"),
    SORT_DIRECTION("sort_direction"),
    //phone numbers
    PH_N_COUNTRY("country"),
    PH_N_SEARCH("search"),
    //services
    SRV_PHONE_NUMBER("phone_number"),
    SRV_PLAN_ID("plan_id"),
    SRV_DISPLAY_NAME("display_name"),
    SRV_ADDRESS("address"),
    SRV_CITY("city"),
    SRV_STATE("state"),
    SRV_POSTAL_CODE("postal_code"),
    SRV_COUNTRY("country"),
    SRV_CHANGES_EMULATE("emulate"),
    //contacts
    CONT_PHONE_NUMBER("phone_number"),
    CONT_LABEL_ID("label_id"),
    CONT_IS_CONFIRMED("is_confirmed"),
    //contact labels
    CONT_LBL_DISPLAY_NAME("display_name"),
    CONT_LBL_IS_GLOBAL("is_global"),
    CONT_LBL_IS_AUTOMATIC("is_automatic"),
    //messages
    MSG_CONTACT_ID("contact_id"),
    MSG_COMMUNICATION_DIRECTION("communication_direction"),
    MSG_CREATED_AT("created_at"),
    MSG_COMMUNICATION_ID("communication_id")
    ;

    public String representation;

    RequestModifiers(String representation) {
        this.representation = representation;
    }
}
