package com.jsy_jiaobao.po.personal;

import java.io.Serializable;
/**
 * 下级单位信息Model
 * @author admin
 *
 */
public class UnitSectionMessage implements Serializable{

	private static final long serialVersionUID = -1266180329942453863L;
	public int UnitID; // 单位ID
	public String UnitName;// 单位名称
	public int IsMyUnit;// 单位标志1我所在单位，2我的上级单位，如果一个人同时在上级和本单位，我所在单位优先
	public int MessageCount;// 未读数量
	public int UnitType;// 单位类型1教育局2学校，3班级
	public int getUnitID() {
		return UnitID;
	}
	public void setUnitID(int unitID) {
		UnitID = unitID;
	}
	public String getUnitName() {
		return UnitName;
	}
	public void setUnitName(String unitName) {
		UnitName = unitName;
	}
	public int getIsMyUnit() {
		return IsMyUnit;
	}
	public void setIsMyUnit(int isMyUnit) {
		IsMyUnit = isMyUnit;
	}
	public int getMessageCount() {
		return MessageCount;
	}
	public void setMessageCount(int messageCount) {
		MessageCount = messageCount;
	}
	public int getUnitType() {
		return UnitType;
	}
	public void setUnitType(int unitType) {
		UnitType = unitType;
	}
	
}
