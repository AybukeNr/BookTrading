package org.example.constant;


public class RestApiList {

    //api version
    public static final String API = "/api";
    public static final String VERSION = "/v1";

    //crud endpoints
    public static final String BOOKS = API + VERSION + "/books";
    public static final String GET_ALL_BOOKS = "/getAllBooks";
    public static final String GET_BOOKS_BY_OWNER_ID = "/getBookByOwnerId";
    public static final String GET_BOOK_BY_ID = "/getBookById";
    public static final String CREATE_BOOK = "/createBook";
    public static final String UPDATE_BOOK_STATUS   = "/updateBookStat";
    public static final String UPDATE_BOOK = "/updateBook";
    public static final String DELETE_BOOK = "/deleteBook";
    public static final String CREATE_MANY_BOOK = "/createManyBooks";
    public static final String CREATE_LISTS = "/createLists";
    public static final String GET_BOOK_CONDITION = "/{bookId}/condition";
    public static final String GET_OFFER_BOOK_BY_ID ="/getOfferBookById";
    public static final String GET_ENABLED_BOOKS = "/getEnabledBooks";

    public static final String GET_USER_RESPONSE_BY_ID = "/getUserResponseById";
}
