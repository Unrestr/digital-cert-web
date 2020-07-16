package com.greatmap.digital.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.greatmap.digital.model.DcSealLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日志信息 服务类
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
public interface DcSealLogService extends IService<DcSealLog> {

    /**
     * 获取日志中的各种操作统计
     * @param djjgdm
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String, Object>> statisticCertInfoOperation(String djjg, Date startDate, Date endDate);

    /**
     * 通过操作详情名称获取各机构的操作占比
     * @param czxq
     * @return
     */
    List<Map<String,Object>> statisticInspectionQuantity(String czxq);

    /**
     * 通过时间,操作人员,证照编号查询日志记录
     * @param page
     * @param startDate
     * @param endDate
     * @param czry
     * @param zzbh
     * @return
     */
    Page<DcSealLog> queryLicenseLog(Page<DcSealLog> page, Date startDate, Date endDate, String czry, String zzbh);
}
