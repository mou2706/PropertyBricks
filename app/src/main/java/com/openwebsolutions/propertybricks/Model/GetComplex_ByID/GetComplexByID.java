package com.openwebsolutions.propertybricks.Model.GetComplex_ByID;

public class GetComplexByID {
    private ComplexEdit data;
    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public GetComplexByID() {
    }

    /**
     *
     * @param message
     * @param data
     * @param success
     */
    public GetComplexByID(ComplexEdit data, String message, Boolean success) {
        super();
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public ComplexEdit getData() {
        return data;
    }

    public void setData(ComplexEdit data) {
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
