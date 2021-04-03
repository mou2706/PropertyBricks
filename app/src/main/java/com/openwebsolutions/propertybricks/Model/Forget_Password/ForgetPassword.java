package com.openwebsolutions.propertybricks.Model.Forget_Password;

public class ForgetPassword {

    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public ForgetPassword() {
    }

    /**
     *
     * @param message
     * @param success
     */
    public ForgetPassword(String message, Boolean success) {
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
