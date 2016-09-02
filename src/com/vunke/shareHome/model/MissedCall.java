package com.vunke.shareHome.model;

public class MissedCall {
    private String id;

    private String callingPhone;

    private String callingName;

    private String calledPhone;

    private String calledName;

    private String callTime;

    private String callingIp;

    private String callingType;

    private String calledType;

    private Object createTime;

    public MissedCall(String id, String callingPhone, String callingName, String calledPhone, String calledName, String callTime, String callingIp, String callingType, String calledType) {
        this.id = id;
        this.callingPhone = callingPhone;
        this.callingName = callingName;
        this.calledPhone = calledPhone;
        this.calledName = calledName;
        this.callTime = callTime;
        this.callingIp = callingIp;
        this.callingType = callingType;
        this.calledType = calledType;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCallingPhone() {
        return callingPhone;
    }

    public void setCallingPhone(String callingPhone) {
        this.callingPhone = callingPhone == null ? null : callingPhone.trim();
    }

    public String getCallingName() {
        return callingName;
    }

    public void setCallingName(String callingName) {
        this.callingName = callingName == null ? null : callingName.trim();
    }

    public String getCalledPhone() {
        return calledPhone;
    }

    public void setCalledPhone(String calledPhone) {
        this.calledPhone = calledPhone == null ? null : calledPhone.trim();
    }

    public String getCalledName() {
        return calledName;
    }

    public void setCalledName(String calledName) {
        this.calledName = calledName == null ? null : calledName.trim();
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime == null ? null : callTime.trim();
    }

    public String getCallingIp() {
        return callingIp;
    }

    public void setCallingIp(String callingIp) {
        this.callingIp = callingIp == null ? null : callingIp.trim();
    }

    public String getCallingType() {
        return callingType;
    }

    public void setCallingType(String callingType) {
        this.callingType = callingType == null ? null : callingType.trim();
    }

    public String getCalledType() {
        return calledType;
    }

    public void setCalledType(String calledType) {
        this.calledType = calledType == null ? null : calledType.trim();
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }


    @Override
    public String toString() {
        return "MissedCall{" +
                "id='" + id + '\'' +
                ", callingPhone='" + callingPhone + '\'' +
                ", callingName='" + callingName + '\'' +
                ", calledPhone='" + calledPhone + '\'' +
                ", calledName='" + calledName + '\'' +
                ", callTime='" + callTime + '\'' +
                ", callingIp='" + callingIp + '\'' +
                ", callingType='" + callingType + '\'' +
                ", calledType='" + calledType + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}