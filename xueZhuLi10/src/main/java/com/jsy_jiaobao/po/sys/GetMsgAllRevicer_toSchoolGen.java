package com.jsy_jiaobao.po.sys;

import java.io.Serializable;
import java.util.ArrayList;

public class GetMsgAllRevicer_toSchoolGen implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7177217207538949325L;

	private ArrayList<UnitAdminRevicer> selitadmintogen;
	private ArrayList<Selit> selitunitclassadmintogen;
	private ArrayList<Selit> selitunitclassidtogen;
	public ArrayList<UnitAdminRevicer> getSelitadmintogen() {
		return selitadmintogen;
	}
	public void setSelitadmintogen(ArrayList<UnitAdminRevicer> selitadmintogen) {
		this.selitadmintogen = selitadmintogen;
	}
	public ArrayList<Selit> getSelitunitclassadmintogen() {
		return selitunitclassadmintogen;
	}
	public void setSelitunitclassadmintogen(
			ArrayList<Selit> selitunitclassadmintogen) {
		this.selitunitclassadmintogen = selitunitclassadmintogen;
	}
	public ArrayList<Selit> getSelitunitclassidtogen() {
		return selitunitclassidtogen;
	}
	public void setSelitunitclassidtogen(ArrayList<Selit> selitunitclassidtogen) {
		this.selitunitclassidtogen = selitunitclassidtogen;
	}
	
}
