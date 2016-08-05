package com.jsy_jiaobao.po.workol;

/**
 * 作业完成状态
 */
public class HWStatus {
	private int UnId;// 418,Id
	private String Name;// "英语(人教新课标)",
	private int Total;// 38,题目总数
	private int IsF;// 27,已完成数目
	private int UnF;// 11，未完成数目

	public int getUnId() {
		return UnId;
	}

	public void setUnId(int unId) {
		UnId = unId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getTotal() {
		return Total;
	}

	public void setTotal(int total) {
		Total = total;
	}

	public int getIsF() {
		return IsF;
	}

	public void setIsF(int isF) {
		IsF = isF;
	}

	public int getUnF() {
		return UnF;
	}

	public void setUnF(int unF) {
		UnF = unF;
	}

}
