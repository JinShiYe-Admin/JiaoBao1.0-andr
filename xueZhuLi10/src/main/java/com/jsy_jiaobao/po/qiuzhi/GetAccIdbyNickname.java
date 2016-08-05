package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;

public class GetAccIdbyNickname implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4993494194361355486L;
	private int JiaoBaoHao ;
	private String NickName ;
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
}
