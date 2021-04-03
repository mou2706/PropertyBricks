package com.openwebsolutions.propertybricks.Model.DeleteModel;

public class Delete {

    private String message;
    private Boolean Success;

    /**
     * No args constructor for use in serialization
     *
     */
    public Delete() {
    }

    /**
     *
     * @param message
     * @param success
     */
    public Delete(String message, Boolean success) {
        super();
        this.message = message;
        this.Success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return Success;
    }

    public void setSuccess(Boolean success) {
        this.Success = success;
    }

}
