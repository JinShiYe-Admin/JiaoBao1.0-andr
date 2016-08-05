package com.jsy_jiaobao.po.workol;

import java.util.ArrayList;

/**
 * <pre>
 * 取出联动效果
 *  params
 * "gCode">年级代码
 * "subCode">科目代码
 * "uId">教版联动代码
 * "flag">0： 根据年级获取科目，1：根据科目获取教版，2： 根据所有获取UID
 * Args1 为科目列表数据
 * Args2为教版列表
 * Args3为章节列表
 */

public class UnionChapterList {

	private ArrayList<TeaMode> args1;// 科目
	private ArrayList<TeaSubject> args2;// 教版
	private ArrayList<TeaSession> args3;// 章节
	private String args4;
	private String callbackType;// "closeCurrent"
	private String forwardUrl;// 当前Url
	private String message;// 返回数据
	private String navTabId;// ""
	private String statusCode;// 请求状态代码

	public ArrayList<TeaMode> getArgs1() {
		return args1;
	}

	public void setArgs1(ArrayList<TeaMode> args1) {
		this.args1 = args1;
	}

	public ArrayList<TeaSubject> getArgs2() {
		return args2;
	}

	public void setArgs2(ArrayList<TeaSubject> args2) {
		this.args2 = args2;
	}

	public ArrayList<TeaSession> getArgs3() {
		return args3;
	}

	public void setArgs3(ArrayList<TeaSession> args3) {
		this.args3 = args3;
	}

	public String getArgs4() {
		return args4;
	}

	public void setArgs4(String args4) {
		this.args4 = args4;
	}

	public String getCallbackType() {
		return callbackType;
	}

	public void setCallbackType(String callbackType) {
		this.callbackType = callbackType;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}

	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNavTabId() {
		return navTabId;
	}

	public void setNavTabId(String navTabId) {
		this.navTabId = navTabId;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
