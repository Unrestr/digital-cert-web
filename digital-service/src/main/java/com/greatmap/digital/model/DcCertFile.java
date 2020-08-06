package com.greatmap.digital.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
@TableName("DC_CERT_FILE")
@Data
@ToString
public class DcCertFile extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.ID_WORKER)
    private String id;
    /**
     * 文件id
     */
    @TableField("WJID")
    private String wjid;
    /**
     * 文件名称
     */
    @TableField("WJMC")
    private String wjmc;
    /**
     * 相对路径
     */
    @TableField("XDML")
    private String xdml;
    /**
     * 映射地址
     */
    @TableField("YSDZ")
    private String ysdz;
    /**
     * 证照编号
     */
    @TableField("ZZBH")
    private String zzbh;
    /**
     * 证照id
     */
    @TableField("ZZID")
    private String zzid;
    /**
     * 证照名称
     */
    @TableField("ZZMC")
    private String zzmc;
    /**
     * 文件上传时间
     */
    @TableField("SCSJ")
    private Date scsj;

    /**
     * Ofd文件
     */
    @TableField("OFDID")
    private String ofdid;
}
