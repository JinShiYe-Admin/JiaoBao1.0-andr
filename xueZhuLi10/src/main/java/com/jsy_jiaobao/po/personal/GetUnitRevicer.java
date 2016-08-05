package com.jsy_jiaobao.po.personal;

import java.io.Serializable;
import java.util.ArrayList;

import com.jsy_jiaobao.po.sys.GroupUserList;
import com.jsy_jiaobao.po.sys.Selit;

/**
 * 
 */
public class GetUnitRevicer implements Serializable {
	private static final long serialVersionUID = -192170461631712677L;
	private ArrayList<GroupUserList> selit;
	private ArrayList<GroupUserList> genselit;
	private ArrayList<GroupUserList> stuselit;

	public ArrayList<GroupUserList> getSelit() {
		return selit;
	}

	public void setSelit(ArrayList<GroupUserList> selit) {
		this.selit = selit;
	}

	public ArrayList<GroupUserList> getGenselit() {
		return genselit;
	}

	public void setGenselit(ArrayList<GroupUserList> genselit) {
		this.genselit = genselit;
	}

	public ArrayList<GroupUserList> getStuselit() {
		return stuselit;
	}

	public void setStuselit(ArrayList<GroupUserList> stuselit) {
		this.stuselit = stuselit;
	}
}
