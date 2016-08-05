package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;

public class PickedIndex implements Serializable {
	/**
	 * 求知精选List 的数据
	 */
	private static final long serialVersionUID = -2085866137897420864L;
	private int TabID;// 内容ID （下面的GetPickedById用这个字段获取精选内容明细）
	private String Title;// 内容标题
	private String Abstracts;// 内容摘要
	private String Thumbnail;// 字符串数组 答案内容前三张图片地址

	public int getTabID() {
		return TabID;
	}

	public void setTabID(int tabID) {
		TabID = tabID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getAbstracts() {
		return Abstracts;
	}

	public void setAbstracts(String abstracts) {
		Abstracts = abstracts;
	}

	public String getThumbnail() {
		return Thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		Thumbnail = thumbnail;
	}

}
