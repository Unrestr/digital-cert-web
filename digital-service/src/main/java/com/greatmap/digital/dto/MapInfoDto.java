package com.greatmap.digital.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author gaorui
 * @create 2020-06-28 10:49
 */
@Data
public class MapInfoDto implements Serializable {

    /**
     * 不动产单元号
     */
    private String bdcdyh;

    /**
     * 界址点
     */
    private String jzd;

    /**
     * 宗地图 传fmsid
     */
    private List<String> zdt;

    /**
     * 房产分户图 传fmsid
     */
    private List<String> fcfht;
}
