package com.greatmap.digital.util.algorithm.sm4;

import com.greatmap.digital.util.algorithm.base64.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SM4Utils
{
	private static String defaultSecretKey = "JeF8U9wHFOMfs2Y8";
	private static String iv = "UISwD9fW6cFh9SNS";
	private static boolean hexString = false;
	private static Pattern PATTERN = Pattern.compile("\\s*|\t|\r|\n");
	
	private SM4Utils()
	{
	}
	
	public static String encryptDataECB(String plainText,String secretKey)
	{

		if(StringUtils.isBlank(secretKey)){
			secretKey = defaultSecretKey;
		}
		try 
		{
			SM4Context ctx = new SM4Context();
			ctx.isPadding = true;
			ctx.mode = SM4.SM4_ENCRYPT;
			
			byte[] keyBytes;
			if (hexString)
			{
				keyBytes = Util.hexStringToBytes(secretKey);
			}
			else
			{
				keyBytes = secretKey.getBytes();
			}
			
			SM4 sm4 = new SM4();
			sm4.sm4_setkey_enc(ctx, keyBytes);
			byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes("GBK"));
			byte[] result = Base64.encode(encrypted);
			String cipherText = new String(result,"GBK");
			if (cipherText != null && cipherText.trim().length() > 0)
			{
//				Pattern p = Pattern.compile("\\s*|\t|\r|\n");
				Matcher m = PATTERN.matcher(cipherText);
				cipherText = m.replaceAll("");
			}
			return cipherText;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String decryptDataECB(String cipherText,String secretKey)
	{
		if(StringUtils.isBlank(secretKey)){
			secretKey = defaultSecretKey;
		}
		try 
		{
			SM4Context ctx = new SM4Context();
			ctx.isPadding = true;
			ctx.mode = SM4.SM4_DECRYPT;
			
			byte[] keyBytes;
			if (hexString)
			{
				keyBytes = Util.hexStringToBytes(secretKey);
			}
			else
			{
				keyBytes = secretKey.getBytes();
			}
			
			SM4 sm4 = new SM4();
			sm4.sm4_setkey_dec(ctx, keyBytes);
			byte[] decrypted = sm4.sm4_crypt_ecb(ctx, Base64.decode(cipherText));
			
			return new String(decrypted, "GBK");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String encryptDataCBC(String plainText,String secretKey)
	{
		if(StringUtils.isBlank(secretKey)){
			secretKey = defaultSecretKey;
		}
		try 
		{
			SM4Context ctx = new SM4Context();
			ctx.isPadding = true;
			ctx.mode = SM4.SM4_ENCRYPT;
			
			byte[] keyBytes;
			byte[] ivBytes;
			if (hexString)
			{
				keyBytes = Util.hexStringToBytes(secretKey);
				ivBytes = Util.hexStringToBytes(iv);
			}
			else
			{
				keyBytes = secretKey.getBytes();
				ivBytes = iv.getBytes();
			}
			
			SM4 sm4 = new SM4();
			sm4.sm4_setkey_enc(ctx, keyBytes);
			byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes("GBK"));
			byte[] result = Base64.encode(encrypted);
			String cipherText = new String(result,"GBK");
			if (cipherText != null && cipherText.trim().length() > 0)
			{
//				Pattern p = Pattern.compile("\\s*|\t|\r|\n");
				Matcher m = PATTERN.matcher(cipherText);
				cipherText = m.replaceAll("");
			}
			return cipherText;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String decryptDataCBC(String cipherText,String secretKey)
	{
		if(StringUtils.isBlank(secretKey)){
			secretKey = defaultSecretKey;
		}
		try 
		{
			SM4Context ctx = new SM4Context();
			ctx.isPadding = true;
			ctx.mode = SM4.SM4_DECRYPT;
			
			byte[] keyBytes;
			byte[] ivBytes;
			if (hexString)
			{
				keyBytes = Util.hexStringToBytes(secretKey);
				ivBytes = Util.hexStringToBytes(iv);
			}
			else
			{
				keyBytes = secretKey.getBytes();
				ivBytes = iv.getBytes();
			}
			
			SM4 sm4 = new SM4();
			sm4.sm4_setkey_dec(ctx, keyBytes);
			byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, Base64.decode(cipherText));
			return new String(decrypted, "GBK");
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) throws IOException 
	{
		String plainText = "服务端注册-->服务注册-->客户端注册-->服务授权";
		
//		SM4Utils.secretKey = "JeF8U9wHFOMfs2Y8";
//		SM4Utils.hexString = false;

		String secretKey = RandomStringUtils.randomAlphanumeric(16);

		System.out.println("ECB模式");
		String cipherText = SM4Utils.encryptDataECB(plainText,secretKey);
		System.out.println("密文: " + cipherText);
		System.out.println("");
		
		plainText = SM4Utils.decryptDataECB(cipherText,secretKey);
		System.out.println("明文: " + plainText);
		System.out.println("");
		
		System.out.println("CBC模式");
//		SM4Utils.iv = "UISwD9fW6cFh9SNS";
		cipherText = SM4Utils.encryptDataCBC(plainText,secretKey);
		System.out.println("密文: " + cipherText);
		System.out.println("");
		
		plainText = SM4Utils.decryptDataCBC(cipherText,secretKey);
		System.out.println("明文: " + plainText);
	}
}