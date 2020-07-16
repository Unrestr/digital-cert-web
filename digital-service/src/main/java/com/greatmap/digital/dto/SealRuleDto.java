package com.greatmap.digital.dto;

import com.greatmap.framework.support.annotation.FieldLabel;

import java.io.Serializable;

/**
 * 签章规则传参dto
 * @author yc
 */
public class SealRuleDto implements Serializable {

    @FieldLabel("规则ID")
    private String templateId;

    @FieldLabel("规则名称")
    private String templateName;

    @FieldLabel("签章ID")
    private String sealId;

    @FieldLabel("盖章位置类型")
    private String sealPositionType;

    @FieldLabel("印章加盖位置")
    private String spacePage;

    @FieldLabel("加盖位置-横向")
    private Integer x;

    @FieldLabel("加盖位置-纵向")
    private Integer y;

    @FieldLabel("文档类型（O 单面/ E 双面）")
    private String documentType;

    @FieldLabel("骑缝章模式")
    private String ridingSealMode;

    @FieldLabel("盖章起始页")
    private Integer startPage;

    @FieldLabel("盖章结束页")
    private Integer endPage;

    @FieldLabel("纵向偏移量")
    private Integer offsetY;

    @FieldLabel("CA类型")
    private String calx;

    @FieldLabel("行政区化代码")
    private String qxdm;

    @FieldLabel("行政区划名称")
    private String qxmc;

    public String getSealId() {
        return sealId;
    }

    public void setSealId(String sealId) {
        this.sealId = sealId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getSealPositionType() {
        return sealPositionType;
    }

    public void setSealPositionType(String sealPositionType) {
        this.sealPositionType = sealPositionType;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(Integer offsetY) {
        this.offsetY = offsetY;
    }

    public String getSpacePage() {
        return spacePage;
    }

    public void setSpacePage(String spacePage) {
        this.spacePage = spacePage;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public void setStartPage(Integer startPage) {
        this.startPage = startPage;
    }

    public Integer getEndPage() {
        return endPage;
    }

    public void setEndPage(Integer endPage) {
        this.endPage = endPage;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getRidingSealMode() {
        return ridingSealMode;
    }

    public void setRidingSealMode(String ridingSealMode) {
        this.ridingSealMode = ridingSealMode;
    }

    public String getQxdm() {
        return qxdm;
    }

    public void setQxdm(String qxdm) {
        this.qxdm = qxdm;
    }

    public String getCalx() {
        return calx;
    }

    public void setCalx(String calx) {
        this.calx = calx;
    }

    public String getQxmc() {
        return qxmc;
    }

    public void setQxmc(String qxmc) {
        this.qxmc = qxmc;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
