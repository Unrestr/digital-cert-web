package com.greatmap.digital.util;

import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;



/**
 * SM2公钥加密算法实现 包括 -签名,验签 -密钥交换 -公钥加密,私钥解密
 * @author ZH
 *
 */
public class SM2Utils {

	private static BigInteger n = new BigInteger("FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "7203DF6B" + "21C6052B" + "53BBF409" + "39D54123", 16);
	private static BigInteger p = new BigInteger("FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFF", 16);
	private static BigInteger a = new BigInteger("FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFC", 16);
	private static BigInteger b = new BigInteger("28E9FA9E" + "9D9F5E34" + "4D5A9E4B" + "CF6509A7" + "F39789F5" + "15AB8F92" + "DDBCBD41" + "4D940E93", 16);
	private static BigInteger gx = new BigInteger("32C4AE2C" + "1F198119" + "5F990446" + "6A39C994" + "8FE30BBF" + "F2660BE1" + "715A4589" + "334C74C7", 16);
	private static BigInteger gy = new BigInteger("BC3736A2" + "F4F6779C" + "59BDCEE3" + "6B692153" + "D0A9877C" + "C62A4740" + "02DF32E5" + "2139F0A0", 16);

	private static ECDomainParameters ecc_bc_spec;
	private static final int DIGEST_LENGTH = 32;

	private static SecureRandom random = new SecureRandom();
	private static ECCurve.Fp curve;
	private static ECPoint G;
	private static final String PUBLIC_KEY = "PUBLIC_KEY";
	private static final String PRIVATE_KEY = "PRIVATE_KEY";

	static  {
		curve = new ECCurve.Fp(p, a, b);
		G = curve.createPoint(gx, gy);
		ecc_bc_spec = new ECDomainParameters(curve, G, n);
	}

	/**
	 * 生成密钥对
	 * @return 密钥对
	 */
	private static Map<String,Object> generateKeyPairMap() {
		Map<String,Object> keyMap = new HashMap<>(4);
		BigInteger privateKey = random(n.subtract(new BigInteger("1")));
		ECPoint publicKey = G.multiply(privateKey).normalize();
		if (checkPublicKey(publicKey)) {
			keyMap.put(PUBLIC_KEY,publicKey);
			keyMap.put(PRIVATE_KEY,privateKey);
			return keyMap;
		} else {
			return null;
		}
	}

	/**
	 * 获取公钥字符串
	 * @return 公钥字符串
	 */
	private static String getPublicKey(Map<String,Object> keyMap){
		ECPoint publicKey = (ECPoint) keyMap.get(PUBLIC_KEY);
		byte[] keyEncoded = publicKey.getEncoded(true);
		return encryptBase64(keyEncoded);
	}

	/**
	 * 获取私钥字符串
	 * @return 私钥字符串
	 */
	private static String getPrivateKey(Map<String,Object> keyMap){
		BigInteger privateKey = (BigInteger) keyMap.get(PRIVATE_KEY);
		byte[] keyEncoded = privateKey.toByteArray();
		return encryptBase64(keyEncoded);
	}

	/**
	 * 公钥加密
	 * @param input 加密原文
	 * @param publicKey 公钥
	 * @return 密文
	 */
	public static String encrypt(String input, String publicKey){
		byte[] keyByte = decryptBase64(publicKey);
		ECPoint point = curve.decodePoint(keyByte);
		byte[] encrypt = encrypt(input, point);
		return Hex.toHexString(encrypt);
	}

	/**
	 * 公钥加密
	 * @param input 加密原文
	 * @param publicKey 公钥
	 * @return 加密后字节数组
	 */
	private static byte[] encrypt(String input, ECPoint publicKey) {
		byte[] inputBuffer = input.getBytes();
		byte[] c1Buffer;
		ECPoint kpb;
		byte[] t;
		do {
			/* 1 产生随机数k，k属于[1, n-1] */

			BigInteger k = random(n);
			/* 2 计算椭圆曲线点C1 = [k]G = (x1, y1) */
			ECPoint c1 = G.multiply(k);
			c1Buffer = c1.getEncoded(false);
			/*3 计算椭圆曲线点 S = [h]Pb*/
			BigInteger h = ecc_bc_spec.getH();
			if (h != null) {
				ECPoint s = publicKey.multiply(h);
				if (s.isInfinity()) {
					throw new IllegalStateException();
				}
			}
			/* 4 计算 [k]PB = (x2, y2) */
			kpb = publicKey.multiply(k).normalize();
			/* 5 计算 t = KDF(x2||y2, klen) */
			byte[] kpbBytes = kpb.getEncoded(false);
			t = kdf(kpbBytes, inputBuffer.length);
		} while (allZero(t));
		/* 6 计算C2=M^t */
		byte[] c2 = new byte[inputBuffer.length];
		for (int i = 0; i < inputBuffer.length; i++) {
			c2[i] = (byte) (inputBuffer[i] ^ t[i]);
		}
		/* 7 计算C3 = Hash(x2 || M || y2) */
		byte[] c3 = sm3hash(kpb.getXCoord().toBigInteger().toByteArray(), inputBuffer,
				kpb.getYCoord().toBigInteger().toByteArray());
		/* 8 输出密文 C=C1 || C2 || C3 */
		byte[] encryptResult = new byte[c1Buffer.length + c2.length + c3.length];
		System.arraycopy(c1Buffer, 0, encryptResult, 0, c1Buffer.length);
		System.arraycopy(c2, 0, encryptResult, c1Buffer.length, c2.length);
		System.arraycopy(c3, 0, encryptResult, c1Buffer.length + c2.length, c3.length);
		return encryptResult;
	}

	/**
	 * 私钥解密
	 * @param input 密文
	 * @param privateKeyStr 私钥
	 * @return 明文
	 */
	public static String decrypt(String input, String privateKeyStr){
		byte[] keyByte = decryptBase64(privateKeyStr);
		BigInteger privateKey = new BigInteger(keyByte);
		byte[] enStr = Hex.decode(input);
		return decrypt(enStr,privateKey);
	}

	/**
	 * 私钥解密
	 * @param encryptData 密文数据字节数组
	 * @param privateKey 解密私钥
	 * @return 明文
	 */
	private static String decrypt(byte[] encryptData, BigInteger privateKey) {
		byte[] c1Byte = new byte[65];
		System.arraycopy(encryptData, 0, c1Byte, 0, c1Byte.length);
		ECPoint c1 = curve.decodePoint(c1Byte).normalize();
		/*计算椭圆曲线点 S = [h]C1 是否为无穷点*/
		BigInteger h = ecc_bc_spec.getH();
		if (h != null) {
			ECPoint s = c1.multiply(h);
			if (s.isInfinity()) {
				throw new IllegalStateException();
			}
		}
		/* 计算[dB]C1 = (x2, y2) */
		ECPoint dBC1 = c1.multiply(privateKey).normalize();
		/* 计算t = KDF(x2 || y2, klen) */
		byte[] dBC1Bytes = dBC1.getEncoded(false);
		int klen = encryptData.length - 65 - DIGEST_LENGTH;
		byte[] t = kdf(dBC1Bytes, klen);
		if (allZero(t)) {
			System.err.println("all zero");
			throw new IllegalStateException();
		}
		/* 5 计算M'=C2^t */
		byte[] M = new byte[klen];
		for (int i = 0; i < M.length; i++) {
			M[i] = (byte) (encryptData[c1Byte.length + i] ^ t[i]);
		}
		/* 6 计算 u = Hash(x2 || M' || y2) 判断 u == C3是否成立 */
		byte[] C3 = new byte[DIGEST_LENGTH];
		System.arraycopy(encryptData, encryptData.length - DIGEST_LENGTH, C3, 0, DIGEST_LENGTH);
		byte[] u = sm3hash(dBC1.getXCoord().toBigInteger().toByteArray(), M, dBC1.getYCoord().toBigInteger().toByteArray());
		if (Arrays.equals(u, C3)) {
			System.out.println("解密成功");
			return new String(M, StandardCharsets.UTF_8);
		} else {
			System.out.println("解密验证失败");
			return null;
		}

	}

	/**
	 * 判断是否在范围内
	 * @param param src
	 * @param min 最小值
	 * @param max 最大值
	 * @return true or false
	 */
	private static boolean between(BigInteger param, BigInteger min, BigInteger max) {
		return param.compareTo(min) >= 0 && param.compareTo(max) < 0;
	}

	/**
	 * 判断生成的公钥是否合法
	 * @param publicKey 公钥
	 * @return true or false
	 */
	private static boolean checkPublicKey(ECPoint publicKey) {
		if (!publicKey.isInfinity()) {
			BigInteger x = publicKey.getXCoord().toBigInteger();
			BigInteger y = publicKey.getYCoord().toBigInteger();
			if (between(x, new BigInteger("0"), p) && between(y, new BigInteger("0"), p)) {
				BigInteger xResult = x.pow(3).add(a.multiply(x)).add(b).mod(p);
				BigInteger yResult = y.pow(2).mod(p);
				return yResult.equals(xResult) && publicKey.multiply(n).isInfinity();
			}
		}
		return false;
	}

	/**
	 * 字节数组拼接
	 * @param params 待拼接数组
	 * @return 目标数组
	 */
	private static byte[] join(byte[]... params) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] res = null;
		try {
			for (byte[] param : params) {
				baos.write(param);
			}
			res = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * sm3摘要
	 * @param params 参数
	 * @return 摘要
	 */
	private static byte[] sm3hash(byte[]... params) {
		byte[] res = null;
		try {
			res = SM3.hash(join(params));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 取得用户标识字节数组
	 * @param IDA 用户唯一标识
	 * @param aPublicKey 公钥
	 * @return 目标数组
	 */
	private static byte[] ZA(String IDA, ECPoint aPublicKey) {
		byte[] idaBytes = IDA.getBytes();
		int entlenA = idaBytes.length * 8;
		byte[] entla = new byte[] { (byte) (entlenA & 0xFF00), (byte) (entlenA & 0x00FF) };
		return sm3hash(entla, idaBytes, a.toByteArray(), b.toByteArray(), gx.toByteArray(), gy.toByteArray(),
				aPublicKey.getXCoord().toBigInteger().toByteArray(),
				aPublicKey.getYCoord().toBigInteger().toByteArray());
	}

	/**
	 * 数字签名
	 * @param data 原始数据
	 * @param signFlag 签名方唯一标识
	 * @param publicKeyStr 公钥
	 * @param privateKeyStr 私钥
	 * @return 签名字符串
	 */
	public static String sign(String data, String signFlag, String publicKeyStr, String privateKeyStr){
		//TODO 参数校验
		byte[] dePublicKey = decryptBase64(publicKeyStr);
		ECPoint publicKey = curve.decodePoint(dePublicKey);
		byte[] dePrivateKey = decryptBase64(privateKeyStr);
		BigInteger privateKey = new BigInteger(dePrivateKey);
		return sign(data,signFlag,publicKey,privateKey);
	}

	/**
	 * 签名
	 * @param M 签名信息
	 * @param signFlag 签名方唯一标识
	 * @param publicKey 公钥
	 * @param privateKey 私钥
	 * @return 签名
	 */
	private static String sign(String M, String signFlag, ECPoint publicKey, BigInteger privateKey) {
		byte[] ZA = ZA(signFlag, publicKey);
		byte[] M_ = join(ZA, M.getBytes());
		BigInteger e = new BigInteger(1, sm3hash(M_));
		BigInteger k;
		BigInteger r;
		do {
			k = random(n);
			ECPoint p1 = G.multiply(k).normalize();
			BigInteger x1 = p1.getXCoord().toBigInteger();
			r = e.add(x1);
			r = r.mod(n);
		} while (r.equals(BigInteger.ZERO) || r.add(k).equals(n));
		BigInteger s = ((privateKey.add(BigInteger.ONE).modInverse(n))
				       .multiply((k.subtract(r.multiply(privateKey))).mod(n))).mod(n);
		byte[] rBytes = r.toByteArray();
		byte[] sBytes = s.toByteArray();
		String rBase64String = encryptBase64(rBytes);
		String sBase64String = encryptBase64(sBytes);
		return rBase64String + "," + sBase64String;
	}

	/**
	 * 验签
	 * @param data 原始数据
	 * @param signature 签名
	 * @param signFlag 签名方唯一标识
	 * @param publicKey 签名方公钥
	 * @return true or false
	 */
	public static boolean verify(String data, String signature, String signFlag, String publicKey) {
		//密钥格式转换
		byte[] dePublicKey = decryptBase64(publicKey);
		ECPoint aPublicKey = curve.decodePoint(dePublicKey);
		//签名字符串格式转换
		String[] signParts = signature.split(",");
		if(signParts.length < 2){
			return false;
		}
		String rPart = signParts[0];
		String sPart = signParts[1];
		BigInteger r = new BigInteger(decryptBase64(rPart));
		BigInteger s = new BigInteger(decryptBase64(sPart));
		if (!between(r, BigInteger.ONE, n)) {
			return false;
		}
		if (!between(s, BigInteger.ONE, n)) {
			return false;
		}
		byte[] M_ = join(ZA(signFlag, aPublicKey), data.getBytes());
		BigInteger e = new BigInteger(1, sm3hash(M_));
		BigInteger t = r.add(s).mod(n);
		if (t.equals(BigInteger.ZERO)) {
			return false;
		}
		ECPoint p1 = G.multiply(s).normalize();
		ECPoint p2 = aPublicKey.multiply(t).normalize();
		BigInteger x1 = p1.add(p2).normalize().getXCoord().toBigInteger();
		BigInteger R = e.add(x1).mod(n);
		return R.equals(r);
	}

	/**
	 * 密钥派生函数
	 * @param Z src
	 * @param klen 生成klen字节数长度的密钥
	 * @return des
	 */
	private static byte[] kdf(byte[] Z, int klen) {
		int ct = 1;
		int end = (int) Math.ceil(klen * 1.0 / 32);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			for (int i = 1; i < end; i++) {
				baos.write(sm3hash(Z, SM3.toByteArray(ct)));
				ct++;
			}
			byte[] last = sm3hash(Z, SM3.toByteArray(ct));
			if (klen % 32 == 0) {
				baos.write(last);
			} else {
				baos.write(last, 0, klen % 32);
			}
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * BASE64 加密
	 * @param key 需要加密的字节数组
	 * @return 字符串
	 */
	private static String encryptBase64(byte[] key) {
		return new String(Base64.getEncoder().encode(key), StandardCharsets.UTF_8);
	}

	/**
	 * BASE64 解密
	 * @param key 需要解密的字符串
	 * @return 字节数组
	 */
	private static byte[] decryptBase64(String key) {
		try{
			return Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8));
		}catch (IllegalArgumentException e){
			byte[] bytes;
			try{
				bytes = Base64.getMimeDecoder().decode(key.getBytes(StandardCharsets.UTF_8));
			}catch (Exception e1){
				throw new RuntimeException(key + " Base64.getMimeDecoder()解码失败",e1);
			}
			if(null == bytes || bytes.length == 0){
				throw new RuntimeException(key + " Base64.getMimeDecoder()解码失败,返回空串");
			}
			return bytes;
		}
	}

	/**
	 * 随机数生成器
	 * @param max 最大值
	 * @return 随机数
	 */
	private static BigInteger random(BigInteger max) {
		BigInteger r = new BigInteger(256, random);
		while (r.compareTo(max) >= 0) {
			r = new BigInteger(128, random);
		}
		return r;
	}

	/**
	 * 判断字节数组是否全0
	 * @param buffer 字节数组
	 * @return 是否
	 */
	private static boolean allZero(byte[] buffer) {
		if(null == buffer || buffer.length == 0){
			throw new RuntimeException("目标数组为空");
		}
		for (byte value : buffer) {
			if (value != 0){
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {

		System.out.println("-----------------公钥加密与解密-----------------");
		Map<String, Object> stringObjectMap = generateKeyPairMap();
		String publicKey = getPublicKey(stringObjectMap);
		String privateKey = getPrivateKey(stringObjectMap);
		System.out.println("公钥:" + publicKey);
		System.out.println("私钥:" + privateKey);

		String encrypt = encrypt("站在大明门前守卫的禁卫军，事先没有接到\n" +
				"有关的命令，但看到大批盛装的官员来临，也就\n" +
				"以为确系举行大典，因而未加询问。进大明门即\n" +
				"为皇城。文武百官看到端门午门之前气氛平静，\n" +
				"城楼上下也无朝会的迹象，既无几案，站队点名\n" +
				"的御史和御前侍卫“大汉将军”也不见踪影，不免\n" +
				"心中揣测，互相询问：所谓午朝是否讹传？", publicKey);
		System.out.println("密文:" + encrypt);
		System.out.println("解密后明文:" + decrypt(encrypt, privateKey));

		System.out.println("-----------------签名与验签-----------------");
		String IDA = "Heartbeats";
		String M = "要签名的信息";
		String signature = sign(M, IDA, publicKey, privateKey);
		System.out.println("用户标识:" + IDA);
		System.out.println("签名信息:" + M);
		System.out.println("数字签名:" + signature);
		System.out.println("验证签名:" + verify(M, signature, IDA, publicKey));
	}
}
