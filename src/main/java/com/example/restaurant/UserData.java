package com.example.restaurant;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private static List<User> users = new ArrayList<>();

    public static void addUser(User user) {
        users.add(user);
    }

    public static boolean userExists(String gmail) {
        return users.stream().anyMatch(u -> u.getGmail().equalsIgnoreCase(gmail));
    }

    public static boolean validateLogin(String gmail, String password) {
        return users.stream().anyMatch(u ->
                u.getGmail().equalsIgnoreCase(gmail) && u.getPassword().equals(password));
    }

    public static User getUserByGmail(String gmail) {
        for (User user : users) {
            if (user.getGmail().equals(gmail)) {
                return user;
            }
        }
        return null;
    }
}
