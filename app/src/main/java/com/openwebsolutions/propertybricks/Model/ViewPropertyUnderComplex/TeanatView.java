package com.openwebsolutions.propertybricks.Model.ViewPropertyUnderComplex;

public class TeanatView {
    private Integer id;
    private Integer property_id;
    private Integer user_id;
    private String tenantOwner_name;
    private String tenantOwner_phone;
    private String tenantOwner_email;
    private String tenantOwner_address;
    private String tenantOwner_entrydate;
    private Integer tenantOwner_paymentcycle;
    private Object price;
    private String comp_Gstin;
    private String billing_address;
    private String is_present;
    private String image;
    private String created_at;
    private String updated_at;
    private Object deleted_at;

    public TeanatView() {
    }

    public TeanatView(Integer id, Integer propertyId, Integer userId, String tenantOwnerName, String tenantOwnerPhone, String tenantOwnerEmail, String tenantOwnerAddress, String tenantOwnerEntrydate, Integer tenantOwnerPaymentcycle, Object price, String compGstin, String billingAddress, String isPresent, String image, String createdAt, String updatedAt, Object deletedAt) {
        super();
        this.id = id;
        this.property_id = propertyId;
        this.user_id = userId;
        this.tenantOwner_name = tenantOwnerName;
        this.tenantOwner_phone = tenantOwnerPhone;
        this.tenantOwner_email = tenantOwnerEmail;
        this.tenantOwner_address = tenantOwnerAddress;
        this.tenantOwner_entrydate = tenantOwnerEntrydate;
        this.tenantOwner_paymentcycle = tenantOwnerPaymentcycle;
        this.price = price;
        this.comp_Gstin = compGstin;
        this.billing_address = billingAddress;
        this.is_present = isPresent;
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

    public Integer getUserId() {
        return user_id;
    }

    public void setUserId(Integer userId) {
        this.user_id = userId;
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

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
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

    public String getIsPresent() {
        return is_present;
    }

    public void setIsPresent(String isPresent) {
        this.is_present = isPresent;
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
