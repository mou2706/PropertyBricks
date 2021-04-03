package com.openwebsolutions.propertybricks.Model;

public class All {
    private String email;
    private String password;
    private String device_token;

    /**
     * No args constructor for use in serialization
     *
     */
    public All() {
    }

    /**
     *
     * @param email
     * @param deviceToken
     * @param password
     */
    public All(String email, String password, String deviceToken) {
        super();
        this.email = email;
        this.password = password;
        this.device_token = deviceToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceToken() {
        return device_token;
    }

    public void setDeviceToken(String deviceToken) {
        this.device_token = deviceToken;
    }
}
