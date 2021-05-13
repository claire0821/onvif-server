package com.root.onvif.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.root.onvif.model.OnvifDeviceInfo;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.*;

public class XMLUtils {

    public static Element parseElement(String xmlContent, String name) throws Exception {
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        Element element = root.element("Body").element(name);

        return element;
    }

    public static OnvifDeviceInfo parseCapabilities(String xmlContent, OnvifDeviceInfo onvifDeviceInfo) throws Exception {
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        Element element = root.element("Body").element("GetCapabilitiesResponse").element("Capabilities");

        Element media = element.element("Media");
        if(media != null) {
            Element xAddr = media.element("XAddr");
            if(xAddr != null) {
                onvifDeviceInfo.setMediaUrl(xAddr.getText());
            }
        }

        Element imaging = element.element("Imaging");
        if(imaging != null) {
            Element xAddr = imaging.element("XAddr");
            if(xAddr != null) {
                onvifDeviceInfo.setImagingUrl(xAddr.getText());
            }
        }

        Element events = element.element("Events");
        if(events != null) {
            Element xAddr = events.element("XAddr");
            if(xAddr != null) {
                onvifDeviceInfo.setEventsUrl(xAddr.getText());
            }
        }

        Element device = element.element("Device");
        if(device != null) {
            Element xAddr = device.element("XAddr");
            if(xAddr != null) {
                onvifDeviceInfo.setDeviceUrl(xAddr.getText());
            }
        }

        Element ptz = element.element("PTZ");
        if(ptz != null) {
            Element xAddr = ptz.element("XAddr");
            if(xAddr != null) {
                onvifDeviceInfo.setPtzUrl(xAddr.getText());
            }
        }

        Element analytics = element.element("Analytics");
        if(analytics != null) {
            Element xAddr = analytics.element("XAddr");
            if(xAddr != null) {
                onvifDeviceInfo.setAnalyticsUrl(xAddr.getText());
            }
        }

        return onvifDeviceInfo;
    }

    public static Map<String, String> parseDeviceInfomation(String xmlContent) throws Exception {
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        Element element = root.element("Body").element("GetDeviceInformationResponse");
        Iterator iterator = element.elementIterator();

        HashMap<String, String> map = new HashMap<String, String>();
        while (iterator.hasNext()) {
            Element node=(Element) iterator.next();
            map.put(node.getName(),node.getText());
        }
        return map;
    }

    public static Map<String,String> parseMediaProfiles(String xmlContent) throws Exception {
        Map<String,String> res = new HashMap<String, String>();
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        List<Element> elements = root.element("Body").element("GetProfilesResponse").elements("Profiles");
        for(Element item : elements) {
//            System.out.println(item.toString());
            Attribute token = item.attribute("token");
            Element name = item.element("Name");
//            System.out.println(token.getValue());
            res.put(token.getValue(),name.getText());
        }
        return res;
    }

    public static JSONObject parseMediaInfo(String xmlContent, String strProfile) throws Exception {
        Map<String,String> res = new HashMap<String, String>();
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        List<Element> elements = root.element("Body").element("GetProfilesResponse").elements("Profiles");

        JSONArray jsonArray = new JSONArray();

        for(Element item : elements) {
//            System.out.println(item.toString());
            JSONObject profile = new JSONObject();

            Attribute token = item.attribute("token");
            if(token != null && token.getValue().equals(strProfile)) {
                Element name = item.element("Name");
                profile.put("token", token.getValue());
                profile.put("name", name.getText());

                //VideoSource
                Element videoSourceConfiguration = item.element("VideoSourceConfiguration");
                profile.put("VideoSource",getVideoSource(videoSourceConfiguration));
                Element audioSourceConfiguration = item.element("AudioSourceConfiguration");
                profile.put("AudioSource",getAudioSource(audioSourceConfiguration));
                Element audioEncoderConfiguration = item.element("AudioEncoderConfiguration");
                profile.put("AudioEncoder",getAudioEncoder(audioEncoderConfiguration));
                return profile;
//                jsonArray.add(profile);
            }

        }

        return null;
    }

