package com.openwebsolutions.propertybricks.Model;

public class AddTenantModel {

    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public AddTenantModel() {
    }

    /**
     *
     * @param message
     * @param success
     */
    public AddTenantModel(String message, Boolean success) {
        super();
        this.message = message;
        this.success = success;
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
