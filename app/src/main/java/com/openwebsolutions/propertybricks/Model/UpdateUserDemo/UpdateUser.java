package com.openwebsolutions.propertybricks.Model.UpdateUserDemo;

import com.google.gson.annotations.SerializedName;
import com.openwebsolutions.propertybricks.Model.All;
import com.openwebsolutions.propertybricks.Model.User;
import com.openwebsolutions.propertybricks.Model.UserDetail;

public class UpdateUser {
    private String message;
    private Boolean success;
    private User user;

//    private String token;
//    private UserDetail user;
//    private All all;
//
//    @SerializedName("user")
//    private User user;
//    @SerializedName("token")
//    private String token;
//    @SerializedName("device_token")
//    private String device_token;



    /**
     * No args constructor for use in serialization
     *
     */
////    public UpdateUser(String token) {
////        super();
////        this.token=token;
////    }
//    public UpdateUser(User user, String token) {
//        super();
//        this.user = user;
//        this.token = token;
////        this.device_token = device_token;
//    }

//    public UserDetail getUser() {
//        return user;
//    }
//
//    public void setUser(UserDetail user) {
//        this.user = user;
//    }
//
//    public All getAll() {
//        return all;
//    }
//
//    public void setAll(All all) {
//        this.all = all;
//    }

        /**
     *
     * @param message
     * @param success
     */
    public UpdateUser(String message,Boolean success,User user) {
        super();
        this.message = message;
        this.success = success;
        this.user = user;

    }
//    public UpdateUser(User user) {
//        this.user = user;
//    }
//    @Override
//    public String toString() {
//        return "UpdateUser{" +
//                "user=" + user +
//                ", token='" + token + '\'' +
//
//                '}';
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }


//    public UserDetail getUser() {
//        return user;
//    }
//
//    public void setUser(UserDetail user) {
//        this.user = user;
//    }


//
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

//    public String getToken() {
//        return token;
//    }

//    public void setToken(String token) {
//        this.token = token;
//    }

//    public String getDevice_token() {
//        return device_token;
//    }
//
//    public void setDevice_token(String device_token) {
//        this.device_token = device_token;
//    }
}
