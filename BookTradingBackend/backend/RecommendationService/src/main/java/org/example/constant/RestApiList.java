package org.example.constant;


public class RestApiList {

    //api version
    public static final String API = "/api";
    public static final String VERSION = "/v1";

    //crud endpoints
    public static final String RECOMMENDATIONS = API + VERSION + "/recommendations";
    public static final String GET_RECOMMENDATIONS = "/getAllRecommendations";
    public static final String GET_FILTERED_RECOMMENDATIONS = "/getFilteredRecommendations";

    //user manager
    public static final String GET_USERS_INTERESTS = "/getUsersInterests";

}
