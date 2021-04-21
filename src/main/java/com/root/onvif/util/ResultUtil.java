package com.root.onvif.util;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class ResultUtil implements Serializable {

    private int code;
    private String msg;
    private Object data;

    protected ResultUtil(int code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    protected ResultUtil(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static JSONObject success() {
        JSONObject json = new JSONObject();
        json.put("code",ResultCode.SUCCESS.getCode());
        json.put("msg",ResultCode.SUCCESS.getMessage());
        return json;
    }

    public static JSONObject success(String msg) {
        JSONObject json = new JSONObject();
        json.put("code",ResultCode.SUCCESS.getCode());
        json.put("msg",msg);
        return json;
    }

    public static JSONObject success(Object data) {
        JSONObject json = new JSONObject();
        json.put("code",ResultCode.SUCCESS.getCode());
        json.put("msg",ResultCode.SUCCESS.getMessage());
        json.put("data",data);
        return json;
    }

    public static JSONObject error() {
        JSONObject json = new JSONObject();
        json.put("code",ResultCode.ERROR.getCode());
        json.put("msg",ResultCode.ERROR.getMessage());
        return json;
    }

    public static JSONObject error(ResultCode resultCode) {
        JSONObject json = new JSONObject();
        json.put("code",resultCode.getCode());
        json.put("msg",resultCode.getMessage());
        return json;
    }
}
