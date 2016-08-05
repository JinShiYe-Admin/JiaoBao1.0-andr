package com.jsy_jiaobao.main.workol;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.sys.StuInfo;
import com.jsy_jiaobao.po.workol.GetUnionChapterList;
import com.jsy_jiaobao.po.workol.StuErrDetailModel;
import com.jsy_jiaobao.po.workol.StuErrorModel;
import com.jsy_jiaobao.po.workol.StuHW;
import com.jsy_jiaobao.po.workol.StudentErrorPost;
import com.jsy_jiaobao.po.workol.TeaGrade;
import com.jsy_jiaobao.po.workol.TeaMode;
import com.jsy_jiaobao.po.workol.TeaSession;
import com.jsy_jiaobao.po.workol.TeaSubject;
import com.jsy_jiaobao.po.workol.UnionChapterList;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 筛选界面 的Controller
 * 
 * @author admin
 * 
 */
public class ErrorSreenActivityController implements ConstantUrl {
	int mUid = 0, mChapterid = 0;
	private static ErrorSreenActivityController instance;
	private Context mContext;
	private String TAG;

	public static synchronized final ErrorSreenActivityController getInstance() {
		if (instance == null) {
			instance = new ErrorSreenActivityController();
		}
		return instance;
	}

	public ErrorSreenActivityController setContext(Context pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * <pre>
	 * 功能说明：应用系统通过帐户ID和班级ID，获取帐户指定班级的学生数据（个人信息）
	 * 参数名称	是否必须	类型	描述
	 * AccID	是	int	用户帐户ID
	 * UID	是	int	班级ID，通过获取身份角色信息获得，用户身份须为学生。
	 */
	public void getStuInfo(String jiaobaohao, int UID) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("AccID", jiaobaohao);
		params.addBodyParameter("UID", String.valueOf(UID));
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_getStuInfo);
		HttpUtil.InstanceSend(getStuInfo, params, callback);
	}

	/**
	 * 获取错题
	 * 
	 * @param HwInfoId
	 * @param QsId
	 * @param stuErrorModel
	 */
	public void GetStuHWQs(int HwInfoId, int QsId, StuErrorModel stuErrorModel) {

		RequestParams params = new RequestParams();
		params.addBodyParameter("HwInfoId", String.valueOf(HwInfoId));
		params.addBodyParameter("QsId", String.valueOf(QsId));
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_GetStuHWQs);
		callback.setStuErrorModel(stuErrorModel);
		HttpUtil.InstanceSend(Constants.GetStuHWQs, params, callback);
	}

	/**
	 * 学生获取当前作业列表
	 * 
	 * @param StuId
	 *            :学生id
	 * @param IsSelf
	 *            : 0=作业,1=练习
	 */
	public void GetStuHWList(int StuId, int IsSelf) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("StuId", String.valueOf(StuId));
		params.addBodyParameter("IsSelf", String.valueOf(IsSelf));
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_GetStuHWList);
		HttpUtil.InstanceSend(Constants.GetStuHWList, params, callback);
	}

	/**
	 * 获取作业练习列表
	 * 
	 * @param StuId
	 * @param IsSelf
	 * @param PageIndex
	 * @param PageSize
	 */
	public void GetStuHWListPage(int StuId, int IsSelf, int PageIndex,
			int PageSize) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("StuId", String.valueOf(StuId));
		params.addBodyParameter("IsSelf", String.valueOf(IsSelf));
		params.addBodyParameter("PageIndex", String.valueOf(PageIndex));
		Log.d(TAG, PageIndex + "");
		params.addBodyParameter("PageSize", String.valueOf(PageSize));
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_GetStuHWListPage);
		HttpUtil.InstanceSend(Constants.GetStuHWListPage, params, callback);
	}

	/**
	 * <pre>
	 * 取出联动效果
	 *  params
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
	public void GetUnionChapterList(int typeMode, int gCode, int subCode,
			int uId, int flag) {
		Log.d("typeMode+gCode+subCode", typeMode + "+" + gCode + "+" + subCode);
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("gCode", String.valueOf(gCode));
		params.addBodyParameter("subCode", String.valueOf(subCode));
		params.addBodyParameter("uId", String.valueOf(uId));
		params.addBodyParameter("flag", String.valueOf(flag));
		CallBack callback = new CallBack();
		callback.setTypeMode(typeMode);
		callback.setUserTag(Constants.WORKOL_GetUnionChapterList);
		switch (typeMode) {
		case 0:
			if (gCode == -1) {
				callback.setGradeCode(gCode);
			} else {
				HttpUtil.InstanceSend(Constants.GetUnionChapterList, params,
						callback);
			}
			break;
		case 1:
			if (subCode == -1) {
				callback.setSubCode(subCode);
			} else {
				HttpUtil.InstanceSend(Constants.GetUnionChapterList, params,
						callback);
			}
			break;
		case 2:
			if (uId == -1) {
				callback.setVerCode(uId);
			} else {
				HttpUtil.InstanceSend(Constants.GetUnionChapterList, params,
						callback);
			}
			break;
		case 3:
			if (flag == -1) {
				callback.setChaCode(flag);
			} else {
				HttpUtil.InstanceSend(Constants.GetUnionChapterList, params,
						callback);
			}
			break;
		default:
			break;
		}

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
	 * 获取错题本
	 * 
	 * @param post
	 */
	public void GetStuErr(StudentErrorPost post) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = post.getParams();
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_GetStuErr);
		HttpUtil.InstanceSend(Constants.GetStuErr, params, callback);
	}

	/**
	 * 判断某章节是否有试题 参数ChapterId 章节ID 返回：有返回true 没有返回false
	 */
	public void TecQs(int chapterid) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("chapterid", String.valueOf(chapterid));
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_TecQs);
		if (chapterid == -1) {
			callback.setChaCode(-1);
		} else {
			HttpUtil.InstanceSend(Constants.TecQs, params, callback);
		}
	}

	/**
	 * 
	 * @param StuId
	 *            学生id
	 * @param classID
	 *            班级id
	 * @param className
	 *            班级名称
	 * @param Unid
	 *            单位id
	 * @param chapterID
	 *            章节id
	 * @param homeworkName
	 *            作业名称
	 */
	public void StuMakeSelf(int StuId, int classID, String className, int Unid,
			int chapterID, String homeworkName) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("UserJBH",
				BaseActivity.sp.getString("JiaoBaoHao", ""));
		params.addBodyParameter("StuId", String.valueOf(StuId));
		params.addBodyParameter("classID", String.valueOf(classID));
		params.addBodyParameter("className", className);
		params.addBodyParameter("Unid", String.valueOf(Unid));
		params.addBodyParameter("chapterID", String.valueOf(chapterID));
		params.addBodyParameter("homeworkName", homeworkName);
		params.addBodyParameter("schoolName", "");
		params.addBodyParameter("IsSys", "1");
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_StuMakeSelf);
		HttpUtil.InstanceSend(Constants.StuMakeSelf, params, callback);
	}

	/**
	 * 网络请求返回
	 * 
	 * @author admin
	 * 
	 */

	private class CallBack extends RequestCallBack<String> {
		private int typeMode;
		private StuErrorModel mStuErrorModel;

		// 请求失败
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				// 提示网络错误
				ToastUtil.showMessage(mContext, mContext.getResources()
						.getString(R.string.error_internet));
				// 处理错误
				dealResponseInfo("false", this.getUserTag());
				// 判断手机是否有网络
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else {
					// 重新连接服务器
					DialogUtil.getInstance().cannleDialog();
					switch ((Integer) this.getUserTag()) {
					// 获取年级列表
					case Constants.WORKOL_GetGradeList:
						// 获取作业列表
					case Constants.WORKOL_GetStuHWList:
						int code = arg0.getExceptionCode();
						// 判断服务器是否可连
						if (code != 200) {
							// 如果颗粒链接//通讯握手
							LoginActivityController.getInstance().helloService(
									mContext);
						}
						break;
					}
				}
			}
		}

		public void setTypeMode(int typeMode) {
			this.typeMode = typeMode;
		}

		public int getTypeMode() {
			return typeMode;
		}

		public void setGradeCode(int gradeCode) {
			dealResponseInfo("-1", this.getUserTag(), getTypeMode());
		}

		public void setSubCode(int subCode) {
			dealResponseInfo("-1", this.getUserTag(), getTypeMode());
		}

		public void setVerCode(int verCode) {
			dealResponseInfo("-1", this.getUserTag(), getTypeMode());
		}

		public void setChaCode(int chaCode) {
			dealResponseInfo("-1", this.getUserTag());
		}

		public void setStuErrorModel(StuErrorModel stuErrorModel) {
			mStuErrorModel = stuErrorModel;
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {
				switch ((Integer) this.getUserTag()) {
				// 年级列表
				case Constants.WORKOL_GetGradeList:
					// 作业列表
				case Constants.WORKOL_GetStuHWList:
					// 练习列表
				case Constants.WORKOL_GetStuHWListPage:
					// 判断章节是否有试题
				case Constants.WORKOL_TecQs:
					// 错题本
				case Constants.WORKOL_GetStuErr:
					dealResponseInfo(arg0.result, this.getUserTag());// 根据UserTag，处理返回来的数据
					break;
				// 错题详情
				case Constants.WORKOL_GetStuHWQs:
					dealResponseInfo(arg0.result, this.getUserTag(),// 根据userTag,处理返回来的数据
							mStuErrorModel);
					break;
				// 练习查询
				case Constants.WORKOL_StuMakeSelf:// {"ok":true,"stateCode":200,"stateMessage":"发送成功"}
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);//
						String statusCode = jsonObj.getString("stateCode");
						if (!TextUtils.isEmpty(statusCode)) {
							if ("200".equals(statusCode)) {
								// 数据返回成功
								if (jsonObj.getString("ok").equals("true")) {
									// 数据返回为ture
									dealResponseInfo(jsonObj.getString("ok"),
											this.getUserTag());
								} else {
									// 数据返回为false
									dealResponseInfo("okFalse",
											this.getUserTag());
								}
							} else {
								// 数据失败
								ToastUtil.showMessage(mContext,
										jsonObj.getString("stateMessage"));
								// 处理失败
								dealResponseInfo("false", this.getUserTag());
							}
						}
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					break;
				// 章节列表
				case Constants.WORKOL_GetUnionChapterList:
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);// 将数据转换成JSONObject
						String statusCode = jsonObj.getString("statusCode");// 获取状态信息
						if (!TextUtils.isEmpty(statusCode)) {
							if ("200".equals(statusCode)) {
								// 请求成功
								// 处理信息
								dealResponseInfo(arg0.result,
										this.getUserTag(), getTypeMode());
							} else {
								// 请求失败
								// 处理失败
								ToastUtil.showMessage(mContext,
										jsonObj.getString("message"));
								dealResponseInfo("false", this.getUserTag(),
										getTypeMode());
							}
						}
					} catch (Exception e) {
						// 数据类型不正确，处理失败
						dealResponseInfo("false", this.getUserTag(),
								getTypeMode());
						ToastUtil.showMessage(mContext, mContext.getResources()
								.getString(R.string.error_serverconnect)
								+ "r1002");
					}
					break;
				// 其他情况
				default:
					try {
						JSONObject jsonObj = new JSONObject(arg0.result);// {"ResultCode":0,"ResultDesc":"成功!","Data":"False"}
						String ResultCode = jsonObj.getString("ResultCode");
						if ("0".equals(ResultCode)) {
							String data = Des.decrypt(
									jsonObj.getString("Data"),
									BaseActivity.sp_sys.getString("ClientKey",
											""));
							dealResponseInfo(data, this.getUserTag());
						} else if ("8".equals(ResultCode)) {
							LoginActivityController.getInstance().helloService(
									mContext);
							dealResponseInfo("false", this.getUserTag());
						} else {
							ToastUtil.showMessage(mContext,
									jsonObj.getString("ResultDesc"));
							dealResponseInfo("false", this.getUserTag());
						}
					} catch (Exception e) {
						dealResponseInfo("", this.getUserTag());
						ToastUtil.showMessage(mContext, mContext.getResources()
								.getString(R.string.error_serverconnect)
								+ "r1002");
					}
					break;
				}

			}
		}
	}

	/**
	 * 处理信息并传递
	 * 
	 * @param result
	 * @param userTag
	 */
	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(userTag);
		switch ((Integer) userTag) {
		//
		case Constants.WORKOL_TecQs:
			// 如果结果是-1
			if (result.equals("-1")) {
				// 章节有试题
				result = "true";
			}
			post.add(result);
			break;
		// 发布练习，结果
		case Constants.WORKOL_StuMakeSelf:
			post.add(result);
			break;
		// 学生信息结果
		case Constants.WORKOL_getStuInfo:
			StuInfo genInfo = GsonUtil.GsonToObject(result, StuInfo.class);
			post.add(genInfo);
			break;
		// 年级列表结果
		case Constants.WORKOL_GetGradeList:
			ArrayList<TeaGrade> glist = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<TeaGrade>>() {
					}.getType());
			post.add(glist);
			break;
		// 学生作业列表结果
		case Constants.WORKOL_GetStuHWList:
			DialogUtil.getInstance().cannleDialog();
			ArrayList<StuHW> list = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<StuHW>>() {
					}.getType());
			post.add(list);
			break;
		// 学生练习列表结果
		case Constants.WORKOL_GetStuHWListPage:
			DialogUtil.getInstance().cannleDialog();
			ArrayList<StuHW> list2 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<StuHW>>() {
					}.getType());
			post.add(list2);
			break;
		// 学生错题本
		case Constants.WORKOL_GetStuErr:
			if (result != null) {
				ArrayList<StuErrorModel> stuErrs = GsonUtil.GsonToList(result,
						new TypeToken<ArrayList<StuErrorModel>>() {
						}.getType());
				post.add(stuErrs);
			}
			break;

		default:
			break;
		}
		EventBusUtil.post(post);
	}

	/**
	 * 处理错题本详情
	 * 
	 * @param result
	 * @param userTag
	 * @param stuErrorModel
	 */

	public void dealResponseInfo(String result, Object userTag,
			StuErrorModel stuErrorModel) {
		// TODO Auto-generated method stub
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(userTag);
		switch ((Integer) userTag) {
		// 错题本详情
		case Constants.WORKOL_GetStuHWQs:
			DialogUtil.getInstance().cannleDialog();
			StuErrDetailModel detailModel = GsonUtil.GsonToObject(result,
					StuErrDetailModel.class);
			post.add(detailModel);
			post.add(stuErrorModel);
			break;
		default:
			break;

		}
		EventBusUtil.post(post);
	}

	/**
	 * 获取 年级 章节 科目的数据
	 * 
	 * @param result
	 * @param userTag
	 * @param typeMode
	 */

	public void dealResponseInfo(String result, Object userTag, int typeMode) {
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constants.WORKOL_GetUnionChapterList:
			if (result.equals("-1")) {
				post.add(-1);
			} else {
				GetUnionChapterList list1 = null;
				try {
					list1 = GsonUtil.GsonToObject(result,
							GetUnionChapterList.class);
				} catch (Exception e1) {
				}
				if (list1 != null) {
					UnionChapterList d = new UnionChapterList();
					try {
						ArrayList<TeaMode> args1 = GsonUtil.GsonToList(
								list1.getArgs1(),
								new TypeToken<ArrayList<TeaMode>>() {
								}.getType());
						d.setArgs1(args1);
					} catch (Exception e) {
					}
					try {
						ArrayList<TeaSubject> args2 = GsonUtil.GsonToList(
								list1.getArgs2(),
								new TypeToken<ArrayList<TeaSubject>>() {
								}.getType());
						d.setArgs2(args2);
					} catch (Exception e) {
					}
					try {
						ArrayList<TeaSession> args3 = GsonUtil.GsonToList(
								list1.getArgs3(),
								new TypeToken<ArrayList<TeaSession>>() {
								}.getType());
						d.setArgs3(args3);
					} catch (Exception e) {
					}
					d.setArgs4(list1.getArgs4());
					d.setCallbackType(list1.getCallbackType());
					d.setForwardUrl(list1.getForwardUrl());
					d.setMessage(list1.getMessage());
					d.setNavTabId(list1.getNavTabId());
					d.setStatusCode(list1.getStatusCode());
					post.add(d);
				} else {
					post.add(null);
				}
			}
			post.add(typeMode);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);

	}
}
