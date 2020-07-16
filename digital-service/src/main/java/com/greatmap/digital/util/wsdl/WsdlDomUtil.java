package com.greatmap.digital.util.wsdl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * 第二步，使用DOM解析XML Schema，获取元素名称和类型
 * @author guoan
 */
public class WsdlDomUtil {

    private static Logger log = LoggerFactory.getLogger(WsdlDomUtil.class);

    /**
     * 查找某个操作含有的参数
     *
     * @param inputParamList
     * @param document
     * @param typeName         类型名称
     * @param xPath            到element上个结点的xpath
     * @param parentParam
     * @param isSelfDefinition 是否自己定义
     * @throws Exception
     */
    public static void getInputParams(List<ParameterInfo> inputParamList, Document document, String typeName,
                                      String xPath, ParameterInfo parentParam, boolean isSelfDefinition) throws
            Exception {
        // 是否添加结点
        boolean canAddParam = true;

        // 参数
        ParameterInfo param = new ParameterInfo();
        param.setName(typeName);

        // 寻找element节点
        String elementXPath = getElementXPath(xPath, typeName);
        Node elementNode = WsdlDomUtil.findNode(document, elementXPath);

        // 判断element是否为空，为空说明该类型名称可能是直接定义在simpleType或complexType中
        // 判断是否是自己定义，防止同名导致的堆栈溢出问题
        if (elementNode != null && isSelfDefinition) {
            // 是否含type属性
            if (DomUtil.assertNodeAttributeExist(elementNode, "type")) {
                String type = DomUtil.getNodeType(elementNode);

                // 是否是数组，XML Schema中map会定义成List<Object>的形式
                if (DomUtil.isArray(elementNode)) {
                    param.setType(SchemaDefaultType.type_array.getType());
                    param.setChildType(type);

                    // 复杂类型数组
                    if (!DomUtil.isDefaultType(elementNode)) {
                        getInputParams(inputParamList, document, type, xPath, param, false);
                    } else {
                        param.setValue(XPathConstant.VALUEDEF);
                    }
                } else {
                    param.setType(type);

                    // 复杂类型
                    if (!DomUtil.isDefaultType(elementNode)) {
                        getInputParams(inputParamList, document, type, XPathConstant.SCHEMAXPATH, param, false);
                    } else {
                        param.setValue(XPathConstant.VALUEDEF);
                    }
                }
            } else {
                // 是否是数组，XML Schema中map会定义成List<Object>的形式
                if (DomUtil.isArray(elementNode)) {
                    param.setType(SchemaDefaultType.type_array.getType());
                }

                // 如果type属性不存在，说明该结点的类型在其子结点中定义
                // 判断是simpleType还是complexType
                Node simpleTypeNode = getSimpleTypeNode(document, typeName, elementXPath, true);
                if (simpleTypeNode != null) {
                    String type = DomUtil.getNodeBase(simpleTypeNode);
                    if (DomUtil.isArray(elementNode)) {
                        param.setChildType(type);
                    } else {
                        param.setType(type);
                    }
                    param.setValue(XPathConstant.VALUEDEF);
                    // TODO: 2018/2/24
                    // 忽略限定条件
                } else {
                    // 寻找complexType
                    List<Node> nodeList = getComplexTypeSequenceElement(document, typeName, elementXPath, true);
                    if (nodeList.size() != 0) {
                        for (Node tempNode : nodeList) {
                            // 遍历element
                            String elementName = DomUtil.getNodeName(tempNode);
                            String sequenceXPath = getSequenceXPathByName(document, typeName, elementXPath, true,
                                    elementName);
                            getInputParams(inputParamList, document, elementName, sequenceXPath, param, true);
                        }
                    } else {
                        log.warn("unknown type " + typeName + ",please check your document");
                    }
                }
            }
        } else {
            // 非自己定义的Type添加到父结点
            canAddParam = false;

            // 判断是simpleType还是complexType
            Node simpleTypeNode = getSimpleTypeNode(document, typeName, xPath, false);
            if (simpleTypeNode != null) {
                String type = DomUtil.getNodeBase(simpleTypeNode);
                parentParam.setType(type);
                parentParam.setValue(XPathConstant.VALUEDEF);
                // TODO: 2018/2/24
                // 忽略限定条件
            } else {
                // 寻找complexType
                List<Node> nodeList = getComplexTypeSequenceElement(document, typeName, elementXPath, false);
                if (nodeList.size() != 0) {
                    for (Node tempNode : nodeList) {
                        // 遍历element
                        String elementName = DomUtil.getNodeName(tempNode);
                        String sequenceXPath = getSequenceXPathByName(document, typeName, elementXPath, false,
                                elementName);
                        getInputParams(inputParamList, document, elementName, sequenceXPath, parentParam, true);
                    }
                } else {
                    log.warn("unknown type " + typeName + ",please check your document");
                }
            }
        }
        if (canAddParam) {
            if (parentParam == null) {
                inputParamList.add(param);
            } else {
                parentParam.addChild(param);
            }
        }
    }

