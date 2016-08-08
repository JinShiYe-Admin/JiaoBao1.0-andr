package com.jsy_jiaobao.main.workol;

import java.util.ArrayList;
import org.json.JSONObject;
import android.content.Context;
import android.text.TextUtils;
import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.personalcenter.MessageCenterActivity;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.sys.UserClass;
import com.jsy_jiaobao.po.sys.UserUnit;
import com.jsy_jiaobao.po.workol.DesHW;
import com.jsy_jiaobao.po.workol.GetUnionChapterList;
import com.jsy_jiaobao.po.workol.TeaGrade;
import com.jsy_jiaobao.po.workol.TeaMakeHW;
import com.jsy_jiaobao.po.workol.TeaMode;
import com.jsy_jiaobao.po.workol.TeaSession;
import com.jsy_jiaobao.po.workol.TeaSubject;
import com.jsy_jiaobao.po.workol.UnionChapterList;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 老师发布作业Controller
 * 
 * @author admin
 * 
 */

public class TeacherPublishWorkActivityController {
	private static TeacherPublishWorkActivityController instance;
	private Context mContext;



	public static synchronized  TeacherPublishWorkActivityController getInstance() {
		if (instance == null) {
			instance = new TeacherPublishWorkActivityController();
		}
		return instance;
	}

