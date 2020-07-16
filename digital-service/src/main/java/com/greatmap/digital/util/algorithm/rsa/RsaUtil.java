package com.greatmap.digital.util.algorithm.rsa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greatmap.framework.commons.mapper.JsonMapper;
import com.greatmap.framework.commons.utils.StringUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密算法工具类
 * @author guoan
 */
public class RsaUtil {
    /**
     * 定义加密方式
     */
    private final static String KEY_RSA = "RSA";
    /**
     * 定义签名算法
     */
    private final static String KEY_RSA_SIGNATURE = "MD5withRSA";
    /**
     * 定义公钥算法
     */
    private final static String KEY_RSA_PUBLICKEY = "RSAPublicKey";
    /**
     * 定义私钥算法
     */
    private final static String KEY_RSA_PRIVATEKEY = "RSAPrivateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK_256 = 256;

    private RsaUtil() {
    }

    /**
     * 生成公钥、私钥对象
     * @return 封装公钥私钥对象的map
     */
    public static Map<String, Object> generateKey() {
        Map<String, Object> map = new HashMap<>(2);
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);
            generator.initialize(1024);
            KeyPair keyPair = generator.generateKeyPair();
            // 公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 将密钥封装为map
            map.put(KEY_RSA_PUBLICKEY, publicKey);
            map.put(KEY_RSA_PRIVATEKEY, privateKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * 获取公钥字符串
     * @param map 封装公钥私钥对象的map
     * @return 返回公钥字符串
     */
    public static String getPublicKey(Map<String, Object> map) {
        String str = "";
        try {
            Key key = (Key) map.get(KEY_RSA_PUBLICKEY);
            byte[] publicKey = key.getEncoded();
            str = encryptBase64(publicKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    /**
     * 获取私钥字符串
     * @param map 封装公钥私钥对象的map
     * @return 返回私钥字符串
     */
    public static String getPrivateKey(Map<String, Object> map) {
        String str = "";
        try {
            Key key = (Key) map.get(KEY_RSA_PRIVATEKEY);
            str = encryptBase64(key.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    /**
     * 用私钥对参数字符串生成数字签名
     * @param privateKey 私钥
     * @param dataStr 参数字符串
     * @return 数字签名
     */
    public static String sign(String privateKey, String dataStr) {
        String str = "";
        try {
            byte[] data = dataStr.getBytes("UTF-8");
            return sign(privateKey, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用私钥对参数字节数组生成数字签名
     * @param privateKey 私钥
     * @param data 参数字节数组
     * @return 数字签名
     */
    public static String sign(String privateKey, byte[] data) {
        String str = "";
        try {
            //指定加密算法
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            //对私钥进行base64解码
            byte[] bytes = decryptBase64(privateKey);
            //构造PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec pkc = new PKCS8EncodedKeySpec(bytes);
            // 取私钥对象
            PrivateKey key = factory.generatePrivate(pkc);
            // 用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initSign(key);
            signature.update(data);
            str = encryptBase64(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    /**
     * 用公钥对数字签名进行校验
     * @param publicKey 公钥
     * @param dataStr 原文本
     * @param sign 数字签名
     * @return 数字签名是否合法
     */
    public static boolean verify(String publicKey, String dataStr, String sign) {

        try {
            byte[] data = dataStr.getBytes("UTF-8");
            return verify(publicKey, data, sign);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 用公钥对数字签名进行校验
     * @param publicKey 公钥
     * @param data 原文本字节数组
     * @param sign 数字签名
     * @return 数字签名是否合法
     */
    public static boolean verify(String publicKey, byte[] data, String sign) {
        boolean flag = false;
        try {
            //指定加密算法
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            //对公钥进行base64解码
            byte[] bytes = decryptBase64(publicKey);
            //构造X509EncodedKeySpec对象
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            //取公钥对象
            PublicKey key = factory.generatePublic(keySpec);
            //用公钥验证数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initVerify(key);
            signature.update(data);
            flag = signature.verify(decryptBase64(sign));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * 公钥加密
     * @param publicKey 公钥
     * @param dataStr 需要加密的数据
     * @return 加密后的数据
     */
    public static String encryptByPublicKey(String publicKey, String dataStr) {
        try {
            byte[] result = encryptByPublicKey(publicKey, dataStr.getBytes("UTF-8"));
            return encryptBase64(result);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 公钥加密
     * @param publicKey 公钥
     * @param data 需要加密数据的字节数组
     * @return 加密后的数据字节数组
     */
    public static byte[] encryptByPublicKey(String publicKey, byte[] data) {
        byte[] result = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //对公钥进行base64解码
            byte[] bytes = decryptBase64(publicKey);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            //取得公钥
            PublicKey pubKey = factory.generatePublic(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            int inputLen = data.length;

            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用私钥解密数据
     * @param privateKey 私钥
     * @param dataStr 需要解密的数据
     * @return 解密后的字符串
     */
    public static String decryptByPrivateKey256(String privateKey, String dataStr) {
        try {
            byte[] result = decryptByPrivateKey(privateKey, decryptBase64(dataStr),MAX_DECRYPT_BLOCK_256);
            return new String(result);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 使用私钥解密数据，最大解密密文大小为256
     * @param privateKey 私钥
     * @param dataStr 需要解密的数据
     * @return 解密后的字符串
     */
    public static String decryptByPrivateKey(String privateKey, String dataStr) {
        try {
            byte[] result = decryptByPrivateKey(privateKey, decryptBase64(dataStr),MAX_DECRYPT_BLOCK);
            return new String(result);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 使用私钥解密数据
     * @param privateKey 私钥
     * @param data 需要解密的字节数组
     * @param maxDecryptBlock 解密密文大小
     * @return 解密后的字符串
     */
    public static byte[] decryptByPrivateKey(String privateKey, byte[] data,int maxDecryptBlock) {
        byte[] result = null;
        try {
            // 获取私钥字符串时,进行了encryptBase64操作,因此此处需对私钥解密
            byte[] bytes = decryptBase64(privateKey);
            // 取得私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey priKey = factory.generatePrivate(keySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, priKey);

            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > maxDecryptBlock) {
                    cache = cipher
                            .doFinal(data, offSet, maxDecryptBlock);
                } else {
                    cache = cipher
                            .doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * maxDecryptBlock;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 使用私钥加密数据
     * @param privateKey 私钥
     * @param dataStr 需要加密的字符串
     * @return 加密后的字符串
     */
    public static String encryptByPrivateKey(String privateKey, String dataStr) {
        try {
            byte[] result = encryptByPrivateKey(privateKey, dataStr.getBytes("UTF-8"));
            return encryptBase64(result);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 使用私钥加密数据
     * @param privateKey 私钥
     * @param data 需要加密的字符数组
     * @return 加密后的字节数组
     */
    public static byte[] encryptByPrivateKey(String privateKey, byte[] data) {
        try {
            byte[] bytes = decryptBase64(privateKey);
            // 取得私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey priKey = factory.generatePrivate(keySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, priKey);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用公钥解密数据
     * @param publicKey 公钥
     * @param dataStr 加密字符串
     * @return 解密后的数据
     */
    public static String decryptByPublicKey(String publicKey, String dataStr) {
        try {
            byte[] result = decryptByPublicKey(publicKey, decryptBase64(dataStr));
            return new String(result,"UTF-8");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 使用公钥解密数据
     * @param publicKey  公钥
     * @param data 加密字节数组
     * @return  解密后的字节数组
     */
    public static byte[] decryptByPublicKey(String publicKey, byte[] data) {
        try {
            //对公钥进行base64解码
            byte[] bytes = decryptBase64(publicKey);
            //取得公钥
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey pubKey = factory.generatePublic(keySpec);
            //对数据解密
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * BASE64 解密
     * @param key 需要解密的字符串
     * @return 字节数组
     */
    public static byte[] decryptBase64(String key) {
        return javax.xml.bind.DatatypeConverter.parseBase64Binary(key);
    }

    /**
     * BASE64 加密
     * @param key 需要加密的字节数组
     * @return 字符串
     */
    public static String encryptBase64(byte[] key) {
        return javax.xml.bind.DatatypeConverter.printBase64Binary(key);
    }

    /**
     * 生成公钥私钥并存入map对象中
     * @return 存放公钥私钥的map
     */
    public static Map<String,String> getKeyMap(){
        Map<String,String> keyMap = new HashMap<>(16);
        Map<String,Object> map = RsaUtil.generateKey();
        keyMap.put("publicKey",RsaUtil.getPublicKey(map));
        keyMap.put("privateKey",RsaUtil.getPrivateKey(map));
        return keyMap;
    }

    /**
     * 公钥加密map对象的数据
     * @param map 需要加密的map对象
     * @param publicKey 公钥
     * @return 返回加密后的数据
     */
    public static String encryptMapByPublicKey(Map<String,Object> map,String publicKey){
        //将请求参数转成json字符串
        String jsonParam = JsonMapper.toJsonString(map);
        try {
            //将json字符串进行utf-8编码
            jsonParam = URLEncoder.encode(jsonParam, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //对请求参数进行公钥加密
        String encryptParam = RsaUtil.encryptByPublicKey(publicKey, jsonParam);

        Map<String, String> vertimap = new HashMap<>(16);
        vertimap.put("encryptParam", encryptParam);
        // 将加密参数转成json字符串
        String jsonVerti = JsonMapper.toJsonString(vertimap);
        return jsonVerti;
    }

    /**
     * 私钥解密数据并验证时间戳参数
     * @param encryptParam 加密数据
     * @param privateKey 私钥
     * @return 返回解密后的数据
     */
    public static Map<String,Object> decryptStrByPrivateKey(String encryptParam,String privateKey){
        Map<String,String> decryptMap;
        Map<String, Object> depmap;
        ObjectMapper mapper = new ObjectMapper();
        String decryptParam = "";
        try {
            decryptMap = mapper.readValue(encryptParam, Map.class);
            decryptParam = decryptMap.get("encryptParam").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            decryptParam = RsaUtil.decryptByPrivateKey(privateKey, decryptParam);
            decryptParam = URLDecoder.decode(decryptParam, "UTF-8");
            //验证时间戳参数，防止恶意请求
            try {
                depmap = mapper.readValue(decryptParam, Map.class);
            } catch (Exception er) {
                throw new RuntimeException("数据非法，请联系系统管理员申请！" + er.getMessage());
            }
            Map<String, String> mapValue = (Map) depmap;
            if (!mapValue.containsKey("timestamp")) {
                throw new RuntimeException("参数非法，未包含timestamp信息！");
            }
            String timestamp = mapValue.get("timestamp").toString();
            if (StringUtils.isBlank(timestamp)) {
                throw new RuntimeException("参数非法，timestamp信息为空！");
            }
            long lngRequestTime = Long.valueOf(timestamp);
            long currentTime = System.currentTimeMillis();
            Long s = (currentTime - lngRequestTime) / (1000 * 60);
            if (s >= 30) {
                throw new RuntimeException("链接超时，请检查时间戳！");
            }
        } catch (Exception er) {
            throw new RuntimeException("数据非法，请联系系统管理员申请获取正确的key！");
        }
        return depmap;
    }

    /**
     * 私钥加密map对象数据
     * @param map 加密对象
     * @param privateKey 私钥
     * @return 返回私钥加密数据
     */
    public static String encryptMapByPrivateKey(Map<String,Object> map,String privateKey){
        String jsonParam = JsonMapper.toJsonString(map);
        try {
            jsonParam = URLEncoder.encode(jsonParam, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String resultParam = RsaUtil.encryptByPrivateKey(privateKey, jsonParam);
        Map<String, String> vertimap = new HashMap<>(16);
        vertimap.put("result", resultParam);
        // 将加密参数转成json字符串
        String jsonVerti = JsonMapper.toJsonString(vertimap);
        return jsonVerti;
    }

    /**
     * 公钥解密数据
     * @param encryptParam 加密数据
     * @param publicKey 公钥
     * @return 返回解密后的数据
     */
    public static Map<String,Object> decryptStrByPublicKey(String encryptParam,String publicKey){
        Map<String,String> decryptMap;
        Map<String, Object> depmap;
        ObjectMapper mapper = new ObjectMapper();
        String decryptParam = "";
        try {
            decryptMap = mapper.readValue(encryptParam, Map.class);
            decryptParam = decryptMap.get("result").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            decryptParam = RsaUtil.decryptByPublicKey(publicKey, decryptParam);
            decryptParam = URLDecoder.decode(decryptParam, "UTF-8");
            try {
                depmap = mapper.readValue(decryptParam, Map.class);
            } catch (Exception e) {
                throw new RuntimeException("数据非法，请联系系统管理员申请！" + e.getMessage());
            }

        } catch (Exception er) {
            throw new RuntimeException("数据非法，请联系系统管理员申请获取正确的key！");
        }
        return depmap;
    }

    public static void main(String[] args) {
        Map<String, Object> stringObjectMap = generateKey();
        String privateKey = getPrivateKey(stringObjectMap);
        String publicKey = getPublicKey(stringObjectMap);
        System.out.println(privateKey);
        System.out.println(publicKey);
    }
}
