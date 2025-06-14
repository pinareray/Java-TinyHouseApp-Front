package business.constants;

public class ApiUrls {
    public static final String BASE_URL = "http://localhost:8080/api";
    public static final String USER_REGISTER = BASE_URL + "/users/register";
    public static final String USER_LOGIN = BASE_URL + "/users/login";
    public static final String USER_GET_ALL = BASE_URL + "/users/getall";
    public static final String USER_GET_BY_ID = BASE_URL + "/users/getbyid";
    public static final String USER_DELETE_BY_ID = BASE_URL + "/users/deletebyid";
    public static final String USER_UPDATE = BASE_URL + "/users/update";
    public static final String USER_ADD = BASE_URL + "/users/add";

    public static final String HOUSE_GET_ALL = BASE_URL + "/houses/getall";
    public static final String HOUSE_GET_BY_ID = BASE_URL + "/houses/getbyid";
    public static final String HOUSE_ADD = BASE_URL + "/houses/add";
    public static final String HOUSE_UPDATE = BASE_URL + "/houses/update";
    public static final String HOUSE_DELETE = BASE_URL + "/houses/deletebyid";
    public static final String HOUSE_GET_BY_HOST_ID = BASE_URL + "/houses/getbyhostid";

}
