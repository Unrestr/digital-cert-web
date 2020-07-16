package com.greatmap.digital.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 证明打印信息
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@TableName("DC_REGISTRATION_PRINT")
public class DcRegistrationPrint extends BaseModel implements Serializable {

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
     * 证明权利或事项
     */
    private String zmqlhsx;
    /**
     * 权利人(申请人)
     */
    private String qlr;
    /**
     * 义务人
     */
    private String ywr;
    /**
     * 坐落
     */
    private String zl;
    /**
     * 不动产单元号
     */
    private String bdcdyh;
    /**
     * 其他
     */
    private String qt;
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

    public String getZmqlhsx() {
        return zmqlhsx;
    }

    public void setZmqlhsx(String zmqlhsx) {
        this.zmqlhsx = zmqlhsx;
    }

    public String getQlr() {
        return qlr;
    }

    public void setQlr(String qlr) {
        this.qlr = qlr;
    }

    public String getYwr() {
        return ywr;
    }

    public void setYwr(String ywr) {
        this.ywr = ywr;
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

    public String getQt() {
        return qt;
    }

    public void setQt(String qt) {
        this.qt = qt;
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
        return "DcRegistrationPrint{" +
        ", id=" + id +
        ", zzbh=" + zzbh +
        ", jc=" + jc +
        ", nf=" + nf +
        ", x=" + x +
        ", djsj=" + djsj +
        ", bh=" + bh +
        ", zmqlhsx=" + zmqlhsx +
        ", qlr=" + qlr +
        ", ywr=" + ywr +
        ", zl=" + zl +
        ", bdcdyh=" + bdcdyh +
        ", qt=" + qt +
        ", fj=" + fj +
        ", cxewm=" + cxewm +
        "}";
    }
}
