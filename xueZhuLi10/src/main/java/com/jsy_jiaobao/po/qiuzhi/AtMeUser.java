package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;
/**被邀请回答问题的用户*/
public class AtMeUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7372734689168594275L;
	private int JiaoBaoHao;// 教宝号
	private String NickName;// 昵称
	private String UserPoint;// 积分
	private String UserDesc;// 用户说明
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
	public String getUserPoint() {
		return UserPoint;
	}
	public void setUserPoint(String userPoint) {
		UserPoint = userPoint;
	}
	public String getUserDesc() {
		return UserDesc;
	}
	public void setUserDesc(String userDesc) {
		UserDesc = userDesc;
	}

}
