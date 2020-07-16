package com.greatmap.digital.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.greatmap.digital.annotation.SystemLog;
import com.greatmap.digital.base.BaseController;
import com.greatmap.digital.model.DcCertFile;
import com.greatmap.digital.service.DcCertFileService;
import com.greatmap.digital.util.ReflectUtil;
import com.greatmap.framework.web.controller.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gaorui
 * @since 2020-01-04
 */
@Api(value = "证照文件操作接口", description = "证照文件操作接口")
@CrossOrigin
@RestController
@RequestMapping("/dcCertFile")
@SystemLog(description = "证照文件管理")
public class DcCertFileController extends BaseController {

    @Autowired
    private ReflectUtil reflectUtil;

    @Autowired
    private DcCertFileService dcCertFileService;

    @SystemLog(description = "获取有效证照信息分页")
    @ApiOperation("获取有效证照信息分页")
    @GetMapping(value = "findCertFilePage")
    public RestResult findCertFilePage(@ApiParam(value = "当前页(大于等于0)", defaultValue = "0") @RequestParam(required = false, defaultValue = "0") int nCurrent,
                                       @ApiParam(value = "每页记录数量", defaultValue = "10") @RequestParam(required = false, defaultValue = "10") int nSize,
                                       @ApiParam(value = "排序字段", example = "id DESC") @RequestParam(required = false) String orderByField,
                                       @ApiParam(value = "是否升序") @RequestParam(required = false) boolean isAsc) {
        RestResult restResult = renderSuccess();
   /*     Page<DcCertFile> page = new Page<>(nCurrent, nSize, orderByField,isAsc);
        Page<DcCertFile> certFilePage = dcCertFileService.selectPage(page);
        restResult.setData(certFilePage);*/

        Page<DcCertFile> page = new Page<>(nCurrent, nSize, orderByField, isAsc);
        ArrayList<DcCertFile> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            DcCertFile dcCertTemplate = (DcCertFile) reflectUtil.autoSetValue(DcCertFile.class);
            list.add(dcCertTemplate);
        }
        page.setRecords(list);
        restResult.setData(page);

        return restResult;
    }

}

