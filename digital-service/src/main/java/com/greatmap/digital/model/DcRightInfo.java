package com.greatmap.digital.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 权利信息
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@TableName("DC_RIGHT_INFO")
public class DcRightInfo extends BaseModel implements Serializable {

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
     * 证照类型
     */
    private String zzlx;
    /**
     * 权利性质
     */
    private String qlxz;
    /**
     * 使用权起始时间
     */
    private Date syqqssj;
    /**
     * 使用权结束时间
     */
    private Date syqjssj;
    /**
     * 权利其他状况
     */
    private String qlqtzk;
    private String tdqllx;
    /**
     * 土地权利性质
     */
    private String tdqlxz;
    /**
     * 土地权利人性质(1：全体业主共有 2：个人所有)
     */
    private String tdqlrxz;
    /**
     * 附记
     */
    private String fj;


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

    public String getZzlx() {
        return zzlx;
    }

    public void setZzlx(String zzlx) {
        this.zzlx = zzlx;
    }

    public String getQlxz() {
        return qlxz;
    }

    public void setQlxz(String qlxz) {
        this.qlxz = qlxz;
    }

    public Date getSyqqssj() {
        return syqqssj;
    }

    public void setSyqqssj(Date syqqssj) {
        this.syqqssj = syqqssj;
    }

    public Date getSyqjssj() {
        return syqjssj;
    }

    public void setSyqjssj(Date syqjssj) {
        this.syqjssj = syqjssj;
    }

    public String getQlqtzk() {
        return qlqtzk;
    }

    public void setQlqtzk(String qlqtzk) {
        this.qlqtzk = qlqtzk;
    }

    public String getTdqllx() {
        return tdqllx;
    }

    public void setTdqllx(String tdqllx) {
        this.tdqllx = tdqllx;
    }

    public String getTdqlxz() {
        return tdqlxz;
    }

    public void setTdqlxz(String tdqlxz) {
        this.tdqlxz = tdqlxz;
    }

    public String getTdqlrxz() {
        return tdqlrxz;
    }

    public void setTdqlrxz(String tdqlrxz) {
        this.tdqlrxz = tdqlrxz;
    }

    public String getFj() {
        return fj;
    }

    public void setFj(String fj) {
        this.fj = fj;
    }

    @Override
    public String toString() {
        return "DcRightInfo{" +
        ", id=" + id +
        ", zzbh=" + zzbh +
        ", zzlx=" + zzlx +
        ", qlxz=" + qlxz +
        ", syqqssj=" + syqqssj +
        ", syqjssj=" + syqjssj +
        ", qlqtzk=" + qlqtzk +
        ", tdqllx=" + tdqllx +
        ", tdqlxz=" + tdqlxz +
        ", tdqlrxz=" + tdqlrxz +
        ", fj=" + fj +
        "}";
    }
}
