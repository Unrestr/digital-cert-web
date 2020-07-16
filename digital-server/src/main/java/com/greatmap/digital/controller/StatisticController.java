package com.greatmap.digital.controller;

import com.greatmap.digital.annotation.SystemLog;
import com.greatmap.digital.base.BaseController;
import com.greatmap.digital.service.StatisticsService;
import com.greatmap.digital.util.ReflectUtil;
import com.greatmap.framework.web.controller.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.DigestException;
import java.util.*;

/**
 * @author gaorui
 * @create 2020-01-09 11:08
 */
@Api(value = "统计管理操作接口", description = "统计管理操作接口")
@CrossOrigin
@RestController
@RequestMapping("/statistics")
@SystemLog(description = "统计管理")
public class StatisticController extends BaseController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private ReflectUtil reflectUtil;

    @SystemLog(description = "证照统计")
    @ApiOperation("证照统计")
    @GetMapping("statisticCertInfo")
    public RestResult statisticCertInfo(@ApiParam("登记机构代码") @RequestParam(value = "djjgdm",required = false) String djjgdm,
                                      @ApiParam("开始时间") @RequestParam(value = "startDate",required = false) Date startDate,
                                      @ApiParam("结束时间") @RequestParam(value = "endDate",required = false) Date endDate) {
        RestResult restResult = renderSuccess();
        HashMap<String, Object> map = new HashMap<>();
        //实发电子证书量
        Integer realCertificateNum = statisticsService.selectRealCertificateNum(djjgdm,startDate,endDate,1,1);
        map.put("realCertificateNum",realCertificateNum);
        //有效电子证书量
        Integer effectiveCertificateNum = statisticsService.selectRealCertificateNum(djjgdm,startDate,endDate,0,1);
        map.put("effectiveCertificateNum",effectiveCertificateNum);
        //实发电子证明量
        Integer realProveNum = statisticsService.selectRealCertificateNum(djjgdm,startDate,endDate,0,2);
        map.put("realProveNum",realProveNum);
        //有效电子证明量
        Integer effectiveProveNum = statisticsService.selectRealCertificateNum(djjgdm,startDate,endDate,1,2);
        map.put("effectiveProveNum",effectiveProveNum);
        //获取电子证书,电子证明,其他证明的按天的统计图数据
        List<Map<String,Object>> list = statisticsService.selectNumByDay(djjgdm,startDate,endDate);
        map.put("list",list);
        restResult.setData(map);
        return restResult;
    }


    @SystemLog(description = "证照操作统计")
    @ApiOperation("证照操作统计")
    @GetMapping("statisticCertInfoOperation")
    public RestResult statisticCertInfoOperation(@ApiParam("登记机构名称") @RequestParam(value = "djjg",required = false) String djjg,
                                      @ApiParam("开始时间") @RequestParam(value = "startDate",required = false) Date startDate,
                                      @ApiParam("结束时间") @RequestParam(value = "endDate",required = false) Date endDate) {
        RestResult restResult = renderSuccess();
        List<Map<String,Object>> list = statisticsService.statisticCertInfoOperation(djjg,startDate,endDate);
        restResult.setData(list);
        return restResult;
    }


    @SystemLog(description = "各机构查验数量占比统计")
    @ApiOperation("各机构查验数量占比统计")
    @GetMapping("statisticInspectionQuantity")
    public RestResult statisticInspectionQuantity(@ApiParam("操作详情名称") @RequestParam(value = "czxq",required = true) String czxq) throws DigestException {
        RestResult restResult = renderSuccess();
        if(StringUtils.isBlank(czxq)){
            throw new DigestException("操作名称不允许为空");
        }
        List<Map<String,Object>> list = statisticsService.statisticInspectionQuantity(czxq);
        restResult.setData(list);
        return restResult;
    }
}
