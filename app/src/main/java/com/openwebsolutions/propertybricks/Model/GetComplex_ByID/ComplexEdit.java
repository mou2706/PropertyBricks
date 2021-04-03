package com.openwebsolutions.propertybricks.Model.GetComplex_ByID;

public class ComplexEdit {
    private Integer id;
    private String complex_name;
    private String complex_des;
    private String image;
    private String complex_location;
    private String latitude;
    private String longitude;
    private String created_at;
    private String updated_at;
    private Object deleted_at;

    /**
     * No args constructor for use in serialization
     *
     */
    public ComplexEdit() {
    }

    /**
     *
     * @param updatedAt
     * @param id
     * @param complexDes
     * @param complexLocation
     * @param createdAt
     * @param deletedAt
     * @param image
     * @param complexName
     * @param longitude
     * @param latitude
     */
    public ComplexEdit(Integer id, String complexName, String complexDes, String image, String complexLocation, String latitude, String longitude, String createdAt, String updatedAt, Object deletedAt) {
        super();
        this.id = id;
        this.complex_name = complexName;
        this.complex_des = complexDes;
        this.image = image;
        this.complex_location = complexLocation;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getComplexName() {
        return complex_name;
    }

    public void setComplexName(String complexName) {
        this.complex_name = complexName;
    }

    public String getComplexDes() {
        return complex_des;
    }

    public void setComplexDes(String complexDes) {
        this.complex_des = complexDes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getComplexLocation() {
        return complex_location;
    }

    public void setComplexLocation(String complexLocation) {
        this.complex_location = complexLocation;
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
