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
    public static final String UPDATE_BOOK_STATUS   = "/updateBook";


    public static final String PROCESS_SALES = "/processSale" ;
}
