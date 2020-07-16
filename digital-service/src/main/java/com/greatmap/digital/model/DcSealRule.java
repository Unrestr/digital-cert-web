package com.greatmap.digital.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
@TableName("DC_SEAL_RULE")
@Data
public class DcSealRule extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则名称
     */
    @TableField("QZGZMC")
    private String qzgzmc;
    /**
     * 签章规则类型(骑缝章,普通章)
     */
    @TableField("QZGZLX")
    private String qzgzlx;
    /**
     * 签章ID
     */
    @TableField("SEALID")
    private String sealid;
    /**
     * 横轴偏移
     */
    @TableField("LEFTX")
    private Integer leftx;
    /**
     * 纵轴偏移
     */
    @TableField("LEFTY")
    private Integer lefty;
    /**
     * 签章宽度
     */
    @TableField("WIDTH")
    private Integer width;
    /**
     * 签章高度
     */
    @TableField("HEIGHT")
    private Integer height;
    /**
     * 页码
     */
    @TableField("PAGE")
    private String page;
    /**
     * CA类型
     */
    @TableField("CALX")
    private String calx;
    /**
     * 签章信息
     */
    @TableField("QZXX")
    private String qzxx;
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
    /**
     * ID
     */
    @TableId(value = "ID",type = IdType.ID_WORKER)
    private Long id;

    /**
     * 状态
     */
    @TableField("ZT")
    private String zt;
}
