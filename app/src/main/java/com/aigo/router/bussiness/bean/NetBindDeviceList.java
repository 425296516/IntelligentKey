package com.aigo.router.bussiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangcirui on 2017/1/16.
 */

public class NetBindDeviceList implements Serializable{


    /**
     * result : {"result":true}
     * deviceList : [{"deviceTypeId":"1","deviceSN":"1234261","deviceName":"智能按键524","notes_name":"客厅智能按键652"},{"deviceTypeId":"1","deviceSN":"1234371","deviceName":"智能按键98","notes_name":"客厅智能按键726"},{"deviceTypeId":"1","deviceSN":"123476","deviceName":"智能按键480","notes_name":"客厅智能按键560"},{"deviceTypeId":"1","deviceSN":"1234760","deviceName":"智能按键428","notes_name":"客厅智能按键791"},{"deviceTypeId":"1","deviceSN":"1234126","deviceName":"智能按键714","notes_name":"客厅智能按键769"},{"deviceTypeId":"1","deviceSN":"123413","deviceName":"智能按键40","notes_name":"客厅智能按键167"}]
     */

    private ResultBean result;
    private List<DeviceListBean> deviceList;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public List<DeviceListBean> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceListBean> deviceList) {
        this.deviceList = deviceList;
    }

    public static class ResultBean implements Serializable {
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

    public static class DeviceListBean implements Serializable{
        /**
         * deviceTypeId : 1
         * deviceSN : 1234261
         * deviceName : 智能按键524
         * notes_name : 客厅智能按键652
         */

        private String deviceTypeId;
        private String deviceSN;
        private String deviceName;
        private String notes_name;

        public String getDeviceTypeId() {
            return deviceTypeId;
        }

        public void setDeviceTypeId(String deviceTypeId) {
            this.deviceTypeId = deviceTypeId;
        }

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

        public String getNotes_name() {
            return notes_name;
        }

        public void setNotes_name(String notes_name) {
            this.notes_name = notes_name;
        }
    }
}
