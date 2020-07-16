package com.greatmap.digital.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 证书打印信息
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@TableName("DC_PROPERTY_PRINT")
public class DcPropertyPrint extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;
    /**
     * 证照编号
     */
    private String zzbh;
    /**
     * 简称
     */
    private String jc;
    /**
     * 年份
     */
    private String nf;
    /**
     * 县
     */
    private String x;
    /**
     * 登记时间
     */
    private Date djsj;
    /**
     * 编号
     */
    private String bh;
    /**
     * 权利人
     */
    private String qlr;
    /**
     * 共有情况
     */
    private String gyqk;
    /**
     * 坐落
     */
    private String zl;
    /**
     * 不动产单元号
     */
    private String bdcdyh;
    /**
     * 权利类型
     */
    private String qllx;
    /**
     * 权利性质
     */
    private String qlxz;
    /**
     * 用途
     */
    private String yt;
    /**
     * 面积
     */
    private Long mj;
    /**
     * 使用期限
     */
    private Date syqs;
    /**
     * 权利其他状况
     */
    private String qlqtqk;
    /**
     * 附记
     */
    private String fj;
    /**
     * 查询二维码
     */
    private String cxewm;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZzbh() {
        return zzbh;
    }

    public void setZzbh(String zzbh) {
        this.zzbh = zzbh;
    }

    public String getJc() {
        return jc;
    }

    public void setJc(String jc) {
        this.jc = jc;
    }

    public String getNf() {
        return nf;
    }

    public void setNf(String nf) {
        this.nf = nf;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public Date getDjsj() {
        return djsj;
    }

    public void setDjsj(Date djsj) {
        this.djsj = djsj;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getQlr() {
        return qlr;
    }

    public void setQlr(String qlr) {
        this.qlr = qlr;
    }

    public String getGyqk() {
        return gyqk;
    }

    public void setGyqk(String gyqk) {
        this.gyqk = gyqk;
    }

    public String getZl() {
        return zl;
    }

    public void setZl(String zl) {
        this.zl = zl;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getQllx() {
        return qllx;
    }

    public void setQllx(String qllx) {
        this.qllx = qllx;
    }

    public String getQlxz() {
        return qlxz;
    }

    public void setQlxz(String qlxz) {
        this.qlxz = qlxz;
    }

    public String getYt() {
        return yt;
    }

    public void setYt(String yt) {
        this.yt = yt;
    }

    public Long getMj() {
        return mj;
    }

    public void setMj(Long mj) {
        this.mj = mj;
    }

    public Date getSyqs() {
        return syqs;
    }

    public void setSyqs(Date syqs) {
        this.syqs = syqs;
    }

    public String getQlqtqk() {
        return qlqtqk;
    }

    public void setQlqtqk(String qlqtqk) {
        this.qlqtqk = qlqtqk;
    }

    public String getFj() {
        return fj;
    }

    public void setFj(String fj) {
        this.fj = fj;
    }

    public String getCxewm() {
        return cxewm;
    }

    public void setCxewm(String cxewm) {
        this.cxewm = cxewm;
    }

    @Override
    public String toString() {
        return "DcPropertyPrint{" +
        ", id=" + id +
        ", zzbh=" + zzbh +
        ", jc=" + jc +
        ", nf=" + nf +
        ", x=" + x +
        ", djsj=" + djsj +
        ", bh=" + bh +
        ", qlr=" + qlr +
        ", gyqk=" + gyqk +
        ", zl=" + zl +
        ", bdcdyh=" + bdcdyh +
        ", qllx=" + qllx +
        ", qlxz=" + qlxz +
        ", yt=" + yt +
        ", mj=" + mj +
        ", syqs=" + syqs +
        ", qlqtqk=" + qlqtqk +
        ", fj=" + fj +
        ", cxewm=" + cxewm +
        "}";
    }
}
