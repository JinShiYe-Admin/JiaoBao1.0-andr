package com.jsy_jiaobao.po.sign.search;

import java.util.List;

public class GetFenceUnitInfo {
	private String ResultCode;
	private String ResultDesc;
	private List<FenceUnitInfo> Data;
	public String getResultCode() {
		return ResultCode;
	}
	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}
	public String getResultDesc() {
		return ResultDesc;
	}
	public void setResultDesc(String resultDesc) {
		ResultDesc = resultDesc;
	}
	public List<FenceUnitInfo> getData() {
		return Data;
	}
	public void setData(List<FenceUnitInfo> data) {
		Data = data;
	}
}