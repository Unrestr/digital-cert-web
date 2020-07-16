package com.greatmap.digital.service;

import com.baomidou.mybatisplus.service.IService;
import com.greatmap.digital.dto.MapInfoDto;
import com.greatmap.digital.model.DcCertMap;

import java.security.DigestException;
import java.util.List;

/**
 * <p>
 * 附图信息 宗地图和房产分户图 服务类
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
public interface DcCertMapService extends IService<DcCertMap> {

    /**
     * 根据证照编号获取附图信息
     * @param zzbh
     * @return
     */
    List<MapInfoDto> listByZzbh(String zzbh) throws DigestException;
}
