package com.greatmap.digital.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.greatmap.digital.dto.SealDto;
import com.greatmap.digital.dto.SealRuleDto;
import com.greatmap.digital.model.DcSealRule;
import com.greatmap.framework.web.controller.RestResult;

import java.util.Date;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
public interface DcSealRuleService extends IService<DcSealRule> {

    /**
     * 查询规则列表
     * @param page
     * @param qzgzmc
     * @param qxdm
     * @return
     */
    Page<DcSealRule> getSealTemplatePageList(Page<DcSealRule> page, String qzgzmc,String qxdm,String zt);

    /**
     * 新增规则
     * @param restResult
     * @param sealRuleDto
     * @return
     */
    String addSealTemplate(RestResult restResult, SealRuleDto sealRuleDto);

    /**
     * 删除规则
     * @param restResult
     * @param qzgzid
     * @param qzgzmc
     * @param qxdm
     * @return
     */
    String logoffSealTemplate(RestResult restResult,String qzgzid, String qzgzmc,String qxdm);

    /**
     * 修改规则
     * @param restResult
     * @param SealRuleDto
     * @return
     */
    String updateSealTemplate(RestResult restResult, SealRuleDto SealRuleDto);

    /**
     * 获取数字签章列表
     * @param restResult
     * @param pageNo
     * @param pageSize
     * @param sealId
     * @param sealName
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    Page<SealDto> getSealPageList(RestResult restResult, Integer pageNo, Integer pageSize, String sealId, String sealName, String status, String bindingState, Date startDate, Date endDate);

}
