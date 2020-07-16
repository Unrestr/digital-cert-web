package com.greatmap.digital.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 单元信息
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@TableName("DC_UNIT_INFO")
public class DcUnitInfo extends BaseModel implements Serializable {

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
     * 证号
     */
    private String zh;
    /**
     * 不动产登记证明号
     */
    private String bdcdjzmh;
    /**
     * 不动产单元号
     */
    private String bdcdyh;
    /**
     * 坐落
     */
    private String zl;
    /**
     * 用途(单独房屋 单独土地存值 多个用/分隔)
     */
    private String yt;
    /**
     * 土地用途(房地一体存土地用途)
     */
    private String tdyt;
    /**
     * 面积(房屋存建筑面积 土地存使用权面积)
     */
    private Double mj;
    /**
     * 土地使用权面积
     */
    private Double tdsyqmj;
    /**
     * 分摊面积
     */
    private Double ftmj;
    /**
     * 套内面积
     */
    private Double tnmj;
    /**
     * 面积单位
     */
    private String mjdw;
    /**
     * 用途代码(多个用^分隔)
     */
    private String ytdm;
    /**
     * 土地用途代码(多个用^分隔)
     */
    private String tdytdm;
    /**
     * 土地面积单位
     */
    private String tdmjdw;
    /**
     * 面积单位代码
     */
    private String mjdwdm;
    /**
     * 土地面积单位代码
     */
    private String tdmjdwdm;
    /**
     * 状态
     */
    private String zt;


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

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getBdcdjzmh() {
        return bdcdjzmh;
    }

    public void setBdcdjzmh(String bdcdjzmh) {
        this.bdcdjzmh = bdcdjzmh;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getZl() {
        return zl;
    }

    public void setZl(String zl) {
        this.zl = zl;
    }

    public String getYt() {
        return yt;
    }

    public void setYt(String yt) {
        this.yt = yt;
    }

    public String getTdyt() {
        return tdyt;
    }

    public void setTdyt(String tdyt) {
        this.tdyt = tdyt;
    }

    public Double getMj() {
        return mj;
    }

    public void setMj(Double mj) {
        this.mj = mj;
    }

    public Double getTdsyqmj() {
        return tdsyqmj;
    }

    public void setTdsyqmj(Double tdsyqmj) {
        this.tdsyqmj = tdsyqmj;
    }

    public Double getFtmj() {
        return ftmj;
    }

    public void setFtmj(Double ftmj) {
        this.ftmj = ftmj;
    }

    public Double getTnmj() {
        return tnmj;
    }

    public void setTnmj(Double tnmj) {
        this.tnmj = tnmj;
    }

    public String getMjdw() {
        return mjdw;
    }

    public void setMjdw(String mjdw) {
        this.mjdw = mjdw;
    }

    public String getYtdm() {
        return ytdm;
    }

    public void setYtdm(String ytdm) {
        this.ytdm = ytdm;
    }

    public String getTdytdm() {
        return tdytdm;
    }

    public void setTdytdm(String tdytdm) {
        this.tdytdm = tdytdm;
    }

    public String getTdmjdw() {
        return tdmjdw;
    }

    public void setTdmjdw(String tdmjdw) {
        this.tdmjdw = tdmjdw;
    }

    public String getMjdwdm() {
        return mjdwdm;
    }

    public void setMjdwdm(String mjdwdm) {
        this.mjdwdm = mjdwdm;
    }

    public String getTdmjdwdm() {
        return tdmjdwdm;
    }

    public void setTdmjdwdm(String tdmjdwdm) {
        this.tdmjdwdm = tdmjdwdm;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    @Override
    public String toString() {
        return "DcUnitInfo{" +
        ", id=" + id +
        ", zzbh=" + zzbh +
        ", zh=" + zh +
        ", bdcdjzmh=" + bdcdjzmh +
        ", bdcdyh=" + bdcdyh +
        ", zl=" + zl +
        ", yt=" + yt +
        ", tdyt=" + tdyt +
        ", mj=" + mj +
        ", tdsyqmj=" + tdsyqmj +
        ", ftmj=" + ftmj +
        ", tnmj=" + tnmj +
        ", mjdw=" + mjdw +
        ", ytdm=" + ytdm +
        ", tdytdm=" + tdytdm +
        ", tdmjdw=" + tdmjdw +
        ", mjdwdm=" + mjdwdm +
        ", tdmjdwdm=" + tdmjdwdm +
        ", zt=" + zt +
        "}";
    }
}
