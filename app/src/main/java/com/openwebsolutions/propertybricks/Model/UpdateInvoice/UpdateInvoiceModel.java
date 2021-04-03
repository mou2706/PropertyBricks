package com.openwebsolutions.propertybricks.Model.UpdateInvoice;

import com.openwebsolutions.propertybricks.Model.BillingModel;

import java.util.ArrayList;

public class UpdateInvoiceModel {
    private ArrayList<BillingModel> data ;
    private String message;
    private Boolean success;

    public UpdateInvoiceModel(ArrayList<BillingModel> data, String message, Boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public ArrayList<BillingModel> getData() {
        return data;
    }

    public void setData(ArrayList<BillingModel> data) {
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
