package business.constants;

public class ApiUrls {
    public static final String BASE_URL = "http://localhost:8080/api";

    // User
    public static final String USER_REGISTER = BASE_URL + "/users/register";
    public static final String USER_LOGIN = BASE_URL + "/users/login";
    public static final String USER_GET_ALL = BASE_URL + "/users/getall";
    public static final String USER_GET_BY_ID = BASE_URL + "/users/getbyid";
    public static final String USER_DELETE_BY_ID = BASE_URL + "/users/deletebyid";
    public static final String USER_UPDATE = BASE_URL + "/users/update";
    public static final String USER_ADD = BASE_URL + "/users/add";

    // House
    public static final String HOUSE_GET_ALL = BASE_URL + "/houses/getall";
    public static final String HOUSE_GET_BY_ID = BASE_URL + "/houses/getbyid";
    public static final String HOUSE_ADD = BASE_URL + "/houses/add";
    public static final String HOUSE_UPDATE = BASE_URL + "/houses/update";
    public static final String HOUSE_DELETE = BASE_URL + "/houses/deletebyid";
    public static final String HOUSE_GET_BY_HOST_ID = BASE_URL + "/houses/getbyhostid";
    public static final String HOUSE_GET_ACTIVE = BASE_URL + "/houses/getActiveHouses";

    // Reservation
    public static final String RESERVATION_BASE = BASE_URL + "/reservations";
    public static final String RESERVATION_ADD = RESERVATION_BASE;
    public static final String RESERVATION_UPDATE = RESERVATION_BASE;
    public static final String RESERVATION_DELETE = RESERVATION_BASE + "/{id}";
    public static final String RESERVATION_GET_BY_ID = RESERVATION_BASE + "/{id}";
    public static final String RESERVATION_GET_ALL = RESERVATION_BASE;
    public static final String RESERVATION_GET_BY_USER = RESERVATION_BASE + "/user";
    public static final String RESERVATION_GET_BY_USER_ID = RESERVATION_BASE + "/user/{userId}";
    public static final String RESERVATION_GET_BY_HOUSE_ID = RESERVATION_BASE + "/house/{houseId}";
    public static final String RESERVATION_GET_BY_HOUSE = RESERVATION_BASE + "/house/{houseId}";

    // Comment
    public static final String COMMENT_ADD = BASE_URL + "/comments/add";
    public static final String COMMENT_UPDATE = BASE_URL + "/comments/update";
    public static final String COMMENT_DELETE = BASE_URL + "/comments/delete";
    public static final String COMMENT_GET_BY_HOUSE_ID = BASE_URL + "/comments/getByHouseId";
    public static final String COMMENT_GET_ALL = BASE_URL + "/comments/getAll";

    // Payment
    public static final String PAYMENT_ADD = BASE_URL + "/payments";
    public static final String PAYMENT_DELETE = BASE_URL + "/payments";
    public static final String PAYMENT_GET_BY_ID = BASE_URL + "/payments";
    public static final String PAYMENT_GET_ALL = BASE_URL + "/payments";
    public static final String PAYMENT_GET_BY_USER_ID = BASE_URL + "/payments/user";
    public static final String PAYMENT_GET_BY_RESERVATION_ID = BASE_URL + "/payments/reservation";
    public static final String PAYMENT_GET_MONTHLY_INCOME_BY_HOST_ID = BASE_URL + "/payments/monthly-income";

    // Admin
    public static final String ADMIN_GET_SUMMARY = BASE_URL + "/admin/summary";
    public static final String ADMIN_GET_STATISTICS = BASE_URL + "/admin/statistics";

}
