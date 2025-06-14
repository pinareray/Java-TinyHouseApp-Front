package core.session;

import entites.dtos.UserDto;

public class UserSession {
    public static UserDto currentUser;

    public static void clear() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
