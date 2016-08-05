package com.jsy.xuezhuli.utils;

public class URLtoUTF8 {
	// 转换为%E4%BD%A0形式
	public static String toUtf8String(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = String.valueOf(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (byte aB : b) {
					int k = aB;
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(URLtoUTF8.toUtf8String("20141125091852abe0_2013-03-03 12.49.44.jpg"));
	}
}
