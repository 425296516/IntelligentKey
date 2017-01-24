package com.aigo.router.bussiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangcirui on 2017/1/12.
 */

public class NetScene implements Serializable{


    /**
     * result : {"result":true}
     * sceneList : [{"sceneId":"1","isActivate":"1","trigger_deviceSN":"12345678","trigger_deviceName":"","trigger_describe":"我的按键111","execute_deviceSN":"null","execute_deviceName":"","execute_describe":"智能按键111","triggerId":"1","triggerName":"按下","executeId":"3","executeName":"关灯","link_id":"","link_name":null,"link_no":null,"message":"16908299"},{"sceneId":"2","isActivate":"1","trigger_deviceSN":"12345678","trigger_deviceName":"","trigger_describe":"我的按键111","execute_deviceSN":"null","execute_deviceName":"","execute_describe":"智能按键111","triggerId":"1","triggerName":"按下","executeId":"2","executeName":"开灯","link_id":"","link_name":null,"link_no":null,"message":"16908299"},{"sceneId":"3","isActivate":"1","trigger_deviceSN":"12345678","trigger_deviceName":"","trigger_describe":"我的按键111","execute_deviceSN":"null","execute_deviceName":"","execute_describe":"智能按键111","triggerId":"1","triggerName":"按下","executeId":"2","executeName":"开灯","link_id":"","link_name":null,"link_no":null,"message":"16908299"},{"sceneId":"4","isActivate":"1","trigger_deviceSN":"12345678","trigger_deviceName":"","trigger_describe":"我的按键111","execute_deviceSN":"null","execute_deviceName":"","execute_describe":"智能按键111","triggerId":"1","triggerName":"按下","executeId":"2","executeName":"开灯","link_id":"","link_name":null,"link_no":null,"message":"16908299"},{"sceneId":"5","isActivate":"1","trigger_deviceSN":"12345678","trigger_deviceName":"","trigger_describe":"我的按键111","execute_deviceSN":"null","execute_deviceName":"","execute_describe":"智能按键111","triggerId":"1","triggerName":"按下","executeId":"2","executeName":"开灯","link_id":"","link_name":null,"link_no":null,"message":"16908299"}]
     */

    private ResultBean result;
    private List<SceneListBean> sceneList;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public List<SceneListBean> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<SceneListBean> sceneList) {
        this.sceneList = sceneList;
    }

    public static class ResultBean implements Serializable{
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

    public static class SceneListBean implements Serializable{
        /**
         * sceneId : 1
         * isActivate : 1
         * trigger_deviceSN : 12345678
         * trigger_deviceName :
         * trigger_describe : 我的按键111
         * execute_deviceSN : null
         * execute_deviceName :
         * execute_describe : 智能按键111
         * triggerId : 1
         * triggerName : 按下
         * executeId : 3
         * executeName : 关灯
         * link_id :
         * link_name : null
         * link_no : null
         * message : 16908299
         */

        private String sceneId;
        private String isActivate;
        private String isPush;
        private String trigger_deviceSN;
        private String trigger_deviceName;
        private String trigger_describe;
        private String execute_deviceSN;
        private String execute_deviceName;
        private String execute_describe;
        private String triggerId;
        private String triggerName;
        private String executeId;
        private String executeName;
        private String link_id;
        private String link_name;
        private String link_no;
        private String message;

        public String getSceneId() {
            return sceneId;
        }

        public void setSceneId(String sceneId) {
            this.sceneId = sceneId;
        }

        public String getIsPush() {
            return isActivate;
        }

        public void setIsPush(String isPush) {
            this.isPush = isPush;
        }

        public String getIsActivate() {
            return isActivate;
        }

        public void setIsActivate(String isActivate) {
            this.isActivate = isActivate;
        }

        public String getTrigger_deviceSN() {
            return trigger_deviceSN;
        }

        public void setTrigger_deviceSN(String trigger_deviceSN) {
            this.trigger_deviceSN = trigger_deviceSN;
        }

        public String getTrigger_deviceName() {
            return trigger_deviceName;
        }

        public void setTrigger_deviceName(String trigger_deviceName) {
            this.trigger_deviceName = trigger_deviceName;
        }

        public String getTrigger_describe() {
            return trigger_describe;
        }

        public void setTrigger_describe(String trigger_describe) {
            this.trigger_describe = trigger_describe;
        }

        public String getExecute_deviceSN() {
            return execute_deviceSN;
        }

        public void setExecute_deviceSN(String execute_deviceSN) {
            this.execute_deviceSN = execute_deviceSN;
        }

        public String getExecute_deviceName() {
            return execute_deviceName;
        }

        public void setExecute_deviceName(String execute_deviceName) {
            this.execute_deviceName = execute_deviceName;
        }

        public String getExecute_describe() {
            return execute_describe;
        }

        public void setExecute_describe(String execute_describe) {
            this.execute_describe = execute_describe;
        }

        public String getTriggerId() {
            return triggerId;
        }

        public void setTriggerId(String triggerId) {
            this.triggerId = triggerId;
        }

        public String getTriggerName() {
            return triggerName;
        }

        public void setTriggerName(String triggerName) {
            this.triggerName = triggerName;
        }

        public String getExecuteId() {
            return executeId;
        }

        public void setExecuteId(String executeId) {
            this.executeId = executeId;
        }

        public String getExecuteName() {
            return executeName;
        }

        public void setExecuteName(String executeName) {
            this.executeName = executeName;
        }

        public String getLink_id() {
            return link_id;
        }

        public void setLink_id(String link_id) {
            this.link_id = link_id;
        }

        public Object getLink_name() {
            return link_name;
        }

        public void setLink_name(String link_name) {
            this.link_name = link_name;
        }

        public Object getLink_no() {
            return link_no;
        }

        public void setLink_no(String link_no) {
            this.link_no = link_no;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
