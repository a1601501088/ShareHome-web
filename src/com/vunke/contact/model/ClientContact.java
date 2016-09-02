package com.vunke.contact.model;

/**
 * Created by Administrator on 2016/5/13.
 */
public class ClientContact {
    private String name = "";
    private  String phone = "";

    public ClientContact() {
    }

    public ClientContact(String name, String phone) {
        this.name = name;
        this.phone = phone;
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
}
