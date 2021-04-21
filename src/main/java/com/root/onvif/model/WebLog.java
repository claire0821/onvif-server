package com.root.onvif.model;

import java.util.Date;
import java.util.Map;

public class WebLog {
    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作时间
     */
    private Date startTime;

    /**
     * 消耗时间
     */
    private Integer spendTime;

    /**
     * 根路径
     */
    private String basePath;

    /**
     * URI
     */
    private String uri;

    /**
     * URL
     */
    private String url;

    /**
     * 请求类型
     */
    private String method;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 请求参数
     */
    private Map<String, String[]> parameter;

    /**
     * 请求返回的结果
     */
    private Object result;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Integer spendTime) {
        this.spendTime = spendTime;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<String, String[]> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, String[]> parameter) {
        this.parameter = parameter;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        String WebLog = "WebLog{" +
                "uri='" + uri + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' + "\n\r" +
//                ", description='" + description + '\'' + "\n\r" +
//                ", username='" + username + '\'' +
                ", startTime=" + startTime.toString() +
                ", spendTime=" + spendTime + "ms" +
                ", basePath='" + basePath + '\'' +
                ", ip='" + ip + '\'' + "\n\r" +
                ", parameter=";

        String str = "";
        for(Map.Entry<String, String[]> entry : parameter.entrySet()) {
            String[] value = entry.getValue();
            String strValue = "";
            for (String val : value) {
                strValue += val + ",";
            }
            strValue = strValue.substring(0, strValue.length() - 1);

            str +="" + entry.getKey() + ":" + strValue + ",";
        }
        if(str.length() > 0) {
            str = str.substring(0,str.length() - 1);
        }

        WebLog += str;
        WebLog += "\n\r" +
                ", result=" + result +
                '}';
        return WebLog;
    }


}
