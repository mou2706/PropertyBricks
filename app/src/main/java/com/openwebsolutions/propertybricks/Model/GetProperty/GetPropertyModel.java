package com.openwebsolutions.propertybricks.Model.GetProperty;

import java.util.ArrayList;

public class GetPropertyModel {

    private ArrayList<Datum> data = null;
    private String message;
    private Boolean success;
    /**
     * No args constructor for use in serialization
     *
     */
    public GetPropertyModel() {
    }

    /**
     *
     * @param message
     * @param data
     * @param success
     */
    public GetPropertyModel(ArrayList<Datum> data, String message, Boolean success) {
        super();
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
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
