package com.openwebsolutions.propertybricks.Model.GetInvoiceModel;

import com.openwebsolutions.propertybricks.Model.BillingModel;
import com.openwebsolutions.propertybricks.Model.GetTenantDetails.Datum_Tenant;
import com.openwebsolutions.propertybricks.Model.User;

import java.util.ArrayList;

public class GetInvoiceModel {
    private ArrayList<BillingModel> data ;
    private String message;
    private Boolean success;
    private User user;


    public GetInvoiceModel(User user,ArrayList<BillingModel> data, String message, Boolean success) {
        super();
        this.data = data;
        this.message = message;
        this.success = success;
        this.user=user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
