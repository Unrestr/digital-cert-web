package com.greatmap.digital.service.impl;

import com.greatmap.digital.dto.MapInfoDto;
import com.greatmap.digital.mapper.DcCertMapMapper;
import com.greatmap.digital.model.DcCertMap;
import com.greatmap.digital.service.DcCertMapService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.DigestException;
import java.util.List;

/**
 * <p>
 * 附图信息 宗地图和房产分户图 服务实现类
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Service
public class DcCertMapServiceImpl extends ServiceImpl<DcCertMapMapper, DcCertMap> implements DcCertMapService {

    @Autowired
    private DcCertMapMapper dcCertMapMapper;

    @Override
    public List<MapInfoDto> listByZzbh(String zzbh) throws DigestException {
        if(StringUtils.isBlank(zzbh)){
            throw new DigestException("证照编号不能为空");
        }
        return dcCertMapMapper.listByZzbh(zzbh);
    }
}
