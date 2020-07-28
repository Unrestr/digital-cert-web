package com.greatmap.digital.dto.rest;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 证照签章传值信息V1.0
 * @author gaorui
 * @create 2020-06-29 11:23
 */
@Data
public class CertDto implements Serializable {
    /**
     * 权利人信息
     */
    private List<QlrDto> qlrList;

    /**
     * 权利信息
     */
    private QlxxDto qlxx;

    /**
     * 单元信息
     */
    private List<DyxxDto> dyxxList;

    /**
     * 证书基础信息
     */
    private JcxxDto jcxx;

    /**
     * 证书打印信息(证书或者证明打印信息需一个有值)
     */
    private ZsDyxx zsxx;

    /**
     * 证明打印信息
     */
    private ZmDyxx zmxx;

    /**
     * 关联信息
     */
    private Glxx  glxx;

    /**
     * 不动产权证号
     */
    private String zh;

    /**
     * 证照名称
     */
    private String zzmc;

    /**
     * 证照类型 1：证书 2：证明 3...
     */
    private String zzlx;

    /**
     * 区县代码
     */
    private String qxdm;

    /**
     * 区县名称
     */
    private String qxmc;

    /**
     * 用户名
     */
    private String yhm;

    /**
     * 登记机构
     */
    private String djjg;

    /**
     * ip
     */
    private String ip;

    /**
     * 业务号
     */
    private String ywh;

}
