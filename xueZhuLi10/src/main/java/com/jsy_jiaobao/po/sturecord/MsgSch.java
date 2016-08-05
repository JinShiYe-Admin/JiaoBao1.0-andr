package com.jsy_jiaobao.po.sturecord;

import java.util.ArrayList;

public class MsgSch {
	public String ttype;//信息类型
    public ArrayList<String> schs;//学校|未读数     "
	public String getTtype() {
		return ttype;
	}
	public void setTtype(String ttype) {
		this.ttype = ttype;
	}
	public ArrayList<String> getSchs() {
		return schs;
	}
	public void setSchs(ArrayList<String> schs) {
		this.schs = schs;
	}
    
}
