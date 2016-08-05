package com.jsy_jiaobao.po.qiuzhi;

import java.util.ArrayList;

public class GetPicked {
	private int TabID;//  精选ID
	private String PTitle;//  精选标题
	private String PickDescipt;//  精选说明
	private String RecDate ;// 日期
	private String VedioConntent;//  精选视频的url地址，目前为空
	private String ImgContent ;// 字符串数组 轮播图片地址片段（每个地址需要加上baseImgUrl这个字段以得到一个完整的url地址）
	private String baseImgUrl;//
	private ArrayList<PickedIndex> PickContent;//
	public int getTabID() {
		return TabID;
	}
	public void setTabID(int tabID) {
		TabID = tabID;
	}
	public String getPTitle() {
		return PTitle;
	}
	public void setPTitle(String pTitle) {
		PTitle = pTitle;
	}
	public String getPickDescipt() {
		return PickDescipt;
	}
	public void setPickDescipt(String pickDescipt) {
		PickDescipt = pickDescipt;
	}
	public String getRecDate() {
		return RecDate;
	}
	public void setRecDate(String recDate) {
		RecDate = recDate;
	}
	public String getVedioConntent() {
		return VedioConntent;
	}
	public void setVedioConntent(String vedioConntent) {
		VedioConntent = vedioConntent;
	}
	public String getImgContent() {
		return ImgContent;
	}
	public void setImgContent(String imgContent) {
		ImgContent = imgContent;
	}
	public String getBaseImgUrl() {
		return baseImgUrl;
	}
	public void setBaseImgUrl(String baseImgUrl) {
		this.baseImgUrl = baseImgUrl;
	}
	public ArrayList<PickedIndex> getPickContent() {
		return PickContent;
	}
	public void setPickContent(ArrayList<PickedIndex> pickContent) {
		PickContent = pickContent;
	}

}
