package com.openwebsolutions.propertybricks.Model.PropertyAddedINComplex;

public class PropertyAddedINComplex {
    private PropertyAdd data;
    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public PropertyAddedINComplex() {
    }

    /**
     *
     * @param message
     * @param data
     * @param success
     */
    public PropertyAddedINComplex(PropertyAdd data, String message, Boolean success) {
        super();
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public PropertyAdd getData() {
        return data;
    }

    public void setData(PropertyAdd data) {
        this.data = data;
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
}
