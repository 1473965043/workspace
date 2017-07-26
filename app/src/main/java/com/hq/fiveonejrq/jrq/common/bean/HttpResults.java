package com.hq.fiveonejrq.jrq.common.bean;

import java.util.List;

/**
 * Created by guodong on 2017/7/25.
 */

public class HttpResults<T> {
    //返回码
    private int error_code;
    //返回内容
    private String reason;
    //数据对象
    private List<T> result;

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

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
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
