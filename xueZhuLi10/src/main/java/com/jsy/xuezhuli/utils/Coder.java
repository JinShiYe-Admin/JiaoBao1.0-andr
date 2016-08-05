package com.jsy.xuezhuli.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;


/**
 *MD5
 *SHA
 *URL
 */
public class Coder {   
    public static final String KEY_SHA = "SHA";   
    public static final String KEY_MD5 = "MD5";   
  
    /**  
     * MD5加密  
     *   
     * @param data  
     * @return  
     * @throws Exception  
     */  
    public static byte[] encryptMD5(byte[] data) throws Exception {   
  
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);   
        md5.update(data);   
  
        return md5.digest();   
  
    }   
  
    /**  
     * SHA加密  
     *   
     * @param data  
     * @return  
     * @throws Exception  
     */  
    public static byte[] encryptSHA(byte[] data) throws Exception {   
  
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);   
        sha.update(data);   
  
        return sha.digest();   
  
    }  
    /**
     * URL解密
     * @param json
     * @return 解密后的字符串
     */
    public static String decodeURL(String json){
    	try {
    		return URLDecoder.decode(json,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    /**
     * URL加密
     * @param json
     * @return 加密后的字符串
     */
    public static String encodeURL(String json){
    	try {
    		return URLEncoder.encode(json,"UTF-8");
    	} catch (UnsupportedEncodingException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
}  
