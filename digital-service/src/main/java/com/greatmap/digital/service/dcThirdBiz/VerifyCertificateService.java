package com.greatmap.digital.service.dcThirdBiz;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.greatmap.digital.dc3psDto.VerifyCertificateDto;
import com.greatmap.digital.excepition.DigitalThirdException;
import com.greatmap.digital.service.DcCertInfoService;
import com.greatmap.digital.util.FileUtil;
import com.greatmap.digital.util.FileUtils;
import com.greatmap.digital.util.StringUtils;
import com.greatmap.fms.model.FileInfo;
import com.greatmap.fms.service.FileInfoService;
import com.greatmap.fms.service.FileUploadService;
import com.greatmap.framework.web.controller.RestResult;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 验证证书正确性业务逻辑处理类
 *
 * @author 宋梦杉
 * @create 2020-06-30
 */
@Service
public class VerifyCertificateService {

    private Logger logger = LoggerFactory.getLogger(VerifyCertificateService.class);


    @Reference(registry = "fms")
    private FileUploadService fileUploadService;

    @Autowired
    private DcCertInfoService dcCertInfoService;

    /**
     * 解析zip文件中参数
     *
     * @param multipartFile 压缩包
     * @return
     */
    public RestResult verifyCertificateInfo(MultipartFile multipartFile, String tempDirecotry) {
        InputStream inputStream = null;
        RestResult restResult = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e){
            logger.error("第三方查验电子证照：获取文件流失败！");
             restResult = new RestResult(false, "获取请求文件流失败！");
            return restResult;
        }
        //解压文件
        tempDirecotry = tempDirecotry + "verify";
        File upZipFile = FileUtil.upzipCaseinfo(inputStream, tempDirecotry);
        IOUtils.closeQuietly(inputStream);

        if (upZipFile == null) {
            logger.error("第三方查验电子证照：未成功解压压缩包！");
             restResult = new RestResult(false, "未成功解压压缩包");
            return restResult;
        }
        ParseVerifyCXml parseInternetEstateXml = new ParseVerifyCXml(FileUtil.getXmlContent(upZipFile));
        //获取xml内容JSON数据
        JSONObject jsonObject = parseInternetEstateXml.parseContent();

        String zh = jsonObject.getString("zh");
        String zjzl = jsonObject.getString("zjzl");
        String yhm = jsonObject.getString("yhm");
        String jg = jsonObject.getString("jg");
        String ip = jsonObject.getString("ip");

        if(StringUtils.isBlank(zh)){
            logger.error("第三方查验电子证照：XML中证号为空！");
             restResult = new RestResult(false, "XML中证号为空");
            return restResult;
        } else if(StringUtils.isBlank(zh)){
            logger.error("第三方查验电子证照：XML中证照类型为空！");
             restResult = new RestResult(false, "XML中证照类型为空");
            return restResult;
        }

        //获取待验证文件
        File[] files = upZipFile.listFiles((dir, name) -> name.endsWith(".pdf"));
        if (files == null || files.length < 1) {
            throw new DigitalThirdException("第三方查验电子证照：传输文件内容错误，缺少pdf文件！");
        }
        MultipartFile pdfMultiFile = FileUtils.getMulFileByPath(files[0].getPath());

        //验证
         restResult = new RestResult();
        restResult = dcCertInfoService.verifyCertificate(restResult, zjzl, zh, "", yhm, jg, ip);

        //删除压缩文件及文件夹
        String fileName = upZipFile.getPath() + ".zip";
        File zipFile = new File(fileName);
        if (zipFile.exists()) {
            zipFile.delete();
        }
        FileUtils.deleteDirectory(upZipFile.getPath());

        return restResult;
    }
}
