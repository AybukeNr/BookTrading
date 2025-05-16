package org.example.constant;

public class RestApiList {

    // API version
    public static final String API = "/api";
    public static final String VERSION = "/v1";

    // Base endpoints
    public static final String SHIPPINGS = API + VERSION + "/shippings";

    // SHIPPING endpoints
    public static final String GET_USERS_SHIPPINGS = "/getUsersShippings";
    public static final String GET_ALL_SHIPPINGS = "/getTransactionsByUserId";
    public static final String GET_SHIPPING_BY_SN = "/getShippingById";
    public static final String GET_ALL_EXCHANGES = "/getAllExchanges";
    public static final String GET_EXCHANGE_BY_TID = "/getExchangeById";
    public static final String GET_USERS_EXCHANGE = "/getUsersExchanges";
    public static final String GET_ALL_EXS_BY_STATUS = "/getAllExsByStatus";
    public static final String CANCEL_EXCHANGE = "/cancelExchange";
    public static final String CREATE_SHIPPING = "/createShipping";
    public static final String UPDATE_SHIPPING = "/updateShipping";
    public static final String REFUND_DEPOSITS = "/refundDeposits";
    public static final String FINALIZE_PAYMENT = "/finalizePayment";
    public static final String SET_DELIVERED = "/setDelivered";
    public static final String TRANSFER_ALL = "/transfer";
    public static final String UPDATE_LIST_STATUS = "/updateListStat";
    public static final String TEST_SHIPPING_MAIL ="/test-shipping-mail";
    public static final String GET_EXCHANGE_INFOS = "/getExchangeInfos";


    public static final String SET_STATUS = "/setStatus";
}

