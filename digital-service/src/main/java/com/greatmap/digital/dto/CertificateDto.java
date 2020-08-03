package com.greatmap.digital.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * @author gaorui
 * @create 2020-07-15 17:23
 */
@Data
public class CertificateDto implements Serializable {

    /**
     * 权利人名称
     */
    private String qlrmc;

    /**
     * 权利人证件种类
     */
    private String qlrzjh;

    /**
     * 证号/证明号
     */
    private String bdcqzh;

    /**
     * 证照名称
     */
    private String zzmc;

    /**
     * 颁发时间
     */
    private Date bfsj;

    /**
     * 请求颁发机构
     */
    private String qqbfdjjg;

    /**
     * 颁发原因
     */
    private String bfyy;

    /**
     * 状态
     */
    private String zt;

    /**
     * pdfId
     */
    private String pdfid;

    /**
     *ofdid
     */
    private String ofdid;
}
