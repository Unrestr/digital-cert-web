package com.greatmap.digital.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * <p>
 * 证照基础信息表  证照和单元信息  权利人信息存在1:N关系      权利信息1:1
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@TableName("DC_CERT_INFO")
public class DcCertInfo extends BaseModel implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;
    /**
     * 证照标识码
     */
    @TableField("ZZBSM")
    private String zzbsm;
    /**
     * 证照编号
     */
    private String zzbh;
    /**
     * 证照名称
     */
    private String zzmc;
    /**
     * 登记机构
     */
    private String djjg;
    /**
     * 登记时间
     */
    private Date djsj;
    /**
     * 登记原因
     */
    private String djyy;
    /**
     * 登记机构代码
     */
    private String djjgdm;
    /**
     * 证号
     */
    private String zh;
    /**
     * 证照分类
     */
    private String zzfl;
    /**
     * 区县代码
     */
    private String qxdm;
    /**
     * 区县名称
     */
    private String qxmc;
    /**
     * 证照注销日期
     */
    private Date zzzxrq;
    /**
     * 注销原因
     */
    private String zxyy;
    /**
     * 证照注销机构
     */
    private String zxjg;
    /**
     * 证照注销机构代码
     */
    private String zxjgdm;
    /**
     * 颁发人员
     */
    private String bfry;
    /**
     * 颁发时间
     */
    private Date bfsj;
    /**
     * 状态
     */
    private String zt;
    /**
     * 证照来源
     */
    private String zzly;

    /**
     * 业务号
     */
    private String ywh;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZzbsm() {
        return zzbsm;
    }

    public void setZzbsm(String zzbsm) {
        this.zzbsm = zzbsm;
    }

    public String getZzbh() {
        return zzbh;
    }

    public void setZzbh(String zzbh) {
        this.zzbh = zzbh;
    }

    public String getZzmc() {
        return zzmc;
    }

    public void setZzmc(String zzmc) {
        this.zzmc = zzmc;
    }

    public String getDjjg() {
        return djjg;
    }

    public void setDjjg(String djjg) {
        this.djjg = djjg;
    }

    public Date getDjsj() {
        return djsj;
    }

    public void setDjsj(Date djsj) {
        this.djsj = djsj;
    }

    public String getDjyy() {
        return djyy;
    }

    public void setDjyy(String djyy) {
        this.djyy = djyy;
    }

    public String getDjjgdm() {
        return djjgdm;
    }

    public void setDjjgdm(String djjgdm) {
        this.djjgdm = djjgdm;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getZzfl() {
        return zzfl;
    }

    public void setZzfl(String zzfl) {
        this.zzfl = zzfl;
    }

    public String getQxdm() {
        return qxdm;
    }

    public void setQxdm(String qxdm) {
        this.qxdm = qxdm;
    }

    public String getQxmc() {
        return qxmc;
    }

    public void setQxmc(String qxmc) {
        this.qxmc = qxmc;
    }

    public Date getZzzxrq() {
        return zzzxrq;
    }

    public void setZzzxrq(Date zzzxrq) {
        this.zzzxrq = zzzxrq;
    }

    public String getZxyy() {
        return zxyy;
    }

    public void setZxyy(String zxyy) {
        this.zxyy = zxyy;
    }

    public String getZxjg() {
        return zxjg;
    }

    public void setZxjg(String zxjg) {
        this.zxjg = zxjg;
    }

    public String getZxjgdm() {
        return zxjgdm;
    }

    public void setZxjgdm(String zxjgdm) {
        this.zxjgdm = zxjgdm;
    }

    public String getBfry() {
        return bfry;
    }

    public void setBfry(String bfry) {
        this.bfry = bfry;
    }

    public Date getBfsj() {
        return bfsj;
    }

    public void setBfsj(Date bfsj) {
        this.bfsj = bfsj;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getZzly() {
        return zzly;
    }

    public void setZzly(String zzly) {
        this.zzly = zzly;
    }

    public String getYwh() {
        return ywh;
    }

    public void setYwh(String ywh) {
        this.ywh = ywh;
    }
}
