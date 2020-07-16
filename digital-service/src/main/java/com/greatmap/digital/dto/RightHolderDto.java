package com.greatmap.digital.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 权利人信息
 * @author gaorui
 * @create 2020-06-28 10:32
 */
@Data
public class RightHolderDto implements Serializable {

    /**
     * 权利人名称
     */
    private String mc;

    /**
     * 关系
     */
    private String gx;

    /**
     * 权利人类型
     */
    private String ryfl;

    /**
     * 证件类型
     */
    private String zjlx;

    /**
     * 证件号
     */
    private String zjh;

    /**
     * 共有方式
     */
    private String gyfs;

    /**
     * 共有比例
     */
    private String gybl;

    /**
     * 是否持证
     */
    private String sfcz;
}
