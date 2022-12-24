package com.nomi.caysenda.ghn.api;

public class GHNURL {
    public static final String ENV_URL = "https://online-gateway.ghn.vn";
    public static final String GET_SERVICE = ENV_URL+"/shiip/public-api/v2/shipping-order/available-services";
    /** ORDER */
    public static final String UPDATE_ORDER = ENV_URL+"/shiip/public-api/v2/shipping-order/update";
    public static final String CREATE_ORDER = ENV_URL+"/shiip/public-api/v2/shipping-order/create";
    public static final String CANCEL_ORDER = ENV_URL+"/shiip/public-api/v2/switch-status/cancel";
    public static final String RETURN_ORDER = ENV_URL+"/shiip/public-api/v2/switch-status/return";
    public static final String PRINT_ORDER = ENV_URL+"/shiip/public-api/v2/a5/gen-token";
    public static final String INFO_ORDER = ENV_URL+"/shiip/public-api/v2/shipping-order/detail";
    public static final String UPDATE_COD_ORDER = ENV_URL+"/shiip/public-api/v2/shipping-order/updateCOD";
    /***/
    /** Store */
    public static final String GET_STORE = ENV_URL+"/shiip/public-api/v2/shop/all";
    /***/
    /** Address */
    public static final String GET_PROVINCE = ENV_URL+"/shiip/public-api/master-data/province";
    public static final String GET_DISTRICT = ENV_URL+"/shiip/public-api/master-data/district";
    public static final String GET_WARD = ENV_URL+"/shiip/public-api/master-data/ward";
    /***/
    /** Canculator */
    public static final String CANCULATE_FEE = ENV_URL+"/shiip/public-api/v2/shipping-order/fee";
    /***/
    /** PickShift*/
    public static final String PICK_SHIFT = ENV_URL+"/shiip/public-api/v2/shift/date";
}
