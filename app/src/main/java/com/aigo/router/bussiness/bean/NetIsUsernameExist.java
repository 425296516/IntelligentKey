package com.aigo.router.bussiness.bean;

/**
 * Created by zhangcirui on 2017/1/15.
 */

public class NetIsUsernameExist {

    private ResultObject result;
    private boolean exist;


    public ResultObject getResult() {
        return result;
    }

    public void setResult(ResultObject result) {
        this.result = result;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

}
