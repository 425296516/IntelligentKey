package com.aigo.router.bussiness.bean;

/**
 * Created by zhangcirui on 2017/1/12.
 */

public class NetSceneBean {

    private String sceneId;
    private String triggerSN;
    private String executeSN;
    private String deviceTriggerId;
    private String deviceExecuteId;
    private int isActivate;

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getTriggerSN() {
        return triggerSN;
    }

    public void setTriggerSN(String triggerSN) {
        this.triggerSN = triggerSN;
    }

    public String getExecuteSN() {
        return executeSN;
    }

    public void setExecuteSN(String executeSN) {
        this.executeSN = executeSN;
    }

    public String getDeviceTriggerId() {
        return deviceTriggerId;
    }

    public void setDeviceTriggerId(String deviceTriggerId) {
        this.deviceTriggerId = deviceTriggerId;
    }

    public String getDeviceExecuteId() {
        return deviceExecuteId;
    }

    public void setDeviceExecuteId(String deviceExecuteId) {
        this.deviceExecuteId = deviceExecuteId;
    }

    public int getIsActivate() {
        return isActivate;
    }

    public void setIsActivate(int isActivate) {
        this.isActivate = isActivate;
    }

    @Override
    public String toString() {
        return "NetScene{" +
                "sceneId='" + sceneId + '\'' +
                ", triggerSN='" + triggerSN + '\'' +
                ", executeSN='" + executeSN + '\'' +
                ", deviceTriggerId='" + deviceTriggerId + '\'' +
                ", deviceExecuteId='" + deviceExecuteId + '\'' +
                ", isActivate='" + isActivate + '\'' +
                '}';
    }
}
