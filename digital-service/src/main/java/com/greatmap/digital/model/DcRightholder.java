package com.greatmap.digital.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 权利人信息 
 * </p>
 *
 * @author gaorui
 * @since 2020-06-30
 */
@TableName("DC_RIGHTHOLDER")
public class DcRightholder extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.ID_WORKER)
    private String id;
    /**
     * 证照编号
     */
    @TableField("ZZBH")
    private String zzbh;
    /**
     * 证号
     */
    @TableField("ZH")
    private String zh;
    /**
     * 名称
     */
    @TableField("MC")
    private String mc;
    /**
     * 关系
     */
    @TableField("GX")
    private String gx;
    /**
     * 证件类型
     */
    @TableField("ZJLX")
    private String zjlx;
    /**
     * 权利人证件号
     */
    @TableField("ZJH")
    private String zjh;
    /**
     * 共有方式
     */
    @TableField("GYFS")
    private String gyfs;
    /**
     * 共有比例
     */
    @TableField("GYBL")
    private String gybl;
    /**
     * 是否持证
     */
    @TableField("SFCZ")
    private String sfcz;
    /**
     * 联系电话
     */
    @TableField("LXDH")
    private String lxdh;
    /**
     * 共有情况
     */
    @TableField("GYQK")
    private String gyqk;
    /**
     * 人员分类
     */
    @TableField("RYFL")
    private String ryfl;
    /**
     * 不动产单元号
     */
    @TableField("BDCDYH")
    private String bdcdyh;
    /**
     * 共有人
     */
    @TableField("GYR")
    private String gyr;
    /**
     * 共有人证件号
     */
    @TableField("GYRZJH")
    private String gyrzjh;


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

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getGx() {
        return gx;
    }

    public void setGx(String gx) {
        this.gx = gx;
    }

    public String getZjlx() {
        return zjlx;
    }

    public void setZjlx(String zjlx) {
        this.zjlx = zjlx;
    }

    public String getZjh() {
        return zjh;
    }

    public void setZjh(String zjh) {
        this.zjh = zjh;
    }

    public String getGyfs() {
        return gyfs;
    }

    public void setGyfs(String gyfs) {
        this.gyfs = gyfs;
    }

    public String getGybl() {
        return gybl;
    }

    public void setGybl(String gybl) {
        this.gybl = gybl;
    }

    public String getSfcz() {
        return sfcz;
    }

    public void setSfcz(String sfcz) {
        this.sfcz = sfcz;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getGyqk() {
        return gyqk;
    }

    public void setGyqk(String gyqk) {
        this.gyqk = gyqk;
    }

    public String getRyfl() {
        return ryfl;
    }

    public void setRyfl(String ryfl) {
        this.ryfl = ryfl;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    public String getGyr() {
        return gyr;
    }

    public void setGyr(String gyr) {
        this.gyr = gyr;
    }

    public String getGyrzjh() {
        return gyrzjh;
    }

    public void setGyrzjh(String gyrzjh) {
        this.gyrzjh = gyrzjh;
    }

    @Override
    public String toString() {
        return "DcRightholder{" +
        ", id=" + id +
        ", zzbh=" + zzbh +
        ", zh=" + zh +
        ", mc=" + mc +
        ", gx=" + gx +
        ", zjlx=" + zjlx +
        ", zjh=" + zjh +
        ", gyfs=" + gyfs +
        ", gybl=" + gybl +
        ", sfcz=" + sfcz +
        ", lxdh=" + lxdh +
        ", gyqk=" + gyqk +
        ", ryfl=" + ryfl +
        ", bdcdyh=" + bdcdyh +
        ", gyr=" + gyr +
        ", gyrzjh=" + gyrzjh +
        "}";
    }
}