	public TeacherPublishWorkActivityController setContext(Context pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * 取教师关联的班级 
	 * @param unit unit
     */
	public void getmyUserClass(UserUnit unit) {
		DialogUtil.getInstance().getDialog(mContext,
				R.string.getting_myGrade_waiting);
		RequestParams params = new RequestParams();
		params.addBodyParameter("UID", String.valueOf(unit.getUnitID()));
		params.addBodyParameter("AccID",
				BaseActivity.sp.getString("JiaoBaoHao", ""));
		CallBack callback = new CallBack();
		callback.setSchoolName(unit.getUnitName());
		callback.setUserTag(Constant.msgcenter_publish_getmyUserClass);
		HttpUtil.InstanceSend(ConstantUrl.getmyUserClass, params, callback);
	}

	/**
	 * 获取老师的自定义作业列表 ChapterID：章节ID teacherJiaobaohao:老师教宝号
	 */
	public void GetDesHWList(int ChapterID) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("ChapterID", String.valueOf(ChapterID));
		params.addBodyParameter("teacherJiaobaohao",
				BaseActivity.sp.getString("JiaoBaoHao", ""));
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_GetDesHWList);
		HttpUtil.InstanceSend(Constants.GetDesHWList, params, callback);
	}

	/**
	 * 老师发布作业接口[teacherJiaobaohao=5236705, classID=72202, className=班级测试001,
	 * chapterID=178, DoLv=1, AllNum=10, SelNum=5, InpNum=5, LongTime=10,
	 * ExpTime=2015-11-28 23:59:28, homeworkName=2015-11-28语文第一单元10.0,
	 * schoolName=开发部测试学校, TecName=老师A, Additional=, AdditionalDes=, HwType=1,
	 * DesId=0, IsAnSms=false, IsQsSms=false, IsRep=false, Distribution=1:5,2:5]
	 */
	public void TecMakeHW(TeaMakeHW hw) {
		RequestParams params = hw.getParams();
		params.addBodyParameter("IsSys", "1");
		CallBack callback = new CallBack();
		callback.setClassName(hw.getClassName());
		callback.setUserTag(Constants.WORKOL_TecMakeHW);
		HttpUtil.InstanceSend(Constants.TecMakeHW, params, callback);
	}

	/**
	 * <pre>
	 * 取出联动效果
	 *  params
	 *  position>点击的项目
	 * "gCode">年级代码
	 * "subCode">科目代码
	 * "uId">教版联动代码
	 * "flag">0： 根据年级获取科目，1：根据科目获取教版，2： 根据所有获取UID
	 * return
	 * Args1 为科目列表数据
	 * Args2为教版列表
	 * Args3为章节列表
	 * statusCode=200表示成功
	 */
	public void GetUnionChapterList(int typeMode, int psoition, int gCode,
			int subCode, int uId, int flag) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("gCode", String.valueOf(gCode));
		params.addBodyParameter("subCode", String.valueOf(subCode));
		params.addBodyParameter("uId", String.valueOf(uId));
		params.addBodyParameter("flag", String.valueOf(flag));
		System.out.println("psoition:" + psoition + ";" + "gCode:"
				+ String.valueOf(gCode) + ";subCode:" + String.valueOf(subCode)
				+ ";uId:" + String.valueOf(uId) + ";flag:"
				+ String.valueOf(flag));
		CallBack callback = new CallBack();
		callback.setGetUnionChapterList_Position(psoition);
		callback.setTypeMode(typeMode);
		callback.setUserTag(Constants.WORKOL_GetUnionChapterList);
		HttpUtil.InstanceSend(Constants.GetUnionChapterList, params, callback);
	}

	/**
	 * 获取年级列表
	 */
	public void GetGradeList() {
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_GetGradeList);
		HttpUtil.InstanceSend(Constants.GetGradeList, null, callback);
	}

	/**
	 * 判断某章节是否有试题 参数ChapterId 章节ID 返回：有返回true 没有返回false
	 */
	public void TecQs(int chapterid) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("chapterid", String.valueOf(chapterid));
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_TecQs);
		HttpUtil.InstanceSend(Constants.TecQs, params, callback);
	}

	private class CallBack extends RequestCallBack<String> {

		private int GetUnionChapterList_Position;
		private int typeMode;
		private String SchoolName;
		private String ClassName;

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else {
					ToastUtil.showMessage(mContext, mContext.getResources()
							.getString(R.string.error_internet));
				}
				switch ((Integer) getUserTag()) {
				case Constants.WORKOL_TecMakeHW:
					dealResponseInfo("false", this.getUserTag(),
							this.getClassName());
					break;
				case Constants.WORKOL_GetUnionChapterList:
					dealResponseInfo("false", this.getUserTag(),
							getGetUnionChapterList_Position(), getTypeMode());
					break;
				case Constants.WORKOL_TecQs:
					dealResponseInfo("ConnectFailed", this.getUserTag());
					break;
				default:
					dealResponseInfo("false", this.getUserTag());
					break;
				}

			}
		}

		/**
		 * 获取数据成功
		 */
		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				switch ((Integer) getUserTag()) {
				case Constants.WORKOL_TecQs:
					dealResponseInfo(arg0.result, this.getUserTag());
					break;
				case Constants.WORKOL_TecMakeHW:
					try {
						String statusCode = String.valueOf(arg0.statusCode);
						if (!TextUtils.isEmpty(statusCode)) {
							if ("200".equals(statusCode)) {
								dealResponseInfo(arg0.result,
										this.getUserTag(), this.getClassName());

							} else {
								dealResponseInfo("false", this.getUserTag(),
										this.getClassName());
							}
						}
					} catch (Exception e) {
						dealResponseInfo("false", this.getUserTag(),
								this.getClassName());
						ToastUtil.showMessage(mContext, mContext.getResources()
								.getString(R.string.error_serverconnect)
								+ "r1002");
					}
					break;
				case Constants.WORKOL_GetGradeList:
					dealResponseInfo(arg0.result, this.getUserTag());
					break;
				case Constant.msgcenter_publish_getmyUserClass:
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);//
						String ResultCode = jsonObj.getString("ResultCode");
						if (!TextUtils.isEmpty(ResultCode)) {
							if ("0".equals(ResultCode)) {
								String data = Des.decrypt(jsonObj
										.getString("Data"),
										MessageCenterActivity.sp_sys.getString(
												"ClientKey", ""));
								dealResponseInfo(data, this.getUserTag(),
										getSchoolName());
							} else if ("8".equals(ResultCode)) {
								LoginActivityController.getInstance()
										.helloService(mContext);
							} else {
								ToastUtil.showMessage(mContext,
										jsonObj.getString("ResultDesc"));
							}
						}
					} catch (Exception e) {
						dealResponseInfo("false", this.getUserTag());
						ToastUtil.showMessage(mContext, mContext.getResources()
								.getString(R.string.error_serverconnect)
								+ "r1002");
					}
					break;
				case Constants.WORKOL_GetUnionChapterList:
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);//
						String statusCode = jsonObj.getString("statusCode");
						if (!TextUtils.isEmpty(statusCode)) {
							if ("200".equals(statusCode)) {
								dealResponseInfo(arg0.result,
										this.getUserTag(),
										getGetUnionChapterList_Position(),
										getTypeMode());
							} else {
								ToastUtil.showMessage(mContext,
										jsonObj.getString("message"));
								dealResponseInfo("false", this.getUserTag(),
										getGetUnionChapterList_Position(),
										getTypeMode());
							}
						}
					} catch (Exception e) {
						dealResponseInfo("false", this.getUserTag());
						ToastUtil.showMessage(mContext, mContext.getResources()
								.getString(R.string.error_serverconnect)
								+ "r1002");
					}
					break;
				case Constants.WORKOL_GetDesHWList:
					dealResponseInfo(arg0.result, this.getUserTag());
					break;
				default:
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);//
						String statusCode = null;
						String ResultCode = null;
						try {
							statusCode = jsonObj.getString("statusCode");
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							ResultCode = jsonObj.getString("ResultCode");
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (!TextUtils.isEmpty(statusCode)) {
							if ("200".equals(statusCode)) {
								dealResponseInfo(arg0.result, this.getUserTag());
							} else {
								ToastUtil.showMessage(mContext,
										jsonObj.getString("message"));
								dealResponseInfo("false", this.getUserTag());
							}
						} else if (!TextUtils.isEmpty(ResultCode)) {
							if ("0".equals(ResultCode)) {
								String data = Des.decrypt(jsonObj
										.getString("Data"),
										MessageCenterActivity.sp_sys.getString(
												"ClientKey", ""));
								dealResponseInfo(data, this.getUserTag());
							} else if ("8".equals(ResultCode)) {
								LoginActivityController.getInstance()
										.helloService(mContext);
							} else {
								ToastUtil.showMessage(mContext,
										jsonObj.getString("ResultDesc"));
							}
						}
					} catch (Exception e) {
						dealResponseInfo("0", this.getUserTag());
						ToastUtil.showMessage(mContext, mContext.getResources()
								.getString(R.string.error_serverconnect)
								+ "r1002");
					}
					break;
				}
			}
		}

		public int getGetUnionChapterList_Position() {
			return GetUnionChapterList_Position;
		}

		public void setGetUnionChapterList_Position(
				int getUnionChapterList_Position) {
			GetUnionChapterList_Position = getUnionChapterList_Position;
		}

		public int getTypeMode() {
			return typeMode;
		}

		public void setTypeMode(int typeMode) {
			this.typeMode = typeMode;
		}

		public String getSchoolName() {
			return SchoolName;
		}

		public void setSchoolName(String schoolName) {
			SchoolName = schoolName;
		}

		public String getClassName() {
			return ClassName;
		}

		public void setClassName(String className) {
			ClassName = className;
		}

	}

	private void dealResponseInfo(String result, Object userTag, String str) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constants.WORKOL_TecMakeHW:
			post.add(result);
			post.add(str);
			break;
		case Constant.msgcenter_publish_getmyUserClass:
			ArrayList<UserClass> list1 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<UserClass>>() {
					}.getType());
			post.add(list1);
			post.add(str);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constants.WORKOL_TecQs:
			post.add(result);
			break;
		case Constants.WORKOL_TecMakeHW:
			post.add(result);
			break;
		case Constants.WORKOL_GetGradeList:
			ArrayList<TeaGrade> glist = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<TeaGrade>>() {
					}.getType());
			post.add(glist);
			break;
		case Constant.msgcenter_publish_getmyUserClass:
			ArrayList<UserClass> list1 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<UserClass>>() {
					}.getType());
			post.add(list1);
			break;

		case Constants.WORKOL_GetDesHWList:
			if (!TextUtils.isEmpty(result)) {
				ArrayList<DesHW> list11 = GsonUtil.GsonToList(result,
						new TypeToken<ArrayList<DesHW>>() {
						}.getType());
				post.add(list11);
			}
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}

	private void dealResponseInfo(String result, Object userTag, int i,
			int typeMode) {
		ArrayList<Object> post = new ArrayList<>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constants.WORKOL_GetUnionChapterList:
			GetUnionChapterList list = null;
			try {
				list = GsonUtil.GsonToObject(result, GetUnionChapterList.class);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (list != null) {
				UnionChapterList d = new UnionChapterList();
				try {
					ArrayList<TeaMode> args1 = GsonUtil.GsonToList(
							list.getArgs1(),
							new TypeToken<ArrayList<TeaMode>>() {
							}.getType());
					d.setArgs1(args1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					ArrayList<TeaSubject> args2 = GsonUtil.GsonToList(
							list.getArgs2(),
							new TypeToken<ArrayList<TeaSubject>>() {
							}.getType());
					d.setArgs2(args2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					ArrayList<TeaSession> args3 = GsonUtil.GsonToList(
							list.getArgs3(),
							new TypeToken<ArrayList<TeaSession>>() {
							}.getType());
					d.setArgs3(args3);
				} catch (Exception e) {
					e.printStackTrace();
				}
				d.setArgs4(list.getArgs4());
				d.setCallbackType(list.getCallbackType());
				d.setForwardUrl(list.getForwardUrl());
				d.setMessage(list.getMessage());
				d.setNavTabId(list.getNavTabId());
				d.setStatusCode(list.getStatusCode());
				post.add(1, d);
			} else {
				post.add(1, null);
			}
			post.add(2, i);
			post.add(3, typeMode);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}
