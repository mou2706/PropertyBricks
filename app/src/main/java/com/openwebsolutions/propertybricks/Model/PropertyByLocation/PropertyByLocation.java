package com.openwebsolutions.propertybricks.Model.PropertyByLocation;

import java.util.ArrayList;

public class PropertyByLocation {

    private ArrayList<Property> data = null;
    private String message;
    private Boolean success;

    public PropertyByLocation() {
    }

    public PropertyByLocation(ArrayList<Property> data, String message, Boolean success) {
        super();
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public ArrayList<Property> getData() {
        return data;
    }

    public void setData(ArrayList<Property> data) {
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
