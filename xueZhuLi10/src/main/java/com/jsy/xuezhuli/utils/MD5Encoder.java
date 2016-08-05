package com.jsy.xuezhuli.utils;

import java.security.MessageDigest;

/**
 * MD5加密
 * @author 
 *
 */

public class MD5Encoder {

	public static String ecode(String pwd) {
		
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest(pwd.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < bytes.length; i++) {

				String s = Integer.toHexString(0xff & bytes[i]);
				if (s.length() == 1) {
					sb.append("0" + s);
				} else {
					sb.append(s);
				}

			}
			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
