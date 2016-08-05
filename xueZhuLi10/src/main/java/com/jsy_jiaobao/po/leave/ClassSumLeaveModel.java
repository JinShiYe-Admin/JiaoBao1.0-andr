package com.jsy_jiaobao.po.leave;

/**
 * 查询统计班级model
 * @author 
 *
 */
public class ClassSumLeaveModel {
	private int UnitClassId;//,班级ID，用来做为取该班学生的请假统计的输入参数。
	private String ClassStr;//,班级名称
	private String GradeStr;//,年级名称
	private int Amount;//补课假次数
	private int Amount2;// 其他假次数
	public int getUnitClassId() {
		return UnitClassId;
	}
	public void setUnitClassId(int unitClassId) {
		UnitClassId = unitClassId;
	}
	public String getClassStr() {
		return ClassStr;
	}
	public void setClassStr(String classStr) {
		ClassStr = classStr;
	}
	public String getGradeStr() {
		return GradeStr;
	}
	public void setGradeStr(String gradeStr) {
		GradeStr = gradeStr;
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
