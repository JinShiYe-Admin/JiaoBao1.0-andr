package com.jsy_jiaobao.po.sign.search;

import java.util.List;

public class GetSignInfo {
	
	private String ResultDesc;
	private String ResultCode;
	private List<GetDaySign> Data;
	
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
	public List<GetDaySign> getData() {
		return Data;
	}
	public void setData(List<GetDaySign> data) {
		Data = data;
	}

	
}
