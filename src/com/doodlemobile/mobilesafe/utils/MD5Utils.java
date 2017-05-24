package com.doodlemobile.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * 
 * @author hhw
 */
public class MD5Utils {

	/**
	 * 使用MD5加密算法加密字符串
	 * 
	 * @param pwd
	 *            要加密的字符串
	 * @return 加密过后的字符串
	 */
	public static String encode(String pwd) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			pwd += "MobileSafe";
			byte[] bytes = pwd.getBytes();
			byte[] bs = messageDigest.digest(bytes);

			StringBuilder sb = new StringBuilder();
			for (byte b : bs) {
				int i = b & 0xff;
				String hexString = Integer.toHexString(i);
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
