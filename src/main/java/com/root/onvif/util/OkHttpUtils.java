package com.root.onvif.util;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OkHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    public String okHttp3XmlPost(String url, String body) throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(null, body))
                .addHeader("Content-Type", "application/soap+xml")
                .build();

        Response response = null;

        try {
            response = OkHttpClientSingleton.getInstance().newCall(request).execute();
        } catch (Exception e) {
            logger.error("连接[" + url + "]异常");
            logger.error(e.toString());
            throw e;
        }

//        LOGGER.debug("连接 url=[" + url + "], 发送[" + body + "],  返回[" + response.toString() + "]");

//        LOGGER.debug("response code = " + response.code());
        if (response.isSuccessful()) {
            return response.body() == null ? "" : response.body().string();
        } else if (response.code() == 401) {
//            LOGGER.debug("response code: " + response.code() + ", response body: " + (response.body() == null ? "" : response.body().string()));
            throw new Exception("设备认证失败(用户名或密码错误)");
        } else {
//            LOGGER.debug("response code: " + response.code() + ", response body: " + (response.body() == null ? "" : response.body().string()));
            throw new Exception("返回不成功");
        }
    }

//    public byte[] getFile(String url, String authorization) throws Exception {
//        if (authorization == null) {
//            authorization = "NOAuthorization";
//        }
//
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Authorization", authorization)
//                .build();
//        Response response = null;
//        try {
//            response = OkHttpClientSingleton.getInstance().newCall(request).execute();
//        } catch (Exception e) {
//            LOGGER.debug("连接[" + url + "]异常");
//            throw new RuntimeException(e);
//        }
//
//        if (response.isSuccessful()) {
//            if (response .body() != null) {
//                return response.body().bytes();
//            }
//            return new byte[0];
//        } else {
//            return new byte[0];
//        }
//    }
}
