package com.jsy_jiaobao.po.sys;

import java.util.List;

public class UnitClassALLRevicer {
	//本班家长，表示学校老师直接发给家长，不需要转发。需要把GroupSelit[]中的selit以selitadmintomem为name提交给api
    public List<Selit> selitunitclassidtogen;
    //本班学生，表示学校老师直接发给学生,不需要转发。需要把GroupSelit[]中的selit以selitadmintogen为name提交给api
    public List<Selit> selitunitclassidtostu;
    //发给班主任，表示该信息需要由班主任转给家长。需要把GroupSelit[]中的selit以selitunitclassadmintogen为name提交给api
    public List<Selit> selitunitclassadmintogen;
    //发给班主任，表示该信息需要由班主任转给学生。需要把GroupSelit[]中的selit以selitunitclassadmintostu为name提交给api
    public List<Selit> selitunitclassadmintostu;

    public List<Selit> getSelitunitclassidtogen() {
		return selitunitclassidtogen;
	}
	public void setSelitunitclassidtogen(List<Selit> selitunitclassidtogen) {
		this.selitunitclassidtogen = selitunitclassidtogen;
	}
	public List<Selit> getSelitunitclassidtostu() {
		return selitunitclassidtostu;
	}
	public void setSelitunitclassidtostu(List<Selit> selitunitclassidtostu) {
		this.selitunitclassidtostu = selitunitclassidtostu;
	}
	public List<Selit> getSelitunitclassadmintogen() {
		return selitunitclassadmintogen;
	}
	public void setSelitunitclassadmintogen(List<Selit> selitunitclassadmintogen) {
		this.selitunitclassadmintogen = selitunitclassadmintogen;
	}
	public List<Selit> getSelitunitclassadmintostu() {
		return selitunitclassadmintostu;
	}
	public void setSelitunitclassadmintostu(List<Selit> selitunitclassadmintostu) {
		this.selitunitclassadmintostu = selitunitclassadmintostu;
	}
}