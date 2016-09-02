package com.vunke.shareHome.model;

import java.util.Date;

public class SysServiceMsg {
    private String msgId;

    private String msgPhone;

    private String msgContent;

    private String msgStutas;

    private Date msgTime;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId == null ? null : msgId.trim();
    }

    public String getMsgPhone() {
        return msgPhone;
    }

    public void setMsgPhone(String msgPhone) {
        this.msgPhone = msgPhone == null ? null : msgPhone.trim();
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent == null ? null : msgContent.trim();
    }

    public String getMsgStutas() {
        return msgStutas;
    }

    public void setMsgStutas(String msgStutas) {
        this.msgStutas = msgStutas == null ? null : msgStutas.trim();
    }

    public Date getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Date msgTime) {
        this.msgTime = msgTime;
    }
}