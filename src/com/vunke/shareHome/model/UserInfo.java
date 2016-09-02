package com.vunke.shareHome.model;

import java.util.Date;

public class UserInfo {
	/**
	 * 用户ID
	 */
    private String userId;

    /**
     * 用户电话号码
     */
    private String userDn;

    /**
     * 用户密码
     */
    private String userPass;

    /**
     * 用户状态
     */
    private String status;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户手机
     */
    private String userMobile;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 注册IP
     */
    private String userIp;

    /**
     * 邀请码
     */
    private String  requestCode;
    /**
     * 过期时间
     */
    private Date outTime;

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public UserInfo(String userDn, String userPass, String status, String userType, String userMobile, String userIp) {
		this.userDn = userDn;
		this.userPass = userPass;
		this.status = status ;
		this.userType = userType ;
		this.userMobile = userMobile;
		this.createTime = new Date();
		this.userIp = userIp;
	}
    
    public UserInfo() {}

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserDn() {
        return userDn;
    }

    public void setUserDn(String userDn) {
        this.userDn = userDn;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }
}