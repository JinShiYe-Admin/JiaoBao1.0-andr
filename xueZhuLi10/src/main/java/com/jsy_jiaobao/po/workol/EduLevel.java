package com.jsy_jiaobao.po.workol;

/**
 * 学力
 */
public class EduLevel {
	private int Level;// 6095,
	private String Name;// "英语第一节（测试）",
	private int ID;// 学力Id
	private String LevelID;// 学力值
	private String ParentID;// 父节点Id
	private boolean isExpanded;// 是否展开
	private boolean isParentExpanded = true;// 父节点是否展开
	private int Padding;// 缩进
	private boolean haveChild = true;// 是否有子节点

	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getParentID() {
		return ParentID;
	}

	public void setParentID(String parentID) {
		ParentID = parentID;
	}

	public boolean isExpanded() {
		return isExpanded;
	}

	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	public String getLevelID() {
		return LevelID;
	}

	public void setLevelID(String levelID) {
		LevelID = levelID;
	}

	public int getPadding() {
		return Padding;
	}

	public void setPadding(int padding) {
		Padding = padding;
	}

	public boolean isParentExpanded() {
		return isParentExpanded;
	}

	public void setParentExpanded(boolean isParentExpanded) {
		this.isParentExpanded = isParentExpanded;
	}

	public boolean isHaveChild() {
		return haveChild;
	}

	public void setHaveChild(boolean haveChild) {
		this.haveChild = haveChild;
	}

}
