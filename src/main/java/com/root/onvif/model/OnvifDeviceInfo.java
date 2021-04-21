package com.root.onvif.model;

public class OnvifDeviceInfo {

    private String ip;
    private int port;

    private String onvifAddress;
    private String username;
    private String password;

    private String mediaUrl;
    private String imagingUrl;
    private String eventsUrl;
    private String deviceUrl;
    private String ptzUrl;
    private String analyticsUrl;

    public OnvifDeviceInfo() {
        ip = "";
        port = 0;

        onvifAddress = "";
        username = "";
        password = "";
        mediaUrl = "";
        imagingUrl = "";
        eventsUrl = "";
        deviceUrl = "";
        ptzUrl = "";
        analyticsUrl = "";

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getOnvifAddress() {
        return onvifAddress;
    }

    public void setOnvifAddress(String onvifAddress) {
        this.onvifAddress = onvifAddress;
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

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getImagingUrl() {
        return imagingUrl;
    }

    public void setImagingUrl(String imagingUrl) {
        this.imagingUrl = imagingUrl;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    public String getDeviceUrl() {
        return deviceUrl;
    }

    public void setDeviceUrl(String deviceUrl) {
        this.deviceUrl = deviceUrl;
    }

    public String getPtzUrl() {
        return ptzUrl;
    }

    public void setPtzUrl(String ptzUrl) {
        this.ptzUrl = ptzUrl;
    }

    public String getAnalyticsUrl() {
        return analyticsUrl;
    }

    public void setAnalyticsUrl(String analyticsUrl) {
        this.analyticsUrl = analyticsUrl;
    }

    @Override
    public String toString() {
        return "OnvifDeviceInfo{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", onvifAddress='" + onvifAddress + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", imagingUrl='" + imagingUrl + '\'' +
                ", eventsUrl='" + eventsUrl + '\'' +
                ", deviceUrl='" + deviceUrl + '\'' +
                ", ptzUrl='" + ptzUrl + '\'' +
                ", analyticsUrl='" + analyticsUrl + '\'' +
                '}';
    }

    public boolean compare(OnvifDeviceInfo onvifDeviceInfo) {
        if(!this.ip.equals(onvifDeviceInfo.ip)) return false;
        if(this.port != onvifDeviceInfo.port) return false;
        if(!this.onvifAddress.equals(onvifDeviceInfo.onvifAddress)) return false;
        if(!this.username.equals(onvifDeviceInfo.username)) return false;
        if(!this.password.equals(onvifDeviceInfo.password)) return false;
        if(!this.mediaUrl.equals(onvifDeviceInfo.mediaUrl)) return false;
        if(!this.imagingUrl.equals(onvifDeviceInfo.imagingUrl)) return false;
        if(!this.eventsUrl.equals(onvifDeviceInfo.eventsUrl)) return false;
        if(!this.deviceUrl.equals(onvifDeviceInfo.deviceUrl)) return false;
        if(!this.ptzUrl.equals(onvifDeviceInfo.ptzUrl)) return false;
        if(!this.analyticsUrl.equals(onvifDeviceInfo.analyticsUrl)) return false;
        return true;
    }
}
