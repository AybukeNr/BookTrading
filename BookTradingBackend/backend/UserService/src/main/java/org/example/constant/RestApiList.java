package org.example.constant;

public class RestApiList {

    //api version
    public static final String API = "/api";
    public static final String VERSION = "/v1";

    //crud endpoints
    public static final String USERS = API + VERSION + "/users";
    public static final String GET_ALL_USERS = "/getAllUsers";
    public static final String GET_USER_BY_ID = "/getUserById";
    public static final String CREATE_USER = "/createUser";
    public static final String UPDATE_USER   = "/updateUser";
    public static final String GET_ADDRESSES = "/getAddresses";

    //auth endpoints
    public static final String AUTH = API + VERSION + "/auth";
    public static final String LOGIN = "/login";
    public static final String REGISTER= "/register";
    public static final String CHANGE_PASSWORD = "/changePass";
    public static final String FORGOT_PASSWORD = "/forgotPass";
    public static final String CREATE_ACCOUNT = "/createAccount";

    public static final String MAIL = API + VERSION + "/mail";
    public static final String TEST_REGISTER_MAIL = MAIL + "/test-register-mail";
    public static final String TEST_FORGOT_MAIL = MAIL + "/test-forgot-mail";


    public static final String TEST_SALE_LIST_MAIL = "/test-sale-list-mail";

    public static final String LIST_MAIL_INFOS = "/getInfos";

    public static final String TEST_SHIPPING_MAIL ="/test-shipping-mail";
    public static final String TEST_TRANSACTION_CREATED = "/test-transaction-created";

    public static final String TEST_TRANSACTION_COMPLATED = "/test-transaction-complated";
    public static final String TEST_EXCHANGE_LIST_UPDATE = "/test-exchange-list-update";
    public static final String TEST_ACCEPTED_OFFER = "/test-accepted-offer";
    public static final String REDUCE_TRUST_POINT = "/reduceTrustPoint";
    public static final String GET_USER_RESPONSE_BY_ID = "/getUserResponseById";
    public static final String CREATE_CARD = "/createCard";
}
