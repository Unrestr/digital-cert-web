package com.greatmap.digital.util.wsdl;

import com.greatmap.digital.excepition.DigitalException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.wsdl.*;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 第一步，使用wsdl4j解析wsdl，wsdl文档结构推荐参考http://blog.csdn.net/wudouguerwxx/article/details/2036821
 *
 * @author guoan
 */
public class WsdlUtil {

    private static Logger log = LoggerFactory.getLogger(WsdlUtil.class);

    private static WSDLFactory wsdlFactory;
    private static WSDLReader wsdlReader;

    private static DocumentBuilder documentBuilder;

    private static XPath xPath;

    /**
     * @return
     * @throws WSDLException
     */
    private static WSDLFactory getWsdlFactory() throws WSDLException {
        if (wsdlFactory == null) {
            wsdlFactory = WSDLFactory.newInstance();
        }
        return wsdlFactory;
    }

    /**
     * @return
     * @throws WSDLException
     */
    private static WSDLReader getWsdlReader() throws WSDLException {
        if (wsdlReader == null) {
            wsdlReader = getWsdlFactory().newWSDLReader();
            wsdlReader.setFeature("javax.wsdl.verbose", true);
            wsdlReader.setFeature("javax.wsdl.importDocuments", true);
        }
        return wsdlReader;
    }

    /**
     * @return
     * @throws ParserConfigurationException
     */
    public static DocumentBuilder getDBBuilder() throws ParserConfigurationException {
        if (documentBuilder == null) {
            // 得到DOM解析器的工厂实例
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            // 从DOM工厂中获得DOM解析器
            documentBuilder = dbFactory.newDocumentBuilder();
        }
        return documentBuilder;
    }

    /**
     * @param wsdlUrl
     * @return
     * @throws WSDLException
     */
    public static Document getDefinitionDocument(String wsdlUrl) throws WSDLException {
        // 获取wsdl定义
        Definition def = getWsdlReader().readWSDL(wsdlUrl);
        // 转文档流
        WSDLWriter writer = getWsdlFactory().newWSDLWriter();
        Document document = writer.getDocument(def);
        return document;
    }

    /**
     * 得到document的查找工具xpath,不支持命名空间
     *
     * @return
     */
    public static XPath getXPath() {
        if (xPath == null) {
            xPath = XPathFactory.newInstance().newXPath();
        }
        return xPath;
    }

