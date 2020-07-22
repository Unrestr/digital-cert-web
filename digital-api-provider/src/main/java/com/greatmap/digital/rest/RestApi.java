package com.greatmap.digital.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.greatmap.dex.aop.ResolvedDexParams;
import com.greatmap.digital.base.BaseController;
import com.greatmap.digital.dto.CertificateDto;
import com.greatmap.digital.dto.rest.CertDto;
import com.greatmap.digital.dto.rest.QlrQueryDto;
import com.greatmap.digital.excepition.DigitalThirdException;
import com.greatmap.digital.service.DcCertInfoService;
import com.greatmap.digital.service.dcThirdBiz.DownloadCertificateService;
import com.greatmap.digital.service.dcThirdBiz.VerifyCertificateService;
import com.greatmap.fms.service.FileUploadService;
import com.greatmap.framework.core.util.CollectionUtil;
import com.greatmap.framework.web.controller.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.greatmap.digital.base.DcConstants.RequestParam.*;

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
    private VerifyCertificateService verifyCertificateService;

    @Autowired
    private DownloadCertificateService downloadCertificateService;


    @Value("${tempDirectory}")
    private String tempDirectory;

    @Reference
    private FileUploadService fileUploadService;

    /**
     * 添加证照文件
     *
     * @param requestData
     * @return
     */
    @PostMapping("addPropertyCertificate")
    @ApiOperation("新增证书签章")
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

       /* if (CollectionUtil.isEmpty(certParam.getDyxxList()) && requestParam.containsKey(JCXX))) {
            throw new DigitalThirdException("缺少单元基础信息");
        }
        if (!requestParam.containsKey(ZSDYXX)) {
            throw new DigitalThirdException("缺少需要打印信息");
        }*/
        CertDto certDto = certParam;
        Map<String, String> addCert = dcCertInfoService.addCertificate(certDto, certDto.getIp(), certDto.getDjjg(), certDto.getZh(), certDto.getYhm());
        List<Map<String, String>> list = new ArrayList<>();
        list.add(addCert);
        return list;
    }

    @PostMapping("addRegistrationCertificate")
    @ApiOperation("新增证明签章")
    public Object addRegistrationCertificate(String requestData) {
        if (StringUtils.isBlank(requestData)) {
            throw new DigitalThirdException("生成证书失败,传入信息为空。");
        }
        JSONObject requestParam = JSONObject.parseObject(requestData);
        if (!(requestParam.containsKey(QLR_LIST) && requestParam.containsKey(QLXX))) {
            throw new DigitalThirdException("缺少权利人、权利信息");
        }

        if (!(requestParam.containsKey(DYXX_LIST) && requestParam.containsKey(JCXX))) {
            throw new DigitalThirdException("缺少单元基础信息");
        }
        if (!requestParam.containsKey(ZMDYXX)) {
            throw new DigitalThirdException("缺少需要打印信息");
        }
        CertDto certDto = JSONObject.parseObject(requestData, CertDto.class);
        Map<String, String> addCert = dcCertInfoService.addCertificate(certDto, certDto.getIp(), certDto.getDjjg(), certDto.getZh(), certDto.getYhm());
        List<Map<String, String>> list = new ArrayList<>();
        list.add(addCert);
        return list;
    }

   /* @ApiOperation("验证数字证书")
    @PostMapping("verifyCertificate")
    public Object verifyCertificate(@ApiParam("证照信息") HttpServletRequest request) {
        ResponseMsg responseMsg = new ResponseMsg();
        Date now = new Date();
        String receiveDate = DateUtil.format(now, "yyyyMMdd");
        String receiveTime = DateUtil.format(now, "HHmmss");
        String receiverId = dcSystemCode;
        responseMsg.setReceiveDate(receiveDate);
        responseMsg.setReceiveTime(receiveTime);
        responseMsg.setReceiverId(receiverId);

        MultipartFile multipartFile = null;
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            //请求中有文件，说明请求参数被处理为xml文件和附件材料一起压缩成zip包传递，获取zip包写入指定目录
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multiRequest.getFileNames();
            if (iter.hasNext()) {
                String next = iter.next();
                multipartFile = multiRequest.getFile(next);
            }
        }
        if (multipartFile == null) {
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
            responseMsg.setRtnMessage(String.format("获取请求文件失败！"));
            return responseMsg.toString();
        }
        try {
            RestResult restResult = new RestResult();
            restResult = verifyCertificateService.verifyCertificateInfo(multipartFile, tempDirectory);
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_SUCCESS);

            JSONArray respJsonArray = new JSONArray();
            JSONObject dataJsonObject = new JSONObject();
            dataJsonObject.put("isSuccess", restResult.isSuccess());
            dataJsonObject.put("result", restResult.getMessage());
            respJsonArray.add(dataJsonObject);
            responseMsg.setData(respJsonArray);
            return responseMsg;


        } catch (DigitalThirdException e) {
            responseMsg.setTxStatusEnum(TxStatusEnum.RESPONSE_FAIL);
            responseMsg.setRtnMessage(e.getMessage());
            throw new DigitalThirdException(e.getMessage());
        }
    }*/

   /* @ApiOperation("验证数字证书-测试")
    @PostMapping("verifyCertificateTest")
    public RestResult verifyCertificateTest(@ApiParam("压缩包") MultipartFile verifyCertificateZip, HttpServletRequest request) {

        if (verifyCertificateZip == null || verifyCertificateZip.isEmpty()) {
            throw new DigitalThirdException("传入参数数据为空!");
        }
        RestResult restResult = verifyCertificateService.verifyCertificateInfo(verifyCertificateZip, tempDirectory);
        return restResult;
    }*/

   /* @ApiOperation("下载电子证书")
    @PostMapping("downloadCertificate")
    @ResolveThirdParams(encryptResponse = false)
    public void downloadCertificate(@ApiParam(value = "解密后请求参数") @RequestBody String data, HttpServletResponse response) {
        JSONArray jsonArray = JSONArray.parseArray(data);
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        String zh = jsonObject.getString("zh");
        String yhm = jsonObject.getString("yhm");
        String jg = jsonObject.getString("jg");
        String ip = jsonObject.getString("ip");
        downloadCertificateService.downloadCertificate(tempDirectory, response, zh, yhm, jg, ip);
    }*/

    @ApiOperation("验证数字证书")
    @PostMapping(value = "verify")
    public String verifyCertificate(@ApiParam(value = "证照名称") @RequestParam(required = true) String zzmc,
                                    @ApiParam(value = "证号/证明号") @RequestParam(required = true) String zh,
                                    @ApiParam(value = "用户名") @RequestParam(required = true) String userName,
                                    @ApiParam(value = "登记机构") @RequestParam(required = true) String djjg,
                                    @ApiParam(value = "IP") @RequestParam(required = true) String ip,
                                    String file) {
        if (StringUtils.isBlank(file)) {
            throw new DigitalThirdException("上传电子证照为空，无法验证！");
        }
        RestResult restResult = renderSuccess();
        dcCertInfoService.verifyCertificate(restResult, zzmc, zh, file, userName, djjg, ip);
        return restResult.getMessage();
    }

    @ApiOperation("下载电子证书")
    @PostMapping("download")
    public String download(@ApiParam(value = "证号/证明号") @RequestParam(required = true) String zh,
                           @ApiParam(value = "用户名") @RequestParam(required = true) String userName,
                           @ApiParam(value = "登记机构") @RequestParam(required = true) String djjg,
                           @ApiParam(value = "IP") @RequestParam(required = true) String ip) {

        String result = downloadCertificateService.download(zh, userName, djjg, ip);
        return result;
    }

    @ApiOperation("电子证照查询")
    @PostMapping("queryCertificateList")
    @ResolvedDexParams(name = "dexHttpServer", validate = "paramsValidate")
    public Object queryCertificateList(@ApiParam("传入参数") @RequestBody String params) {
        Map<String, String> oneWindowParam = JSON.parseObject(params, new TypeReference<Map<String, String>>() {
        });
        List<QlrQueryDto> qlrQueryDtos = JSONObject.parseArray(oneWindowParam.get("data"), QlrQueryDto.class);
        if (CollectionUtil.isEmpty(qlrQueryDtos)) {
            throw new DigitalThirdException("查询参数不能为空!");
        }
        QlrQueryDto queryDto = qlrQueryDtos.get(0);
        List<CertificateDto> result = downloadCertificateService.queryCertificateList(queryDto.getZh(), queryDto.getQlrmc(), queryDto.getQlrzjh(), queryDto.getYhm(), queryDto.getDjjg(), queryDto.getIp());
        return result;
    }
}
