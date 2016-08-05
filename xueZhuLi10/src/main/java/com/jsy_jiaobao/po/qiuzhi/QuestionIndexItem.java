package com.jsy_jiaobao.po.qiuzhi;

import java.io.Serializable;

/**
 * 
 * <pre>
 *                    _ooOoo_
 *                   o8888888o
 *                   88" . "88
 *                   (| -_- |)
 *                   O\  =  /O
 *                ____/`---'\____
 *              .'  \\|     |//  `.
 *             /  \\|||  :  |||//  \
 *            /  _||||| -:- |||||-  \
 *            |   | \\\  -  /// |   |
 *            | \_|  ''\---/''  |   |
 *            \  .-\__  `-`  ___/-. /
 *          ___`. .'  /--.--\  `. . __
 *       ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ======`-.____`-.___\_____/___.-`____.-'======
 *                    `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 			               佛祖保佑       永无BUG
 * 
 * 首页问题列表，按要求排序和包含一个回答。
 */
public class QuestionIndexItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6983637870956345566L;
	private int TabID;//
	private String Title;// 标题
	private int AnswersCount;// 回答数量
	private int AttCount;// 关注数量，
	private int ViewCount;// 浏览数量（打开看过明细的数量）
	private String CategorySuject;// 话题名称
	private int CategoryId;// 话题ID
	private String LastUpdate;// 动态更新日期
	private String AreaCode;// 区域代码
	private int JiaoBaoHao;//
	private int rowCount;// 记录数量，用于取第二页记录起给参数赋值
	private AnswerItem answer;
	private String Thumbnail;
	
	private String KnContent;
	public int getTabID() {
		return TabID;
	}
	public void setTabID(int tabID) {
		TabID = tabID;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public int getAnswersCount() {
		return AnswersCount;
	}
	public void setAnswersCount(int answersCount) {
		AnswersCount = answersCount;
	}
	public int getAttCount() {
		return AttCount;
	}
	public void setAttCount(int attCount) {
		AttCount = attCount;
	}
	public int getViewCount() {
		return ViewCount;
	}
	public void setViewCount(int viewCount) {
		ViewCount = viewCount;
	}
	public String getCategorySuject() {
		return CategorySuject;
	}
	public void setCategorySuject(String categorySuject) {
		CategorySuject = categorySuject;
	}
	public int getCategoryId() {
		return CategoryId;
	}
	public void setCategoryId(int categoryId) {
		CategoryId = categoryId;
	}
	public String getLastUpdate() {
		return LastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		LastUpdate = lastUpdate;
	}
	public String getAreaCode() {
		return AreaCode;
	}
	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}
	public int getJiaoBaoHao() {
		return JiaoBaoHao;
	}
	public void setJiaoBaoHao(int jiaoBaoHao) {
		JiaoBaoHao = jiaoBaoHao;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public String getThumbnail() {
		return Thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		Thumbnail = thumbnail;
	}
	public AnswerItem getAnswer() {
		return answer;
	}
	public void setAnswer(AnswerItem answer) {
		this.answer = answer;
	}
	public String getKnContent() {
		return KnContent;
	}
	public void setKnContent(String knContent) {
		KnContent = knContent;
	}

}
