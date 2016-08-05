package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

/**
 * 
 * 单位通知信息
 */
public class UnitNoticInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7554639530701894604L;
	public int JiaoBaoHao;// 发表才教宝ID
	public String NoticMsg;// 通知正文，列表时不包含此字段
	public int NoticType;// 类型
	public String Recdate;// 日期
	public String Subject;// 标题
	public String TabIDStr;// 加密ID
	public String UserName;// 发表用户

	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}

	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}

	public String getNoticMsg() {
		return NoticMsg;
	}

	public void setNoticMsg(String noticMsg) {
		NoticMsg = noticMsg;
	}

	public int getNoticType() {
		return NoticType;
	}

	public void setNoticType(int noticType) {
		NoticType = noticType;
	}

	public String getRecdate() {
		try {
			return Recdate.split("\\.")[0].replace("T", "  ");
		} catch (Exception e) {
			return Recdate;
		}
	}

	public void setRecdate(String recdate) {
		Recdate = recdate;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getTabIDStr() {
		return TabIDStr;
	}

	public void setTabIDStr(String tabIDStr) {
		TabIDStr = tabIDStr;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

}
