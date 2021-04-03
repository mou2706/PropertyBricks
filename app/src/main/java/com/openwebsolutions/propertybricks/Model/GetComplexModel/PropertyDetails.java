package com.openwebsolutions.propertybricks.Model.GetComplexModel;

public class PropertyDetails {
    private Integer id;
    private Integer user_id;
    private Integer complex_id;
    private String property_name;
    private String property_location;
    private String latitude;
    private String longitude;
    private String property_des;
    private String property_price;
    private String image;
    private String created_at;
    private String updated_at;
    private Object deleted_at;

    public PropertyDetails() {
    }

    public PropertyDetails(Integer id, Integer userId, Integer complexId, String propertyName, String propertyLocation, String latitude, String longitude, String propertyDes, String propertyPrice, String image, String createdAt, String updatedAt, Object deletedAt) {
        super();
        this.id = id;
        this.user_id = userId;
        this.complex_id = complexId;
        this.property_name = propertyName;
        this.property_location = propertyLocation;
        this.latitude = latitude;
        this.longitude = longitude;
        this.property_des = propertyDes;
        this.property_price = propertyPrice;
        this.image = image;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
        this.deleted_at = deletedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer userId) {
        this.user_id = userId;
    }

    public Integer getComplexId() {
        return complex_id;
    }

    public void setComplexId(Integer complexId) {
        this.complex_id = complexId;
    }

    public String getPropertyName() {
        return property_name;
    }

    public void setPropertyName(String propertyName) {
        this.property_name = propertyName;
    }

    public String getPropertyLocation() {
        return property_location;
    }

    public void setPropertyLocation(String propertyLocation) {
        this.property_location = propertyLocation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPropertyDes() {
        return property_des;
    }

    public void setPropertyDes(String propertyDes) {
        this.property_des = propertyDes;
    }

    public String getPropertyPrice() {
        return property_price;
    }

    public void setPropertyPrice(String propertyPrice) {
        this.property_price = propertyPrice;
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

    public Object getDeletedAt() {
        return deleted_at;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deleted_at = deletedAt;
    }
}
