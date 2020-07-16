package com.greatmap.digital.dto.rest;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gaorui
 * @create 2020-06-29 14:16
 */
@Data
public class JcxxDto implements Serializable {

    /**
     * 登记机构
     */
    private String djjg;

    /**
     * 登记机构代码
     */
    private String djjgdm;

    /**
     * 登记机构
     */
    private Date djsj;

    /**
     * 登记原因
     */
    private String djyy;

    /**
     * 证照注销日期
     */
    private Date zzzxrq;

    /**
     * 注销原因
     */
    private String zxyy;

    /**
     * 注销机构
     */
    private String zxjg;

    /**
     * 注销机构
     */
    private String zxjgdm;


    /**
     * 注销章(注销流程)
     */
    private String sign;

    /**
     * 注销人员
     */
    private String zxry;

    /**
     * 业务号
     */
    private String ywh;

    /**
     * 颁发人员
     */
    private String bfry;

    /**
     * 颁发时间
     */
    private Date bfsj;

    /**
     * 证照来源
     */
    private String zzly;

    /**
     * 原证号(上一笔证号)
     */
    private String yzh;
}
