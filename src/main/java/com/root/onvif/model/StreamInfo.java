package com.root.onvif.model;

import com.root.onvif.rtsp.RtspState;

public class StreamInfo {
    private RtspState state;
    private byte[] headers;
    private byte[] outData;
    private String url;

    public RtspState getState() {
        return state;
    }

    public void setState(RtspState state) {
        this.state = state;
    }

    public byte[] getHeaders() {
        return headers;
    }

    public void setHeaders(byte[] headers) {
        this.headers = headers;
    }

    public byte[] getOutData() {
        return outData;
    }

    public void setOutData(byte[] outData) {
        this.outData = outData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
