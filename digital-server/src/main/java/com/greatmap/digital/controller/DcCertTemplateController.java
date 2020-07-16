package com.greatmap.digital.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.greatmap.digital.annotation.SystemLog;
import com.greatmap.digital.base.BaseController;
import com.greatmap.digital.model.DcCertTemplate;
import com.greatmap.digital.service.DcCertTemplateService;
import com.greatmap.digital.util.ReflectUtil;
import com.greatmap.framework.web.controller.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.DigestException;

/**
 * <p>
 * 模板信息表。第三方系统传值json数据电子证照通过json打印证照 然后盖章。需要通过模板标识找对应模板打印再盖章 前端控制器
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Api(description = "模板信息 前端控制器",value = "模板信息")
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
    public RestResult listTemplate(@ApiParam("区县名称") @RequestParam(value = "qxmc",required = false) String qxmc,
                                   @ApiParam("模板标识") @RequestParam(value = "mbbs",required = false) String mbbs,
                                   @ApiParam(value = "当前页(大于等于0)", defaultValue = "0") @RequestParam(required = false, defaultValue = "0") int nCurrent,
                                   @ApiParam(value = "每页记录数量", defaultValue = "10") @RequestParam(required = false, defaultValue = "10") int nSize,
                                   @ApiParam(value = "排序字段", example = "id DESC") @RequestParam(required = false) String orderByField,
                                   @ApiParam(value = "是否升序") @RequestParam(required = false) boolean isAsc) {
        RestResult restResult = renderSuccess();
        Page<DcCertTemplate> page = new Page<>(nCurrent, nSize, orderByField, isAsc);
        Page<DcCertTemplate> dcCertTemplatePage = dcCertTemplateService.listTemplate(page,qxmc,mbbs);
        restResult.setData(dcCertTemplatePage);
        return restResult;
    }


    @SystemLog(description = "修改证照模板状态")
    @ApiOperation("修改证照模板状态")
    @PostMapping("updateTemplate")
    public RestResult updateTemplate(@ApiParam("模板ID") @RequestParam(value = "id",required = true) String id,
                                     @ApiParam("状态") @RequestParam(value = "zt",required = true) String zt) throws DigestException {
        boolean flag = dcCertTemplateService.updateTemplate(id,zt);
        if(flag){
            return renderSuccess("修改成功");
        }
        return renderFailure("修改失败");
    }
}

