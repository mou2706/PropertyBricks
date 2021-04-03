
package com.openwebsolutions.propertybricks.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("comp_gstin")
    @Expose
    private String comp_gstin;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("id")
    @Expose
    private Integer id;

    private String device_token;

    /**
     * No args constructor for use in serialization
     * 
     */
    public User() {
    }

    /**
     * 
     * @param id
     * @param updatedAt
     * @param phone
     * @param compGstin
     * @param address
     * @param email
     * @param createdAt
     * @param name
     */
    public User(String name, String email, String address, String phone, String compGstin, String updatedAt, String createdAt, Integer id,String image) {
        super();
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.comp_gstin = compGstin;
        this.updated_at = updatedAt;
        this.created_at = createdAt;
        this.id = id;
        this.image = image;

    }



    public User(String name, String email, String address, String phone, String comp_gstin, String image) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.comp_gstin = comp_gstin;
        this.image = image;
    }



    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", comp_gstin='" + comp_gstin + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", image='" + image + '\'' +
                ", id=" + id +
                '}';
    }

    public String getDeviceToken() {
        return device_token;
    }

    public void setDeviceToken(String deviceToken) {
        this.device_token = deviceToken;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompGstin() {
        return comp_gstin;
    }

    public void setCompGstin(String compGstin) {
        this.comp_gstin = compGstin;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updated_at = updatedAt;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
