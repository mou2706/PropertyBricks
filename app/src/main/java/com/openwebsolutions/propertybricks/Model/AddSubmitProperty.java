package com.openwebsolutions.propertybricks.Model;

public class AddSubmitProperty {
    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public AddSubmitProperty() {
    }

    /**
     *
     * @param message
     * @param success
     */
    public AddSubmitProperty(String message, Boolean success) {
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
