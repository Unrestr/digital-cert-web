package com.greatmap.digital.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 证照查询分页信息
 * @author gaorui
 * @create 2020-06-28 9:17
 */
@Data
public class CertInfoDto implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * 颁发机构
     */
    private String bfjg;

    /**
     * 证照编号
     */
    private String zzbh;

    /**
     * 颁发时间
     */
    private Date bfsj;

    /**
     *持证主体
     */
    private String czzt;

    /**
     *不动产权证号
     */
    private String bdcqzh;

    /**
     *不动产登记证明号
     */
    private String bdcdjzmh;

    /**
     *坐落
     */
    private String zl;

    /**
     *面积
     */
    private Double mj;

    /**
     *状态
     */
    private String zt;

    /**
     *ywh
     */
    private String ywh;


}
