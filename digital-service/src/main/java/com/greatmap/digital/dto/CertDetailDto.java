package com.greatmap.digital.dto;

import com.greatmap.digital.model.DcCertMap;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 证照详情信息
 * @author gaorui
 * @create 2020-06-28 9:46
 */
@Data
public class CertDetailDto implements Serializable {

    /**
     * 证照基础信息
     */
  private CertBaseDto certBaseDto;

    /**
     * 单元信息
     */
  private List<UnitDto> unitDtoList;

    /**
     * 权利人信息
     */
  private List<RightHolderDto> rightHolderDtoList;

  /**
   * 权利信息
   */
  private RightInfoDto rightInfoDto;

  /**
   * 附图信息
   */
  private Map<String,List<DcCertMap>> certMap;

  /**
   * 证照fmsid
   */
  private String zzfmsid;
}
