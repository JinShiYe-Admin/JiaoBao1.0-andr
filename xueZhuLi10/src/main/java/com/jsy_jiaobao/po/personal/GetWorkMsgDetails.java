package com.jsy_jiaobao.po.personal;

import java.util.List;

/**

 *         
 * 获取 事务 信息详情
 */
public class GetWorkMsgDetails {

	private CommMsg Model;
	private List<FeeBack> FeebackList;
	public CommMsg getModel() {
		return Model;
	}
	public void setModel(CommMsg model) {
		Model = model;
	}
	public List<FeeBack> getFeebackList() {
		return FeebackList;
	}
	public void setFeebackList(List<FeeBack> feebackList) {
		FeebackList = feebackList;
	}
}
