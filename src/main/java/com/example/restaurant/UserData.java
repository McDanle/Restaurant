package com.example.restaurant;

import java.util.ArrayList;
import java.util.List;

public class UserData {


    private static final List<User> users = new ArrayList<>();

    public static void addUser(User user) {
        users.add(user);
    }


    public static boolean userExists(String gmail) {
        return users.stream().anyMatch(u -> u.getGmail().equalsIgnoreCase(gmail));
    }

    public static boolean validateLogin(String gmail, String password) {
        return users.stream().anyMatch(u ->
                u.getGmail().equalsIgnoreCase(gmail) &&
                        u.getPassword().equals(password)
        );
    }

    public static User getUserByGmail(String gmail) {
        return users.stream()
                .filter(u -> u.getGmail().equalsIgnoreCase(gmail))
                .findFirst()
                .orElse(null);
    }

    public static boolean updatePassword(String gmail, String newPassword) {
        User user = getUserByGmail(gmail);
        if (user != null && !user.getPassword().equals(newPassword)) {
            user.setPassword(newPassword);
            return true;
        }
        return false; // Either user not found or new password is same as old
    }


    public static List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