    /**
     * 获取element的xpath
     *
     * @param xPath    父结点xpath
     * @param typeName 名称
     * @return
     */
    private static String getElementXPath(String xPath, String typeName) {
        String elementXPath = xPath + "/*[local-name()='element' and @name='" + typeName + "']";
        return elementXPath;
    }

    /**
     * 获取simpleType结点
     *
     * @param document
     * @param simpleTypeName   simpleType名称，当为自己定义时可以为空
     * @param elementXPath     XPath路径，当非自己定义时可以为空
     * @param isSelfDefinition 是否为自己定义
     * @return
     * @throws Exception
     */
    private static Node getSimpleTypeNode(Document document, String simpleTypeName, String elementXPath,
                                          boolean isSelfDefinition) throws Exception {
        String simpleTypeXPath = isSelfDefinition
                ? elementXPath + "/*[local-name()='simpleType']/*[local-name()='restriction']"
                : XPathConstant.SCHEMAXPATH + "/*[local-name()='simpleType' and @name='" + simpleTypeName
                + "']/*[local-name()='restriction']";
        Node node = WsdlDomUtil.findNode(document, simpleTypeXPath);
        return node;
    }

    /**
     * 获取complexType中element
     *
     * @param document
     * @param complexTypeName  complexType名称，当为自己定义时可以为空
     * @param elementXPath     XPath路径，当非自己定义时可以为空
     * @param isSelfDefinition 是否为自己定义
     * @return
     * @throws Exception
     */
    private static List<Node> getComplexTypeSequenceElement(Document document, String complexTypeName,
                                                            String elementXPath, boolean isSelfDefinition) throws
            Exception {
        List<Node> nodeList = new ArrayList<Node>();
        // 判断是否有继承
        String extensionXpath = isSelfDefinition
                ? elementXPath + "/*[local-name()='complexType']/*[local-name()='complexContent']/*[local-name()"
                + "='extension']"
                : XPathConstant.SCHEMAXPATH + "/*[local-name()='complexType' and @name='" + complexTypeName
                + "']/*[local-name()='complexContent']/*[local-name()='extension']";
        Node extension = WsdlDomUtil.findNode(document, extensionXpath);
        if (extension != null) {
            // 存在继承
            // 添加父类
            String parentTypeName = DomUtil.getAttributeValue(extension, "base").split(":")[1];
            List<Node> parentElements = getComplexTypeSequenceElement(document, parentTypeName, elementXPath, false);
            if (parentElements != null && parentElements.size() > 0) {
                nodeList.addAll(parentElements);
            }
            // 查找自己
            String selfXpath = extensionXpath + "/*[local-name()='sequence']/*[local-name()" + "='element']";
            NodeList selfList = WsdlDomUtil.findNodeList(document, selfXpath);
            if (selfList != null && selfList.getLength() > 0) {
                nodeList.addAll(DomUtil.covertNodeListToList(selfList));
            }
        } else {
            // 查找自己的属性
            String elementsOfSequenceXpath = isSelfDefinition
                    ? elementXPath + "/*[local-name()='complexType']/*[local-name()='sequence']/*[local-name()"
                    + "='element']"
                    : XPathConstant.SCHEMAXPATH + "/*[local-name()='complexType' and @name='" + complexTypeName
                    + "']/*[local-name()='sequence']/*[local-name()='element']";
            NodeList elementsOfSequence = WsdlDomUtil.findNodeList(document, elementsOfSequenceXpath);
            nodeList = DomUtil.covertNodeListToList(elementsOfSequence);
        }
        return nodeList;
    }

