package com.jsy.xuezhuli.utils;

import java.nio.channels.AsynchronousCloseException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPerferencesPackage {

		
		private static SharedPreferences myshared;
		
		private static SharedPerferencesPackage mypackage;
		
		
		SharedPerferencesPackage(Context thecontext){
			
			myshared =thecontext.getSharedPreferences("theData", 0);
			
		
			
		}
		
		
		@SuppressLint("ParserError")
		public synchronized static SharedPerferencesPackage  getInstance(Context thecontext){
			
			if (mypackage == null)
			{
				mypackage =new SharedPerferencesPackage(thecontext);
			}
			
			
			return mypackage;
			
		}
		
		public static void putBoolean(String key,Boolean myboolean){
			
			Editor edt =myshared.edit();
			
			edt.putBoolean(key, myboolean).commit();
			
		}
		
		
		public static void putString(String key,String myString){
			
			Editor edt =myshared.edit();
			
			edt.putString(key, myString).commit();
			
		}
		
		
		public static void putInt(String key,Integer myInteger){
			
			Editor edt =myshared.edit();
			
			edt.putInt(key, myInteger).commit();
			
		}
		
		public static void putFloat(String key,Float myFloat){
			
			Editor edt =myshared.edit();
			
			edt.putFloat(key, myFloat).commit();
			
		}
		
		
		public static Boolean getBoolean(String key){
			
			return	myshared.getBoolean(key, true);
			
		}
		
		
		public static String getString(String key){
			
			return	myshared.getString(key, "");
			
		}
		
		public static Integer getInt(String key){
			
			return	myshared.getInt(key, 0);
			
		}
		
		public static Float getFloat(String key){
			
			return	myshared.getFloat(key, 0);
			
		}
	
		
		
	
	
}
