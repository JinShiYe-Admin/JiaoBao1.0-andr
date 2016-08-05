package com.jsy_jiaobao.po.sturecord;

public class BaseInfo {

	private StuRecGenPackage stubase;
	private int newc;// 最新未读数
	private int schc;// 校园通知未读数
	private int clsc;// 班级通知未读数
	private int bxc;// 日常表现未读数
	private int qpc;// 期评未读数
	private int tecc;// 老师留言未读数
	private int genc;// 家长留言未读数
	private int ispack;// 1:选择的孩子为档案包类型,0为学生类型
	private int packid;// 档案包ID;
	private int stuid;// 学生ID
	private int packschun;// 0:有档案包,无学校资料;1:同时有档案包和学校资料;2:无档案包,有资料;
	private int islink;// 是否为被关联的亲戚包
	public StuRecGenPackage getStubase() {
		return stubase;
	}
	public void setStubase(StuRecGenPackage stubase) {
		this.stubase = stubase;
	}
	public int getNewc() {
		return newc;
	}
	public void setNewc(int newc) {
		this.newc = newc;
	}
	public int getSchc() {
		return schc;
	}
	public void setSchc(int schc) {
		this.schc = schc;
	}
	public int getClsc() {
		return clsc;
	}
	public void setClsc(int clsc) {
		this.clsc = clsc;
	}
	public int getBxc() {
		return bxc;
	}
	public void setBxc(int bxc) {
		this.bxc = bxc;
	}
	public int getQpc() {
		return qpc;
	}
	public void setQpc(int qpc) {
		this.qpc = qpc;
	}
	public int getTecc() {
		return tecc;
	}
	public void setTecc(int tecc) {
		this.tecc = tecc;
	}
	public int getGenc() {
		return genc;
	}
	public void setGenc(int genc) {
		this.genc = genc;
	}
	public int getIspack() {
		return ispack;
	}
	public void setIspack(int ispack) {
		this.ispack = ispack;
	}
	public int getPackid() {
		return packid;
	}
	public void setPackid(int packid) {
		this.packid = packid;
	}
	public int getStuid() {
		return stuid;
	}
	public void setStuid(int stuid) {
		this.stuid = stuid;
	}
	public int getPackschun() {
		return packschun;
	}
	public void setPackschun(int packschun) {
		this.packschun = packschun;
	}
	public int getIslink() {
		return islink;
	}
	public void setIslink(int islink) {
		this.islink = islink;
	}
	
}
