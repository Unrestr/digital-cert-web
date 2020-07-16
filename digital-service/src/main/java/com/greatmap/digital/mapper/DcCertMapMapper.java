package com.greatmap.digital.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.greatmap.digital.dto.MapInfoDto;
import com.greatmap.digital.model.DcCertMap;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 附图信息 宗地图和房产分户图 Mapper 接口
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Mapper
public interface DcCertMapMapper extends BaseMapper<DcCertMap> {

    List<MapInfoDto> listByZzbh(String zzbh);
}
