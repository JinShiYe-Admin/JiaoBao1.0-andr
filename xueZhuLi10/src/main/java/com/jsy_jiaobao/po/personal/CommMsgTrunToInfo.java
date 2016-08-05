package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

public class CommMsgTrunToInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1148271570369508233L;
	//用户ID
	public int UserID;
	//AccID
	public int JiaoBaoHao;
	//1单位，2班级，UnitID是班级ID，还是单位ID
	public int UnitType;
	//单位ID
	public int UnitID;
	//tomem,togen,tostu 对应1,3,4 转发给单位人员，转发给家长，转发给学生
	public String Who;
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}
	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}
	public int getUnitType() {
		return UnitType;
	}
	public void setUnitType(int unitType) {
		UnitType = unitType;
	}
	public int getUnitID() {
		return UnitID;
	}
	public void setUnitID(int unitID) {
		UnitID = unitID;
	}
	public String getWho() {
		return Who;
	}
	public void setWho(String who) {
		Who = who;
	}
	
}
