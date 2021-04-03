package com.openwebsolutions.propertybricks.Model.PropertyEditBy_Id;

public class GetPropertyEdit {

    private Data_Property data;
    private String message;

    /**
     * No args constructor for use in serialization
     *
     */
    public GetPropertyEdit() {
    }

    /**
     *
     * @param message
     * @param data
     */
    public GetPropertyEdit(Data_Property data, String message) {
        super();
        this.data = data;
        this.message = message;
    }

    public Data_Property getData() {
        return data;
    }

    public void setData(Data_Property data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
