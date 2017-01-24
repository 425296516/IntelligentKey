package com.aigo.router.bussiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangcirui on 2017/1/12.
 */

public class NetBehaviour implements Serializable{


    /**
     * result : {"result":true}
     * behaviourList : [{"behaviourId":"5","deviceTypeId":"3","behaviourType":"2","behaviourName":"发送短信","action":"ff","behaviourIcon":null}]
     */

    private ResultBean result;
    private List<BehaviourListBean> behaviourList;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public List<BehaviourListBean> getBehaviourList() {
        return behaviourList;
    }

    public void setBehaviourList(List<BehaviourListBean> behaviourList) {
        this.behaviourList = behaviourList;
    }

    public static class ResultBean  implements Serializable{
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

    public static class BehaviourListBean implements Serializable{
        /**
         * behaviourId : 5
         * deviceTypeId : 3
         * behaviourType : 2
         * behaviourName : 发送短信
         * action : ff
         * behaviourIcon : null
         */

        private String behaviourId;
        private String deviceTypeId;
        private String behaviourType;
        private String behaviourName;
        private String action;
        private Object behaviourIcon;

        public String getBehaviourId() {
            return behaviourId;
        }

        public void setBehaviourId(String behaviourId) {
            this.behaviourId = behaviourId;
        }

        public String getDeviceTypeId() {
            return deviceTypeId;
        }

        public void setDeviceTypeId(String deviceTypeId) {
            this.deviceTypeId = deviceTypeId;
        }

        public String getBehaviourType() {
            return behaviourType;
        }

        public void setBehaviourType(String behaviourType) {
            this.behaviourType = behaviourType;
        }

        public String getBehaviourName() {
            return behaviourName;
        }

        public void setBehaviourName(String behaviourName) {
            this.behaviourName = behaviourName;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Object getBehaviourIcon() {
            return behaviourIcon;
        }

        public void setBehaviourIcon(Object behaviourIcon) {
            this.behaviourIcon = behaviourIcon;
        }
    }
}
