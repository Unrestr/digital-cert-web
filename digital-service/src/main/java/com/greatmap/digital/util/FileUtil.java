package com.greatmap.digital.util;

import com.greatmap.digital.excepition.DigitalException;
import com.greatmap.digital.excepition.DigitalThirdException;
import com.greatmap.digital.util.algorithm.base64.Base64;
import com.greatmap.framework.core.util.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.UUID;

/**
 * @author gaorui
 * @create 2020-01-07 10:14
 */
public class FileUtil {

    /**
     * 将MultipartFile转换成File对象
     *
     * @param multipartFile
     * @return
     */
    public static File multipartFileToFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty() || StringUtils.isBlank(multipartFile.getOriginalFilename())) {
            throw new DigitalException("文件为空或属性不完整");
        }
        String fileName = multipartFile.getOriginalFilename();
        try {
            File file = File.createTempFile(RandomUtil.simpleUUID(), fileName.substring(fileName.lastIndexOf(".")));
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解压压缩包
     *
     * @param inputStream
     * @return
     */
    public static File upzipCaseinfo(InputStream inputStream, String filePath) {
        if (inputStream == null) {
            return null;
        }
        String s = String.format("%s/%s.zip", filePath, UUID.randomUUID().toString());
        File file = com.greatmap.framework.core.io.FileUtil.writeFromStream(inputStream, s);
        //解压到当前文件夹
        String descFileName = s.substring(0, s.length() - 4);
        unZipFiles(file.getAbsolutePath(), descFileName, null);
        return new File(descFileName);
    }

    /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到descFileName目录下
     *
     * @param zipFileName  需要解压的ZIP文件
     * @param descFileName 目标文件
     */
    public static boolean unZipFiles(String zipFileName, String descFileName, String encoding) {
        if (com.greatmap.framework.commons.utils.StringUtils.isBlank(encoding)) {
            encoding = null;
        }
        String descFileNames = descFileName;
        if (!descFileNames.endsWith(File.separator)) {
            descFileNames = descFileNames + File.separator;
        }
        try {
            // 根据ZIP文件创建ZipFile对象
            ZipFile zipFile = new ZipFile(zipFileName, encoding);
            ZipEntry entry = null;
            String entryName = null;
            String descFileDir = null;
            byte[] buf = new byte[4096];
            int readByte = 0;
            // 获取ZIP文件里所有的entry
            @SuppressWarnings("rawtypes")
            Enumeration enums = zipFile.getEntries();
            // 遍历所有entry
            while (enums.hasMoreElements()) {
                entry = (ZipEntry) enums.nextElement();
                // 获得entry的名字
                entryName = entry.getName();
                descFileDir = descFileNames + entryName;
                if (entry.isDirectory()) {
                    // 如果entry是一个目录，则创建目录
                    new File(descFileDir).mkdirs();
                    continue;
                } else {
                    // 如果entry是一个文件，则创建父目录
                    new File(descFileDir).getParentFile().mkdirs();
                }
                File file = new File(descFileDir);
                // 打开文件输出流
                OutputStream os = new FileOutputStream(file);
                // 从ZipFile对象中打开entry的输入流
                InputStream is = zipFile.getInputStream(entry);
                while ((readByte = is.read(buf)) != -1) {
                    os.write(buf, 0, readByte);
                }
                os.close();
                is.close();
            }
            zipFile.close();
            // log.debug("文件解压成功!");
            return true;
        } catch (Exception e) {
            // log.debug("文件解压失败：" + e.getMessage());
            return false;
        }
    }

    /**
     * 获取xml内容
     * 查找解压目录下面xml文件，获取流程数据内容
     *
     * @param dirZip
     * @return
     */
    public static String getXmlContent(File dirZip) {
        if (dirZip == null) {
            return null;
        }
        //查找解压目录下面xml文件，获取流程数据内容
        File[] files = dirZip.listFiles((dir, name) -> name.endsWith(".xml"));
        if (files == null || files.length < 1) {
            throw new DigitalThirdException("传输文件内容错误，缺少xml文件！");
        }
        String content = com.greatmap.framework.core.io.FileUtil.readString(files[0], StandardCharsets.UTF_8);
        return content;
    }

    /**
     * InputStream转化为byte[]数组
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
    /**
     * byte数组转file
     * @param bytes
     * @param filePath
     * @param fileName
     * @return
     */
    public static File getFileByBytes(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            // 判断文件目录是否存在
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(filePath + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static File base64ToFile(String base64) {
        if(base64==null||"".equals(base64)) {
            return null;
        }
        byte[] buff= Base64.decode(base64);
        File file=null;
        FileOutputStream fout=null;
        try {
            file = File.createTempFile("verify", ".pdf");
            fout=new FileOutputStream(file);
            fout.write(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fout!=null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }
}
