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

    //auth endpoints
    public static final String AUTH = API + VERSION + "/auth";
    public static final String LOGIN = "/login";
    public static final String REGISTER= "/register";
    public static final String CHANGE_PASSWORD = "/changePass";
    public static final String FORGOT_PASSWORD = "/forgotPass";

}
