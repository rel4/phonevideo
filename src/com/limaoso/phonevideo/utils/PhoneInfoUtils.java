package com.limaoso.phonevideo.utils;

import java.util.regex.Pattern;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class PhoneInfoUtils {
	private static TelephonyManager telephonyManager;

	/**
	 * 获取电话号码
	 */

	public static String getPhoneNumber(Context context) {
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager.getLine1Number() != null) {
			int length = telephonyManager.getLine1Number().length();
			if (length > 11) {
				String tel = telephonyManager.getLine1Number().trim()
						.substring(length - 11, length);
				return tel;
			}
			return telephonyManager.getLine1Number().trim();
		} else {
			return null;
		}

	}

	public static String getPhoneImei(Context context) {
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	public static String getPhoneMac(Context context) {
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();

		if (info.getMacAddress() != null) {

			String mac = "";
			char[] macArray = info.getMacAddress().toCharArray();
			for (int i = 0; i < macArray.length; i++) {
				if (isNumeric(macArray[i]) || check(macArray[i])) {
					mac += macArray[i];
				}
			}
			return mac;
		} else {
			return null;
		}

	}

	public static boolean isNumeric(char macArray) {

		if (!Character.isDigit(macArray)) {
			return false;
		}

		return true;
	}

	public static boolean check(char macArray) {
		// char c = macArray.charAt(0);
		if (((macArray >= 'a' && macArray <= 'z') || (macArray >= 'A' && macArray <= 'Z'))) {
			return true;
		} else {
			return false;
		}
	}
}
