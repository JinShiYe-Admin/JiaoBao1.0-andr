package com.jsy_jiaobao.po.personal;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 获取事务信息接收单位列表
 */
public class CommMsgRevicerUnitList implements Serializable {
	private static final long serialVersionUID = 9163682352086449629L;
	private MyUnit myUnit;// 本单位
	private ArrayList<CommMsgRevicerUnit> UnitParents;// 上级单位数组可能有多个
	private ArrayList<CommMsgRevicerUnit> subUnits;// 下级单位数组可能有多个
	private ArrayList<CommMsgRevicerUnitClass> UnitClass;// 班级数组，我所执教的班级，数组，如果是教育局，这个对象为null

	public MyUnit getMyUnit() {
		return myUnit;
	}

	public void setMyUnit(MyUnit myUnit) {
		this.myUnit = myUnit;
	}

	public ArrayList<CommMsgRevicerUnit> getUnitParents() {
		return UnitParents;
	}

	public void setUnitParents(ArrayList<CommMsgRevicerUnit> unitParents) {
		UnitParents = unitParents;
	}

	public ArrayList<CommMsgRevicerUnit> getSubUnits() {
		return subUnits;
	}

	public void setSubUnits(ArrayList<CommMsgRevicerUnit> subUnits) {
		this.subUnits = subUnits;
	}

	public ArrayList<CommMsgRevicerUnitClass> getUnitClass() {
		return UnitClass;
	}

	public void setUnitClass(ArrayList<CommMsgRevicerUnitClass> unitClass) {
		UnitClass = unitClass;
	}
}
