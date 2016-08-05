package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;

/**主题*/
public class Subject implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -902885787629303225L;
	private int TabID;// 1,
    private String Subject;// "教学\r\n",
    private int QCount;// 0,
    private int AttCount;// 0,
    private int ParentId;// 0
    private boolean isAtted;
	public int getTabID() {
		return TabID;
	}
	public void setTabID(int tabID) {
		TabID = tabID;
	}
	public String getSubject() {
		return Subject;
	}
	public void setSubject(String subject) {
		Subject = subject;
	}
	public int getQCount() {
		return QCount;
	}
	public void setQCount(int qCount) {
		QCount = qCount;
	}
	public int getAttCount() {
		return AttCount;
	}
	public void setAttCount(int attCount) {
		AttCount = attCount;
	}
	public int getParentId() {
		return ParentId;
	}
	public void setParentId(int parentId) {
		ParentId = parentId;
	}
	public boolean isAtted() {
		return isAtted;
	}
	public void setAtted(boolean isAtted) {
		this.isAtted = isAtted;
	}
    
}
