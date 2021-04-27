package com.root.onvif.mbg.model;

import java.io.Serializable;

public class TbDev implements Serializable {
    private String ip;

    private Integer port;

    private String username;

    private String password;

    private String onvifAddress;

    private String mediaUrl;

    private String imagingUrl;

    private String eventsUrl;

    private String deviceUrl;

    private String ptzUrl;

    private String analyticsUrl;

    private static final long serialVersionUID = 1L;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOnvifAddress() {
        return onvifAddress;
    }

    public void setOnvifAddress(String onvifAddress) {
        this.onvifAddress = onvifAddress;
    }

    public String getMediaUrl() {
        return mediaUrl == null ? "" : mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl == null ? "" : mediaUrl;
    }

    public String getImagingUrl() {
        return imagingUrl == null ? "" : imagingUrl;
    }

    public void setImagingUrl(String imagingUrl) {
        this.imagingUrl = imagingUrl == null ? "" : imagingUrl;
    }

    public String getEventsUrl() {
        return eventsUrl == null ? "" : eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl == null ? "" : eventsUrl;
    }

    public String getDeviceUrl() {
        return deviceUrl == null ? "" : deviceUrl;
    }

    public void setDeviceUrl(String deviceUrl) {
        this.deviceUrl = deviceUrl == null ? "" : deviceUrl;
    }

    public String getPtzUrl() {
        return ptzUrl == null ? "" : ptzUrl;
    }

    public void setPtzUrl(String ptzUrl) {
        this.ptzUrl = ptzUrl == null ? "" : ptzUrl;
    }

    public String getAnalyticsUrl() {
        return analyticsUrl == null ? "" : analyticsUrl;
    }

    public void setAnalyticsUrl(String analyticsUrl) {
        this.analyticsUrl = analyticsUrl  == null ? "" : analyticsUrl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ip=").append(ip);
        sb.append(", port=").append(port);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", onvifAddress=").append(onvifAddress);
        sb.append(", mediaUrl=").append(mediaUrl);
        sb.append(", imagingUrl=").append(imagingUrl);
        sb.append(", eventsUrl=").append(eventsUrl);
        sb.append(", deviceUrl=").append(deviceUrl);
        sb.append(", ptzUrl=").append(ptzUrl);
        sb.append(", analyticsUrl=").append(analyticsUrl);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}