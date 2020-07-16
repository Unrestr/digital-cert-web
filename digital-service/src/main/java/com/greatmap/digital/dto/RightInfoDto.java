package com.greatmap.digital.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权利信息
 * @author gaorui
 * @create 2020-06-28 10:38
 */
@Data
public class RightInfoDto implements Serializable {

    /**
     * 权利类型
     */
    private String qllx;

    /**
     * 权利性质
     */
    private String qlxz;

    /**
     * 使用权起
     */
    private Date syqq;

    /**
     * 使用权止
     */
    private Date syqz;

    /**
     * 权利其他情况
     */
    private String qlqtqk;

    /**
     * 附记
     */
    private String fj;


}
