package com.jsy_jiaobao.po.sys;

import java.util.List;

/**
 *                   _ooOoo_
 *                  o8888888o
 *                  88" . "88
 *                  (| -_- |)
 *                  O\  =  /O
 *               ____/`---'\____
 *             .'  \\|     |//  `.
 *            /  \\|||  :  |||//  \
 *           /  _||||| -:- |||||-  \
 *           |   | \\\  -  /// |   |
 *           | \_|  ''\---/''  |   |
 *           \  .-\__  `-`  ___/-. /
 *         ___`. .'  /--.--\  `. . __
 *      ."" '<  `.___\_<|>_/___.'  >'"".
 *     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *     \  \ `-.   \_ __\ /__ _/   .-` /  /
 *======`-.____`-.___\_____/___.-`____.-'======
 *                   `=---='
 *^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *         		    佛祖保佑       永无BUG
 */
public class GetRevicerList {

	private List<UnitRevicer> parentUnitRevicer;//上级单位接收人
	private UnitRevicer myUnitRevicer;//本单位接收人
	private List<UnitRevicer> subUnitRevicer;// null,下级单位接收人
	private List<ClassRevicer> UnitClassRevicer;// null,班级接收人（家长或老师）
	private List<UnitAdminRevicer> selitadmintomem;// null,单位管理员，转发给本单位人员
	private List<UnitAdminRevicer> selitadmintogen;// null,学校管理员,转发给学校家长
	private List<UnitAdminRevicer> selitadmintostu;// null,学校管理员，转发给本交学生
	private UnitClassALLRevicer unitClassAdminRevicer;// null}//群发（下发通知）时班级接收者
	
	public List<UnitRevicer> getParentUnitRevicer() {
		return parentUnitRevicer;
	}
	public void setParentUnitRevicer(List<UnitRevicer> parentUnitRevicer) {
		this.parentUnitRevicer = parentUnitRevicer;
	}
	public List<UnitRevicer> getSubUnitRevicer() {
		return subUnitRevicer;
	}
	public void setSubUnitRevicer(List<UnitRevicer> subUnitRevicer) {
		this.subUnitRevicer = subUnitRevicer;
	}
	public List<ClassRevicer> getUnitClassRevicer() {
		return UnitClassRevicer;
	}
	public void setUnitClassRevicer(List<ClassRevicer> unitClassRevicer) {
		UnitClassRevicer = unitClassRevicer;
	}
	public List<UnitAdminRevicer> getSelitadmintomem() {
		return selitadmintomem;
	}
	public void setSelitadmintomem(List<UnitAdminRevicer> selitadmintomem) {
		this.selitadmintomem = selitadmintomem;
	}
	public List<UnitAdminRevicer> getSelitadmintogen() {
		return selitadmintogen;
	}
	public void setSelitadmintogen(List<UnitAdminRevicer> selitadmintogen) {
		this.selitadmintogen = selitadmintogen;
	}
	public List<UnitAdminRevicer> getSelitadmintostu() {
		return selitadmintostu;
	}
	public void setSelitadmintostu(List<UnitAdminRevicer> selitadmintostu) {
		this.selitadmintostu = selitadmintostu;
	}
	public UnitRevicer getMyUnitRevicer() {
		return myUnitRevicer;
	}
	public void setMyUnitRevicer(UnitRevicer myUnitRevicer) {
		this.myUnitRevicer = myUnitRevicer;
	}
	public UnitClassALLRevicer getUnitClassAdminRevicer() {
		return unitClassAdminRevicer;
	}
	public void setUnitClassAdminRevicer(UnitClassALLRevicer unitClassAdminRevicer) {
		this.unitClassAdminRevicer = unitClassAdminRevicer;
	}
	
}
