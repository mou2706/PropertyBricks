package com.openwebsolutions.propertybricks.Model.DeleteTenantInProperty;

public class DeleteTenantInProperty {

    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public DeleteTenantInProperty() {
    }

    /**
     *
     * @param message
     * @param success
     */
    public DeleteTenantInProperty(String message, Boolean success) {
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
