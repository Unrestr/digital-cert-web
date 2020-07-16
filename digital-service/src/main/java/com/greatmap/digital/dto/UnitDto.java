package com.greatmap.digital.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gaorui
 * @create 2020-06-28 10:01
 */
@Data
public class UnitDto implements Serializable {

    /**
     * 不动产单元号
     */
    private String bdcdyh;

    /**
     * 坐落
     */
    private String zl;

    /**
     * 用途
     */
    private String yt;

    /**
     * 面积
     */
    private Date mj;

    /**
     * 用途代码
     */
    private String tdytdm;

    /**
     * 面积单位
     */
    private String mjdw;
}
