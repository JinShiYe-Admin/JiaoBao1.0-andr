package com.jsy_jiaobao.po.sign;

import java.util.List;

public class GetSignWay {
	
	private String ResultDesc;
	private String ResultCode;
	private List<ParentSignWay> Data;
	
	public String getResultDesc() {
		return ResultDesc;
	}
	public void setResultDesc(String resultDesc) {
		ResultDesc = resultDesc;
	}
	public String getResultCode() {
		return ResultCode;
	}
	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}
	public List<ParentSignWay> getData() {
		return Data;
	}
	public void setData(List<ParentSignWay> data) {
		Data = data;
	}

	
}
