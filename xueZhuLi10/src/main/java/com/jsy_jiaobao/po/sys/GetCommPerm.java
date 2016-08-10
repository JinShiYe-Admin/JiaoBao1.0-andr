package com.jsy_jiaobao.po.sys;

/**
 * 新建事务权限
 */
public class GetCommPerm {	
	private boolean ParentCommRight;// 对上级单位发送事务权限
	private boolean UnitCommRight;// 对同级单位发送事务权限
	private boolean SubUnitCommRight;// 对下级发送事务权限
	public boolean isParentCommRight() {
		return ParentCommRight;
	}
	public void setParentCommRight(boolean parentCommRight) {
		ParentCommRight = parentCommRight;
	}
	public boolean isUnitCommRight() {
		return UnitCommRight;
	}
	public void setUnitCommRight(boolean unitCommRight) {
		UnitCommRight = unitCommRight;
	}
	public boolean isSubUnitCommRight() {
		return SubUnitCommRight;
	}
	public void setSubUnitCommRight(boolean subUnitCommRight) {
		SubUnitCommRight = subUnitCommRight;
	}
}