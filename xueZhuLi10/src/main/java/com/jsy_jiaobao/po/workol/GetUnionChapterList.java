package com.jsy_jiaobao.po.workol;

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

public class GetUnionChapterList {

	private String args1;// 科目数据
	private String args2;// 教版数据
	private String args3;// 章节数据
	private String args4;
	private String callbackType;// "closeCurrent"
	private String forwardUrl;// ""
	private String message;// "操作成功"
	private String navTabId;// ""
	private String statusCode;// "200"

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

	public String getArgs1() {
		return args1;
	}

	public void setArgs1(String args1) {
		this.args1 = args1;
	}

	public String getArgs2() {
		return args2;
	}

	public void setArgs2(String args2) {
		this.args2 = args2;
	}

	public String getArgs3() {
		return args3;
	}

	public void setArgs3(String args3) {
		this.args3 = args3;
	}

}
