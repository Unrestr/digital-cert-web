package com.greatmap.digital.controller;


import com.greatmap.digital.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 证明打印信息 前端控制器
 * </p>
 *
 * @author gaorui
 * @since 2020-06-25
 */
@Api(description = "证明打印信息 前端控制器",value = "证明打印信息")
@CrossOrigin
@RestController
@RequestMapping("/dcRegistrationPrint")
public class DcRegistrationPrintController extends BaseController {

}

