package com.aigo.router.bussiness.bean;

import java.util.List;

/**
 * Created by zhangcirui on 2017/1/12.
 */

public class NetDeviceList {


    /**
     * result : {"result":true}
     * deviceList : [{"deviceTypeId":"1","typeName":"按键","typeIcon":null,"count":4,"device":[{"deviceSN":"123456","deviceName":"智能按键","remarks":"客厅智能按键"},{"deviceSN":"1234619","deviceName":"智能按键271","remarks":"客厅智能按键71"},{"deviceSN":"1234128","deviceName":"智能按键633","remarks":"客厅智能按键818"},{"deviceSN":"1234616","deviceName":"智能按键0","remarks":"客厅智能按键36"}]},{"deviceTypeId":"2","typeName":"LED灯","typeIcon":null,"count":2,"device":[{"deviceSN":"1234450","deviceName":"智能按键142","remarks":"客厅智能按键88"},{"deviceSN":"1234779","deviceName":"智能按键92","remarks":"客厅智能按键749"}]}]
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

    public static class DeviceListBean {
        /**
         * deviceTypeId : 1
         * typeName : 按键
         * typeIcon : null
         * count : 4
         * device : [{"deviceSN":"123456","deviceName":"智能按键","remarks":"客厅智能按键"},{"deviceSN":"1234619","deviceName":"智能按键271","remarks":"客厅智能按键71"},{"deviceSN":"1234128","deviceName":"智能按键633","remarks":"客厅智能按键818"},{"deviceSN":"1234616","deviceName":"智能按键0","remarks":"客厅智能按键36"}]
         */

        private String deviceTypeId;
        private String typeName;
        private String typeIcon;
        private int count;
        private List<DeviceBean> device;

        public String getDeviceTypeId() {
            return deviceTypeId;
        }

        public void setDeviceTypeId(String deviceTypeId) {
            this.deviceTypeId = deviceTypeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeIcon() {
            return typeIcon;
        }

        public void setTypeIcon(String typeIcon) {
            this.typeIcon = typeIcon;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<DeviceBean> getDevice() {
            return device;
        }

        public void setDevice(List<DeviceBean> device) {
            this.device = device;
        }

        public static class DeviceBean {
            /**
             * deviceSN : 123456
             * deviceName : 智能按键
             * remarks : 客厅智能按键
             */

            private String deviceSN;
            private String deviceName;
            private String remarks;

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

            public String getRemarks() {
                return remarks;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }
        }
    }
}
