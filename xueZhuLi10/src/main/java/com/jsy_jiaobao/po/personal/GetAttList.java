package com.jsy_jiaobao.po.personal;

import java.io.Serializable;
import java.util.List;

public class GetAttList implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4587069571254947444L;
	private List<Attlist> AttList;

	public List<Attlist> getAttList() {
		return AttList;
	}

	public void setAttList(List<Attlist> attList) {
		AttList = attList;
	}
}
