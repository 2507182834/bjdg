package com.dagu.utils;

public class AjaxMessageUtil<T> {

    public static final int SUCCESS = 0;
    public static final int FAIL = 1;

    private int status;    //状态码：0 成功； 1： 失败
    private T data;        //信息携带的数据
    private String tips;   //提示信息

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
