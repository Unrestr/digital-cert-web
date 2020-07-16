package com.greatmap.digital.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.greatmap.digital.annotation.DcLog;
import com.greatmap.digital.annotation.SystemLog;
import com.greatmap.digital.base.BaseController;
import com.greatmap.digital.dto.CertDetailDto;
import com.greatmap.digital.dto.CertInfoDto;
import com.greatmap.digital.service.DcCertInfoService;
import com.greatmap.digital.util.ReflectUtil;
import com.greatmap.digital.util.algorithm.base64.Base64Encoder;
import com.greatmap.framework.web.controller.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;

/**
 * <p>
 * 证照现势信息前端控制器
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
@Api(value = "证照信息操作接口", description = "证照信息操作接口")
@CrossOrigin
@RestController
@RequestMapping("/dcCertInfo")
@SystemLog(description = "证照管理")
public class DcCertInfoController extends BaseController {

    @Autowired
    private ReflectUtil reflectUtil;

    @Autowired
    private DcCertInfoService dcCertInfoService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @SystemLog(description = "获取证照信息分页列表")
    @ApiOperation("获取证照信息分页列表")
    @GetMapping(value = "findCertInfoPage")
    public RestResult findCertInfoPage(@ApiParam(value = "当前页(大于等于0)", defaultValue = "0") @RequestParam(required = false, defaultValue = "0") int nCurrent,
                                       @ApiParam(value = "每页记录数量", defaultValue = "10") @RequestParam(required = false, defaultValue = "10") int nSize,
                                       @ApiParam(value = "排序字段", example = "id DESC") @RequestParam(required = false) String orderByField,
                                       @ApiParam(value = "是否升序") @RequestParam(required = false) boolean isAsc,
                                       @ApiParam(value = "颁发机构") @RequestParam(required = false) String bfjg,
                                       @ApiParam(value = "权利人") @RequestParam(required = false) String qlr,
                                       @ApiParam(value = "状态") @RequestParam(required = false) String zt,
                                       @ApiParam(value = "证照类型(区分证书,证明和其他)") @RequestParam(required = false) String zzlx,
                                       @ApiParam(value = "证照编号") @RequestParam(required = false) String zzbh) {
        RestResult restResult = renderSuccess();
        Page<CertInfoDto> page = new Page<>(nCurrent, nSize, orderByField, isAsc);
        Page<CertInfoDto> certFilePage = dcCertInfoService.findCertInfoPage(page, bfjg, qlr, zt, zzlx, zzbh);
        restResult.setData(certFilePage);
        return restResult;
    }

    @SystemLog(description = "根据id获取证照详情信息")
    @ApiOperation("根据id获取证照详情信息")
    @GetMapping(value = "getCertInfoById")
    public RestResult getCertInfoById(@ApiParam(value = "证照ID") @RequestParam(required = true) String id) {
        RestResult restResult = renderSuccess();
        CertDetailDto dcCertInfo = dcCertInfoService.getCertInfoById(id);
        restResult.setData(dcCertInfo);
        return restResult;
    }


    @ApiOperation("验证数字证书")
    @PostMapping(value = "verifyCertificate")
    public RestResult verifyCertificate(@ApiParam(value = "证照名称") @RequestParam() String zzmc,
                                        @ApiParam(value = "证号/证明号") @RequestParam() String zh,
                                        @ApiParam(value = "用户") @RequestParam() String yh,
                                        @ApiParam(value = "登记机构") @RequestParam() String djjg,
                                        @ApiParam(value = "IP") @RequestParam() String ip,
                                        MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return renderFailure("上传电子证照为空，无法验证！");
        }
        RestResult restResult = renderSuccess();
        String encode = null;
        try {
            encode = Base64.getEncoder().encodeToString(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dcCertInfoService.verifyCertificate(restResult,zzmc, zh, encode,yh,djjg,ip);
        return restResult;
    }

}

