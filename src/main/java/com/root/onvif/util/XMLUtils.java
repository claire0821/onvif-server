package com.root.onvif.util;

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

    public static String parseSnapshotUri(String xmlContent) throws Exception {
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        Element element = root.element("Body");
        element = element.element("GetSnapshotUriResponse");
        element = element.element("MediaUri");
        Element uri = element.element("Uri");
        return uri.getText();
    }

}
