package com.openwebsolutions.propertybricks.Model.ModelDemo_tenant;

import android.graphics.Bitmap;

public class ModelPersonal {
    String name,phone,email,address,entry_date,payment_cycle;
    Bitmap image;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getPayment_cycle() {
        return payment_cycle;
    }

    public void setPayment_cycle(String payment_cycle) {
        this.payment_cycle = payment_cycle;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
