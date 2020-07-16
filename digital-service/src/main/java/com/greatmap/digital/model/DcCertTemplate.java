package com.greatmap.digital.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 模板信息表。第三方系统传值json数据电子证照通过json打印证照 然后盖章。需要通过模板标识找对应模板打印再盖章
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@TableName("DC_CERT_TEMPLATE")
public class DcCertTemplate extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;
    /**
     * 模板名称
     */
    private String mbbs;
    /**
     * 证照名称
     */
    private String zzmc;
    /**
     * 区县代码
     */
    private String qxdm;
    /**
     * 区县名称
     */
    private String qxmc;
    /**
     * 状态
     */
    private String zt;
    /**
     * 备注
     */
    private String bz;
    /**
     * 模板示例
     */
    private String mbsl;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMbbs() {
        return mbbs;
    }

    public void setMbbs(String mbbs) {
        this.mbbs = mbbs;
    }

    public String getZzmc() {
        return zzmc;
    }

    public void setZzmc(String zzmc) {
        this.zzmc = zzmc;
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

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getMbsl() {
        return mbsl;
    }

    public void setMbsl(String mbsl) {
        this.mbsl = mbsl;
    }

    @Override
    public String toString() {
        return "DcCertTemplate{" +
        ", id=" + id +
        ", mbbs=" + mbbs +
        ", zzmc=" + zzmc +
        ", qxdm=" + qxdm +
        ", qxmc=" + qxmc +
        ", zt=" + zt +
        ", bz=" + bz +
        ", mbsl=" + mbsl +
        "}";
    }
}
