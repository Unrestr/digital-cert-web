package com.greatmap.digital.dto.rest;

import lombok.Data;

import java.io.Serializable;

/**
 * @author gaorui
 * @create 2020-07-02 15:41
 */
@Data
public class Glxx implements Serializable {

    /**
     * 证号
     */
    private String zh;

    /**
     * 关联证号
     */
    private String glzh;

    /**
     * 关联类型(共有关联 沿革关联)
     */
    private String gllx;
}
