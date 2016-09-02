package com.vunke.shareHome.model;

import java.util.Calendar;
import java.util.Date;

public class UserLoginLog {
    private String loginId;

    private String userName;

    private String userType;

    private Date loginTime;

    private String appVesionName;

    private Date appMotifyTime;

    private String appVesionCode;

    private String userIp;

    public UserLoginLog(String userName, String userType,  String appVesionName, Date appMotifyTime, String appVesionCode, String userIp) {
        Calendar calendar = Calendar.getInstance();
        this.userName = userName;
        this.userType = userType;
        this.loginTime = calendar.getTime();
        this.appVesionName = appVesionName;
        this.appMotifyTime = appMotifyTime;
        this.appVesionCode = appVesionCode;
        this.userIp = userIp;
    }

    public UserLoginLog() {
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId == null ? null : loginId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getAppVesionName() {
        return appVesionName;
    }

    public void setAppVesionName(String appVesionName) {
        this.appVesionName = appVesionName == null ? null : appVesionName.trim();
    }

    public Date getAppMotifyTime() {
        return appMotifyTime;
    }

    public void setAppMotifyTime(Date appMotifyTime) {
        this.appMotifyTime = appMotifyTime;
    }

    public String getAppVesionCode() {
        return appVesionCode;
    }

    public void setAppVesionCode(String appVesionCode) {
        this.appVesionCode = appVesionCode == null ? null : appVesionCode.trim();
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp == null ? null : userIp.trim();
    }
}