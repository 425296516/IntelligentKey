package com.aigo.router.bussiness.bean;

import java.util.List;

/**
 * Created by zhangcirui on 2017/1/12.
 */

public class NetLinkman {


    /**
     * result : {"result":true}
     * linkmanList : [{"link_id":"1","link_name":"tom","link_no":"111"},{"link_id":"2","link_name":"Jerry","link_no":"222"}]
     */

    private ResultBean result;
    private List<LinkmanListBean> linkmanList;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public List<LinkmanListBean> getLinkmanList() {
        return linkmanList;
    }

    public void setLinkmanList(List<LinkmanListBean> linkmanList) {
        this.linkmanList = linkmanList;
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

    public static class LinkmanListBean {
        /**
         * link_id : 1
         * link_name : tom
         * link_no : 111
         */

        private String link_id;
        private String link_name;
        private String link_no;

        public String getLink_id() {
            return link_id;
        }

        public void setLink_id(String link_id) {
            this.link_id = link_id;
        }

        public String getLink_name() {
            return link_name;
        }

        public void setLink_name(String link_name) {
            this.link_name = link_name;
        }

        public String getLink_no() {
            return link_no;
        }

        public void setLink_no(String link_no) {
            this.link_no = link_no;
        }
    }
}
