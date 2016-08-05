package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;

public class UserInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9197595778840694491L;
	private int JiaoBaoHao;// 教宝号
	private String NickName;// 昵称
	private String UserName;// 姓名
	private String IdFlag;// 称号
	private int State;// 状态1正常，0禁用
	private boolean IsKnlFeezeUser;// true=封号中，false=正常
	private int DUnitId;// >0有单位，=0无单位
	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}
	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getIdFlag() {
		return IdFlag;
	}
	public void setIdFlag(String idFlag) {
		IdFlag = idFlag;
	}
	public int getState() {
		return State;
	}
	public void setState(int state) {
		State = state;
	}
	public boolean isIsKnlFeezeUser() {
		return IsKnlFeezeUser;
	}
	public void setIsKnlFeezeUser(boolean isKnlFeezeUser) {
		IsKnlFeezeUser = isKnlFeezeUser;
	}
	public int getDUnitId() {
		return DUnitId;
	}
	public void setDUnitId(int dUnitId) {
		DUnitId = dUnitId;
	}

}
