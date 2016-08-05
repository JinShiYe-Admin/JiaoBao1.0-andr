package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 获取主题列表
 */
public class GetAllCategory implements Serializable {

	private static final long serialVersionUID = -4877515153345769341L;
	private Subject item;
	private ArrayList<Subject> subitem;

	public Subject getItem() {
		return item;
	}

	public void setItem(Subject item) {
		this.item = item;
	}

	public ArrayList<Subject> getSubitem() {
		return subitem;
	}

	public void setSubitem(ArrayList<Subject> subitem) {
		this.subitem = subitem;
	}

}
