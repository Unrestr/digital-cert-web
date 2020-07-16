package com.greatmap.digital.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.greatmap.digital.annotation.SystemLog;
import com.greatmap.digital.base.BaseController;
import com.greatmap.digital.model.DcSealLog;
import com.greatmap.digital.service.DcSealLogService;
import com.greatmap.framework.web.controller.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 日志信息 前端控制器
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Api(description = "日志信息 前端控制器",value = "日志信息")
@CrossOrigin
@RestController
@RequestMapping("/dcSealLog")
public class DcSealLogController extends BaseController {

    @Autowired
    private DcSealLogService dcSealLogService;

    @SystemLog(description = "证照日志查询接口")
    @ApiOperation("证照日志查询接口")
    @GetMapping("queryLicenseLog")
    public RestResult queryLicenseLog(@ApiParam(value = "当前页(大于等于0)", defaultValue = "0") @RequestParam(required = false, defaultValue = "0") int nCurrent,
                                      @ApiParam(value = "每页记录数量", defaultValue = "10") @RequestParam(required = false, defaultValue = "10") int nSize,
                                      @ApiParam(value = "排序字段", example = "id DESC") @RequestParam(required = false) String orderByField,
                                      @ApiParam(value = "是否升序") @RequestParam(required = false) boolean isAsc,
                                      @ApiParam(value = "开始时间") @RequestParam(required = false) Date startDate,
                                      @ApiParam(value = "结束时间") @RequestParam(required = false) Date endDate,
                                      @ApiParam(value = "操作人员") @RequestParam(required = false) String czry,
                                      @ApiParam(value = "证照编号") @RequestParam(required = false) String zzbh
                                      ) {
        RestResult restResult = renderSuccess();
        Page<DcSealLog> page = new Page<>(nCurrent, nSize, orderByField, isAsc);
        Page<DcSealLog> dcSealLogPage = dcSealLogService.queryLicenseLog(page,startDate,endDate,czry,zzbh);
        restResult.setData(dcSealLogPage);
        return restResult;
    }
}

