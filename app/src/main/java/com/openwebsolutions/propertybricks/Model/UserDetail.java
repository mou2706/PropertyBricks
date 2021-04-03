package com.openwebsolutions.propertybricks.Model;

public class UserDetail {
    private Integer id;
    private String name;
    private String email;
    private String address;
    private Integer comp_gstin;
    private String image;
    private String created_at;
    private String updated_at;
    private String phone;
    private String device_token;

    public UserDetail() {
    }

    public UserDetail(Integer id, String name, String email, String address, Integer compGstin, String image, String createdAt, String updatedAt, String phone,String deviceToken) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.comp_gstin = compGstin;
        this.image = image;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
        this.phone = phone;
        this.device_token = deviceToken;
    }

    public String getDeviceToken() {
        return device_token;
    }

    public void setDeviceToken(String deviceToken) {
        this.device_token = deviceToken;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCompGstin() {
        return comp_gstin;
    }

    public void setCompGstin(Integer compGstin) {
        this.comp_gstin = compGstin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updated_at = updatedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
