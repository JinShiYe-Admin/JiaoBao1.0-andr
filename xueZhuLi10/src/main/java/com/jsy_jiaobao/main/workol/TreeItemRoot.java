package com.jsy_jiaobao.main.workol;

import com.jsy_jiaobao.po.workol.TeaGrade;
import com.jsy_jiaobao.po.workol.TeaMode;
import com.jsy_jiaobao.po.workol.TeaSession;
import com.jsy_jiaobao.po.workol.TeaSubject;

public class TreeItemRoot {
	/** 年级 */
	final static int TYPE_GRADE = 0;
	/** 科目 */
	final static int TYPE_MODE = 1;
	/** 教版 */
	final static int TYPE_SUBJECT = 2;
	/** 章节 */
	final static int TYPE_SESSION = 3;
	/** 标题 */
	final static int TYPE_TITLE = 4;
	public String name;// 名称
	public String select = "";// 所选
	public int TabID;// Id
	public int Padding;// 边距
	public int TYPE;// 类型

	/**
	 * 年级
	 * 
	 * @param name d
	 * @param teaGrade g
	 */
	public TreeItemRoot(String name, TeaGrade teaGrade) {
		this.name = name;
		if (teaGrade != null) {
			this.select = teaGrade.getGradeName();
			this.TabID = teaGrade.getGradeCode();
		} else {
			this.select = "请选择年级";
		}
		this.TYPE = TYPE_GRADE;
	}

	/**
	 * 科目
	 * 
	 * @param name g
	 * @param teaMode g
	 */
	public TreeItemRoot(String name, TeaMode teaMode) {
		this.name = name;
		if (teaMode != null) {
			this.select = teaMode.getSubjectName();
			this.TabID = teaMode.getSubjectCode();
		} else {
			this.select = "请选择科目";
		}
		this.TYPE = TYPE_MODE;
	}

	/**
	 * 教版
	 *
	 * @param name g
	 * @param teaSubject g
	 * @param i g
	 */
	public TreeItemRoot(String name, TeaSubject teaSubject, int i) {
		this.name = name;
		if (teaSubject != null) {
			this.select = teaSubject.getVersionName();
			this.TabID = teaSubject.getTabID();
		} else {
			this.select = "请选择教版";
		}
		this.Padding = i;
		this.TYPE = TYPE_SUBJECT;
	}

	/**
	 * 章节
	 *
	 * @param name g
	 * @param session gh
	 * @param padding g
	 */
	public TreeItemRoot(String name, TeaSession session, int padding) {
		this.name = name;
		if (session != null) {
			this.select = session.getChapterName();
			this.TabID = session.getTabID();
		} else {
			this.select = "请选择章节";
		}
		this.Padding = padding;
		this.TYPE = TYPE_SESSION;
	}

	/**
	 * 名称
	 * 
	 * @param name g
	 * @param teaSession g
	 */
	public TreeItemRoot(String name, TeaSession teaSession) {
		this.name = name;
		if (teaSession != null) {
			this.select = teaSession.getChapterName();
			this.TabID = teaSession.getTabID();
		} else {
			this.select = "无内容";
		}
		this.TYPE = TYPE_TITLE;
	}

	@Override
	public String toString() {
		return name;
	}
}
