package com.jsy_jiaobao.po.personal;

import java.io.Serializable;
import java.util.ArrayList;

import com.jsy_jiaobao.po.sys.Selit;

public class GetUnitClassRevicer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6205116345913156940L;
	private ArrayList<Selit> selit;
	private ArrayList<Selit> genselit;
	private ArrayList<Selit> stuselit;
	
	public ArrayList<Selit> getSelit() {
		return selit;
	}
	public void setSelit(ArrayList<Selit> selit) {
		this.selit = selit;
	}
	public ArrayList<Selit> getGenselit() {
		return genselit;
	}
	public void setGenselit(ArrayList<Selit> genselit) {
		this.genselit = genselit;
	}
	public ArrayList<Selit> getStuselit() {
		return stuselit;
	}
	public void setStuselit(ArrayList<Selit> stuselit) {
		this.stuselit = stuselit;
	}
}
