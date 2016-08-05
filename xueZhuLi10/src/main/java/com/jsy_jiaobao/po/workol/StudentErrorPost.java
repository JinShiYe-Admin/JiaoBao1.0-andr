package com.jsy_jiaobao.po.workol;

import android.util.Log;
/**
 * 请求错题本需要上传的数据
 */

import com.lidroid.xutils.http.RequestParams;

/**
 * 错题本请求接口所需数据
 * 
 * @author admin
 * 
 */
public class StudentErrorPost {

	// <param name="StuId">学生ID</param>
	private int StuId;
	// <param name="IsSelf">0作业,1练习</param>
	private int IsSelf;
	// <param name="PageIndex">页码</param>
	private int PageIndex;
	// <param name="PageSize">页记录数</param>
	private int PageSize;
	// <param name="chapterid">章节ID</param>
	private int chapterid;
	// <param name="CreateDate_Min">开始时间</param>
	private String CreateDate_Min;
	// <param name="CreateDate_Max">结束时间</param>
	private String CreateDate_Max;

	// <param name="QsLv"></param>
	private int QsLv;

	// @property (nonatomic,strong) NSString *gradeCode;//年级
	private int gradeCode;
	// @property (nonatomic,strong) NSString *subjectCode;//科目
	private int subjectCode;
	// @property (nonatomic,strong) NSString *unid;//教版
	private int unid;

	public RequestParams getParams() {
		if (check()) {
			Log.e("booleen", check() + "");
			RequestParams params = new RequestParams();
			params.addBodyParameter("StuId", String.valueOf(StuId));
			params.addBodyParameter("IsSelf", String.valueOf(IsSelf));
			params.addBodyParameter("PageIndex", String.valueOf(PageIndex));
			params.addBodyParameter("PageSize", String.valueOf(PageSize));
			if (chapterid != 0) {
				params.addBodyParameter("chapterid", String.valueOf(chapterid));
			}
			if (gradeCode != 0) {
				params.addBodyParameter("gradeCode", String.valueOf(gradeCode));
			}
			if (subjectCode != 0) {
				params.addBodyParameter("subjectCode",
						String.valueOf(subjectCode));
			}
			if (unid != 0) {
				params.addBodyParameter("unid", String.valueOf(unid));
			}

			if (CreateDate_Min != null) {
				params.addBodyParameter("CreateDate_Min", CreateDate_Min);
			}
			if (CreateDate_Max != null) {
				params.addBodyParameter("CreateDate_Max", CreateDate_Max);
			}
			if (QsLv != 0) {
				params.addBodyParameter("QsLv", String.valueOf(QsLv));
			}
			return params;
		} else {
			return null;
		}
	}

	public boolean check() {
		if (StuId == 0) {
			return false;
		}
		if (PageIndex == 0) {
			return false;
		}
		if (PageSize == 0) {
			return false;
		}
		return true;
	}

	public int getStuId() {
		return StuId;
	}

	public void setStuId(int stuId) {
		StuId = stuId;
	}

	public int getIsSelf() {
		return IsSelf;
	}

	public void setIsSelf(int isSelf) {
		IsSelf = isSelf;
	}

	public int getPageIndex() {
		return PageIndex;
	}

	public void setPageIndex(int pageIndex) {
		PageIndex = pageIndex;
	}

	public int getPageSize() {
		return PageSize;
	}

	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}

	public int getChapterid() {
		return chapterid;
	}

	public void setChapterid(int chapterid) {
		this.chapterid = chapterid;
	}

	public String getCreateDate_Min() {
		return CreateDate_Min;
	}

	public void setCreateDate_Min(String createDate_Min) {
		CreateDate_Min = createDate_Min;
	}

	public String getCreateDate_Max() {
		return CreateDate_Max;
	}

	public void setCreateDate_Max(String createDate_Max) {
		CreateDate_Max = createDate_Max;
	}

	public int getQsLv() {
		return QsLv;
	}

	public void setQsLv(int qsLv) {
		QsLv = qsLv;
	}

	public int getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(int gradeCode) {
		this.gradeCode = gradeCode;
	}

	public int getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(int subjectCode) {
		this.subjectCode = subjectCode;
	}

	public int getUnid() {
		return unid;
	}

	public void setUnid(int unid) {
		this.unid = unid;
	}

}
