package com.greatmap.digital.util.wsdl;

import javax.xml.XMLConstants;

/**
 * 放置对应内容的XPath和其他常量 我也是醉了，import中的xsd文件哪怕写入了命名空间，但就是不解析，快要气炸了，暂且搁置命名空间问题
 *
 * @author guoan
 */
public class XPathConstant {
    /**
     * 默认的value值，表示需要填入内容
     */
    public static String VALUEDEF = "?";

    /*--------------------------------------------------------发送消息--------------------------------------------------------------*/
    /*-------------------------------------------------------包含命名空间------------------------------------------------------------*/
    /**
     * 默认的wsdl命名空间
     */
    public static String DEFAULTWSDLNS = "http://schemas.xmlsoap.org/wsdl/";

    /**
     * binding对应的传输uri，soap1.1
     */
    public static String SOAPBINDING11 = "http://schemas.xmlsoap.org/wsdl/soap/";

    /**
     * binding对应的传输uri，soap1.2
     */
    public static String SOAPBINDING12 = "http://schemas.xmlsoap.org/wsdl/soap12/";

    /**
     * 默认的xml schema命名空间
     */
    public static String DEFAULTSCHEMANS = XMLConstants.W3C_XML_SCHEMA_NS_URI;

    /**
     * definitions xpath
     */
    public static String DEFXPATHNS = "//*[namespace-uri()='" + DEFAULTWSDLNS + "' and local-name()='definitions']";

    /**
     * types xpath
     */
    public static String TYPESXPATHNS = DEFXPATHNS + "/*[namespace-uri()='" + DEFAULTWSDLNS
            + "' and local-name()='types']";

    /**
     * wsdl import xpath
     */
    public static String IMPORTXPATHNS = DEFXPATHNS + "/*[namespace-uri()='" + DEFAULTWSDLNS
            + "' and local-name()='import']";

    /**
     * schema xpath
     */
    public static String SCHEMAXPATHNS = "//*[namespace-uri()='" + DEFAULTSCHEMANS + "' and local-name()='schema']";

    /**
     * schema import xpath
     */
    public static String SCHEMAIMPORTPATHNS = SCHEMAXPATHNS + "/*[namespace-uri()='" + DEFAULTSCHEMANS
            + "' and local-name()='import']";

    /*-------------------------------------------------------不含命名空间------------------------------------------------------------*/
    /**
     * definitions xpath
     */
    public static String DEFXPATH = "//*[local-name()='definitions']";

    /**
     * types xpath
     */
    public static String TYPESXPATH = DEFXPATH + "/*[local-name()='types']";

    /**
     * wsdl import xpath
     */
    public static String IMPORTXPATH = DEFXPATH + "/*[local-name()='import']";

    /**
     * schema xpath
     */
    public static String SCHEMAXPATH = "//*[local-name()='schema']";

    /**
     * schema import xpath
     */
    public static String SCHEMAIMPORTPATH = SCHEMAXPATH + "/*[local-name()='import']";

    /*--------------------------------------------------------接收消息--------------------------------------------------------------*/
    /*-------------------------------------------------------包含命名空间------------------------------------------------------------*/
    /**
     * 默认的soap1.1命名空间
     */
    public static String SOAP11 = "http://schemas.xmlsoap.org/soap/envelope/";

    /**
     * 默认的soap1.2命名空间
     */
    public static String SOAP12 = "http://www.w3.org/2003/05/soap-envelope";

    /**
     * header xpath
     */
    public static String HEADER11NSXPATH = "//*[namespace-uri()='" + SOAP11
            + "' and local-name()='Envelope']/*[namespace-uri()='" + SOAP11 + "' and local-name()='Header']";

    /**
     * body xpath
     */
    public static String BODY11NSXPATH = "//*[namespace-uri()='" + SOAP11
            + "' and local-name()='Envelope']/*[namespace-uri()='" + SOAP11 + "' and local-name()='Body']";

    /**
     * fault xpath
     */
    public static String FAULT11NSXPATH = BODY11NSXPATH + "/*[namespace-uri()='" + SOAP11
            + "' and local-name()='Fault']";

    /**
     * faultcode xpath
     */
    public static String FAULTCODE11NSXPATH = FAULT11NSXPATH + "/*[namespace-uri()='" + SOAP11
            + "' and local-name()='faultcode']";

    /**
     * faultstring xpath
     */
    public static String FAULTSTRING11NSXPATH = FAULT11NSXPATH + "/*[namespace-uri()='" + SOAP11
            + "' and local-name()='faultstring']";

    /**
     * header xpath
     */
    public static String HEADER12NSXPATH = "//*[namespace-uri()='" + SOAP12
            + "' and local-name()='Envelope']/*[namespace-uri()='" + SOAP12 + "' and local-name()='Header']";

    /**
     * body xpath
     */
    public static String BODY12NSXPATH = "//*[namespace-uri()='" + SOAP12
            + "' and local-name()='Envelope']/*[namespace-uri()='" + SOAP12 + "' and local-name()='Body']";

    /**
     * fault xpath
     */
    public static String FAULT12NSXPATH = BODY12NSXPATH + "/*[namespace-uri()='" + SOAP12
            + "' and local-name()='Fault']";

    /**
     * faultcode xpath
     */
    public static String FAULTCODE12NSXPATH = FAULT12NSXPATH + "/*[namespace-uri()='" + SOAP12
            + "' and local-name()='Code']/*[namespace-uri()='" + SOAP12 + "' and local-name()='Value']";

    /**
     * faultreason xpath
     */
    public static String FAULTREASON12NSXPATH = FAULT12NSXPATH + "/*[namespace-uri()='" + SOAP12
            + "' and local-name()='Reason']/*[namespace-uri()='" + SOAP12 + "' and local-name()='Text']";

    /**
     * faultdetail xpath
     */
    public static String FAULTDETAIL12NSXPATH = FAULT12NSXPATH + "/*[namespace-uri()='" + SOAP12
            + "' and local-name()='Detail']";

    /*-------------------------------------------------------不含命名空间------------------------------------------------------------*/
    /**
     * header xpath
     */
    public static String HEADERXPATH = "//*[local-name()='Envelope']/*[local-name()='Header']";

    /**
     * body xpath
     */
    public static String BODYXPATH = "//*[local-name()='Envelope']/*[local-name()='Body']";

    /**
     * fault xpath
     */
    public static String FAULTXPATH = BODYXPATH + "/*[local-name()='Fault']";

    /**
     * faultcode xpath
     */
    public static String FAULTCODE11XPATH = FAULTXPATH + "/*[local-name()='faultcode']";

    /**
     * faultstring xpath
     */
    public static String FAULTSTRING11XPATH = FAULTXPATH + "/*[local-name()='faultstring']";

    /**
     * faultcode xpath
     */
    public static String FAULTCODE12XPATH = FAULTXPATH + "/*[local-name()='Code']/*[local-name()='Value']";

    /**
     * faultreason xpath
     */
    public static String FAULTREASON12XPATH = FAULTXPATH + "/*[local-name()='Reason']/*[local-name()='Text']";

    /**
     * faultdetail xpath
     */
    public static String FAULTDETAIL12XPATH = FAULTXPATH + "/*[local-name()='Detail']";
}
