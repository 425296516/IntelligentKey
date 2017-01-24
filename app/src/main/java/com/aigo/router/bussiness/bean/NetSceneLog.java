package com.aigo.router.bussiness.bean;

/**
 * Created by zhangcirui on 2017/1/12.
 */

public class NetSceneLog {

    private String sceneLogId;
    private String sceneId;
    private String executeResult;
    private long executeTime;
    private String executeInfo;

    public String getSceneLogId() {
        return sceneLogId;
    }

    public void setSceneLogId(String sceneLogId) {
        this.sceneLogId = sceneLogId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(String executeResult) {
        this.executeResult = executeResult;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    public String getExecuteInfo() {
        return executeInfo;
    }

    public void setExecuteInfo(String executeInfo) {
        this.executeInfo = executeInfo;
    }

    @Override
    public String toString() {
        return "NetSceneLog{" +
                "sceneLogId='" + sceneLogId + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", executeResult='" + executeResult + '\'' +
                ", executeTime=" + executeTime +
                ", executeInfo='" + executeInfo + '\'' +
                '}';
    }
}
