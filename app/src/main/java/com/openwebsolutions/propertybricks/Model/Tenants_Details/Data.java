package com.openwebsolutions.propertybricks.Model.Tenants_Details;

public class Data {

    private Integer id;
    private Integer property_id;
    private String tenantOwner_name;
    private String tenantOwner_phone;
    private String tenantOwner_email;
    private String tenantOwner_address;
    private String tenantOwner_entrydate;
    private Integer tenantOwner_paymentcycle;
    private String comp_Gstin;
    private String billing_address;
    private String image;
    private String created_at;
    private String updated_at;
    private Object deleted_at;

    public Data() {
    }


    public Data(Integer id, Integer propertyId, String tenantOwnerName, String tenantOwnerPhone, String tenantOwnerEmail, String tenantOwnerAddress, String tenantOwnerEntrydate, Integer tenantOwnerPaymentcycle, String compGstin, String billingAddress, String image, String createdAt, String updatedAt, Object deletedAt) {
        super();
        this.id = id;
        this.property_id = propertyId;
        this.tenantOwner_name = tenantOwnerName;
        this.tenantOwner_phone = tenantOwnerPhone;
        this.tenantOwner_email = tenantOwnerEmail;
        this.tenantOwner_address = tenantOwnerAddress;
        this.tenantOwner_entrydate = tenantOwnerEntrydate;
        this.tenantOwner_paymentcycle = tenantOwnerPaymentcycle;
        this.comp_Gstin = compGstin;
        this.billing_address = billingAddress;
        this.image = image;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
        this.deleted_at = deletedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPropertyId() {
        return property_id;
    }

    public void setPropertyId(Integer propertyId) {
        this.property_id = propertyId;
    }

    public String getTenantOwnerName() {
        return tenantOwner_name;
    }

    public void setTenantOwnerName(String tenantOwnerName) {
        this.tenantOwner_name = tenantOwnerName;
    }

    public String getTenantOwnerPhone() {
        return tenantOwner_phone;
    }

    public void setTenantOwnerPhone(String tenantOwnerPhone) {
        this.tenantOwner_phone = tenantOwnerPhone;
    }

    public String getTenantOwnerEmail() {
        return tenantOwner_email;
    }

    public void setTenantOwnerEmail(String tenantOwnerEmail) {
        this.tenantOwner_email = tenantOwnerEmail;
    }

    public String getTenantOwnerAddress() {
        return tenantOwner_address;
    }

    public void setTenantOwnerAddress(String tenantOwnerAddress) {
        this.tenantOwner_address = tenantOwnerAddress;
    }

    public String getTenantOwnerEntrydate() {
        return tenantOwner_entrydate;
    }

    public void setTenantOwnerEntrydate(String tenantOwnerEntrydate) {
        this.tenantOwner_entrydate = tenantOwnerEntrydate;
    }

    public Integer getTenantOwnerPaymentcycle() {
        return tenantOwner_paymentcycle;
    }

    public void setTenantOwnerPaymentcycle(Integer tenantOwnerPaymentcycle) {
        this.tenantOwner_paymentcycle = tenantOwnerPaymentcycle;
    }

    public String getCompGstin() {
        return comp_Gstin;
    }

    public void setCompGstin(String compGstin) {
        this.comp_Gstin = compGstin;
    }

    public String getBillingAddress() {
        return billing_address;
    }

    public void setBillingAddress(String billingAddress) {
        this.billing_address = billingAddress;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updated_at = updatedAt;
    }

    public Object getDeletedAt() {
        return deleted_at;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deleted_at = deletedAt;
    }

}
