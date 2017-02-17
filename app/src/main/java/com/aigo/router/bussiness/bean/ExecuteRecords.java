package com.aigo.router.bussiness.bean;

import java.util.List;

/**
 * Created by zhangcirui on 2017/1/17.
 */

public class ExecuteRecords
{
    /**
     * result : {"result":true}
     * logList : [{"id":"3","sceneId":"3","username":"ceshi1","triggerSN":"macbbb","executeResult":"执行成功","trigger_device_name":"客厅按键2","executeTime":"2017-01-16 17:16:03","executeInfo":"蜂鸣器警报"},{"id":"2","sceneId":"2","username":"ceshi1","triggerSN":"macaaa","executeResult":"执行成功","trigger_device_name":"客厅按键1","executeTime":"2017-01-16 17:14:56","executeInfo":"发短信给儿子"},{"id":"1","sceneId":"1","username":"ceshi1","triggerSN":"macssss","executeResult":"执行成功","trigger_device_name":"客厅按键1","executeTime":"2017-01-16 17:13:34","executeInfo":"发短信给儿子"}]
     */

    private ResultBean result;
    private List<LogListBean> logList;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public List<LogListBean> getLogList() {
        return logList;
    }

    public void setLogList(List<LogListBean> logList) {
        this.logList = logList;
    }

    public static class ResultBean {
        /**
         * result : true
         */

        private boolean result;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }
    }

    public static class LogListBean {
        /**
         * id : 3
         * sceneId : 3
         * username : ceshi1
         * triggerSN : macbbb
         * executeResult : 执行成功
         * trigger_device_name : 客厅按键2
         * executeTime : 2017-01-16 17:16:03
         * executeInfo : 蜂鸣器警报
         */

        private String id;
        private String sceneId;
        private String username;
        private String triggerSN;
        private String deviceTypeId;
        private String executeResult;
        private String trigger_device_name;
        private String executeTime;
        private String executeInfo;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSceneId() {
            return sceneId;
        }

        public void setSceneId(String sceneId) {
            this.sceneId = sceneId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDeviceTypeId(){
            return deviceTypeId;
        }

        public void setDeviceTypeId(String deviceTypeId){
            this.deviceTypeId = deviceTypeId;
        }

        public String getTriggerSN() {
            return triggerSN;
        }

        public void setTriggerSN(String triggerSN) {
            this.triggerSN = triggerSN;
        }

        public String getExecuteResult() {
            return executeResult;
        }

        public void setExecuteResult(String executeResult) {
            this.executeResult = executeResult;
        }

        public String getTrigger_device_name() {
            return trigger_device_name;
        }

        public void setTrigger_device_name(String trigger_device_name) {
            this.trigger_device_name = trigger_device_name;
        }

        public String getExecuteTime() {
            return executeTime;
        }

        public void setExecuteTime(String executeTime) {
            this.executeTime = executeTime;
        }

        public String getExecuteInfo() {
            return executeInfo;
        }

        public void setExecuteInfo(String executeInfo) {
            this.executeInfo = executeInfo;
        }
    }
}
