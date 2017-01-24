package com.aigo.router.bussiness.bean;

/**
 * Created by zhangcirui on 2017/1/12.
 */

public class ResultObject {


    /**
     * result : {"result":true,"reason":"错误"}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * result : true
         * reason : 错误
         */

        private boolean result;
        private String reason;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
