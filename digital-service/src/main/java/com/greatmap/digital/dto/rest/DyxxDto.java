package com.greatmap.digital.dto.rest;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaorui
 * @create 2020-06-29 14:01
 */
@Data
public class DyxxDto implements Serializable {

    /**
     * 不动产权证号
     */
    private String bdcqzh;

    /**
     * 不动产登记证明号
     */
    private String bdcdjzmh;

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
     * 土地用途
     */
    private String tdyt;

    /**
     * 面积
     */
    private Double jzmj;

    /**
     * 土地使用权面积
     */
    private Double tdsyqmj;

    /**
     * 分摊面积
     */
    private Double ftmj;

    /**
     * 套内面积
     */
    private Double tnmj;

    /**
     * 面积单位
     */
    private String mjdw;

    /**
     * 用途代码
     */
    private String ytdm;

    /**
     * 土地用途代码
     */
    private String tdytdm;

    /**
     * 土地面积单位
     */
    private String tdmjdw;

    /**
     * 面积单位代码
     */
    private String mjdwdm;

    /**
     * 土地面积单位代码
     */
    private String tdmjdwdm;

    /**
     * 状态
     */
    private String zt;
}
