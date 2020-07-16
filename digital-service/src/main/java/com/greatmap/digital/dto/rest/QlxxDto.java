package com.greatmap.digital.dto.rest;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权利信息
 * @author gaorui
 * @create 2020-06-29 13:57
 */
@Data
public class QlxxDto implements Serializable {

    /**
     * 权利性质
     */
    private String qlxz;

    /**
     * 使用权起始时间
     */
    private Date syqqssj;

    /**
     * 使用权结束时间
     */
    private Date syqjssj;

    /**
     * 权利其他状况
     */
    private String qlqtzk;

    /**
     * 土地权利类型
     */
    private String tdqllx;

    /**
     * 土地权利性质
     */
    private String tdqlxz;

    /**
     * 附记
     */
    private String fj;
}
