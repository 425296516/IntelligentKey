package com.aigo.router.bussiness.bean;

import java.util.List;

/**
 * Created by zhangcirui on 2017/1/20.
 */

public class Message {


    /**
     * result : {"result":true}
     * messageList : ["儿子！你快回来！","儿子！老子不行了！","warning！warning！warning！"]
     */

    private ResultBean result;
    private List<String> messageList;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
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
}
