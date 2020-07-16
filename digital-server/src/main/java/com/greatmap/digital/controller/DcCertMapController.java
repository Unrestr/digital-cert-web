package com.greatmap.digital.controller;


import com.greatmap.digital.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 附图信息 宗地图和房产分户图 前端控制器
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Api(description = "附图信息 宗地图和房产分户图 前端控制器",value = "附图信息 宗地图和房产分户图")
@CrossOrigin
@RestController
@RequestMapping("/dcCertMap")
public class DcCertMapController extends BaseController {

}

