package com.openwebsolutions.propertybricks.Model.ComplexDelete;

public class ComplexDelete {

    private String message;
    private Integer success;

    /**
     * No args constructor for use in serialization
     *
     */
    public ComplexDelete() {
    }

    /**
     *
     * @param message
     * @param success
     */
    public ComplexDelete(String message, Integer success) {
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

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

}
