package com.openwebsolutions.propertybricks.Model.PropertyEditBy_Id;

public class Data_Property {
    private Integer id;
    private String property_name;
    private String property_location;
    private String property_des;
    private String property_price;
    private String image;
    private String created_at;
    private String updated_at;
    private Object deleted_at;
    private PropertyTenant_Edit tenant;

    public Data_Property() {
    }


    public Data_Property(Integer id, String propertyName, String propertyLocation, String propertyDes, String propertyPrice, String image, String createdAt, String updatedAt, Object deletedAt,PropertyTenant_Edit tenant) {
        super();
        this.id = id;
        this.property_name = propertyName;
        this.property_location = propertyLocation;
        this.property_des = propertyDes;
        this.property_price = propertyPrice;
        this.image = image;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
        this.deleted_at = deletedAt;
        this.tenant = tenant;
    }
    public PropertyTenant_Edit getTenant() {
        return tenant;
    }

    public void setTenant(PropertyTenant_Edit tenant) {
        this.tenant = tenant;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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