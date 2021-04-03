
package com.openwebsolutions.propertybricks.Model;


import com.google.gson.annotations.SerializedName;

public class Register {

    @SerializedName("user")
    private User user;
    @SerializedName("token")
    private String token;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Register() {
    }

    /**
     * 
     * @param token
     * @param user
     */
    public Register(User user, String token) {
        super();
        this.user = user;
        this.token = token;
    }

    public Register(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Register{" +
                "user=" + user +
                ", token='" + token + '\'' +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
