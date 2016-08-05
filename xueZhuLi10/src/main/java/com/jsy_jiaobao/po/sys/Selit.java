package com.jsy_jiaobao.po.sys;

import java.io.Serializable;

/**
 * 分组人员model
 */
public class Selit implements Serializable {
	private static final long serialVersionUID = -3802611090650534670L;
	private String selit;// ": "OUU2NDUwMkY5MTY4QzhFN0JCQTU5QTdBQzcwRTFGRUI0N0RFRDhDQzg4MjJCQ0MxOTBCMjYzMjJBRTkzMjE5RkQ1OUQ3OTM5RTExMkE4MjlGMzQ2REU4RUNCNjI3NUNBQzJFMUU5RDgzQUFBNDdBMTUwMDAzMzAwOENDRjREMDRDQjUxNzVENUFDODY1NDhGQkJGMTI3MjU2QzM2Qjk4MjNDMUE2NkFCODBERTM4NjY1RkFGNjM0QTE3QUY5ODU5OEU2OTc2RTkzMTIwQkMwNQ",接收对象json串已加密，需要把这个做为参数提交给发表交流信息api。
	private String AccID;// ": "5150001",;>0有教宝号，=0没有教宝号，有无AcccID需要特殊显示
	private int isAdmin;// ": 0,;1管理员0不是管理员，管理员特殊显示，
	private String Name;// ": "LM(123*,有)",;//显示值
	private int SendFlag;// ": 1;//=0表示不收短信，只在电脑或手机App上查看该消息

	public String getSelit() {
		return selit;
	}

	public void setSelit(String selit) {
		this.selit = selit;
	}

	public String getAccID() {
		return AccID;
	}

	public void setAccID(String accID) {
		AccID = accID;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getSendFlag() {
		return SendFlag;
	}

	public void setSendFlag(int sendFlag) {
		SendFlag = sendFlag;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Selit))
			return false;
		final Selit other = (Selit) o;

		if (this.AccID.equals(other.AccID) && this.isAdmin == other.isAdmin
				&& this.Name.equals(other.Name)
				&& this.selit.equals(other.selit)
				&& this.SendFlag == other.SendFlag)
			return true;
		else
			return false;

	}
}
