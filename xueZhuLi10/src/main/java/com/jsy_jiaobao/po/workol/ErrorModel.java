package com.jsy_jiaobao.po.workol;

import android.content.Context;
/**
 * 错题本Model
 * @author admin
 *
 */
public class ErrorModel {
	private Context mContext;//上下文
	private ErrorModel mErrorModel;//声明
	private int Tabid;//Error的Id
	private int DoC;// "DoC":1}/重复次数
	private int QsLv;// "QsLv":1,//困难度
	private String QsCon;// "QsCon"://问题内容
	private String Answer;// "Answer":null,//答案内容
	private String QsCorectAnswer;// "QsCorectAnswer":"A" 正确答案,
	private String QsExplain;// "QsExplain":null}答案解释
	private StuErrorModel mStuErrorModel;
	private StuErrDetailModel mStuErrDetailModel;

	public ErrorModel(Context context) {
		mContext = context;
	}

	public Context getContext() {
		return mContext;
	}

	public ErrorModel newInstance() {
		if (mErrorModel == null) {
			mErrorModel = new ErrorModel(mContext);
		}
		return mErrorModel;
	}

	public int getTabid() {
		return Tabid;
	}

	public void setTabid(int tabid) {
		Tabid = tabid;
	}

	public int getDoC() {
		return DoC;
	}

	public void setDoC(int doC) {
		DoC = doC;
	}

	public int getQsLv() {
		return QsLv;
	}

	public void setQsLv(int qsLv) {
		QsLv = qsLv;
	}

	public String getQsCon() {
		return QsCon;
	}

	public void setQsCon(String qsCon) {
		QsCon = qsCon;
	}

	public String getAnswer() {
		return Answer;
	}

	public void setAnswer(String answer) {
		Answer = answer;
	}

	public String getQsCorectAnswer() {
		return QsCorectAnswer;
	}

	public void setQsCorectAnswer(String qsCorectAnswer) {
		QsCorectAnswer = qsCorectAnswer;
	}

	public String getQsExplain() {
		return QsExplain;
	}

	public void setQsExplain(String qsExplain) {
		QsExplain = qsExplain;
	}

	public void setStuErrorModel(StuErrorModel stuErrorModel) {
		mStuErrorModel = stuErrorModel;
		Tabid = stuErrorModel.getTabid();
		QsLv = stuErrorModel.getQsLv();
		DoC = stuErrorModel.getDoC();
		Answer = stuErrorModel.getAnswer();
	}

	public void setStuErrDetailModel(StuErrDetailModel stuErrDetailModel) {
		mStuErrDetailModel = stuErrDetailModel;
		if(stuErrDetailModel.getQsCon()!=null){
			QsCon = stuErrDetailModel
					.getQsCon()
					.replaceAll("<br />", "\\\n")
					.replaceAll("<br/>", "\\\n")
					.replaceAll("<br>", "\\\n")
					.replaceAll(
							"（<input type=\"text\" class=\"ad-tk-input\" style=\"width:60\">）",
							"[     ]")
					.replaceAll(
							"(<input type=\"text\" class=\"ad-tk-input\" style=\"width:60\">)",
							"[     ]")
					.replaceAll(
							"<input type=\"text\" class=\"ad-tk-input\" style=\"width:60\">",
							"[     ]").replaceAll("<[^>]*>", "");
		}else{
			QsCon=null;
		}
		
		QsCorectAnswer = stuErrDetailModel.getQsCorectAnswer();
		QsExplain = stuErrDetailModel.getQsExplain();
	}

}
