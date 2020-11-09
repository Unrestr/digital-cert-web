package com.greatmap.digital.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.greatmap.dex.aop.ResolvedDexParams;
import com.greatmap.digital.base.BaseController;
import com.greatmap.digital.dto.CertificateDto;
import com.greatmap.digital.dto.rest.CertDto;
import com.greatmap.digital.dto.rest.DcRequestDto;
import com.greatmap.digital.excepition.DigitalThirdException;
import com.greatmap.digital.service.DcCertInfoService;
import com.greatmap.digital.service.dcThirdBiz.DownloadCertificateService;
import com.greatmap.framework.core.util.CollectionUtil;
import com.greatmap.framework.web.controller.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaorui
 * @create 2020-01-08 14:18
 */
@Api(description = "关联证书信息 前端控制器", value = "关联证书信息")
@RestController
@RequestMapping(value = "/rest/api/")
public class RestApi extends BaseController {

    @Autowired
    private DcCertInfoService dcCertInfoService;

    @Autowired
    private DownloadCertificateService downloadCertificateService;


    /**
     * 添加证照文件
     *
     * @return
     */
    @PostMapping("addPropertyCertificate")
    @ApiOperation("新增证书签章")
    //@ResolvedDexParams(name = "dexHttpServer", validate = "paramsValidate")
    public Object addPropertyCertificate(@ApiParam("传入参数") @RequestBody String params) {
           if (StringUtils.isBlank(params)) {
            throw new DigitalThirdException("生成证书失败,传入信息为空。");
        }
        Map<String, String> oneWindowParam = JSON.parseObject(params, new TypeReference<Map<String, String>>() {
        });
        List<CertDto> certDtos = JSONObject.parseArray(oneWindowParam.get("data"), CertDto.class);
        if (CollectionUtil.isEmpty(certDtos)) {
            throw new DigitalThirdException("证照打印信息为空!");
        }
        CertDto certParam = certDtos.get(0);
        if (CollectionUtil.isEmpty(certParam.getQlrList()) || certParam.getQlxx() == null) {
            throw new DigitalThirdException("缺少权利人、权利信息");
        }

        if (CollectionUtil.isEmpty(certParam.getDyxxList()) || certParam.getJcxx() == null) {
            throw new DigitalThirdException("缺少单元基础信息");
        }
        if (certParam.getZsxx() == null) {
            throw new DigitalThirdException("缺少需要打印信息");
        }
        CertDto certDto = certParam;
        Map<String, String> addCert = dcCertInfoService.addCertificate(certDto, certDto.getIp(), certDto.getDjjg(), certDto.getZh(), certDto.getYhm());
        List<Map<String, String>> list = new ArrayList<>();
        list.add(addCert);
        return list;
    }

    @PostMapping("addRegistrationCertificate")
    @ApiOperation("新增证明签章")
    //@ResolvedDexParams(name = "dexHttpServer", validate = "paramsValidate")
    public Object addRegistrationCertificate(@ApiParam("传入参数") @RequestBody String params) {
        if (StringUtils.isBlank(params)) {
            throw new DigitalThirdException("生成证书失败,传入信息为空。");
        }
        Map<String, String> oneWindowParam = JSON.parseObject(params, new TypeReference<Map<String, String>>() {
        });
        List<CertDto> certDtos = JSONObject.parseArray(oneWindowParam.get("data"), CertDto.class);
        if (CollectionUtil.isEmpty(certDtos)) {
            throw new DigitalThirdException("证照打印信息为空!");
        }
        CertDto certParam = certDtos.get(0);
        if (CollectionUtil.isEmpty(certParam.getQlrList()) || certParam.getQlxx() == null) {
            throw new DigitalThirdException("缺少权利人、权利信息");
        }

        if (CollectionUtil.isEmpty(certParam.getDyxxList()) || certParam.getJcxx() == null) {
            throw new DigitalThirdException("缺少单元基础信息");
        }
        if (certParam.getZmxx() == null) {
            throw new DigitalThirdException("缺少需要打印信息");
        }
        CertDto certDto = certParam;
        Map<String, String> addCert = dcCertInfoService.addCertificate(certDto, certDto.getIp(), certDto.getDjjg(), certDto.getZh(), certDto.getYhm());
        List<Map<String, String>> list = new ArrayList<>();
        list.add(addCert);
        return list;
    }

