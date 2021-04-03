package com.openwebsolutions.propertybricks.Model.Tenants_Details;

public class TenantDetailsPerProductId {

    private Data data;
    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public TenantDetailsPerProductId() {
    }

    /**
     *
     * @param message
     * @param data
     * @param success
     */
    public TenantDetailsPerProductId(Data data, String message, Boolean success) {
        super();
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
