package com.openwebsolutions.propertybricks.Model.GetComplexModel;

import java.util.ArrayList;

public class Datum_Complex {
    private Integer id;
    private String complex_name;
    private String complex_des;
    private String image;
    private String complex_location;
    private String created_at;
    private String updated_at;
    private Object deleted_at;
    private ArrayList<PropertyDetails> property = null;


    /**
     * No args constructor for use in serialization
     *
     */
    public Datum_Complex() {
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
     */
    public Datum_Complex(Integer id, String complexName, String complexDes, String image, String complexLocation, String createdAt, String updatedAt, Object deletedAt) {
        super();
        this.id = id;
        this.complex_name = complexName;
        this.complex_des = complexDes;
        this.image = image;
        this.complex_location = complexLocation;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
        this.deleted_at = deletedAt;
    }

    public ArrayList<PropertyDetails> getProperty() {
        return property;
    }

    public void setProperty(ArrayList<PropertyDetails> property) {
        this.property = property;
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
