package com.jsy_jiaobao.po.workol;

/**
 * 提交答案后 的下一题
 * 
 * @author admin
 * 
 */
public class StuSubQs {
	// 是否有下一题？　０没有了
	private int ReNum;
	// 题目页面
	private String HWHTML;
	// 题目类型
	private String HWType;
	// 下一题答案ID加密的
	private String nextIDAnswer;
	// 下一题答案ID未加密
	private int sizeNum;
	// 我选的答案
	private String MyAnswer;

	public int getReNum() {
		return ReNum;
	}

	public void setReNum(int reNum) {
		ReNum = reNum;
	}

	public String getHWHTML() {
		return HWHTML;
	}

	public void setHWHTML(String hWHTML) {
		HWHTML = hWHTML;
	}

	public String getHWType() {
		return HWType;
	}

	public void setHWType(String hWType) {
		HWType = hWType;
	}

	public String getNextIDAnswer() {
		return nextIDAnswer;
	}

	public void setNextIDAnswer(String nextIDAnswer) {
		this.nextIDAnswer = nextIDAnswer;
	}

	public int getSizeNum() {
		return sizeNum;
	}

	public void setSizeNum(int sizeNum) {
		this.sizeNum = sizeNum;
	}

	public String getMyAnswer() {
		return MyAnswer;
	}

	public void setMyAnswer(String myAnswer) {
		MyAnswer = myAnswer;
	}

}
