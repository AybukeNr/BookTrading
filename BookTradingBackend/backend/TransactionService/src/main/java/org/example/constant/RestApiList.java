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
    public static final String SET_STATUS = "/setStatus";
    public static final String GET_USERS_EXCHANGES = "/getUsersExchanges";
    public static final String GET_USERS_SALES = "/getUsersSales";
    public static final String GET_TRANSACTION_INFOS = "/getTransactionInfos";
    public static final String GET_TRANSACTION_ALL_INFOS = "/getTransactionAllInfos";

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
    public static final String GET_LIST_PRICE= "/getListPrice";
    public static final String GET_OFFER_BOOKS = "/getOfferBooks";
    public static final String GET_EXCHANGE_BOOKS = "/getExchangeBooks";
    public static final String GET_LIST_TYPE = "/getListType";
    public static final String GET_LISTS_OWNERID = "/getListsOwnerId";


    public static final String CREATE_ACCOUNT = "/createAccount";
    public static final String PROCESS_SALES = "/processSale" ;
   //library manager
    public static final String GET_BOOK_CONDITION = "/{bookId}/condition";

    //user manager
    public static final String REDUCE_TRUST_POINT = "/reduceTrustPoint";
    public static final String GET_USER_INFOS = "/getUserInfos";

    //exchange manager
    public static final String GET_EXCHANGE_INFOS = "/getExchangeInfos";



}

