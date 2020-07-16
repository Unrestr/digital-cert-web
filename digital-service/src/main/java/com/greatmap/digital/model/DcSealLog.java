package com.greatmap.digital.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 日志信息
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@TableName("DC_SEAL_LOG")
@Data
@ToString
public class DcSealLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;
    /**
     * 操作人员
     */
    private String czry;
    /**
     * 证号
     */
    private String zh;
    /**
     * 所属机构
     */
    private String ssjg;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 请求时间
     */
    private Date qqsj;
    /**
     * 操作详情
     */
    private String czxq;

    /**
     * 响应结果
     */
    private String xyjg;

}
