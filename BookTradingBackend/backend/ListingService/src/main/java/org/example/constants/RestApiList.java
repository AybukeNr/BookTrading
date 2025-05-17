package org.example.constants;


public class RestApiList {

    //api version
    public static final String API = "/api";
    public static final String VERSION = "/v1";

    //crud endpoints
    public static final String LISTS = API + VERSION + "/lists";
    public static final String GET_ALL_LISTS = "/getAllLists";
    public static final String GET_LISTS_BY_OWNER_ID = "/getListsByOwnerId";
    public static final String GET_LIST_BY_ID = "/getListsById";
    public static final String CREATE_LISTS = "/createLists";
    public static final String TAKE_OFFER = "/takeOffer";
    public static final String GET_RECIEVED_OFFERS = "/getRecievedOffers";
    public static final String UPDATE_OFFER = "/updateOffer";
    public static final String UPDATE_LIST_STATUS = "/updateListStat";
    public static final String TAKE_PAYMENT = "/takePayment";
    public static final String CREATE_SHIPPING = "/createShipping";
    public static final String CREATE_TRANSACTION = "/createTransaction";
    public static final String GET_ADDRESSES = "/getAddresses";
   // public static final String UPDATE_BOOK_STATUS   = "/updateBook";
    public static final String UPDATE_BOOK_STATUS   = "/updateBookStat";
    public static final String LIST_MAIL_INFOS = "/getInfos";
    public static final String TEST_SALE_LIST_MAIL = "/test-sale-list-mail";
    public static final String TEST_TRANSACTION_CREATED = "/test-transaction-created";
    public static final String TEST_EXCHANGE_LIST_UPDATE = "/test-exchange-list-update";
    public static final String TEST_ACCEPTED_OFFER = "/test-accepted-offer";
    public static final String GET_LIST_PRICE= "/getListPrice";
    public static final String GET_LIST_BY_ID_OFFER = "/getListForOffer";
    public static final String GET_USER_RESPONSE_BY_ID = "/getUserResponseById";
    public static final String GET_LISTS_EXCLUDING_OWNER = "/getListsExcludingOwner";
    public static final String GET_EXCHANGE_BOOKS = "/getExchangeBooks";
    public static final String GET_LIST_TYPE = "/getListType";
    public static final String GET_LISTS_OWNERID = "/getListsOwnerId";
    public static final String GET_FILTERED_RECOMMENDATIONS = "/getFilteredRecommendations";



    public static final String PROCESS_SALES = "/processSale" ;
}