    public static JSONObject getVideoSource(Element videoSourceConfiguration) {
        if(videoSourceConfiguration == null) return null;
        JSONObject videoSource = new JSONObject();
        Attribute token = videoSourceConfiguration.attribute("token");
        videoSource.put("token", token.getValue());
        Element name = videoSourceConfiguration.element("Name");
        videoSource.put("Name", name.getText());
        Element useCount = videoSourceConfiguration.element("UseCount");
        videoSource.put("UseCount", useCount.getText());
        Element sourceToken = videoSourceConfiguration.element("SourceToken");
        videoSource.put("SourceToken", sourceToken.getText());

        Element bounds = videoSourceConfiguration.element("Bounds");
        JSONObject boundsJson = new JSONObject();
        Attribute x = bounds.attribute("x");
        boundsJson.put("x",x.getValue());
        Attribute y = bounds.attribute("y");
        boundsJson.put("y",y.getValue());
        Attribute width = bounds.attribute("width");
        boundsJson.put("width",width.getValue());
        Attribute height = bounds.attribute("height");
        boundsJson.put("height",height.getValue());
        videoSource.put("Bounds", boundsJson);
        return videoSource;
    }

    public static JSONObject getAudioSource(Element audioSourceConfiguration) {
        if(audioSourceConfiguration == null) return null;
        JSONObject audioSource = new JSONObject();
        Attribute token = audioSourceConfiguration.attribute("token");
        audioSource.put("token",token.getValue());
        Element name = audioSourceConfiguration.element("Name");
        audioSource.put("Name", name.getText());
        Element useCount = audioSourceConfiguration.element("UseCount");
        audioSource.put("UseCount", useCount.getText());
        Element sourceToken = audioSourceConfiguration.element("SourceToken");
        audioSource.put("SourceToken", sourceToken.getText());
        return audioSource;

    }

    public static JSONObject getAudioEncoder(Element audioEncoderConfiguration) {
        if(audioEncoderConfiguration == null) return null;
        JSONObject audioEncoder = new JSONObject();
        Attribute token = audioEncoderConfiguration.attribute("token");
        audioEncoder.put("token",token.getValue());

        Element name = audioEncoderConfiguration.element("Name");
        audioEncoder.put("Name",name.getText());

        Element useCount = audioEncoderConfiguration.element("UseCount");
        audioEncoder.put("UseCount",useCount.getText());

        Element encoding = audioEncoderConfiguration.element("Encoding");
        audioEncoder.put("Encoding",encoding.getText());

        Element bitrate = audioEncoderConfiguration.element("Bitrate");
        audioEncoder.put("Bitrate",bitrate.getText());

        Element sampleRate = audioEncoderConfiguration.element("SampleRate");
        audioEncoder.put("SampleRate",sampleRate.getText());

        Element multicast = audioEncoderConfiguration.element("Multicast");
        JSONObject multicastJson = new JSONObject();
        Element address = multicast.element("Address");
        JSONObject addressJson = new JSONObject();
        Element type = address.element("Type");
        addressJson.put("Type",type.getText());
        Element iPv4Address = address.element("IPv4Address");
        addressJson.put("IPv4Address",iPv4Address.getText());
        multicastJson.put("Address",addressJson);

        Element port = multicast.element("Port");
        multicastJson.put("Port",port.getText());

        Element tTL = multicast.element("TTL");
        multicastJson.put("TTL",tTL.getText());

        Element autoStart = multicast.element("AutoStart");
        multicastJson.put("AutoStart",autoStart.getText());

        audioEncoder.put("Multicast",multicastJson);

        Element sessionTimeout = audioEncoderConfiguration.element("SessionTimeout");
        audioEncoder.put("SessionTimeout",sessionTimeout.getText());

        return audioEncoder;
    }
    public static String parseSnapshotUri(String xmlContent) throws Exception {
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        Element element = root.element("Body");
        element = element.element("GetSnapshotUriResponse");
        element = element.element("MediaUri");
        Element uri = element.element("Uri");
        return uri.getText();
    }

    public static String parseStreamUri(String xmlContent) throws Exception {
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        Element element = root.element("Body");
        element = element.element("GetStreamUriResponse");
        element = element.element("MediaUri");
        Element uri = element.element("Uri");
        return uri.getText();
    }
}