    /**
     * 得到wsdl文档中方法名
     *
     * @param wsdlUrl       wsdl地址
     * @param operationList 方法集合
     * @throws WSDLException
     */
    public static void getOperationList(String wsdlUrl, List<String> operationList) throws WSDLException {
        // 获取wsdl定义
        Definition def = getWsdlReader().readWSDL(wsdlUrl);

        // 遍历bindings
        Map bindings = def.getBindings();
        Iterator<Map.Entry> iterator = bindings.entrySet().iterator();
        while (iterator.hasNext()) {
            Binding binding = (Binding) iterator.next().getValue();
            if (binding != null) {
                List extEles = binding.getExtensibilityElements();
                if (extEles != null && extEles.size() > 0) {
                    ExtensibilityElement extensibilityElement = (ExtensibilityElement) extEles.get(0);
                    if (extensibilityElement != null) {
                        String namespaceUri = extensibilityElement.getElementType().getNamespaceURI();
                        // 默认使用soap1.1的binding，与soapui调用一致
                        if (XPathConstant.SOAPBINDING11.equals(namespaceUri)
                                || XPathConstant.SOAPBINDING12.equals(namespaceUri)) {
                            List<Operation> operations = binding.getPortType().getOperations();
                            for (Operation operation : operations) {
                                operationList.add(operation.getName());
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据操作名称查询具体对象 有点蛋疼，wsdl4j不存在直接寻找operation的api
     *
     * @param wsdlUrl
     * @param operationName
     * @return
     * @throws WSDLException
     */
    public static Operation getOperationByName(String wsdlUrl, String operationName) throws WSDLException {
        Operation targetOp = null;
        // 获取wsdl定义
        Definition def = getWsdlReader().readWSDL(wsdlUrl);

        // 遍历bindings
        Map bindings = def.getBindings();
        Iterator<Map.Entry> iterator = bindings.entrySet().iterator();
        while (iterator.hasNext()) {
            Binding binding = (Binding) iterator.next().getValue();
            if (binding != null) {
                List extEles = binding.getExtensibilityElements();
                if (extEles != null && extEles.size() > 0) {
                    ExtensibilityElement extensibilityElement = (ExtensibilityElement) extEles.get(0);
                    if (extensibilityElement != null) {
                        String namespaceUri = extensibilityElement.getElementType().getNamespaceURI();
                        // 默认使用soap1.1的binding，与soapui调用一致
                        if (XPathConstant.SOAPBINDING11.equals(namespaceUri)) {
                            List<Operation> operations = binding.getPortType().getOperations();
                            for (Operation operation : operations) {
                                if (operation.getName().equals(operationName)) {
                                    targetOp = operation;
                                    break;
                                }
                            }
                            break;
                        }
                        if (XPathConstant.SOAPBINDING12.equals(namespaceUri)) {
                            List<Operation> operations = binding.getPortType().getOperations();
                            for (Operation operation : operations) {
                                if (operation.getName().equals(operationName)) {
                                    targetOp = operation;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return targetOp;
    }

    /**
     * 获取对应方法的详细参数名及类型
     *
     * @param wsdlUrl
     * @param operationName 操作名
     * @return
     * @throws WSDLException
     */
    public static List<ParameterInfo> getMethodParams(String wsdlUrl, String operationName) throws WSDLException {
        Operation operation = getOperationByName(wsdlUrl, operationName);
        List<ParameterInfo> parameterInfoList = null;
        if (operation == null) {
            log.error("can not find operation " + operationName + " , please check again");
            throw new DigitalException("can not find operation " + operationName + " , please check again");
        } else {
            // 输入
            Map inputParts = operation.getInput().getMessage().getParts();
            parameterInfoList = findParamsByOperation(wsdlUrl, inputParts);
        }
        return parameterInfoList;
    }

    /**
     * 获取对应方法的返回类型
     *
     * @param wsdlUrl
     * @param operationName 操作名
     * @return
     * @throws WSDLException
     */
    public static List<ParameterInfo> getMethodReturn(String wsdlUrl, String operationName) throws WSDLException {
        // 我更希望返回值限定为json串，会方便很多
        Operation operation = getOperationByName(wsdlUrl, operationName);
        List<ParameterInfo> parameterInfoList = null;
        if (operation == null) {
            log.error("can not find operation " + operationName + " , please check again");
            throw new DigitalException("can not find operation " + operationName + " , please check again");
        } else {
            // 输出
            Map outputParts = operation.getOutput().getMessage().getParts();
            parameterInfoList = findParamsByOperation(wsdlUrl, outputParts);
        }
        return parameterInfoList;
    }

    /**
     * 获取wsdl数据类型定义容器Types中XML Schema前缀，用于拼接形成完整标签 目前不需要使用，因为对XML Schema均忽略命名空间
     *
     * @param wsdlUrl
     * @return
     * @throws WSDLException
     */
    @Deprecated
    public static String getSchemaPrefix(String wsdlUrl) throws WSDLException {
        String prefix = "";
        // 获取wsdl定义
        Definition def = getWsdlReader().readWSDL(wsdlUrl);

        // 获取Types XML Schema文档定义
        Types types = def.getTypes();

        // 获取标签前缀定义
        ExtensibilityElement extensibilityElement = (ExtensibilityElement) types.getExtensibilityElements().get(0);
        Schema schema = (Schema) extensibilityElement;
        prefix = schema.getElement().getPrefix();
        if (!StringUtils.isBlank(prefix)) {
            // 前缀非空时加入:
            prefix += ":";
        } else {
            prefix = "";
        }
        return prefix;
    }

    /**
     * 查找所有的import结点
     *
     * @param document
     * @param xpath
     * @return
     * @throws Exception
     */
    public static List<Document> getImportDocumentList(Document document, XPath xpath) throws Exception {
        List<Document> importDocumentList = new ArrayList<Document>();
        // 查找def中所有的import
        NodeList importNodeList = (NodeList) xpath.evaluate(XPathConstant.IMPORTXPATH, document,
                XPathConstants.NODESET);
        if (importNodeList != null) {
            for (int i = 0; i < importNodeList.getLength(); i++) {
                Node importNode = importNodeList.item(i);
                String location = DomUtil.getAttributeValue(importNode, "location");
                // 把要解析的xml文档读入DOM解析器
                if (!StringUtils.isBlank(location)) {
                    Document importDocument = getDefinitionDocument(location);
                    importDocumentList.add(importDocument);
                }
            }
        }

        // 查找schema中所有的import
        NodeList importSchemaNodeList = (NodeList) xpath.evaluate(XPathConstant.SCHEMAIMPORTPATH, document,
                XPathConstants.NODESET);
        if (importSchemaNodeList != null) {
            for (int i = 0; i < importSchemaNodeList.getLength(); i++) {
                Node importNode = importSchemaNodeList.item(i);
                String location = DomUtil.getAttributeValue(importNode, "schemaLocation");
                // 把要解析的xml文档读入DOM解析器
                if (!StringUtils.isBlank(location)) {
                    Document importDocument = getDBBuilder().parse(location);
                    importDocumentList.add(importDocument);
                }
            }
        }
        return importDocumentList;
    }

    /**
     * 遍历import 目前不使用
     *
     * @param definition
     * @param operationList
     * @throws WSDLException
     */
    private static void findImport(Definition definition, List<String> operationList) throws WSDLException {
        Map imports = definition.getImports();
        Iterator<Map.Entry> iterator = imports.entrySet().iterator();
        while (iterator.hasNext()) {
            Import anImport = (Import) iterator.next().getValue();
            log.info("import nameSpace:" + anImport.getNamespaceURI() + ",location:" + anImport.getLocationURI());
            // 递归
            getOperationList(anImport.getLocationURI(), operationList);
        }
    }

    /**
     * 遍历part寻找对应方法参数
     *
     * @param wsdlUrl
     * @param parts
     * @return
     * @throws WSDLException
     */
    private static List<ParameterInfo> findParamsByOperation(String wsdlUrl, Map parts) throws WSDLException {
        Document document = getDefinitionDocument(wsdlUrl);
        // 参数list
        List<ParameterInfo> paramsList = new ArrayList<ParameterInfo>();
        String schemaXPath = XPathConstant.SCHEMAXPATH;

        Iterator<Map.Entry> entryIterator = parts.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Part part = (Part) entryIterator.next().getValue();
            // RPC样式，取type，此时请求并非soap协议，不使用
            if (part.getTypeName() != null) {
                String typeName = part.getTypeName().getLocalPart();
            }
            // 文档样式，取element
            if (part.getElementName() != null) {
                String typeName = part.getElementName().getLocalPart();
                try {
                    WsdlDomUtil.getInputParams(paramsList, document, typeName, schemaXPath, null, true);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return paramsList;
    }

    /**
     * 测试XPath是否存在结点，一般自用
     *
     * @param wsdlUrl
     * @param path
     * @throws ParserConfigurationException
     * @throws XPathExpressionException
     * @throws IOException
     * @throws SAXException
     * @throws WSDLException
     */
    @Deprecated
    public static void testXpath(String wsdlUrl, String path)
            throws ParserConfigurationException, XPathExpressionException, IOException, SAXException, WSDLException {
        Document document = getDefinitionDocument(wsdlUrl);
        XPath xpath = XPathFactory.newInstance().newXPath();
        Node node = (Node) xpath.evaluate(path, document, XPathConstants.NODE);
        if (node != null) {
            System.out.println("ok");
        }
    }
}