    /**
     * 获取complexType中element上一级sequence的xpath
     *
     * @param document
     * @param complexTypeName  complexType名称，当为自己定义时可以为空
     * @param elementXPath     XPath路径，当非自己定义时可以为空
     * @param isSelfDefinition 是否为自己定义
     * @param elementName      结点名称
     * @return
     * @throws Exception
     */
    private static String getSequenceXPathByName(Document document, String complexTypeName, String elementXPath,
                                                 boolean isSelfDefinition, String elementName) throws Exception {
        String result = "";
        // 判断是否有继承
        String extensionXpath = isSelfDefinition
                ? elementXPath + "/*[local-name()='complexType']/*[local-name()='complexContent']/*[local-name()"
                + "='extension']"
                : XPathConstant.SCHEMAXPATH + "/*[local-name()='complexType' and @name='" + complexTypeName
                + "']/*[local-name()='complexContent']/*[local-name()='extension']";
        Node extension = WsdlDomUtil.findNode(document, extensionXpath);
        if (extension != null) {
            // 存在继承
            // 在父类查找
            String parentTypeName = DomUtil.getAttributeValue(extension, "base").split(":")[1];
            result = getSequenceXPathByName(document, parentTypeName, elementXPath, false, elementName);

            // 在自己查找
            String sequenceXPath = extensionXpath + "/*[local-name()='sequence']";
            String eleXPath = sequenceXPath + "/*[local-name()='element' and @name='" + elementName + "']";
            Node selfNode = WsdlDomUtil.findNode(document, eleXPath);
            if (selfNode != null) {
                result = sequenceXPath;
            }
        } else {
            // 在自己查找
            String sequenceXPath = isSelfDefinition
                    ? elementXPath + "/*[local-name()='complexType']/*[local-name()='sequence']"
                    : XPathConstant.SCHEMAXPATH + "/*[local-name()='complexType' and @name='" + complexTypeName
                    + "']/*[local-name()='sequence']";
            String eleXpath = sequenceXPath + "/*[local-name()='element' and @name='" + elementName + "']";
            Node selfNode = WsdlDomUtil.findNode(document, eleXpath);
            if (selfNode != null) {
                result = sequenceXPath;
            }
        }
        return result;
    }

    /**
     * 在document中查找结点，如果查找不到，则进入import结点中递归查找
     *
     * @param document
     * @param xpathStr
     * @return
     * @throws Exception
     */
    public static Node findNode(Document document, String xpathStr) throws Exception {
        XPath xpath = WsdlUtil.getXPath();
        Node node = (Node) xpath.evaluate(xpathStr, document, XPathConstants.NODE);
        if (node == null) {
            List<Document> importDocumentList = WsdlUtil.getImportDocumentList(document, xpath);
            for (Document importDoucment : importDocumentList) {
                node = findNode(importDoucment, xpathStr);
                if (node != null) {
                    return node;
                }
            }
        }
        return node;
    }

    /**
     * 在document中查找结点，如果查找不到，则进入import结点中递归查找
     *
     * @param document
     * @param xpathStr
     * @return
     * @throws Exception
     */
    public static NodeList findNodeList(Document document, String xpathStr) throws Exception {
        XPath xpath = WsdlUtil.getXPath();
        NodeList nodeList = (NodeList) xpath.evaluate(xpathStr, document, XPathConstants.NODESET);
        if (nodeList.getLength() == 0) {
            List<Document> importDocumentList = WsdlUtil.getImportDocumentList(document, xpath);
            for (Document importDoucment : importDocumentList) {
                nodeList = findNodeList(importDoucment, xpathStr);
                if (nodeList.getLength() > 0) {
                    return nodeList;
                }
            }
        }
        return nodeList;
    }
}
