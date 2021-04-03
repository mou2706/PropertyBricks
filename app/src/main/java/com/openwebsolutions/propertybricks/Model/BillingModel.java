package com.openwebsolutions.propertybricks.Model;

import android.widget.TextView;

import com.openwebsolutions.propertybricks.Model.GetProperty.Tenant;

public class BillingModel {
    private Integer id;
    private Integer user_id;
    private Integer tenant_id;


    private String month;
    private String year;
      String payment_status;
     private String image;
    private String created_at;
    private String updated_at;
    private Object deleted_at;

    public BillingModel(Integer id, Integer user_id, Integer tenant_id, String month, String year, String payment_status, String image, String created_at, String updated_at, Object deleted_at) {
        this.id = id;
        this.user_id = user_id;
        this.tenant_id = tenant_id;
        this.month = month;
        this.year = year;
        this.payment_status = payment_status;
        this.image = image;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(Integer tenant_id) {
        this.tenant_id = tenant_id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Object getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Object deleted_at) {
        this.deleted_at = deleted_at;
    }


}
