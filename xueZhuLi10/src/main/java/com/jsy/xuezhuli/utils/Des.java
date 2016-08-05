package com.jsy.xuezhuli.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Des {
	private byte[] desKey;

	/**
	 *  解密数据
	 * @param message
	 * @param key 
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String message, String key) throws Exception {

		byte[] bytesrc = Base64Helper.decode(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte,"UTF-8");
		
	}

	/**
	 * 加密数据
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String message, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		byte[] resultByte = cipher.doFinal(message.getBytes("UTF-8"));
		return Base64Helper.encode(resultByte);
	}

	public static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}

	public static void main(String[] args) throws Exception {
		String key = "90807061";
		String a = encrypt("{\"JiaoBaoHao\":5150001,\"Nickname\":\"momo\",\"TrueName\":\"李四田\"}", key);
//
		System.out.println("加密后的数据为:" + a);
		String desTest = decrypt("sLpHoB+dGMPReA7Sv32PlhoIzibN5GATwVIscHQwqHJnx4A7shnyIEcTEy/cLL4ixZ0zaAphZDavlHcZC5lIIg==", key);
		System.out.println("解密后的数据:" + desTest);

	}

}