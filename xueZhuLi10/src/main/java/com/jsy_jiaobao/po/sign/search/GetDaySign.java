package com.jsy_jiaobao.po.sign.search;

import java.util.List;

public class GetDaySign {

	private String HandleResult;
	private List<SignInfo> daylist;
	
	public String getHandleResult() {
		return HandleResult;
	}
	public void setHandleResult(String handleResult) {
		HandleResult = handleResult;
	}
	public List<SignInfo> getDaylist() {
		return daylist;
	}
	public void setDaylist(List<SignInfo> daylist) {
		this.daylist = daylist;
	}
}
