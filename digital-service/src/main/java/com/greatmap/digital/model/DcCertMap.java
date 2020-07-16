package com.greatmap.digital.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 附图信息 宗地图和房产分户图
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@TableName("DC_CERT_MAP")
public class DcCertMap extends BaseModel implements Serializable {

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
     * 图片名称
     */
    private String tpmc;
    /**
     * 图片信息
     */
    private String tpxx;
    /**
     * 界址点
     */
    private String jzd;
    /**
     * 不动产单元号
     */
    private String bdcdyh;


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

    public String getTpmc() {
        return tpmc;
    }

    public void setTpmc(String tpmc) {
        this.tpmc = tpmc;
    }

    public String getTpxx() {
        return tpxx;
    }

    public void setTpxx(String tpxx) {
        this.tpxx = tpxx;
    }

    public String getJzd() {
        return jzd;
    }

    public void setJzd(String jzd) {
        this.jzd = jzd;
    }

    public String getBdcdyh() {
        return bdcdyh;
    }

    public void setBdcdyh(String bdcdyh) {
        this.bdcdyh = bdcdyh;
    }

    @Override
    public String toString() {
        return "DcCertMap{" +
        ", id=" + id +
        ", zzbh=" + zzbh +
        ", tpmc=" + tpmc +
        ", tpxx=" + tpxx +
        ", jzd=" + jzd +
        ", bdcdyh=" + bdcdyh +
        "}";
    }
}
