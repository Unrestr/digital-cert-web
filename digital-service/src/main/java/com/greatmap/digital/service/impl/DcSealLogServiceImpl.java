package com.greatmap.digital.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.greatmap.digital.mapper.DcSealLogMapper;
import com.greatmap.digital.model.DcSealLog;
import com.greatmap.digital.service.DcSealLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日志信息 服务实现类
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Service
public class DcSealLogServiceImpl extends ServiceImpl<DcSealLogMapper, DcSealLog> implements DcSealLogService {

    @Autowired(required = false)
    private DcSealLogMapper dcSealLogMapper;

    @Override
    public List<Map<String, Object>> statisticCertInfoOperation(String djjg, Date startDate, Date endDate) {
        List<String> czxqList = dcSealLogMapper.selectCzxqList();
        List<Map<String, Object>> list = dcSealLogMapper.statisticCertInfoOperation(czxqList,djjg, startDate, endDate);
        return list;
    }

    @Override
    public List<Map<String, Object>> statisticInspectionQuantity(String czxq) {
        return dcSealLogMapper.statisticInspectionQuantity(czxq);
    }

    @Override
    public Page<DcSealLog> queryLicenseLog(Page<DcSealLog> page, Date startDate, Date endDate, String czry, String zzbh) {
        EntityWrapper<DcSealLog> dcSealLogEntityWrapper = new EntityWrapper<>();
        if(startDate!=null){
            dcSealLogEntityWrapper.ge("qqsj",startDate);
        }
        if(endDate!=null){
            dcSealLogEntityWrapper.le("qqsj",endDate);
        }
        if(StringUtils.isNotBlank(czry)){
            dcSealLogEntityWrapper.eq("czry",czry);
        }
        if(StringUtils.isNotBlank(zzbh)){
            dcSealLogEntityWrapper.eq("zzbh",zzbh);
        }
        return selectPage(page, dcSealLogEntityWrapper);
    }
}
