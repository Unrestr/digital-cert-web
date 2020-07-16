package com.greatmap.digital.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 关了证书信息
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@TableName("DC_CERT_ASSOTYPE")
public class DcCertAssotype extends BaseModel implements Serializable {

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
     * 业务号
     */
    private String ywh;
    /**
     * 证号
     */
    private String zh;
    /**
     * 关联证号
     */
    private String glzh;
    /**
     * 关联证照编号
     */
    private String glzhbh;
    /**
     * 关联类型(共有关联 沿革关联)
     */
    private String gllx;


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

    public String getGlzh() {
        return glzh;
    }

    public void setGlzh(String glzh) {
        this.glzh = glzh;
    }

    public String getGlzhbh() {
        return glzhbh;
    }

    public void setGlzhbh(String glzhbh) {
        this.glzhbh = glzhbh;
    }

    public String getGllx() {
        return gllx;
    }

    public void setGllx(String gllx) {
        this.gllx = gllx;
    }

    public String getYwh() {
        return ywh;
    }

    public void setYwh(String ywh) {
        this.ywh = ywh;
    }
}
