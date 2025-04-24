package com.example.restaurant;

public class User {
    private String name;
    private String gmail;
    private String password;

    public User(String name, String gmail, String password) {
        this.name = name;
        this.gmail = gmail;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getGmail() {
        return gmail;
    }

    public String getPassword() {
        return password;
    }
}
