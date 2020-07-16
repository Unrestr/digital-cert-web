package com.greatmap.digital.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.greatmap.digital.annotation.SystemLog;
import com.greatmap.digital.base.BaseController;
import com.greatmap.digital.dto.SealRuleDto;
import com.greatmap.digital.model.DcSealRule;
import com.greatmap.digital.service.DcSealRuleService;
import com.greatmap.framework.web.controller.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
@Api(value = "规则管理操作接口", description = "规则管理操作接口")
@CrossOrigin
@RestController
@RequestMapping("/dcSealRule")
@SystemLog(description = "规则管理")
public class DcSealRuleController extends BaseController {

    @Autowired
    private DcSealRuleService dcSealRuleService;

    @SystemLog(description = "查询规则列表")
    @ApiOperation("查询规则列表")
    @GetMapping("getSealTemplatePageList")
    public RestResult getSealTemplatePageList(@ApiParam(value = "当前页(大于等于0)") @RequestParam(required = false, defaultValue = "1") Integer nCurrent,
                                              @ApiParam(value = "每页记录数量") @RequestParam(required = false, defaultValue = "10") Integer nSize,
                                              @ApiParam(value = "排序字段") @RequestParam(required = false) String orderByField,
                                              @ApiParam("规则名称") @RequestParam(required = false) String qzgzmc,
                                              @ApiParam("区县代码") @RequestParam(value = "qxdm") String qxdm,
                                              @ApiParam("状态") @RequestParam(value = "zt") String zt) {
        Page<DcSealRule> page = generatePage(nCurrent, nSize, orderByField);
        return renderSuccess(dcSealRuleService.getSealTemplatePageList(page, qzgzmc, qxdm,zt));
    }

    @SystemLog(description = "新增规则")
    @ApiOperation("新增规则")
    @PostMapping("addSealTemplate")
    public RestResult addSealTemplate(SealRuleDto sealRuleDto) {
        RestResult restResult = renderSuccess();
        restResult.setData(dcSealRuleService.addSealTemplate(restResult, sealRuleDto));
        return restResult;
    }

    @SystemLog(description = "删除规则")
    @ApiOperation("删除规则")
    @PostMapping("logOffSealTemplate")
    public RestResult logOffSealTemplate(@ApiParam("规则id") @RequestParam(value = "qzgzid") String qzgzid,
                                         @ApiParam("规则名称") @RequestParam(value = "qzgzmc") String qzgzmc,
                                         @ApiParam("区县代码") @RequestParam(value = "qxdm") String qxdm) {
        RestResult restResult = renderSuccess();
        restResult.setData(dcSealRuleService.logoffSealTemplate(restResult, qzgzid,qzgzmc, qxdm));
        return restResult;
    }

    @SystemLog(description = "修改规则")
    @ApiOperation("修改规则")
    @PostMapping("updateTemplateById")
    public RestResult updateTemplateById(SealRuleDto sealRuleDto) {
        RestResult restResult = renderSuccess();
        restResult.setData(dcSealRuleService.updateSealTemplate(restResult, sealRuleDto));
        return restResult;
    }

    @SystemLog(description = "获取数字签章列表")
    @ApiOperation("获取数字签章列表")
    @PostMapping("getSealPageList")
    public RestResult getSealPageList(
            @ApiParam(value = "签章ID") @RequestParam(value = "sealId", required = false) String sealId,
            @ApiParam(value = "签章名称") @RequestParam(value = "sealName", required = false) String sealName,
            @ApiParam(value = "签章状态") @RequestParam(value = "status", defaultValue = "0") String status,
            @ApiParam(value = "序列号") @RequestParam(value = "bindingState", defaultValue = "0") String bindingState,
            @ApiParam(value = "当前页(大于等于0)") @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @ApiParam(value = "每页记录数量") @RequestParam(value = "pageSize", defaultValue = "999") Integer pageSize,
            @ApiParam(value = "开始时间") @RequestParam(value = "startDate",required = false) Date startDate,
            @ApiParam(value = "结束时间") @RequestParam(value = "endDate",required = false) Date endDate) {
        RestResult restResult = renderSuccess();
        restResult.setData(dcSealRuleService.getSealPageList(restResult, pageNo, pageSize, sealId, sealName, status,bindingState,startDate,endDate));
        restResult.setSuccess(Boolean.TRUE);
        return restResult;
    }


}

