package com.jsy.xuezhuli.utils;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	public static <T> T GsonToObject(String json, Class T) {
		T t = null;
		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			t = (T) gson.fromJson(json, T);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("json解析错误!");
		}
		return t;
	}
	/**
	 * 
	 * @param json
	 * @param new TypeToken<ArrayList<Object>>() {}.getType()
	 * @return
	 */
	public static <T> T GsonToList(String json, Type type) {
		T t = null;
		try {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			t = (T) gson.fromJson(json, type);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("json解析错误!");
		}
		return t;
	}
}
