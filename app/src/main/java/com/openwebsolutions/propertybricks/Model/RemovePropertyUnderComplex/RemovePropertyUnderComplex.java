package com.openwebsolutions.propertybricks.Model.RemovePropertyUnderComplex;

public class RemovePropertyUnderComplex {
    private RemoveProperty data;
    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public RemovePropertyUnderComplex() {
    }

    public RemovePropertyUnderComplex(RemoveProperty data, String message, Boolean success) {
        super();
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public RemoveProperty getData() {
        return data;
    }

    public void setData(RemoveProperty data) {
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
