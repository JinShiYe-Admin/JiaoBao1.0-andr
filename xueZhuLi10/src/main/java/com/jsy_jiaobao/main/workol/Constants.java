package com.jsy_jiaobao.main.workol;


public class Constants {


	private static String baseUrl = "https://znzy.jiaobaowang.net/AutoHWIndex/";
	public static String GetLogin = baseUrl +"/AtHWPort/GetLogin";
	/**<pre>取出联动效果
	 * params
	 *"gCode">年级代码
	 *"subCode">科目代码
	 *"uId">教版联动代码
	 *"flag">0： 根据年级获取科目，1：根据科目获取教版，2： 根据所有获取UID
	 *return
	 *Args1 为科目列表数据
	 *Args2为教版列表
	 *Args3为章节列表
	 *statusCode=200表示成功
	 */
	public static String GetUnionChapterList = baseUrl +"GetUnionChapterList";
	/**
	 * 获取年级列表
	 */
	public static String GetGradeList = baseUrl +"GetGradeList";
	/**
	 * 判断某章节是否有试题
	 */
	public static String TecQs = baseUrl +"TecQs";
	/**
	 * 发布练习
	 */
	public static String StuMakeSelf = baseUrl +"StuMakeSelf";
	/**
	 * 获取老师的自定义作业列表
	 */
	public static String GetDesHWList = baseUrl +"GetDesHWList";
	/**
	 * 老师发布作业接口
	 */
	public static String TecMakeHW = baseUrl +"TecMakeHW";
	/**
	 * 学生获取当前作业列表
	 */
	public static String GetStuHWList = baseUrl +"GetStuHWList";
	/**
	 * 获取某学生各科作业完成情况 参数：学生ID
	 */
	public static String GetCompleteStatusHW = baseUrl +"GetCompleteStatusHW";
	/**
	 * 获取某学生学力值 
	 */
	public static String GetStuEduLevel = baseUrl +"GetStuEduLevel";
	/**
	 * 获取单题,作业名称,作业题量,作业开始时间,作业时长,作业上交时间
	 */
	public static String GetStuHW = baseUrl +"GetStuHW";
	/**
	 * 获取某作业下某题的作业题及答案
	 */
	public static String GetStuHWQs = baseUrl +"GetStuHWQs";
	/**
	 * 学生递交作业
	 */
	public static String StuSubQs = baseUrl +"StuSubQs";
	
	/**
	 * 获取服务器时间
	 */
	public static String GetSQLDateTIme = baseUrl +"GetSQLDateTIme";
	/*
	 * 获取作业练习列表
	 */
	public static String GetStuHWListPage = baseUrl +"GetStuHWListPage";
	public static String GetStuErr = baseUrl +"GetStuErr";
	public final static int WORKOL_GetUnionChapterList = 200;
	public final static int WORKOL_GetGradeList = 201;
	public final static int WORKOL_GetDesHWList = 202;
	public final static int WORKOL_TecMakeHW = 203;
	public final static int WORKOL_TecMakeHWClick = 204;
	public final static int WORKOL_getGenInfo = 205;
	public final static int WORKOL_GetStuHWList = 206;
	public final static int WORKOL_GetCompleteStatusHW = 207;
	public final static int WORKOL_GetStuEduLevel = 208;
	public final static int WORKOL_getStuInfo = 209;
	public final static int WORKOL_GetStuHW = 210;
	public final static int WORKOL_GetStuHWQs = 211;
	public final static int WORKOL_StuSubQs = 212;
	public final static int WORKOL_TecQs = 213;
	public final static int WORKOL_StuMakeSelf = 214;
	public final static int WORKOL_Notify_nochild = 215;
	public final static int WORKOL_GetSQLDateTIme = 216;
	public final static int WORKOL_GetStuHWListPage = 217;
	public final static int WORKOL_GetStuErr = 218;
	
}
