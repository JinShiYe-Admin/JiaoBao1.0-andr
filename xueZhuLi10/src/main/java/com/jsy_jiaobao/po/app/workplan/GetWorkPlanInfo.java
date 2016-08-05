package com.jsy_jiaobao.po.app.workplan;

import java.util.List;

public class GetWorkPlanInfo {
	
	private String ResultCode;
	private String ResultDesc;
	private List<WorkPlanInfo> Data;
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
	public List<WorkPlanInfo> getData() {
		return Data;
	}
	public void setData(List<WorkPlanInfo> data) {
		Data = data;
	}
	
}
