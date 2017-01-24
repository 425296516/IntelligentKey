package com.aigo.router.bussiness.bean;

/**
 * Created by zhangcirui on 2017/1/15.
 */

public class NetGetVerifyCode {


    /**
     * result : {"result":true,"reason":"验证码发送成功。"}
     * identifier : Ut2511
     */

    private ResultBean result;
    private String identifier;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public static class ResultBean {
        /**
         * result : true
         * reason : 验证码发送成功。
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
