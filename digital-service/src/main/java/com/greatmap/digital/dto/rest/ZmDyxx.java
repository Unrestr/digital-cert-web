package com.greatmap.digital.dto.rest;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaorui
 * @create 2020-07-06 14:07
 */
@Data
public class ZmDyxx implements Serializable {

    /**
     *证明编号
     */
    private String zzbh;

    /**
     *简称
     */
    private String jc;

    /**
     *年份
     */
    private String nf;

    /**
     *县
     */
    private String x;

    /**
     *登记时间
     */
    private String djsj;

    /**
     *编号
     */
    private String bh;

    /**
     *证明权利或事项
     */
    private String zmqlhsx;

    /**
     *权利人(申请人)
     */
    private String qlr;

    /**
     *义务人
     */
    private String ywr;

    /**
     *坐落
     */
    private String zl;

    /**
     *不动产单元号
     */
    private String bdcdyh;

    /**
     *其他
     */
    private String qt;

    /**
     *附记
     */
    private String fj;

    /**
     *查询二维码
     */
    private String cxewm;

    /**
     *不动产登记证明号
     */
    private String bdcdjzmh;
}
