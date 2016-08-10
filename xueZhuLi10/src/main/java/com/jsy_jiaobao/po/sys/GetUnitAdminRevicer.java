package com.jsy_jiaobao.po.sys;

import java.io.Serializable;
import java.util.ArrayList;

public class GetUnitAdminRevicer implements Serializable{
	private static final long serialVersionUID = -2865964682760040722L;
	private ArrayList<UnitAdminRevicer> selitadmintomem;

	public ArrayList<UnitAdminRevicer> getSelitadmintomem() {
		return selitadmintomem;
	}

	public void setSelitadmintomem(ArrayList<UnitAdminRevicer> selitadmintomem) {
		this.selitadmintomem = selitadmintomem;
	}
}