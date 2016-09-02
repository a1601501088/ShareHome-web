package com.vunke.contact.model;

/**
 * Created by Administrator on 2016/5/13.
 */
public class ShareContact {
    private String account;
    private String name;

    public ShareContact() {
    }

    public ShareContact(String account, String name) {
        this.account = account;
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
