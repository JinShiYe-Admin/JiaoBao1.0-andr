package com.jsy_jiaobao.po.personal;

import java.io.Serializable;
import java.util.ArrayList;

import com.jsy_jiaobao.po.sys.Selit;

public class GenRevicer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1758066138019705423L;
	private ArrayList<Selit> genselit;
	public ArrayList<Selit> getGenselit() {
		return genselit;
	}
	public void setGenselit(ArrayList<Selit> genselit) {
		this.genselit = genselit;
	}
}
