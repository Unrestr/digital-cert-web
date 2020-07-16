package com.greatmap.digital.dc3psDto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VerifyCertificateDto implements Serializable {

    /**
     * 证书类型
     */
    private String zslx;

    /**
     * 证书号/证明号
     */
    private String zh;

    /**
     * 用户名
     */
    private String yhm;

    /**
     * 机构
     */
    private String jg;

    /**
     * 操作IP
     */
    private String ip;


}
