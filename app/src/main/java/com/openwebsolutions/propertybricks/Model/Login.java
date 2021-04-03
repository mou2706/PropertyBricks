
package com.openwebsolutions.propertybricks.Model;

public class Login {

    private String token;
    private UserDetail user;
    private All all;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Login() {
    }

    /**
     * 
     * @param token
     */
    public Login(String token) {
        super();
        this.token = token;
    }

    public All getAll() {
        return all;
    }

    public void setAll(All all) {
        this.all = all;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDetail getUser() {
        return user;
    }

    public void setUser(UserDetail user) {
        this.user = user;
    }

}
