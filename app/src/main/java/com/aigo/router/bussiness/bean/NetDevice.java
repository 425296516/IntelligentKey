package com.aigo.router.bussiness.bean;

/**
 * Created by zhangcirui on 2017/1/12.
 */

public class NetDevice {

    private String deviceSN;
    private String deviceName;
    private String deviceType;
    private String describe;
    private int isBind;
    private long bindTime;

    public String getDeviceSN() {
        return deviceSN;
    }

    public void setDeviceSN(String deviceSN) {
        this.deviceSN = deviceSN;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getIsBind() {
        return isBind;
    }

    public void setIsBind(int isBind) {
        this.isBind = isBind;
    }

    public long getBindTime() {
        return bindTime;
    }

    public void setBindTime(long bindTime) {
        this.bindTime = bindTime;
    }

    @Override
    public String toString() {
        return "NetDevice{" +
                "deviceSN='" + deviceSN + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", describe='" + describe + '\'' +
                ", isBind=" + isBind +
                ", bindTime=" + bindTime +
                '}';
    }
}
