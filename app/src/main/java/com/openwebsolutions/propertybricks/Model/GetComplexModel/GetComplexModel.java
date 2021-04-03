package com.openwebsolutions.propertybricks.Model.GetComplexModel;

import java.util.ArrayList;

public class GetComplexModel {

    private ArrayList<Datum_Complex> data = null;
    private String message;
    private Boolean success;

    /**
     * No args constructor for use in serialization
     *
     */
    public GetComplexModel() {
    }

    /**
     *
     * @param message
     * @param data
     * @param success
     */
    public GetComplexModel(ArrayList<Datum_Complex> data, String message, Boolean success) {
        super();
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public ArrayList<Datum_Complex> getData() {
        return data;
    }

    public void setData(ArrayList<Datum_Complex> data) {
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
