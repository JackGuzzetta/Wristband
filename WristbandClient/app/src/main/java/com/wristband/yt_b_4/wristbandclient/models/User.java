package com.wristband.yt_b_4.wristbandclient.models;

/**
 * Created by Mike on 10/5/2017.
 */

public class User {
    private String f_name, l_name, username, password, email;

    public User(String f_name, String l_name, String username, String password, String email) {
        this.f_name = f_name;
        this.l_name = l_name;
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public String getFirstName() {
        return this.f_name;
    }
    public String getLastName() {
        return this.l_name;
    }
    public String getUsername() {
        return this.username;
    }
    public String getEmail() {
        return this.email;
    }
    public String getPassword() {
        return this.password;
    }
}
