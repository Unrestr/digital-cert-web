package com.greatmap.digital.service.dcThirdBiz;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.greatmap.digital.annotation.DcLog;
import com.greatmap.digital.dto.CertificateDto;
import com.greatmap.digital.excepition.DigitalThirdException;
import com.greatmap.digital.mapper.DcCertInfoMapper;
import com.greatmap.digital.model.DcCertFile;
import com.greatmap.digital.model.DcCertInfo;
import com.greatmap.digital.service.DcCertFileService;
import com.greatmap.digital.service.DcCertInfoService;
import com.greatmap.digital.util.FileUtils;
import com.greatmap.digital.util.Path;
import com.greatmap.fms.service.FileInfoService;
import com.greatmap.fms.service.FileUploadService;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证证书正确性业务逻辑处理类
 *
 * @author sms
 * @create 2020-07-01
 */
@Service
public class DownloadCertificateService {

    private Logger logger = LoggerFactory.getLogger(VerifyCertificateService.class);

    @Autowired
    private DcCertInfoService dcCertInfoService;

    @Autowired
    private DcCertFileService dcCertFileService;

    @Reference(registry = "fms")
    private FileUploadService fileUploadService;

    @Reference(registry = "fms")
    private FileInfoService fileInfoService;

    @Autowired
    private DcCertInfoMapper dcCertInfoMapper;

    /**
     * 解析zip文件中参数
     *
     * @param tempDirecotry 临时文件夹路径
     * @param response
     * @param zh
     * @param yhm
     * @param jg
     * @param ip
     */
    @DcLog(operateType = "证照下载")
    public void downloadCertificate(String tempDirecotry, HttpServletResponse response, String zh, String yhm, String jg, String ip) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        //根据zh查找fms已经上传文件
        EntityWrapper<DcCertInfo> dcCertInfoEntityWrapper = new EntityWrapper<>();
        dcCertInfoEntityWrapper.eq("ZH", zh).orderBy("BFSJ", false);
        List<DcCertInfo> dcCertInfoList = dcCertInfoService.selectList(dcCertInfoEntityWrapper);
        if (dcCertInfoList == null || dcCertInfoList.size() < 1) {
            logger.info("下载证照：查询证号没有电子证照");
            return;
        }
        String zzbh = dcCertInfoList.get(0).getZzbh();
        EntityWrapper<DcCertFile> dcCertFileEntityWrapper = new EntityWrapper<>();
        dcCertFileEntityWrapper.eq("ZZBH", zzbh);
        List<DcCertFile> dcCertFiles = dcCertFileService.selectList(dcCertFileEntityWrapper);
        if (dcCertFiles == null || dcCertFiles.size() < 1) {
            logger.info("下载证照：查询证号没有生成电子证照");
            return;
        }
        String fileID = dcCertFiles.get(0).getWjid();
        String fileName = dcCertFiles.get(0).getWjmc();
        //创建文件夹
        Path tempPath = Path.get(tempDirecotry, "download");
        File tempPathFile = new File(tempPath.getPath());
        if (!tempPathFile.exists()) {
            tempPathFile.mkdirs();
        }
        //复制文件进本地临时文件目录
        File targetFile = new File(tempPath + "/" + zzbh + "-" + timestamp + "-" + fileName);
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                logger.info("下载证照：文件写入到" + targetFile.getPath() + "路径失败！");
                return;
            }
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(targetFile);
            byte[] fileByte = fileUploadService.getFileByte(fileID);
            //字节文件写入targetFile文件中
            fileOutputStream.write(fileByte);
        } catch (FileNotFoundException e) {
            logger.info("下载证照：路径" + targetFile.getPath() + "文件未找到！");
            return;
        } catch (IOException e) {
            logger.info("下载证照：文件写入到" + targetFile.getPath() + "路径失败！");
            return;
        } catch (Exception e) {
            logger.info("下载证照：获取fms文件写入临时文件失败！" + e.getMessage());
            return;
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }

        logger.info("下载证照：复制FMS文件成功！");
        String targetPath = tempPath.getPath();
        try {
            //设置为文件流格式
            response.setContentType("application/x-msdownload");
            //设置文件名及字符集
            String downLoadFileName = new String((zh + ".zip").getBytes("UTF-8"), "ISO8859-1");
            response.setHeader("Content-disposition", "attachment; filename=" + downLoadFileName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String zipFileName = targetPath + "/" + zzbh + "-" + timestamp + ".zip";
        try {
            //压缩文件
            FileUtils.zipFiles(targetPath, targetFile.getName(), zipFileName);
        } catch (Exception e) {
            logger.info("下载证照：临时压缩文件创建失败！" + e.getMessage());
        }
        //文件放入响应结果
        File zipFile = new File(zipFileName);
        if (zipFile.exists()) {
            long fileLength = zipFile.length();
            //响应头中文件大小
            response.setHeader("Content-Length", String.valueOf(fileLength));
            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFileName));
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                byte[] buff = new byte[2048];
                int bytesRead;
                //每次读2048个字节写入响应结果
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
                IOUtils.closeQuietly(bis);
                IOUtils.closeQuietly(bos);

                zipFile.delete();
                logger.info("删除临时压缩文件成功！");
                File tempDirFile = new File(targetFile.getPath());
                if (tempDirFile.exists()) {
                    FileUtils.deleteDirectory(tempDirFile.getPath());
                    logger.info("删除临时文件成功！");
                }

            } catch (Exception e) {
                logger.info("下载证照：压缩文件写入响应流失败！" + e.getMessage());
            }
        } else {
            logger.info("下载证照：未取到临时压缩文件！路径：" + zipFileName);
        }
    }

    @DcLog(operateType = "证照下载")
    public List<Map<String,String>> download(String zh, String userName, String djjg, String ip) {
        DcCertInfo dcCertInfo = dcCertInfoService.selectOne(new EntityWrapper<DcCertInfo>().eq("ZH", zh));
        if (dcCertInfo == null) {
            throw new DigitalThirdException("未获取到证照信息" + zh);
        }
        DcCertFile certFile = dcCertFileService.selectOne(new EntityWrapper<DcCertFile>().eq("ZZBH", dcCertInfo.getZzbh()));
        byte[] fileByte = fileUploadService.getFileByte(certFile.getWjid());
        if (fileByte == null || fileByte.length == 0) {
            throw new DigitalThirdException("未获取到文件信息" + zh);
        }
        String encode = Base64.getEncoder().encodeToString(fileByte);
        Map<String,String> map = new HashMap<>(1);
        map.put("zzxx",encode);
        List<Map<String,String>> mapList = Lists.newArrayList();
        mapList.add(map);
        return mapList;
    }

    @DcLog(operateType = "证照查询")
    public List<CertificateDto> queryCertificateList(String zh, String qlrmc, String qlrzjh, String userName, String djjg, String ip) {
        return dcCertInfoMapper.queryCertificateList(zh, qlrmc, qlrzjh);
    }
}
