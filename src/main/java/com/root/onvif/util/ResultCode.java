package com.root.onvif.util;

public enum ResultCode {

    /* 成功状态码*/
    SUCCESS(1,"成功"),

    /* 失败状态码*/
    ERROR(2,"失败"),

    GET_LOCAL_IP_SEGMENT_FAILED(3,"本地ip段获取失败"),
    NO_DEVICE_FOUND(4,"没有搜索到设备"),
    NO_SUCE_IP_SEGMENT(5,"没有该ip段");

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
