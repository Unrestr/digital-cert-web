package com.greatmap.digital.util.wsdl;

import com.greatmap.digital.excepition.DigitalException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.*;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * DOM解析工具类
 * @author guoan
 */
public class DomUtil {
    /**
     * Serialise the supplied W3C DOM subtree.
     * <p/>
     * The output is unformatted.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(NodeList nodeList) throws DOMException {
        return serialize(nodeList, false);
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param node   The DOM node to be serialized.
     * @param format Format the output.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(final Node node, boolean format) throws DOMException {
        StringWriter writer = new StringWriter();
        serialize(node, format, writer);
        return writer.toString();
    }

    /**
     * the supplied W3C DOM subtree.
     *
     * @param node   The DOM node to be serialized.
     * @param format Format the output.
     * @param writer The target writer for serialization.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static void serialize(final Node node, boolean format, Writer writer) throws DOMException {
        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            serialize(node.getChildNodes(), format, writer);
        } else {
            serialize(new NodeList() {
                @Override
                public Node item(int index) {
                    return node;
                }

                @Override
                public int getLength() {
                    return 1;
                }
            }, format, writer);
        }
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @param format   Format the output.
     * @return The subtree in serailised form.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static String serialize(NodeList nodeList, boolean format) throws DOMException {
        StringWriter writer = new StringWriter();
        serialize(nodeList, format, writer);
        return writer.toString();
    }

    /**
     * Serialise the supplied W3C DOM subtree.
     *
     * @param nodeList The DOM subtree as a NodeList.
     * @param format   Format the output.
     * @param writer   The target writer for serialization.
     * @throws DOMException Unable to serialise the DOM.
     */
    public static void serialize(NodeList nodeList, boolean format, Writer writer) throws DOMException {
        try {
            // nodeList为空
            if (nodeList == null) {
                throw new IllegalArgumentException(
                        "XmlUtil.serialize(NodeList nodeList, boolean format, Writer writer)中参数nodeList为");
            }
            TransformerFactory factory = TransformerFactory.newInstance();
            // 设置格式化
            if (format) {
                try {
                    // (用于jdk1.5)设置缩进，如果运行在1.4上会抛出异常
                    factory.setAttribute("indent-number", new Integer(4));
                } catch (Exception e) {
                    throw new DigitalException("设置TransformerFactory的缩进量为4失败:" + e.getMessage());
                }
            }
            // 取得transformer
            Transformer transformer = factory.newTransformer();
            // 设置编码格式
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            // 设置是否忽略xml声明片段
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            if (format) {
                // 设置xml是否进行缩进处理
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                // (用于jdk1.4)也可以这样写http://xml.apache.org/xslt}indent-amount，区别只是命名空间不一样
                transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
            }
            // 处理所有的结点
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (isTextNode(node)) {
                    // 如果是文本结点，则直接输出
                    writer.write(node.getNodeValue());
                } else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                    writer.write(((Attr) node).getValue());
                } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                    transformer.transform(new DOMSource(node), new StreamResult(writer));
                }
            }
        } catch (Exception e) {
            DOMException domExcep = new DOMException(DOMException.INVALID_ACCESS_ERR,
                    "Unable to serailise DOM subtree.");
            domExcep.initCause(e);
            throw domExcep;
        }
    }

    /**
     * 判断node是否为文本结点
     *
     * @param node
     * @return
     */
    public static boolean isTextNode(Node node) {
        short nodeType;

        if (node == null) {
            return false;
        }
        nodeType = node.getNodeType();

        return nodeType == Node.CDATA_SECTION_NODE || nodeType == Node.TEXT_NODE;
    }

    /**
     * 判断结点的属性是否存在
     *
     * @param node
     * @param attributeName
     * @return
     */
    public static boolean assertNodeAttributeExist(Node node, String attributeName) {
        boolean result = false;
        if (node != null) {
            NamedNodeMap attributeMap = node.getAttributes();
            Node attributeNode = attributeMap.getNamedItem(attributeName);
            if (attributeNode != null) {
                if (StringUtils.isNotEmpty(attributeNode.getNodeValue())) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * 将NodeList转换为List<Node>
     *
     * @param nodeList
     * @return
     * @throws Exception
     */
    public static List<Node> covertNodeListToList(NodeList nodeList) throws Exception {
        List<Node> list = new ArrayList<Node>();
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                list.add(nodeList.item(i));
            }
        }
        return list;
    }

    /**
     * 得到结点的属性值
     *
     * @param node
     * @param attributeName
     * @return
     * @throws Exception
     */
    public static String getAttributeValue(Node node, String attributeName) throws Exception {
        String attributeValue = "";
        if (node != null) {
            NamedNodeMap attributeMap = node.getAttributes();
            Node attributeNode = attributeMap.getNamedItem(attributeName);
            if (attributeNode != null) {
                attributeValue = attributeNode.getNodeValue();
            }
        }
        return attributeValue;
    }

    /**
     * 得到结点的name属性值
     *
     * @param node
     * @return
     * @throws Exception
     */
    public static String getNodeName(Node node) throws Exception {
        return getAttributeValue(node, "name");
    }

    /**
     * 得到结点的type属性值
     *
     * @param node
     * @return
     * @throws Exception
     */
    public static String getNodeType(Node node) throws Exception {
        String type = getAttributeValue(node, "type");
        if (StringUtils.isNotEmpty(type)) {
            if (type.indexOf(":") >= 0) {
                return type.split(":")[1];
            } else {
                return type;
            }
        }
        return "";
    }

    /**
     * 得到结点的base属性值，目前仅用于simpletype结点下restriction结点
     *
     * @param node
     * @return
     * @throws Exception
     */
    public static String getNodeBase(Node node) throws Exception {
        String type = getAttributeValue(node, "base");
        if (StringUtils.isNotEmpty(type)) {
            if (type.indexOf(":") >= 0) {
                return type.split(":")[1];
            } else {
                return type;
            }
        }
        return "";
    }

    /**
     * 得到结点的maxOccurs属性值
     *
     * @param node
     * @return
     * @throws Exception
     */
    public static String getNodeMaxOccurs(Node node) throws Exception {
        return getAttributeValue(node, "maxOccurs");
    }

    /**
     * 得到结点的minOccurs属性值
     *
     * @param node
     * @return
     * @throws Exception
     */
    public static String getNodeMinOccurs(Node node) throws Exception {
        return getAttributeValue(node, "minOccurs");
    }

    /**
     * 判断type是否为schema默认的类型
     *
     * @param node
     * @return
     * @throws Exception
     */
    public static boolean isDefaultType(Node node) throws Exception {
        boolean result = false;
        if (node != null) {
            String type = DomUtil.getNodeType(node);
            SchemaDefaultType[] defaultTypes = SchemaDefaultType.values();
            for (int i = 0; i < defaultTypes.length; i++) {
                SchemaDefaultType defaultType = defaultTypes[i];
                if (type.equals(defaultType.getType())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 判断element是否为数组类型
     *
     * @param node
     * @return
     * @throws Exception
     */
    public static boolean isArray(Node node) throws Exception {
        boolean result = false;
        if (node != null) {
            String minOccurs = DomUtil.getNodeMinOccurs(node);
            String maxOccurs = DomUtil.getNodeMaxOccurs(node);
            boolean marker = maxOccurs != null && !"".equals(maxOccurs)
                    && ("unbounded".equals(maxOccurs) || Integer.valueOf(maxOccurs) > 1);
            if (marker) {
                result = true;
            }
        }
        return result;
    }

    public static void removeTextNode(Node root) {
        if (root.hasChildNodes()) {
            NodeList children = root.getChildNodes();
            int count = children.getLength();
            for (int i = count - 1; i >= 0; i--) {
                // 需要从后往前删除，防止出现沙漏效应
                Node child = children.item(i);
                if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.COMMENT_NODE) {
                    child.getParentNode().removeChild(child);
                } else {
                    removeTextNode(child);
                }
            }
        }
    }
}
