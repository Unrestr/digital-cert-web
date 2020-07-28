package com.greatmap.digital.dto.rest;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaorui
 * @create 2020-06-29 11:48
 */
@Data
public class QlrDto implements Serializable {

    /**
     * 名称
     */
    private String qlrmc;

    /**
     * 与权利人关系
     */
    private String gx;

    /**
     * 证件类型
     */
    private String zjlx;

    /**
     * 证件号
     *
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
     * 共有情况
     */
    private String gyqk;

    /**
     * 是否持证
     */
    private String sfcz;

    /**
     * 人员分类（1 权利人 2义务人）
     */
    private String qlrfl;

    /**
     * 权利人类型
     */
    private String qlrlx;

    /**
     * 不动产单元号
     */
    private String bdcdyh;

    /**
     * 联系电话
     */
    private String lxdh;

    /**
     * 共有人
     */
    private String gyr;

    /**
     * 共有人证件号
     */
    private String gyrzjh;


}
