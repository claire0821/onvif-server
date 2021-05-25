package com.root.onvif.model;

import io.netty.channel.ChannelHandlerContext;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class ResponseInfo {
    public static final int OPEN = 1;
    public static final int RUN = 2;
    public static final int CLOSE = 3;

    public static final int HTTP = 1;
    public static final int WS = 2;
//    public String key;
    private String url;
    private int state;
//    private HttpServletResponse response;
    private ChannelHandlerContext response;
//    private ChannelHandlerContext wsRes;
    private boolean sendHeader;//是否已发送头
    private int isHttp;
    private Date lastTime;//最后更新时间
//    public String getKey() {
//        return key;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

//    public HttpServletResponse getResponse() {
//        return response;
//    }
//
//    public void setResponse(HttpServletResponse response) {
//        this.response = response;
//    }

    public ChannelHandlerContext getResponse() {
        return response;
    }

    public void setResponse(ChannelHandlerContext response) {
        this.response = response;
    }

//    public ChannelHandlerContext getWsRes() {
//        return wsRes;
//    }
//
//    public void setWsRes(ChannelHandlerContext wsRes) {
//        this.wsRes = wsRes;
//    }

    public boolean isSendHeader() {
        return sendHeader;
    }

    public void setSendHeader(boolean sendHeader) {
        this.sendHeader = sendHeader;
    }

    public int getIsHttp() {
        return isHttp;
    }

    public void setIsHttp(int isHttp) {
        this.isHttp = isHttp;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
}
