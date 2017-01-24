package com.aigo.router.bussiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangcirui on 2017/1/12.
 */

public class NetDeviceType {


    /**
     * result : {"result":true}
     * typeList : [{"id":"1","typeName":"按键","typeIcon":null,"type":"1"}]
     */

    private ResultBean result;
    private List<TypeListBean> typeList;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public List<TypeListBean> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<TypeListBean> typeList) {
        this.typeList = typeList;
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

    @Override
    public String toString() {
        return "NetDeviceType{" +
                "result=" + result +
                ", typeList=" + typeList +
                '}';
    }

    public static class TypeListBean implements Serializable{
        /**
         * id : 1
         * typeName : 按键
         * typeIcon : null
         * type : 1
         */

        private String deviceTypeId;
        private String typeName;
        private String typeIcon;
        private String type;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "TypeListBean{" +
                    "deviceTypeId='" + deviceTypeId + '\'' +
                    ", typeName='" + typeName + '\'' +
                    ", typeIcon='" + typeIcon + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }



    }
}
