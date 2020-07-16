package com.greatmap.digital.controller;


import com.greatmap.digital.base.BaseController;
import com.greatmap.uums.sso.restClient.common.Auth;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 关联证书信息 前端控制器
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Api(description = "关联证书信息 前端控制器",value = "关联证书信息")
@CrossOrigin
@RestController
@RequestMapping("/dcCertAssotype")
public class DcCertAssotypeController extends BaseController {

}

