package com.jsy_jiaobao.po.personal;

import java.util.List;

/**
 * 单位通知列表
 */
public class UnitNoticResult {
	private String write;// {set;get;} //当前用户能否发表通知
	private List<UnitNoticInfo> list;// {set;get;} //单位通知列表

	public List<UnitNoticInfo> getList() {
		return list;
	}

	public void setList(List<UnitNoticInfo> list) {
		this.list = list;
	}

	public String getWrite() {
		return write;
	}

	public void setWrite(String write) {
		this.write = write;
	}

}
