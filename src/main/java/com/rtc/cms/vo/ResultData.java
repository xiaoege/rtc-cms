package com.rtc.cms.vo;

/**
 * @author ChenHang
 */
public class ResultData<T> {
    private int code;
    private String msg;
    private T data;
    private long count;

    public ResultData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultData(int code, String msg, T data, long count) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }

    public static <T> ResultData success() {
        return new ResultData(200, "success");
    }

    public static <T> ResultData success(T t) {
        return new ResultData(200, "success", t);
    }

    public static <T> ResultData success(T t, long count) {
        return new ResultData(0, "success", t, count);
    }

    public static <T> ResultData fail() {
        return new ResultData(500, "fail");
    }

    public static <T> ResultData fail(T t) {
        return new ResultData(500, "fail", t);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
