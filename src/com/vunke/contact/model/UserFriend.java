package com.vunke.contact.model;

import java.util.Calendar;
import java.util.Date;

public class UserFriend {
    private String friendId;

    private String userName;

    private String friendName;

    private String friendAccount;

    private String friendHead;

    private Date obtainTime;

    private String status;

    private String remark;

    public UserFriend( String userName, String friendName, String friendAccount, String friendHead,  String status, String remark) {
        Calendar calendar = Calendar.getInstance();

        this.userName = userName;
        this.friendName = friendName;
        this.friendAccount = friendAccount;
        this.friendHead = friendHead;
        this.obtainTime = calendar.getTime();
        this.status = status;
        this.remark = remark;
    }

    public UserFriend() {
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId == null ? null : friendId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName == null ? null : friendName.trim();
    }

    public String getFriendAccount() {
        return friendAccount;
    }

    public void setFriendAccount(String friendAccount) {
        this.friendAccount = friendAccount == null ? null : friendAccount.trim();
    }

    public String getFriendHead() {
        return friendHead;
    }

    public void setFriendHead(String friendHead) {
        this.friendHead = friendHead == null ? null : friendHead.trim();
    }

    public Date getObtainTime() {
        return obtainTime;
    }

    public void setObtainTime(Date obtainTime) {
        this.obtainTime = obtainTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}