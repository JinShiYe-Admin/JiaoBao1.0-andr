package com.jsy_jiaobao.po.personal;

import java.io.Serializable;

/*
 * 单位相册Model
 */
public class UnitPGroup implements Serializable {

	private static final long serialVersionUID = 8360689569601289126L;
	private int CreateByjiaobaohao;// 单位相册创建人jiaobaohao ；谁创建谁维护
	private String nameStr;// 相册名称
	private String DesInfo;// 相册描述,目前没有内容
	private int ViewType;// 相册显示类型：0为无限制，1为单位内成员可见，3为相册删除状态，暂时3状态没有用到
	private int UnitID;// 单位id
	private int TabID;// 单位相册对应的ID

	public int getCreateByjiaobaohao() {
		return CreateByjiaobaohao;
	}

	public void setCreateByjiaobaohao(int createByjiaobaohao) {
		CreateByjiaobaohao = createByjiaobaohao;
	}

	public String getNameStr() {
		return nameStr;
	}

	public void setNameStr(String nameStr) {
		this.nameStr = nameStr;
	}

	public String getDesInfo() {
		return DesInfo;
	}

	public void setDesInfo(String desInfo) {
		DesInfo = desInfo;
	}

	public int getViewType() {
		return ViewType;
	}

	public void setViewType(int viewType) {
		ViewType = viewType;
	}

	public int getUnitID() {
		return UnitID;
	}

	public void setUnitID(int unitID) {
		UnitID = unitID;
	}

	public int getTabID() {
		return TabID;
	}

	public void setTabID(int tabID) {
		TabID = tabID;
	}
}
