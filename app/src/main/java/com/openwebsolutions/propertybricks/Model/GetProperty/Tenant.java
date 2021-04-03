package com.openwebsolutions.propertybricks.Model.GetProperty;

public class Tenant {
    public String id;
    public String property_id;
    public String tenantOwner_name;
    public String tenantOwner_phone;
    public String tenantOwner_email;
    public String tenantOwner_address;
    public String tenantOwner_entrydate;
    public String tenantOwner_paymentcycle;
    public String comp_Gstin;
    public String billing_address;
    public String image;
    public  String created_at;
    public String updated_at;
    public String deleted_at;


    public Tenant(String id, String property_id, String tenantOwner_name, String tenantOwner_phone, String tenantOwner_email, String tenantOwner_address, String tenantOwner_entrydate, String tenantOwner_paymentcycle, String comp_Gstin, String billing_address, String image, String created_at, String updated_at, String deleted_at) {
        this.id = id;
        this.property_id = property_id;
        this.tenantOwner_name = tenantOwner_name;
        this.tenantOwner_phone = tenantOwner_phone;
        this.tenantOwner_email = tenantOwner_email;
        this.tenantOwner_address = tenantOwner_address;
        this.tenantOwner_entrydate = tenantOwner_entrydate;
        this.tenantOwner_paymentcycle = tenantOwner_paymentcycle;
        this.comp_Gstin = comp_Gstin;
        this.billing_address = billing_address;
        this.image = image;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getTenantOwner_name() {
        return tenantOwner_name;
    }

    public void setTenantOwner_name(String tenantOwner_name) {
        this.tenantOwner_name = tenantOwner_name;
    }

    public String getTenantOwner_phone() {
        return tenantOwner_phone;
    }

    public void setTenantOwner_phone(String tenantOwner_phone) {
        this.tenantOwner_phone = tenantOwner_phone;
    }

    public String getTenantOwner_email() {
        return tenantOwner_email;
    }

    public void setTenantOwner_email(String tenantOwner_email) {
        this.tenantOwner_email = tenantOwner_email;
    }

    public String getTenantOwner_address() {
        return tenantOwner_address;
    }

    public void setTenantOwner_address(String tenantOwner_address) {
        this.tenantOwner_address = tenantOwner_address;
    }

    public String getTenantOwner_entrydate() {
        return tenantOwner_entrydate;
    }

    public void setTenantOwner_entrydate(String tenantOwner_entrydate) {
        this.tenantOwner_entrydate = tenantOwner_entrydate;
    }

    public String getTenantOwner_paymentcycle() {
        return tenantOwner_paymentcycle;
    }

    public void setTenantOwner_paymentcycle(String tenantOwner_paymentcycle) {
        this.tenantOwner_paymentcycle = tenantOwner_paymentcycle;
    }

    public String getComp_Gstin() {
        return comp_Gstin;
    }

    public void setComp_Gstin(String comp_Gstin) {
        this.comp_Gstin = comp_Gstin;
    }

    public String getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(String billing_address) {
        this.billing_address = billing_address;
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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
}
