package com.greatmap.digital.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 证照基础信息
 * @author gaorui
 * @create 2020-06-28 9:49
 */
@Data
public class CertBaseDto implements Serializable {

    /**
     * 证照类型代码
     */
    private String zzlxdm;

    /**
     * 证照类型名称
     */
    private String zzlxmc;

    /**
     * 证照编号
     */
    private  String zzbh;

    /**
     * 证照标识
     */
    private  String zzbs;

    /**
     * 不动产权证号
     */
    private String bdcqzh;

    /**
     * 不动产登记证明号
     */
    private String bdcdjzmh;

    /**
     * 颁发原因
     */
    private  String bfyy;

    /**
     * 证照有效起始时间
     */
    private Date zzyxqssj;

    /**
     *证照有效结束时间
     */
    private Date zzyxjzsj;

    /**
     *颁发机构
     */
    private String bfjg;

    /**
     *颁发机构代码
     */
    private String bfjgdm;

    /**
     *注销日期
     */
    private Date zxrq;

    /**
     *注销原因
     */
    private String zzyy;

    /**
     *证号
     */
    private String zh;

    /**
     *证照分类
     */
    private String zzfl;
}
