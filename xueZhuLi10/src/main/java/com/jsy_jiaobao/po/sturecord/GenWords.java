package com.jsy_jiaobao.po.sturecord;

import java.util.ArrayList;

public class GenWords {
	
	private String msgtype;//信息类型
	private int pagesize;//每页记录数
	private int rows;//总行数
	private int pages;//总页数
	private int curpage;//当前页
	private ArrayList<StuRec_GenWord> list ;//信息列表
	private ArrayList<DataTable> dtreaded;//在这些ID里的详单对应的就是已读的,否则是未读的
	
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getCurpage() {
		return curpage;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}
	public ArrayList<StuRec_GenWord> getList() {
		return list;
	}
	public void setList(ArrayList<StuRec_GenWord> list) {
		this.list = list;
	}
	public ArrayList<DataTable> getDtreaded() {
		return dtreaded;
	}
	public void setDtreaded(ArrayList<DataTable> dtreaded) {
		this.dtreaded = dtreaded;
	}
}
