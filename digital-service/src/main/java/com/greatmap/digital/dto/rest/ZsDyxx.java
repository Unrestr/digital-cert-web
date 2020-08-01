package com.greatmap.digital.dto.rest;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gaorui
 * @create 2020-06-29 14:29
 */
@Data
public class ZsDyxx implements Serializable {

    /**
     *证照编号
     */
    private String zzbh;

    /**
     * 简称
     */
    private String jc;

    /**
     * 年份
     */
    private String nf;

    /**
     * 县
     */
    private String x;

    /**
     * 编号
     */
    private String bh;

    /**
     * 登记时间
     */
    private String djsj;

    /**
     * 权利人
     */
    private String qlr;

    /**
     * 共有情况
     */
    private String gyqk;

    /**
     * 不动产权证
     */
    private String bdcqzh;

    /**
     * 坐落
     */
    private String zl;

    /**
     * 不动产单元号
     */
    private String bdcdyh;

    /**
     * 共有情况
     */
    private String qllx;

    /**
     * 权利性质
     */
    private String qlxz;

    /**
     * 用途
     */
    private String yt;

    /**
     * 面积
     */
    private String mj;

    /**
     * 使用期限
     */
    private String syqx;

    /**
     * 权利其他状况
     */
    private String qlqtzk;

    /**
     * 附记
     */
    private String fj;

    /**
     * 查询二维码
     */
    private String cxewm;

    private String zh;
}
