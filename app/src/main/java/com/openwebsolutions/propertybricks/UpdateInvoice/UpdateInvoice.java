package com.openwebsolutions.propertybricks.UpdateInvoice;

import com.openwebsolutions.propertybricks.Model.BillingModel;

import java.util.ArrayList;

public class UpdateInvoice {
    private String message;
    private Boolean success;
    private ArrayList<BillingModel> data ;

    public UpdateInvoice(String message, Boolean success, ArrayList<BillingModel> data) {
        this.message = message;
        this.success = success;
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

    public ArrayList<BillingModel> getData() {
        return data;
    }

    public void setData(ArrayList<BillingModel> data) {
        this.data = data;
    }
}
