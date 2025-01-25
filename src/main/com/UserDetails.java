package main.com;

import main.com.Types.UserType;

import java.util.ArrayList;

public class UserDetails {
    private ArrayList<String> saved_rooms = new ArrayList();

    public static UserType user = new UserType();

    public static boolean logout() {
        user = null;
        return true;
    }



    public static void setUser(UserType new_user) {
        user = new_user;
    }

    public static String getId() {
        return user.get_id();
    }

    public static String getUsername() {
        return user.getUsername();
    }


}
