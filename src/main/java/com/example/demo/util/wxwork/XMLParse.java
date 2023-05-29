/**
 * 对企业微信发送给企业后台的消息加解密示例代码.
 *
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package com.example.demo.util.wxwork;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.*;

/**
 * XMLParse class
 * <p>
 * 提供提取消息格式中的密文及生成回复消息格式的接口.
 */
public class XMLParse {

    /**
     * 提取出xml数据包中的加密消息
     *
     * @param xmltext 待提取的xml字符串
     * @return 提取出的加密消息字符串
     * @throws AesException
     */
    public static Object[] extract(String xmltext) throws AesException {
        Object[] result = new Object[3];
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            String FEATURE = null;
            // This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
            // Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
            FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
            dbf.setFeature(FEATURE, true);

            // If you can't completely disable DTDs, then at least do the following:
            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
            // JDK7+ - http://xml.org/sax/features/external-general-entities
            FEATURE = "http://xml.org/sax/features/external-general-entities";
            dbf.setFeature(FEATURE, false);

            // Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
            // Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
            // JDK7+ - http://xml.org/sax/features/external-parameter-entities
            FEATURE = "http://xml.org/sax/features/external-parameter-entities";
            dbf.setFeature(FEATURE, false);

            // Disable external DTDs as well
            FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
            dbf.setFeature(FEATURE, false);

            // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);

            // And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then
            // ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
            // (http://cwe.mitre.org/data/definitions/918.html) and denial
            // of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."

            // remaining parser logic
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xmltext);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("Encrypt");
            result[0] = 0;
            result[1] = nodelist1.item(0).getTextContent();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.ParseXmlError);
        }
    }

    /**
     * 生成xml消息
     *
     * @param encrypt   加密后的消息密文
     * @param signature 安全签名
     * @param timestamp 时间戳
     * @param nonce     随机字符串
     * @return 生成的xml字符串
     */
    public static String generate(String encrypt, String signature, String timestamp, String nonce) {

        String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
                + "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n"
                + "<TimeStamp>%3$s</TimeStamp>\n" + "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
        return String.format(format, encrypt, signature, timestamp, nonce);

    }

    /**
     * xml字符转map
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> readStringXml(String xml) throws DocumentException {
        org.dom4j.Document doc = DocumentHelper.parseText(xml);
        org.dom4j.Element books = doc.getRootElement();
//        System.out.println("rootNode:" + books.getName());
        Iterator Elements = books.elementIterator();
        Map<String, String> map = new HashMap<>();
        while (Elements.hasNext()) {
            org.dom4j.Element user = (org.dom4j.Element) Elements.next();
//            System.out.println("node=" + user.getName() + "\ttext=" + user.getText());
            map.put(user.getName(), user.getText());
        }
        return map;
    }

    /**
     * XML 转换 Map
     * @param xml
     * @return
     */
    public static  Map xmlToMap(String xml) {
        JSONObject obj = new JSONObject();
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            SAXBuilder sb = new SAXBuilder();
            org.jdom2.Document doc = sb.build(is);
            org.jdom2.Element root = doc.getRootElement();
            Map map=iterateElement(root);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * XML 转 Json串
     * @param xml
     * @return
     */
    public static String xmlToJSON(String xml) {
        JSONObject obj = new JSONObject();
        try {
            InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
            SAXBuilder sb = new SAXBuilder();
            org.jdom2.Document doc = sb.build(is);
            org.jdom2.Element root = doc.getRootElement();
            Map map=iterateElement(root);
            obj.put(root.getName(),map);
            return obj.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map  iterateElement(org.jdom2.Element root) {
        List childrenList = root.getChildren();
        org.jdom2.Element element = null;
        Map map = new HashMap();
        List list = null;
        for (int i = 0; i < childrenList.size(); i++) {
            list = new ArrayList();
            element = (org.jdom2.Element) childrenList.get(i);
            if(element.getChildren().size()>0){
                if(root.getChildren(element.getName()).size()>1){
                    if (map.containsKey(element.getName())) {
                        list = (List) map.get(element.getName());
                    }
                    list.add(iterateElement(element));
                    map.put(element.getName(), list);
                }else{
                    map.put(element.getName(), iterateElement(element));
                }
            }else {
                if(root.getChildren(element.getName()).size()>1){
                    if (map.containsKey(element.getName())) {
                        list = (List) map.get(element.getName());
                    }
                    list.add(element.getTextTrim());
                    map.put(element.getName(), list);
                }else{
                    map.put(element.getName(), element.getTextTrim());
                }
            }
        }

        return map;
    }

    /**
     * 字符串key = value 转Map
     * @param str
     * @return
     */
    public static Map<String, String> getStringToMap(String str) {
        // 判断str是否有值
        if (null == str || "".equals(str)) {
            return null;
        }
        // 根据&截取
        String[] strings = str.split(",");

        Map<String, String> map = new HashMap<>();
        // 循环加入map集合
        for (String string : strings) {

            String key = string.substring(0,string.indexOf("="));
            String value = string.substring(string.indexOf("=") + 1);

            map.put(key, value);
        }
        return map;
    }
}