    @ApiOperation("验证数字证书")
    @PostMapping(value = "verify")
    //@ResolvedDexParams(name = "dexHttpServer", validate = "paramsValidate")
    public Object verifyCertificate(@ApiParam("传入参数") @RequestBody String params) {
        Map<String, String> oneWindowParam = JSON.parseObject(params, new TypeReference<Map<String, String>>() {});
        List<DcRequestDto> dcRequestDtos = JSONObject.parseArray(oneWindowParam.get("data"), DcRequestDto.class);
        if (CollectionUtil.isEmpty(dcRequestDtos)) {
            throw new DigitalThirdException("查询参数不能为空!");
        }
        DcRequestDto queryDto = dcRequestDtos.get(0);
        RestResult restResult = renderSuccess();
        dcCertInfoService.verifyCertificate(restResult, queryDto.getZjzl(), queryDto.getZh(), queryDto.getFile(), queryDto.getYhm(), queryDto.getDjjg(), queryDto.getIp());
        Map<String,Object> map = new HashMap<>(2);
        map.put("isSuccess",restResult.isSuccess());
        map.put("result",restResult.getMessage());
        List<Map<String,Object>> mapList = Lists.newArrayList();
        mapList.add(map);
        return mapList;
    }

    @ApiOperation("下载电子证书")
    @PostMapping("download")
    //@ResolvedDexParams(name = "dexHttpServer", validate = "paramsValidate")
    public Object download(@ApiParam("传入参数") @RequestBody String params) {
        Map<String, String> oneWindowParam = JSON.parseObject(params, new TypeReference<Map<String, String>>() {});
        List<DcRequestDto> dcRequestDtos = JSONObject.parseArray(oneWindowParam.get("data"), DcRequestDto.class);
        if (CollectionUtil.isEmpty(dcRequestDtos)) {
            throw new DigitalThirdException("查询参数不能为空!");
        }
        DcRequestDto queryDto = dcRequestDtos.get(0);
        List<Map<String,String>> result = downloadCertificateService.download(queryDto.getZh(),  queryDto.getYhm(), queryDto.getDjjg(), queryDto.getIp());
        return result;
    }


    @ApiOperation("电子证照查询")
    @PostMapping("queryCertificateList")
    //@ResolvedDexParams(name = "dexHttpServer", validate = "paramsValidate")
    public Object queryCertificateList(@ApiParam("传入参数") @RequestBody String params) {
        Map<String, String> oneWindowParam = JSON.parseObject(params, new TypeReference<Map<String, String>>() {});
        List<DcRequestDto> dcRequestDtos = JSONObject.parseArray(oneWindowParam.get("data"), DcRequestDto.class);
        if (CollectionUtil.isEmpty(dcRequestDtos)) {
            throw new DigitalThirdException("查询参数不能为空!");
        }
        DcRequestDto queryDto = dcRequestDtos.get(0);
        List<CertificateDto> result = downloadCertificateService.queryCertificateList(queryDto.getZh(), queryDto.getQlrmc(), queryDto.getQlrzjh(), queryDto.getYhm(), queryDto.getDjjg(), queryDto.getIp());
        return result;
    }

    @ApiOperation("电子证照查询EX")
    @GetMapping("queryCertificateListEx")
    //@ResolvedDexParams(name = "dexHttpServer", validate = "paramsValidate")
    public Object queryCertificateListEx(@ApiParam(value = "权利人名称") @RequestParam(required = false) String qlrmc,
                                         @ApiParam(value = "权利人证件号") @RequestParam(required = false) String qlrzjh,
                                         @ApiParam(value = "证号") @RequestParam(required = false) String zh) {

        List<CertificateDto> result = downloadCertificateService.queryCertificateListEx(qlrmc,qlrzjh,zh);
        return result;
    }

    @ApiOperation("下载电子证书EX")
    @GetMapping("downloadEx")
    //@ResolvedDexParams(name = "dexHttpServer", validate = "paramsValidate")
    public void downloadEx(String pdfId, HttpServletResponse response) {
        if (StringUtils.isEmpty(pdfId)) {
            throw new DigitalThirdException("查询参数不能为空!");
        }
        byte[] result = downloadCertificateService.downloadEx(pdfId);
        if (ArrayUtils.isNotEmpty(result)) {
            response.setContentType("application/pdf");
            try {
                FileCopyUtils.copy(result,response.getOutputStream());
            } catch (Exception e) {
                throw new DigitalThirdException("下载失败", e);
            }
        }
    }
}
