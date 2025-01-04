package org.example.constant;

public class RestApiList {

    // API version
    public static final String API = "/api";
    public static final String VERSION = "/v1";

    // Base endpoints
    public static final String TRANSACTIONS = API + VERSION + "/transactions";
    public static final String PAYMENT = API + VERSION + "/payments";
    public static final String CARD = API + VERSION + "/cards";

    // Transaction endpoints
    public static final String GET_ALL_TRANSACTIONS = "/getAllTransactions";
    public static final String GET_TRANSACTIONS_BY_USER_ID = "/getTransactionsByUserId";
    public static final String GET_TRANSACTION_BY_ID = "/getTransactionById";
    public static final String CREATE_TRANSACTION = "/createTransaction";
    public static final String REFUND_DEPOSITS = "/refundDeposits";
    public static final String FINALIZE_PAYMENT = "/finalizePayment";
    public static final String TAKE_PAYMENT = "/takePayment";
    public static final String TRANSFER_ALL = "/transfer";

    // Payment endpoints
    public static final String CREATE_PAYMENT = "/createPayment";
    public static final String GET_PAYMENT_BY_ID = "/getPaymentById";
    public static final String GET_ALL_PAYMENTS = "/getAllPayments";
    public static final String GET_USERS_PAYMENTS = "/getUserPayments";


    // Card endpoints
    public static final String GET_ALL_CARDS = "/getAllCards";
    public static final String GET_USERS_CARDS = "/getUserCards";
    public static final String CREATE_CARD = "/createCard";

    //List Manager
    public static final String GET_LIST_BY_ID = "/getListsById";
    public static final String UPDATE_LIST_STATUS = "/updateListStat";

    public static final String CREATE_ACCOUNT = "/createAccount";
    public static final String PROCESS_SALES = "/processSale" ;



}

