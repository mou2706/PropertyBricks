package com.openwebsolutions.propertybricks.Model;

import com.google.gson.annotations.SerializedName;

public class GmailFacebook {
    @SerializedName("usersignup")
    private usersignup user;
    @SerializedName("token")
    private String token;

    /**
     * No args constructor for use in serialization
     *
     */
    public GmailFacebook() {
    }

    /**
     *
     * @param token
     * @param user
     */
    public GmailFacebook(usersignup user, String token) {
        super();
        this.user = user;
        this.token = token;
    }

    public GmailFacebook(usersignup user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "GmailFacebook{" +
                "usersignup=" + user +
                ", token='" + token + '\'' +
                '}';
    }

    public usersignup getUser() {
        return user;
    }

    public void setUser(usersignup user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
