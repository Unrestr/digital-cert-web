package com.greatmap.digital.dto;

import com.greatmap.framework.support.annotation.FieldLabel;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dkt on 2019-3-12.
 */
public class SealDto implements Serializable {

    @Id
    @FieldLabel("ID")
    private Long id;
    @FieldLabel("签章名称")
    private String sealName;
    @FieldLabel("签章ID")
    private String sealId;
    @FieldLabel("签章类型")
    private String sealType;
    @FieldLabel("授权次数")
    private Integer authTime;
    @FieldLabel("授权起始")
    private Date authNotBefore;
    @FieldLabel("授权终止")
    private Date authNotAfter;
    @FieldLabel("序列号")
    private String keySN;
    @FieldLabel("证书DN")
    private String certDN;
    @FieldLabel("证书序列号")
    private String certSN;
    @FieldLabel("用户ID")
    private String certOid;
    @FieldLabel("宽度")
    private Float inchWidth;
    @FieldLabel("高度")
    private Float inchHeight;
    @FieldLabel("加密签章")
    private String encBase64;
    @FieldLabel("签章图片")
    private String decBase64;
    //判断是否绑定
    @FieldLabel("证书ID")
    private String certInfoId;
    @FieldLabel("创建时间")
    private String createTime;
    @FieldLabel("使用状态，0启用，1停用")
    private Integer status;
    @FieldLabel("开始时间")
    private Date startDate;
    @FieldLabel("结束时间")
    private Date endDate;
    @FieldLabel("编定状态")
    private String bindStatus;
    @FieldLabel("签章图片")
    private String imageBase64;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSealId() {
        return sealId;
    }

    public void setSealId(String sealId) {
        this.sealId = sealId;
    }

    public String getSealName() {
        return sealName;
    }

    public void setSealName(String sealName) {
        this.sealName = sealName;
    }

    public String getSealType() {
        return sealType;
    }

    public void setSealType(String sealType) {
        this.sealType = sealType;
    }

    public Integer getAuthTime() {
        return authTime;
    }

    public void setAuthTime(Integer authTime) {
        this.authTime = authTime;
    }

    public Date getAuthNotBefore() {
        return authNotBefore;
    }

    public void setAuthNotBefore(Date authNotBefore) {
        this.authNotBefore = authNotBefore;
    }

    public Date getAuthNotAfter() {
        return authNotAfter;
    }

    public void setAuthNotAfter(Date authNotAfter) {
        this.authNotAfter = authNotAfter;
    }

    public String getKeySN() {
        return keySN;
    }

    public void setKeySN(String keySN) {
        this.keySN = keySN;
    }

    public String getCertDN() {
        return certDN;
    }

    public void setCertDN(String certDN) {
        this.certDN = certDN;
    }

    public String getCertSN() {
        return certSN;
    }

    public void setCertSN(String certSN) {
        this.certSN = certSN;
    }

    public String getCertOid() {
        return certOid;
    }

    public void setCertOid(String certOid) {
        this.certOid = certOid;
    }

    public Float getInchWidth() {
        return inchWidth;
    }

    public void setInchWidth(Float inchWidth) {
        this.inchWidth = inchWidth;
    }

    public Float getInchHeight() {
        return inchHeight;
    }

    public void setInchHeight(Float inchHeight) {
        this.inchHeight = inchHeight;
    }

    public String getEncBase64() {
        return encBase64;
    }

    public void setEncBase64(String encBase64) {
        this.encBase64 = encBase64;
    }

    public String getCertInfoId() {
        return certInfoId;
    }

    public void setCertInfoId(String certInfoId) {
        this.certInfoId = certInfoId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus) {
        this.bindStatus = bindStatus;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getDecBase64() {
        return decBase64;
    }

    public void setDecBase64(String decBase64) {
        this.decBase64 = decBase64;
    }
}
