package com.jsy.xuezhuli.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;


/**
 *MD5
 *SHA
 *URL
 */
public class Coder {
    public static final String KEY_MD5 = "MD5";
  
    /**  
     * MD5加密  
     *
     * @throws Exception  
     */  
    public static byte[] encryptMD5(byte[] data) throws Exception {   
  
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);   
        md5.update(data);   
  
        return md5.digest();   
  
    }   

    /**
     * URL加密
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