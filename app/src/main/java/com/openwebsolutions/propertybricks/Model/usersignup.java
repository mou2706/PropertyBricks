package com.openwebsolutions.propertybricks.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class usersignup {

    @SerializedName("email")
    @Expose
    private String email;




    @SerializedName("gmail_id")
    @Expose
    private Integer gmail_id;

    private String device_token;

    /**
     * No args constructor for use in serialization
     *
     */
    public usersignup() {
    }

    /**
     *
     * @param gmail_id
     * @param phone
     * @param email
     * @param name
     */
    public usersignup(String name, String email, String phone,  Integer gmail_id,String image) {
        super();
        this.email = email;

        this.gmail_id = gmail_id;

    }



    public usersignup(String name, String email,  String phone,  String image) {
        this.email = email;
    }



    @Override
    public String toString() {
        return "usersignup{" +
                ", email='" + email + '\'' +


                ", gmail_id=" + gmail_id +
                '}';
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGmail_id() {
        return gmail_id;
    }

    public void setGmail_id(Integer gmail_id) {
        this.gmail_id = gmail_id;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}
