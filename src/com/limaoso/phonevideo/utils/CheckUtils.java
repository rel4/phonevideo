package com.limaoso.phonevideo.utils;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.graphics.Path;

public class CheckUtils {
	/*
	 * 判断是否为用户名应为1-12个字符的字母，数字、中文 [\\da-zA-Z\\d+\\u4E00-\\u9FA5]{1,12}
	 */
	public static boolean isNickName(String nickName) {
		if (nickName == null) {
			return false;
		} else {
			String strPattern = "[a-zA-Z\\d+\\u4e00-\\u9fa5]{1,12}";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(nickName);
			return m.matches();
		}
	}

	/*
	 * 判断是否为密码6-16位数字、字母 [a-zA-Z\d+]{6,16} ^[A-Za-z]+$
	 */
	public static boolean isPassWord(String password) {
		if (password == null) {
			return false;
		} else {
			String strPattern = "[a-zA-Z\\d+]{6,16}";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(password);
			return m.matches();
		}
	}

	// 判断是不是纯数字
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]*)?$");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 简单判断是否是年月日
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean isYMD(String YMD) {
		if (YMD == null) {
			return false;
		} else {
			String strPattern = "\\d{4}-\\d{2}(\\-\\d{2})?";
			Pattern p = Pattern.compile(strPattern);
			Matcher m = p.matcher(YMD);
			return m.matches();
		}
	}

	/**
	 * 验证邮箱输入是否合法
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail) {
		String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		return m.matches();
	}

	/**
	 * 验证是否是手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	// 判断图片存不存在
	public static boolean isIcon(String path) {
		if (path == null || "".equals(path)) {
			return false;
		}
		File file = new File(path);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	// 判断是否是身份证18位
	public static boolean isIDCard(String str) {
		Pattern pattern = Pattern.compile("[^a-zA-Z0-9]{18}");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean chineseFilter(String str)
			throws PatternSyntaxException {
		// 判断是否有汉字 var reg=/^[\u4e00-\u9fa5]{0,}$/;
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher matcher = pattern.matcher(str);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static String charFilter(char str) throws PatternSyntaxException {
		// 只允许字母、数字和汉字
		String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
		Pattern p = Pattern.compile(regEx);
		String s = str + "";
		Matcher m = p.matcher(s);
		return m.replaceAll("").trim();
	}

	public static boolean isInList(String str, List<String> list) {
		for (String s : list) {
			if (s.equals(str)) {
				return true;
			}
		}
		return false;
	}
}
