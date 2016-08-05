package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

/**
 * 发送单位动态的权限
 */
public class PublishPermission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8428754105057435545L;
	private int UnitId;//单位Id
	private String UnitName;//单位名称
	private int UnitType;// 1=教育局，2学校，3班级

	public int getUnitId() {
		return UnitId;
	}

	public void setUnitId(int unitId) {
		UnitId = unitId;
	}

	public String getUnitName() {
		return UnitName;
	}

	public void setUnitName(String unitName) {
		UnitName = unitName;
	}

	public int getUnitType() {
		return UnitType;
	}

	public void setUnitType(int unitType) {
		UnitType = unitType;
	}

}
