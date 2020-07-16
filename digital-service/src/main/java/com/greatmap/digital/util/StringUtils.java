package com.greatmap.digital.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.UnicodeBlock.*;

/**
 * @author guoan
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类<br/>
 * 参考{@link org.apache.commons.lang3.StringUtils}
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	public static String lowerFirst(String str){
		if(StringUtils.isBlank(str)) {
			return "";
		} else {
			return str.substring(0,1).toLowerCase() + str.substring(1);
		}
	}
	
	public static String upperFirst(String str){
		if(StringUtils.isBlank(str)) {
			return "";
		} else {
			return str.substring(0,1).toUpperCase() + str.substring(1);
		}
	}
	
	public static String escapeHtml4(String input){
		if(isBlank(input)){
			return input;
		}
		String value = StringEscapeUtils.escapeHtml4(input);
		
		return value.replace("&middot;", "\u00B7");
		
	}

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)){
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 缩略字符串（替换html）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String rabbr(String str, int length) {
        return abbr(replaceHtml(str), length);
	}
		
	
	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val){
		if (val == null){
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val){
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val){
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val){
		return toLong(val).intValue();
	}

	/**
	 * <p>如果有一个为Blank就返回false</p>
	 * 参考 {@link #isBlank(CharSequence) isBlank}
	 * @param css
	 * @return true|false
	 */
	public static boolean isAnyBlank(final CharSequence... css){
		if (ArrayUtils.isEmpty(css)) {
			return true;
		}
		for (final CharSequence cs : css) {
			if (isBlank(cs)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * <p>如果有一个为Empty就返回false</p>
	 * 参考 {@link #isEmpty(CharSequence) isEmpty}
	 * @param css
	 * @return true|false
	 */
	public static boolean isAnyEmpty(final CharSequence... css){
		if (ArrayUtils.isEmpty(css)) {
			return true;
		}
		for (final CharSequence cs : css) {
			if (isEmpty(cs)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 去掉字符串前后字符
	 * @param value
	 * @param c
	 * @return
	 */
	public static String Trim(String value,char c) {
        int len = value.length();
        int st = 0;
        char[] val = value.toCharArray();
		// avoid getfield opcode
        while ((st < len) && (val[st] == c)) {
            st++;
        }
        while ((st < len) && (val[len - 1] == c)) {
            len--;
        }
        return ((st > 0) || (len < value.length())) ? value.substring(st, len) : value;
    }
	
	/**
	 * 去除前后相同字符串
	 * @param value
	 * @param strTrim
	 * @return
	 */
	public static String TrimString(String value,String strTrim){
		if(StringUtils.isAnyBlank(value)||value.length()<strTrim.length()){
			return value;
		}
		if(StringUtils.isAnyBlank(strTrim)){
			return value.trim();
		}
		String strTemp = "";
		int nLength = strTrim.length();
		//去除前面相同字符串
		strTemp = value.substring(0, nLength);
		while(StringUtils.equals(strTemp, strTrim)){
			value = value.substring(nLength);
			if(value.length()<nLength){
				break;
			}
			strTemp = value.substring(0, nLength);
		}
		//去除后面字符串
		if(value.length()>=nLength){
			strTemp = value.substring(value.length()-nLength);
			while(StringUtils.equals(strTemp, strTrim)){
				value = value.substring(0,value.length()-nLength);
				if(value.length()<nLength){
					break;
				}
				strTemp = value.substring(value.length()-nLength);
			}
		}
		return value;
	}

	/**
	 * 将字符串组合成文件路径
	 * 如：foo,bar组合成foo/bar
	 * @param path
	 * @return
	 */
	public static String splicePath(String... path){
		if(ArrayUtils.isEmpty(path)){
			return null;
		}
		List<String> pathList = new ArrayList<String>();
		for (String item : path) {
			if(StringUtils.isBlank(item)){
				continue;
			}
			//将一个或多个反斜杠替换成正斜杠
			String temp = StringUtils.replaceByPattern(item, "[\\\\?]+", "/");
			if(temp.endsWith("/")){
				temp = temp.substring(0, temp.length()-1);
			}
			if(temp.startsWith("/")){
				temp = temp.substring(1);
			}
			pathList.add(item);
		}
		return StringUtils.join(pathList.toArray(), "/");
	}

	protected static boolean startsWith(String str, String prefix, boolean ignoreCase) {
		if (str == null || prefix == null || str.length() < prefix.length()) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		return ignoreCase && str.substring(0, prefix.length()).toLowerCase().equals(prefix.toLowerCase());
	}

	protected static boolean endsWith(String str, String suffix, boolean ignoreCase) {
		if (null == str || null == suffix || str.length() < suffix.length()) {
			return false;
		}
		if (str.endsWith(suffix)) {
			return true;
		}
		return (ignoreCase && str.substring(str.length() - suffix.length()).toLowerCase().equals(suffix.toLowerCase()));
	}

	/**
	 * 将字符串中符合pattern表达式中的
	 * @param text
	 * @param pattern
	 * @param replaceText
	 * @return
	 */
	public static String replaceByPattern(String text,String pattern,String replaceText){
		if(StringUtils.isAnyBlank(pattern,replaceText)){
			return text;
		}
		Pattern replacePattern = Pattern.compile(pattern);
		Matcher matcher = replacePattern.matcher(text);
		return matcher.replaceAll(replaceText);
	}
	
	/**
	 * 给字符串符合条件n的位置，添加flag(空格，或者换行符)
	 * @param str
	 * @param n
	 * @param flag
	 * @return
	 */
	public static String getNewStr(String str,int n,String flag)
	{
		StringBuffer sb = new StringBuffer();
        for(int i = 0;i<str.length();i++)
        {
        	sb.append(str.charAt(i));
        	if((i+1)%n==0)
        	{
        		sb.append(flag);
        	}
        }
		return sb.toString();
	}
	
	/**
	 * 检查字符串中是否包含中文
	 * @param checkStr
	 * @return
	 */
	public static boolean checkStringContainChinese(String checkStr){
        if(isNotEmpty(checkStr)){
            char[] checkChars = checkStr.toCharArray();
            for(int i = 0; i < checkChars.length; i++){
                char checkChar = checkChars[i];
                if(checkCharContainChinese(checkChar)){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkCharContainChinese(char checkChar){
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(checkChar);
        if(CJK_UNIFIED_IDEOGRAPHS == ub || CJK_COMPATIBILITY_IDEOGRAPHS == ub || CJK_COMPATIBILITY_FORMS == ub ||
                CJK_RADICALS_SUPPLEMENT == ub || CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A == ub || CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B == ub){
            return true;
        }
        return false;
    }
    
    /**
     * 字符串转换unicode
     * @param str
     * @return
     */
    public static String string2Unicode(String str){
    	StringBuffer unicode = new StringBuffer();
    	for(int ii=0;ii<str.length();ii++){
    		char c = str.charAt(ii);
    		String hexB = Integer.toHexString(c);
    		if(hexB.length() <= 2){
    			hexB = "00" + hexB;
    		}
    		unicode.append("\\u" + hexB);
    	}
    	return unicode.toString();

    }
}
