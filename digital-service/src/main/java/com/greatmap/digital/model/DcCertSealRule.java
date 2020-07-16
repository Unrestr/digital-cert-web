package com.greatmap.digital.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *  签章信息表
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
@TableName("DC_CERT_SEAL_RULE")
@Data
public class DcCertSealRule extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "ID",type = IdType.ID_WORKER)
    private Long id;
    /**
     * 证照名称
     */
    @TableField("ZZMC")
    private String zzmc;
    /**
     * 证照类型
     */
    @TableField("ZZLX")
    private String zzlx;
    /**
     * 状态
     */
    @TableField("ZT")
    private Integer zt;
    /**
     * 0注销章1有效章
     */
    @TableField("SFZXZ")
    private Integer sfzxz;
    /**
     * 签章规则名称
     */
    @TableField("QZGZMC")
    private String qzgzmc;
    /**
     * 签章规则ID
     */
    @TableField("QZGZID")
    private String qzgzid;
    /**
     * 区县代码
     */
    @TableField("QXDM")
    private String qxdm;
    /**
     * 区县名称
     */
    @TableField("QXMC")
    private String qxmc;

}
