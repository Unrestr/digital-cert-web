package com.greatmap.digital.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.greatmap.digital.annotation.SystemLog;
import com.greatmap.digital.base.BaseController;
import com.greatmap.digital.model.DcCertSealRule;
import com.greatmap.digital.service.DcCertSealRuleService;
import com.greatmap.framework.web.controller.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 签章信息前端控制器
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
@Api(value = "证照操作接口", description = "证照操作接口")
@CrossOrigin
@RestController
@RequestMapping("/dcCertSealRule")
public class DcCertSealRuleController extends BaseController {

    @Autowired
    private DcCertSealRuleService dcCertSealRuleService;

    @ApiOperation("生成P10")
    @GetMapping("createP10")
    public RestResult createP10(@ApiParam(value = "签章ids",required = false) @RequestParam(required = false) String sealIds,
                                @ApiParam(value = "证书算法") @RequestParam(required = true) String certAlg,
                                HttpServletResponse reponse) {
        RestResult restResult = renderSuccess();
        restResult.setData(dcCertSealRuleService.createP10(restResult, sealIds, certAlg, reponse));
        return restResult;
    }

    @ApiOperation("导入数字证书")
    @PostMapping("uploadCert")
    public RestResult uploadCert(MultipartFile multipartFile) {
        RestResult restResult = renderSuccess();
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            restResult.setData(dcCertSealRuleService.uploadCert(restResult, multipartFile));
        } catch (IOException e) {
            e.printStackTrace();
            return renderFailure("导入数字证书失败！");
        } finally {
            if (inputStream != null) {
                IOUtils.closeQuietly(inputStream);
            }
        }
        return restResult;
    }

    @ApiOperation("获取签章信息")
    @GetMapping(value = "findCertSealRule")
    public RestResult findCertSealRule(@ApiParam(value = "证照名称") @RequestParam(required = false) String zzmc,
                                       @ApiParam(value = "签章规则名称") @RequestParam(required = false) String qzgzmc,
                                       @ApiParam(value = "区县代码") @RequestParam(required = false) String qxdm) {
        RestResult restResult = renderSuccess();
        List<DcCertSealRule> dcCertSealRules = dcCertSealRuleService.findCertSealRule(zzmc, qzgzmc, qxdm);
        restResult.setData(dcCertSealRules);
        return restResult;
    }

    @ApiOperation("获取证书签章对应关系列表")
    @GetMapping("getCertSealTemplatePageList")
    public RestResult getCertSealTemplatePageList(@ApiParam(value = "证照名称") @RequestParam(value = "zzmc",required = false) String zzmc,
                                                  @ApiParam(value = "状态") @RequestParam(value = "zt",required = false) Integer zt,
                                                  @ApiParam(value = "签章规则名称") @RequestParam(value = "qzgzmc",required = false) String qzgzmc,
                                                  @ApiParam(value = "当前页(大于等于0)") @RequestParam(required = false, defaultValue = "1") Integer nCurrent,
                                                  @ApiParam(value = "每页记录数量") @RequestParam(required = false, defaultValue = "10") Integer nSize,
                                                  @ApiParam(value = "排序字段") @RequestParam(required = false) String orderByField,
                                                  @ApiParam(value = "区县代码") @RequestParam(value = "qxdm") String qxdm) {
        RestResult restResult = renderSuccess();
        Page<DcCertSealRule> page = generatePage(nCurrent, nSize, orderByField);
        restResult.setData(dcCertSealRuleService.getCertSealTemplatePageList(page, zzmc, qzgzmc, zt, qxdm));
        return restResult;
    }

    @ApiOperation("添加证书签章对应关系")
    @PostMapping("addCertSealTempalte")
    public RestResult addCertSealTempalte(@ApiParam(value = "签章规则名称") @RequestParam(value = "qzgzmc") String qzgzmc,
                                          @ApiParam(value = "签章规则ID") @RequestParam(value = "qzgzid") String qzgzid,
                                          @ApiParam(value = "证照名称") @RequestParam(value = "zzmc") String zzmc,
                                          @ApiParam(value = "证照类型") @RequestParam(value = "zzlx") String zzlx,
                                          @ApiParam(value = "是否注销章") @RequestParam(value = "sfzxz") Integer sfzxz,
                                          @ApiParam(value = "区县代码") @RequestParam(value = "qxdm") String qxdm,
                                          @ApiParam(value = "区县名称") @RequestParam(value = "qxmc") String qxmc) {
        RestResult restResult = renderSuccess();
        restResult.setData(dcCertSealRuleService.addCertSealTemplate(qzgzmc, qzgzid, zzmc, zzlx, sfzxz, qxdm,qxmc));
        return renderSuccess();
    }

    @ApiOperation("删除证书签章对应关系")
    @PostMapping("deleteCertSealTemplate")
    public RestResult deleteCertSealTemplate(String id) {
        RestResult restResult = renderSuccess();
        restResult.setData(dcCertSealRuleService.deleteCertSealTemplate(id));
        return restResult;
    }

    @ApiOperation("修改证书签章对应关系状态(停用或启用)")
    @PostMapping("updateCertSealTemplate")
    public RestResult updateCertSealTemplate(@ApiParam(value = "id") @RequestParam(value = "id") String id,
                                             @ApiParam(value = "证照名称") @RequestParam(value = "zzmc") String zzmc,
                                             @ApiParam(value = "状态(0表示停用，1表示启用)") @RequestParam(value = "zt") Integer zt,
                                             @ApiParam(value = "区县代码") @RequestParam(value = "qxdm") String qxdm) {
        RestResult restResult = renderSuccess();
        restResult.setData(dcCertSealRuleService.updateCertSealTemplate(id,zzmc,zt,qxdm));
        return restResult;
    }

    @ApiOperation("修改数字签章状态")
    @PostMapping("modifySealStatus")
    public RestResult modifySealStatus(@ApiParam(value = "签章ID") @RequestParam(required = true) String sealId,
                                       @ApiParam(value = "签章ID") @RequestParam(required = true) String status) {
        RestResult restResult = renderSuccess();
        restResult.setData(dcCertSealRuleService.moddifySealStatus(restResult, sealId, status));
        return restResult;
    }

    @ApiOperation("获取数字签章")
    @GetMapping("getSeal")
    public RestResult getSeal(@ApiParam(value = "签章ID") @RequestParam(required = true) String sealId) {
        RestResult restResult = renderSuccess();
        restResult.setData(dcCertSealRuleService.getSealById(restResult, sealId));
        return restResult;
    }
}

