package com.openwebsolutions.propertybricks.Model.ViewPropertyUnderComplex;

import java.util.ArrayList;

public class ViewPropertyUnderComplex {
    private ArrayList<DatumViewProperty> data = null;
    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public ViewPropertyUnderComplex() {
    }

    /**
     *
     * @param message
     * @param data
     * @param success
     */
    public ViewPropertyUnderComplex(ArrayList<DatumViewProperty> data, String message, Boolean success) {
        super();
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public ArrayList<DatumViewProperty> getData() {
        return data;
    }

    public void setData(ArrayList<DatumViewProperty> data) {
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
