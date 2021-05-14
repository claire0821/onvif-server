package com.root.onvif.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.root.onvif.model.OnvifDeviceInfo;
import org.dom4j.*;

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

    //#region 云台
    public static JSONObject parsePTZConfiguration(String xmlContent, String strProfile) throws DocumentException {
        Document document = DocumentHelper.parseText(xmlContent);
        Element root = document.getRootElement();
        List<Element> elements = root.element("Body").element("GetProfilesResponse").elements("Profiles");

        for(Element item : elements) {
            Attribute token = item.attribute("token");
            if(token != null && token.getValue().equals(strProfile)) {
                //PTZConfiguration
                Element ptzConfiguration = item.element("PTZConfiguration");
                if(ptzConfiguration == null) {
                    return null;
                }
                JSONObject ptzConfig = parse(ptzConfiguration);//new JSONObject();

                /*
                Attribute token1 = ptzConfiguration.attribute("token");
                ptzConfig.put("token",token1.getValue());
                Element name = ptzConfiguration.element("Name");
                ptzConfig.put("Name",name.getText());

                Element useCount = ptzConfiguration.element("UseCount");
                ptzConfig.put("UseCount",useCount.getText());

                Element nodeToken = ptzConfiguration.element("NodeToken");
                ptzConfig.put("NodeToken",useCount.getText());

                Element defaultAbsolutePantTiltPositionSpace = ptzConfiguration.element("DefaultAbsolutePantTiltPositionSpace");
                ptzConfig.put("DefaultAbsolutePantTiltPositionSpace",defaultAbsolutePantTiltPositionSpace.getText());

                Element defaultAbsoluteZoomPositionSpace = ptzConfiguration.element("DefaultAbsoluteZoomPositionSpace");
                ptzConfig.put("DefaultAbsoluteZoomPositionSpace",defaultAbsoluteZoomPositionSpace.getText());

                Element defaultRelativePanTiltTranslationSpace = ptzConfiguration.element("DefaultRelativePanTiltTranslationSpace");
                ptzConfig.put("DefaultRelativePanTiltTranslationSpace",defaultRelativePanTiltTranslationSpace.getText());

                Element defaultRelativeZoomTranslationSpace = ptzConfiguration.element("DefaultRelativeZoomTranslationSpace");
                ptzConfig.put("DefaultRelativeZoomTranslationSpace",defaultRelativeZoomTranslationSpace.getText());

                Element defaultContinuousPanTiltVelocitySpace = ptzConfiguration.element("DefaultContinuousPanTiltVelocitySpace");
                ptzConfig.put("DefaultContinuousPanTiltVelocitySpace",defaultContinuousPanTiltVelocitySpace.getText());

                Element defaultContinuousZoomVelocitySpace = ptzConfiguration.element("DefaultContinuousZoomVelocitySpace");
                ptzConfig.put("DefaultContinuousZoomVelocitySpace",defaultContinuousZoomVelocitySpace.getText());

                Element defaultPTZSpeed = ptzConfiguration.element("DefaultPTZSpeed");
                JSONObject defaultPTZSpeedJson = new JSONObject();
                Element panTilt = defaultPTZSpeed.element("PanTilt");
                JSONObject panTiltJson = new JSONObject();

                Attribute x = panTilt.attribute("x");
                panTiltJson.put("x", x.getValue());

                Attribute y = panTilt.attribute("y");
                panTiltJson.put("y", y.getValue());
                defaultPTZSpeedJson.put("Attribute",panTiltJson);

                Element zoom = defaultPTZSpeed.element("Zoom");
                JSONObject zoomJson = new JSONObject();
//                zoom.attribute()
*/
                return ptzConfig;
            }

        }

        return null;
    }
    //#endregion

    //xml转json
    public static JSONObject parse(Element element) {
        if(element == null) return null;
        JSONObject jsonObject = new JSONObject();

        List<Attribute> attributes = element.attributes();
        for(Attribute item : attributes) {
            jsonObject.put(item.getName(),item.getValue());
        }

        List<Element> elements = element.elements();
        //没有子节点并且有text
        if(elements == null || elements.size() == 0) {
//            System.out.println(element.getName() + ":" + element.getText());
            if(element.getText().length() > 0) {
                jsonObject.put(element.getName(),element.getText());
            }
            return jsonObject;
        }

        JSONObject itemJson = new JSONObject();
        for(Element item : elements) {
//            if(item.getName().equals("PanTiltLimits")) {
//                System.out.println(item);
//            }
            List<Element> elements1 = item.elements();
            if(elements1 == null || elements1.size() == 0) {
                if(item.getText().length() > 0) {
//                    System.out.println(item.getName());
                    itemJson.put(item.getName(),item.getText());
                }
            } else {
                JSONObject item1Json = new JSONObject();
                for (Element item1 : elements1) {
                    JSONObject parse = parse(item1);
//                    System.out.println(parse);


                    if(item1.nodeCount() == 1 || item1.attributes().size() == 0) {
                        System.out.println(item1.getName() + ":" + item1.nodeCount());
                        item1Json.putAll(parse);
                    } else {
                        item1Json.put(item1.getName(),parse);
                    }
                }

                itemJson.put(item.getName(),item1Json);
            }

//            System.out.println(itemJson);
//            jsonObject.put(item.getName(),parse);
        }
        jsonObject.put(element.getName(),itemJson);

        return jsonObject;
    }
}
