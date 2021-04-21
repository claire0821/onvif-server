package com.root.onvif.service;

import com.root.onvif.model.OnvifDeviceInfo;
import com.root.onvif.model.UsernameToken;
import com.root.onvif.util.EncryptUtils;
import com.root.onvif.util.OkHttpUtils;
import com.root.onvif.util.XMLUtils;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CapabilitiesService {

    private static OkHttpUtils okHttpUtils = new OkHttpUtils();

    final private static String GET_CAPABILITIES_XML =
            "<?xml version=\"1.0\" encoding=\"utf - 8\"?>\n" +
                    "<s:Envelope\n" +
                    "    xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                    "    <s:Body\n" +
                    "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                    "        xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                    "        <GetCapabilities xmlns=\"http://www.onvif.org/ver10/device/wsdl\">\n" +
                    "\t\t\t<Category>All</Category>\n" +
                    "\t\t</GetCapabilities>\n" +
                    "\t</s:Body>\n" +
                    "</s:Envelope>";

    private static String getHead(UsernameToken usernameToken) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tds=\"http://www.onvif.org/ver10/device/wsdl\" xmlns:tt=\"http://www.onvif.org/ver10/schema\">\n" +
                "  <s:Header>\n" +
                "    <wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "      <wsse:UsernameToken>\n" +
                "        <wsse:Username>" + usernameToken.getUsername() + "</wsse:Username>\n" +
                "        <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">" + usernameToken.getPassword() + "</wsse:Password>\n" +
                "        <wsse:Nonce>" + usernameToken.getNonce() + "</wsse:Nonce>\n" +
                "        <wsu:Created>" + usernameToken.getCreated() + "</wsu:Created>\n" +
                "      </wsse:UsernameToken>\n" +
                "    </wsse:Security>\n" +
                "  </s:Header>\n";
    }

    private static String getHead() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tds=\"http://www.onvif.org/ver10/device/wsdl\" xmlns:tt=\"http://www.onvif.org/ver10/schema\">\n";
    }

    private static String getCapabilitiesXml(UsernameToken usernameToken) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tds=\"http://www.onvif.org/ver10/device/wsdl\" xmlns:tt=\"http://www.onvif.org/ver10/schema\">\n" +
                "  <s:Header>\n" +
                "    <wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "      <wsse:UsernameToken>\n" +
                "        <wsse:Username>" + usernameToken.getUsername() + "</wsse:Username>\n" +
                "        <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">" + usernameToken.getPassword() + "</wsse:Password>\n" +
                "        <wsse:Nonce>" + usernameToken.getNonce() + "</wsse:Nonce>\n" +
                "        <wsu:Created>" + usernameToken.getCreated() + "</wsu:Created>\n" +
                "      </wsse:UsernameToken>\n" +
                "    </wsse:Security>\n" +
                "  </s:Header>\n" +
                "  <s:Body>\n" +
                "    <tds:GetCapabilities>\n" +
                "      <tds:Category>All</tds:Category>\n" +
                "    </tds:GetCapabilities>\n" +
                "  </s:Body>\n" +
                "</s:Envelope>";
    }

    private static String getDeviceInfomationXml(UsernameToken usernameToken) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tds=\"http://www.onvif.org/ver10/device/wsdl\" xmlns:tt=\"http://www.onvif.org/ver10/schema\">\n" +
                "  <s:Header xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "    <wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "      <wsse:UsernameToken>\n" +
                "        <wsse:Username>" + usernameToken.getUsername() + "</wsse:Username>\n" +
                "        <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">" + usernameToken.getPassword() + "</wsse:Password>\n" +
                "        <wsse:Nonce>" + usernameToken.getNonce() + "</wsse:Nonce>\n" +
                "        <wsu:Created>" + usernameToken.getCreated() + "</wsu:Created>\n" +
                "      </wsse:UsernameToken>\n" +
                "    </wsse:Security>\n" +
                "  </s:Header>\n" +
                "  <soap:Body>\n" +
                "    <tds:GetDeviceInformation />\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
    }

    private static String getDeviceInfomationXml() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:tds=\"http://www.onvif.org/ver10/device/wsdl\" xmlns:tt=\"http://www.onvif.org/ver10/schema\">\n" +
                "  <s:Header xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  </s:Header>\n" +
                "  <soap:Body>\n" +
                "    <tds:GetDeviceInformation />\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
    }

    //截图
    private static String getSnapshotUriXml(UsernameToken usernameToken,String profileToken) {
        return  "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:trt=\"http://www.onvif.org/ver10/media/wsdl\" xmlns:tt=\"http://www.onvif.org/ver10/schema\">\n" +
                "  <s:Header xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "    <wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "      <wsse:UsernameToken>\n" +
                "        <wsse:Username>" + usernameToken.getUsername() + "</wsse:Username>\n" +
                "        <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">" + usernameToken.getPassword() + "</wsse:Password>\n" +
                "        <wsse:Nonce>" + usernameToken.getNonce() + "</wsse:Nonce>\n" +
                "        <wsu:Created>" + usernameToken.getCreated() + "</wsu:Created>\n" +
                "      </wsse:UsernameToken>\n" +
                "    </wsse:Security>\n" +
                "  </s:Header>\n" +
                "  <soap:Body>\n" +
                "    <GetSnapshotUri>\n" +
                "      <ProfileToken>" + profileToken + "</ProfileToken>\n" +
                "    </GetSnapshotUri>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
    }

    //#region 云台
    private static String getContinuousMoveXml() {
        return  "<s:Body>\n" +
                "    <tptz:ContinuousMove>\n" +
                "      <tptz:ProfileToken>prof0</tptz:ProfileToken>\n" +
                "      <tptz:Velocity>\n" +
                "        <tt:PanTilt x=\"XXX\" y=\"YYY\" />\n" +
                "        <tt:Zoom x=\"ZZZ\" />\n" +
                "      </tptz:Velocity>\n" +
                "      TIMEOUT\n" +
                "    </tptz:ContinuousMove>\n" +
                "  </s:Body>\n" +
                "</s:Envelope>";
    }
    //#endregion
    private static String getMediaProfilesXml(UsernameToken usernameToken) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:trt=\"http://www.onvif.org/ver10/media/wsdl\" xmlns:tt=\"http://www.onvif.org/ver10/schema\">\n" +
                "  <s:Header xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "    <wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\n" +
                "      <wsse:UsernameToken>\n" +
                "        <wsse:Username>" + usernameToken.getUsername() + "</wsse:Username>\n" +
                "        <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">" + usernameToken.getPassword() + "</wsse:Password>\n" +
                "        <wsse:Nonce>" + usernameToken.getNonce() + "</wsse:Nonce>\n" +
                "        <wsu:Created>" + usernameToken.getCreated() + "</wsu:Created>\n" +
                "      </wsse:UsernameToken>\n" +
                "    </wsse:Security>\n" +
                "  </s:Header>\n" +
                "  <soap:Body>\n" +
                "    <trt:GetProfiles />\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
    }

    public static OnvifDeviceInfo getCapabilities(OnvifDeviceInfo onvifDeviceInfo) throws Exception {
        UsernameToken usernameToken = EncryptUtils.generate(onvifDeviceInfo.getUsername(), onvifDeviceInfo.getPassword());

        String returnXmlContent = okHttpUtils.okHttp3XmlPost(onvifDeviceInfo.getOnvifAddress(), getCapabilitiesXml(usernameToken));
        onvifDeviceInfo = XMLUtils.parseCapabilities(returnXmlContent, onvifDeviceInfo);
//        Iterator iterator = element.elementIterator();
//        while (iterator.hasNext()) {
//            Element node=(Element) iterator.next();
//            System.out.println(node.getName() + ":" + node.getText());
//        }
        return onvifDeviceInfo;
    }

    public static Map<String,String> getDeviceInfomation(OnvifDeviceInfo onvifDeviceInfo) throws Exception {
        String body = "";
        if(onvifDeviceInfo.getUsername().length() > 0 && onvifDeviceInfo.getPassword().length() > 0) {
            UsernameToken usernameToken = EncryptUtils.generate(onvifDeviceInfo.getUsername(), onvifDeviceInfo.getPassword());
            body = getDeviceInfomationXml(usernameToken);
        } else {
            body = getDeviceInfomationXml();
        }

        String returnXmlContent = okHttpUtils.okHttp3XmlPost(onvifDeviceInfo.getOnvifAddress(), body);

        Map<String,String> res = new HashMap<String, String>();
        Element element = XMLUtils.parseElement(returnXmlContent, "GetDeviceInformationResponse");
        Iterator iterator = element.elementIterator();
        while (iterator.hasNext()) {
            Element node=(Element) iterator.next();
            res.put(node.getName(),node.getText());
//            System.out.println(node.getName() + ":" + node.getText());
        }
        return res;
    }

    public static Map<String,String> getMediaProfiles(OnvifDeviceInfo onvifDeviceInfo) throws Exception {
        UsernameToken usernameToken = EncryptUtils.generate(onvifDeviceInfo.getUsername(), onvifDeviceInfo.getPassword());

        String returnXmlContent = okHttpUtils.okHttp3XmlPost(onvifDeviceInfo.getMediaUrl(), getMediaProfilesXml(usernameToken));
        System.out.println(returnXmlContent);
        return XMLUtils.parseMediaProfiles(returnXmlContent);
    }

    public static String getSnapshotUri(OnvifDeviceInfo onvifDeviceInfo,String profileToken) throws Exception {
        UsernameToken usernameToken = EncryptUtils.generate(onvifDeviceInfo.getUsername(), onvifDeviceInfo.getPassword());

        String returnXmlContent = okHttpUtils.okHttp3XmlPost(onvifDeviceInfo.getMediaUrl(), getSnapshotUriXml(usernameToken,profileToken));
        return XMLUtils.parseSnapshotUri(returnXmlContent);
    }

    //#region 云台
    public static void continuousMove(OnvifDeviceInfo onvifDeviceInfo) {
        UsernameToken usernameToken = EncryptUtils.generate(onvifDeviceInfo.getUsername(), onvifDeviceInfo.getPassword());

        String head = getHead(usernameToken);
        getContinuousMoveXml();

        String returnXmlContent = okHttpUtils.okHttp3XmlPost(onvifDeviceInfo.getMediaUrl(), getMediaProfilesXml(usernameToken));
        System.out.println(returnXmlContent);
        return XMLUtils.parseMediaProfiles(returnXmlContent);
    }
    //#endregion
}
