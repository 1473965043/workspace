package com.hq.fiveonejrq.jrq.common.bean;

/**
 * Created by Administrator on 2017/7/19.
 * 网络基础数据类
 * T为形参，从外部传入实体类
 */

public class HttpResult {

    //返回码
    private int error_code;
    //返回内容
    private String reason;
    //数据对象
    private Entity result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Entity getResult() {
        return result;
    }

    public void setResult(Entity result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "error_code=" + error_code +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
