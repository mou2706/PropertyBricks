package com.openwebsolutions.propertybricks.Model.GetTenantDetails;

import java.util.ArrayList;

public class GetTenantModel {

    private ArrayList<Datum_Tenant> data = null;
    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public GetTenantModel() {
    }

    /**
     *
     * @param message
     * @param data
     * @param success
     */
    public GetTenantModel(ArrayList<Datum_Tenant> data, String message, Boolean success) {
        super();
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public ArrayList<Datum_Tenant> getData() {
        return data;
    }

    public void setData(ArrayList<Datum_Tenant> data) {
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
