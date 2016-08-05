package com.jsy_jiaobao.po.leave;

/**
 * 学生统计查询model
 * @author Vktuns
 *
 */
public class StuSumLeavesModel {

	private String	ManName;//, 姓名
	private int Amount;//学生补课假次数 
	private int	Amount2;// 其他假次数
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
	public int getAmount2() {
		return Amount2;
	}
	public void setAmount2(int amount2) {
		Amount2 = amount2;
	}

}
