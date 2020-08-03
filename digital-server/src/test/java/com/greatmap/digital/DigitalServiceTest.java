package com.greatmap.digital;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ofdrw.layout.OFDDoc;
import org.ofdrw.layout.element.Paragraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

/**
 * @author guoan
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DigitalServiceTest {

    private Logger logger = LoggerFactory.getLogger(DigitalServiceTest.class);

    @Test
    public void testOfd() throws Exception{
        Path path = Paths.get("HelloWorld.ofd");
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            Paragraph p = new Paragraph("你好呀，OFD Reader&Writer！");
            ofdDoc.add(p);
        }
        logger.info("生成文档位置: " + path.toAbsolutePath());

    }

    @Test
    public void testFileHashCode() throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        String filePath = "C:\\Users\\15827\\Desktop\\不动产权证书.pdf";
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = fileInputStream.read(buffer, 0, 1024)) != -1) {
            md.update(buffer, 0, length);
        }
        fileInputStream.close();
        byte[] md5Bytes  = md.digest();
        BigInteger bigInt = new BigInteger(1, md5Bytes);//1代表绝对值
        System.out.println("文件hash值为：" + bigInt.toString(16));
    }

    @Test
    public void testGenerateQRCodeImage() throws Exception{

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String text = "宁夏演示";

        BitMatrix bitMatrix = qrCodeWriter.encode(new String(text.getBytes("UTF-8"), "ISO-8859-1"), BarcodeFormat.QR_CODE, 350, 350);

        Path path = FileSystems.getDefault().getPath("C:\\Users\\15827\\Desktop\\testQRCode.PNG");

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

}
