package com.greatmap.digital.util.algorithm.des;

import com.greatmap.digital.excepition.DigitalException;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author guoan
 * DES，3DES，ASE 对称加密工具类
 * 对称密码算法的加密密钥和解密密钥相同，对于大多数对称密码算法，加解密过程互逆
 * AES对DES提高了安全性，有限选择AES加密方式
 */

public class DESUtil {

    /**
     * 定义加密算法
     */
    private final static String ALGORITHM_KEY = "DES";

    /**
     * 生成秘钥
     * @return
     */
    public static String generateSecretKey(){
        String secretKey = "";
        byte[] key = generateKey();
        if (key.length > 0 && key != null){
            secretKey = encryptBase64(key);
        }
        return secretKey;
    }

    /**
     * 生成秘钥
     * @return
     */
    public static byte[] generateKey() {
        KeyGenerator keyGen = null;
        try {
            // 秘钥生成器
            keyGen = KeyGenerator.getInstance(ALGORITHM_KEY);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DigitalException("秘钥生成异常");
        }
        // 初始秘钥生成器
        keyGen.init(56);
        // 生成秘钥
        SecretKey secretKey = keyGen.generateKey();
        // 获取秘钥字节数组
        return secretKey.getEncoded();
    }

    /**
     * 加密算法
     * @param data 明文数据
     * @param key 秘钥
     * @return
     */
    public static String encryptAlgorithm(String data,String key){
        if(StringUtils.isBlank(data)){
            throw new DigitalException("明文数据为空，加密失败！");
        }
        if(StringUtils.isBlank(key)){
            throw new DigitalException("秘钥为空，加密失败!");
        }
        try {
            byte[] encryptData = encryptAlgorithm(data.getBytes("UTF-8"),decryptBase64(key));
            if(encryptData.length > 0 && encryptData != null ){
                return encryptBase64(encryptData);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new DigitalException("转换字节数组异常");
        }

        return "";
    }

    /**
     * 加密算法
     * @param data 明文数据
     * @param key 秘钥
     * @return
     */
    public static byte[] encryptAlgorithm(byte[] data, byte[] key) {

        // 恢复秘钥
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM_KEY);
        Cipher cipher = null;
        byte[] cipherBytes = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM_KEY);
            // 对Cipher初始化,加密模式
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            // 加密数据
            cipherBytes = cipher.doFinal(data);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new DigitalException("无效key异常："+e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DigitalException("未知算法异常"+e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new DigitalException("未知填充异常"+e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new DigitalException("损坏填充异常"+e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new DigitalException("非法阻塞大小异常"+e.getMessage());
        }
        return cipherBytes;
    }

    /**
     * 解密算法
     * @param data 密文数据
     * @param key 秘钥
     * @return
     */
    public static String decryptAlgorithm(String data,String key){
        if(StringUtils.isBlank(data)){
            throw new DigitalException("密文数据为空，解密失败！");
        }
        if(StringUtils.isBlank(key)){
            throw new DigitalException("秘钥为空，解密失败!");
        }
        byte[] decryptData = decryptAlgorithm(decryptBase64(data),decryptBase64(key));
        if(decryptData.length > 0 && decryptData != null ){
            try {
                return new String(decryptData,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new DigitalException("编码转换异常");
            }
        }

        return "";
    }

    /**
     * 解密算法
     * @param data 密文数据
     * @param key 秘钥
     * @return
     */
    public static byte[] decryptAlgorithm(byte[] data, byte[] key) {
        // 恢复秘钥
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM_KEY);
        Cipher cipher = null;
        byte[] plainBytes = null;

        try {
            cipher = Cipher.getInstance(ALGORITHM_KEY);
            // 对Cipher初始化,解密模式
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            // 解密数据
            plainBytes = cipher.doFinal(data);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new DigitalException("无效key异常："+e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new DigitalException("未知算法异常"+e.getMessage());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            throw new DigitalException("未知填充异常"+e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
            throw new DigitalException("损坏填充异常"+e.getMessage());
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            throw new DigitalException("非法阻塞大小异常"+e.getMessage());
        }

        return plainBytes;
    }

    /**
     * BASE64 解密
     * @param key 需要解密的字符串
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] decryptBase64(String key) {
        return javax.xml.bind.DatatypeConverter.parseBase64Binary(key);
    }

    /**
     * BASE64 加密
     * @param key 需要加密的字节数组
     * @return 字符串
     * @throws Exception
     */
    public static String encryptBase64(byte[] key) {
        return javax.xml.bind.DatatypeConverter.printBase64Binary(key);
    }

}