package com.greatmap.digital.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.greatmap.digital.annotation.SystemLog;
import com.greatmap.digital.base.BaseController;
import com.greatmap.digital.dto.DcCertTemplateDto;
import com.greatmap.digital.model.DcCertSealRule;
import com.greatmap.digital.model.DcCertTemplate;
import com.greatmap.digital.service.DcCertTemplateService;
import com.greatmap.digital.util.ReflectUtil;
import com.greatmap.digital.util.StringUtils;
import com.greatmap.fms.model.FileInfo;
import com.greatmap.framework.web.controller.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.DigestException;
import java.util.List;

/**
 * <p>
 * 模板信息表。第三方系统传值json数据电子证照通过json打印证照 然后盖章。需要通过模板标识找对应模板打印再盖章 前端控制器
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Api(description = "模板信息 前端控制器", value = "模板信息")
@CrossOrigin
@RestController
@RequestMapping("/dcCertTemplate")
@SystemLog(description = "电子证照模板管理")
public class DcCertTemplateController extends BaseController {

    @Autowired
    private ReflectUtil reflectUtil;

    @Autowired
    private DcCertTemplateService dcCertTemplateService;

    @SystemLog(description = "查询证照模板列表")
    @ApiOperation("查询证照模板列表")
    @GetMapping("listTemplate")
    public RestResult listTemplate(@ApiParam("区县名称") @RequestParam(value = "qxmc", required = false) String qxmc,
                                   @ApiParam("模板标识") @RequestParam(value = "mbbs", required = false) String mbbs,
                                   @ApiParam(value = "当前页(大于等于0)", defaultValue = "0") @RequestParam(required = false, defaultValue = "0") int nCurrent,
                                   @ApiParam(value = "每页记录数量", defaultValue = "10") @RequestParam(required = false, defaultValue = "10") int nSize,
                                   @ApiParam(value = "排序字段", example = "id DESC") @RequestParam(required = false) String orderByField,
                                   @ApiParam(value = "是否升序") @RequestParam(required = false) boolean isAsc) {
        RestResult restResult = renderSuccess();
        Page<DcCertTemplate> page = new Page<>(nCurrent, nSize, orderByField, isAsc);
        Page<DcCertTemplate> dcCertTemplatePage = dcCertTemplateService.listTemplate(page, qxmc, mbbs);
        restResult.setData(dcCertTemplatePage);
        return restResult;
    }


    @SystemLog(description = "修改证照模板状态")
    @ApiOperation("修改证照模板状态")
    @PostMapping("updateTemplate")
    public RestResult updateTemplate(@ApiParam("模板ID") @RequestParam(value = "id", required = true) String id,
                                     @ApiParam("状态") @RequestParam(value = "zt", required = true) String zt) throws DigestException {
        boolean flag = dcCertTemplateService.updateTemplate(id, zt);
        if (flag) {
            return renderSuccess("修改成功");
        }
        return renderFailure("修改失败");
    }

    @SystemLog(description = "查询规则模板列表")
    @ApiOperation("查询规则模板列表")
    @GetMapping("listTemplatePage")
    public RestResult listTemplatePage(@ApiParam("区县名称") @RequestParam(value = "qxmc", required = false) String qxmc,
                                       @ApiParam("证照名称") @RequestParam(value = "mbbs", required = false) String zzmc,
                                       @ApiParam("规则名称") @RequestParam(value = "gzmc", required = false) String gzmc,
                                       @ApiParam("状态") @RequestParam(value = "zt", required = false) String zt,
                                       @ApiParam(value = "当前页(大于等于0)", defaultValue = "0") @RequestParam(required = false, defaultValue = "0") int nCurrent,
                                       @ApiParam(value = "每页记录数量", defaultValue = "10") @RequestParam(required = false, defaultValue = "10") int nSize,
                                       @ApiParam(value = "排序字段", example = "id DESC") @RequestParam(required = false) String orderByField,
                                       @ApiParam(value = "是否升序") @RequestParam(required = false) boolean isAsc) {
        RestResult restResult = renderSuccess();
        Page<DcCertTemplateDto> page = new Page<>(nCurrent, nSize, orderByField, isAsc);
        Page<DcCertTemplateDto> dcCertTemplatePage = dcCertTemplateService.listTemplatePage(page, qxmc, zzmc, gzmc, zt);
        restResult.setData(dcCertTemplatePage);
        return restResult;
    }

    @SystemLog(description = "获取模板信息")
    @ApiOperation("获取模板信息(获取模板信息 满足前端下拉需求)")
    @GetMapping("getTemplateList")
    public RestResult getTemplateList(@ApiParam("区县名称") @RequestParam(value = "qxmc", required = false) String qxmc,
                                      @ApiParam("证照名称") @RequestParam(value = "zzmc", required = false) String zzmc,
                                      @ApiParam("状态") @RequestParam(value = "zt", required = false) String zt) {
        RestResult restResult = renderSuccess();
        List<DcCertTemplate> dcCertTemplate = dcCertTemplateService.getTemplateList(qxmc, zzmc, zt);
        restResult.setData(dcCertTemplate);
        return restResult;
    }

    @SystemLog(description = "新增模板信息")
    @ApiOperation("新增模板信息")
    @PostMapping("saveTemplate")
    public RestResult saveTemplate(@ApiParam("模板标识") @RequestParam(value = "mbbs", required = false) String mbbs,
                                   @ApiParam("证照名称") @RequestParam(value = "zzmc", required = false) String zzmc,
                                   @ApiParam("区县代码") @RequestParam(value = "qxdm", required = false) String qxdm,
                                   @ApiParam("区县名称") @RequestParam(value = "qxmc", required = false) String qxmc,
                                   @ApiParam("备注") @RequestParam(value = "bz", required = false) String bz,
                                   @ApiParam("状态") @RequestParam(value = "zt", required = false) String zt,
                                   @ApiParam("文件id") @RequestParam(value = "fileId", required = false) String fileId) {
        RestResult restResult = renderSuccess();
        boolean addTemplate = false;
        if (StringUtils.isAnyBlank(mbbs, zzmc, qxdm, zt)) {
            restResult.setMessage("缺少必要参数");
            restResult.setSuccess(false);
            return restResult;
        }
        try {
            addTemplate = dcCertTemplateService.saveTemplate(mbbs, zzmc, qxdm, qxmc, bz, zt, fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        restResult.setData(addTemplate);
        return restResult;
    }

    @SystemLog(description = "删除模板信息")
    @ApiOperation("删除模板信息")
    @PostMapping("delTemplate")
    public RestResult delTemplate(@ApiParam("ID") @RequestParam(value = "id", required = true) String id) {
        RestResult restResult = renderSuccess();
        boolean delTemplate = dcCertTemplateService.delTemplate(id);
        restResult.setData(delTemplate);
        return restResult;
    }

    @SystemLog(description = "预览模板")
    @ApiOperation("预览模板")
    @GetMapping("preview")
    public RestResult preview(@ApiParam("mbbs") @RequestParam(value = "mbbs", required = true) String mbbs) {
        RestResult restResult = renderSuccess();
        FileInfo fmsId = dcCertTemplateService.preview(mbbs);
        restResult.setData(fmsId);
        return restResult;
    }

    @SystemLog(description = "预览签章")
    @ApiOperation("预览签章")
    @GetMapping("previewQz")
    public RestResult previewQz(@ApiParam("qzgzid") @RequestParam(value = "qzgzid", required = true) String qzgzid) {
        RestResult restResult = renderSuccess();
        FileInfo fmsId = null;
        try {
            fmsId = dcCertTemplateService.previewQz(qzgzid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        restResult.setData(fmsId);
        return restResult;
    }

    @SystemLog(description = "查询模板信息")
    @ApiOperation("查询模板信息")
    @GetMapping("list")
    public RestResult list(@ApiParam("区县代码") @RequestParam(value = "qxdm", required = false) String qxdm,
                           @ApiParam("规则名称") @RequestParam(value = "gzmc", required = false) String qzgzmc,
                           @ApiParam("是否注销章") @RequestParam(value = "sfzxz", required = false) String sfzxz,
                           @ApiParam(value = "证照类型") @RequestParam(required = false) String zzlx) {
        RestResult restResult = renderSuccess();
        List<DcCertSealRule> dcCertTemplatePage = dcCertTemplateService.list(qxdm, qzgzmc, sfzxz, zzlx);
        restResult.setData(dcCertTemplatePage);
        return restResult;
    }

    @SystemLog(description = "查询模板标识")
    @ApiOperation("查询模板标识")
    @GetMapping("getMbbsList")
    public RestResult getMbbsList(@ApiParam("区县代码") @RequestParam(value = "qxdm", required = false) String qxdm,
                                  @ApiParam(value = "证照名称") @RequestParam(required = false) String zzmc) {
        RestResult restResult = renderSuccess();
        List<DcCertTemplate> dcCertTemplatePage = dcCertTemplateService.getMbbsList(qxdm,zzmc);
        restResult.setData(dcCertTemplatePage);
        return restResult;
    }
}

