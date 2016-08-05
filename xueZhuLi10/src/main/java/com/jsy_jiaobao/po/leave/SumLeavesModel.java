package com.jsy_jiaobao.po.leave;

/**
 * 老师请假统计Model
 * 
 * @author admin
 * 
 */

public class SumLeavesModel {
	// ManName":"新用户","Amount":3
	private String ManName;// , 姓名
	private int Amount;// 请假次数

	public String getManName() {
		return ManName;
	}

	public void setManName(String manName) {
		ManName = manName;
	}

	public int getAmount() {
		return Amount;
	}

	public void setAmount(int amount) {
		Amount = amount;
	}
}
