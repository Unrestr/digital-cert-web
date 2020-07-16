package com.greatmap.digital.service.dcThirdBiz;

import com.alibaba.fastjson.JSONObject;
import com.greatmap.digital.excepition.DigitalThirdException;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;


/**
 * @author sms
 * @date 2020-07-01
 */
public class ParseVerifyCXml {

    private String content;
    private Document document;
    private Element root;

    private final static String NODE_CONTENT = "content";

    /**
     * @param content
     */
    public ParseVerifyCXml(String content) {
        if (StringUtils.isEmpty(content)) {
            throw new DigitalThirdException("构造解析xml对象实例失败，传入参数为空！");
        }
        this.content = content;
        try {
            this.document = DocumentHelper.parseText(this.content);
            root = this.document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new DigitalThirdException("读取xml内容转换为Document对象失败！");
        }
    }

    /**
     * 解析字符串内容
     */
    public JSONObject parseContent() {
        Node node = root.selectSingleNode(NODE_CONTENT);
        if (node == null) {
            return null;
        }
        return JSONObject.parseObject(node.getStringValue());
    }


}
