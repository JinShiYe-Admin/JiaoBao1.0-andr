package com.jsy_jiaobao.po.sys;

import java.util.ArrayList;
import java.util.List;

public class ClassRevicer {
	private String ClassName;// ": "班级给我",班级名称
	private ArrayList<Selit> teachers_selit;// ": null,
	private ArrayList<Selit> studentgens_genselit;// ":

	public String getClassName() {
		return ClassName;
	}

	public void setClassName(String className) {
		ClassName = className;
	}

	public ArrayList<Selit> getStudentgens_genselit() {
		return studentgens_genselit;
	}

	public void setStudentgens_genselit(ArrayList<Selit> studentgens_genselit) {
		this.studentgens_genselit = studentgens_genselit;
	}

	public ArrayList<Selit> getTeachers_selit() {
		return teachers_selit;
	}

	public void setTeachers_selit(ArrayList<Selit> teachers_selit) {
		this.teachers_selit = teachers_selit;
	}

}
