package com.jsy_jiaobao.main.workol;

import java.util.ArrayList;
import org.json.JSONObject;

import android.content.Context;
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
import com.jsy_jiaobao.po.sys.GenInfo;
import com.jsy_jiaobao.po.workol.EduLevel;
import com.jsy_jiaobao.po.workol.HWStatus;
import com.jsy_jiaobao.po.workol.StuErrDetailModel;
import com.jsy_jiaobao.po.workol.StuErrorModel;
import com.jsy_jiaobao.po.workol.StuHW;
import com.jsy_jiaobao.po.workol.StudentErrorPost;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.qiniu.android.utils.StringUtils;

/**
 * 家长查看 请求信息
 * 
 * @author admin
 * 
 */
public class GenCheckWorkActivityController implements ConstantUrl {
	int mUid = 0, mChapterid = 0;
	private static GenCheckWorkActivityController instance;
	private Context mContext;

	public static synchronized final GenCheckWorkActivityController getInstance() {
		if (instance == null) {
			instance = new GenCheckWorkActivityController();
		}
		return instance;
	}

	public GenCheckWorkActivityController setContext(Context pActivity) {
		mContext = pActivity;
		return this;
	}

	/**
	 * <pre>
	 * 功能说明：应用系统通过帐户ID和班级ID，获取帐户指定班级的家长数据（个人信息）
	 * 参数名称	是否必须	类型	描述
	 * AccID	是	int	用户帐户ID
	 * UID	          是	int	班级ID，通过获取身份角色信息获得，用户身份须为家长。
	 */
	public void getGenInfo(String jiaobaohao, int UID) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("AccID", jiaobaohao);
		params.addBodyParameter("UID", String.valueOf(UID));
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_getGenInfo);
		HttpUtil.InstanceSend(getGenInfo, params, callback);
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
		params.addBodyParameter("PageSize", String.valueOf(PageSize));
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_GetStuHWListPage);
		HttpUtil.InstanceSend(Constants.GetStuHWListPage, params, callback);
	}

	/**
	 * 获取作业练习列表
	 * 
	 * @param stuErrorModel
	 * @param StuId
	 * @param IsSelf
	 * @param PageIndex
	 * @param PageSize
	 */
	public void GetStuHWQs(int HwInfoId, int QsId, StuErrorModel stuErrorModel) {

		RequestParams params = new RequestParams();
		params.addBodyParameter("HwInfoId", String.valueOf(HwInfoId));
		params.addBodyParameter("QsId", String.valueOf(QsId));
		CallBack callback = new CallBack();
		callback.setStuErrorModel(stuErrorModel);
		callback.setUserTag(Constants.WORKOL_GetStuHWQs);
		HttpUtil.InstanceSend(Constants.GetStuHWQs, params, callback);
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
	 * 获取某学生各科作业完成情况 参数：
	 * 
	 * @param StuId
	 *            :学生ID
	 */
	public void GetCompleteStatusHW(int StuId) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		params.addBodyParameter("StuId", String.valueOf(StuId));
		CallBack callback = new CallBack();
		callback.setUserTag(Constants.WORKOL_GetCompleteStatusHW);
		HttpUtil.InstanceSend(Constants.GetCompleteStatusHW, params, callback);
	}

	/**
	 * <pre>
	 *  功能：	获取学生某各科目学力值，
	 * 		    	获取学生某科目各章学力值，
	 * 				获取学生某章各节学力值
	 * 参数名称	是否必须	类型	描述
	 * StuId	是	int	学生ID
	 * uId	否	int	Unid（教版科目ID）
	 * chapterid	否	int	章节ID
	 */

	public void GetStuEduLevel(int StuId, int uId, int chapterid) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		CallBack callback = new CallBack();
		if (chapterid > 0) {
			params.addBodyParameter("StuId", String.valueOf(StuId));
			params.addBodyParameter("uId", String.valueOf(uId));
			params.addBodyParameter("chapterid", String.valueOf(chapterid));
			callback.setEduLevelParentID("-|" + uId + "|" + chapterid);
		} else if (uId > 0) {
			params.addBodyParameter("StuId", String.valueOf(StuId));
			params.addBodyParameter("uId", String.valueOf(uId));
			callback.setEduLevelParentID("-|" + uId);
		} else {
			params.addBodyParameter("StuId", String.valueOf(StuId));
			callback.setEduLevelParentID("-");
		}
		callback.setUserTag(Constants.WORKOL_GetStuEduLevel);
		// 传送数据 ，请求数据
		HttpUtil.InstanceSend(Constants.GetStuEduLevel, params, callback);

	}

	// 4级及以上菜单
	public void GetStuEduLevel(int StuId, int uId, int[] chapterid) {
		DialogUtil.getInstance().getDialog(mContext,
				mContext.getResources().getString(R.string.public_loading));
		RequestParams params = new RequestParams();
		CallBack callback = new CallBack();
		params.addBodyParameter("StuId", String.valueOf(StuId));
		params.addBodyParameter("uId", String.valueOf(uId));
		// 获取多级id
		String[] chapterString = new String[chapterid.length];
		for (int i = 0; i < chapterid.length; i++) {
			chapterString[i] = String.valueOf(chapterid[i]);
		}
		String chapteridString = StringUtils.join(chapterString, "|");
		// 父章节的id
		params.addBodyParameter("chapterid",
				chapterString[chapterString.length - 1]);
		callback.setEduLevelParentID("-|" + uId + "|" + chapteridString);
		String sssparentId = "-|" + uId + "|" + chapteridString;
		Log.d("ParentId", sssparentId);
		callback.setUserTag(Constants.WORKOL_GetStuEduLevel);
		// 传送数据 ，请求数据
		HttpUtil.InstanceSend(Constants.GetStuEduLevel, params, callback);

	}

	/**
	 * 返回状况，及返回信息的处理
	 * 
	 * @author admin
	 * 
	 */

	private class CallBack extends RequestCallBack<String> {
		private String eduLevelParentID;
		private StuErrorModel mStuErrorModel;

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (null != mContext) {
				dealResponseInfo("false", this.getUserTag());
				DialogUtil.getInstance().cannleDialog();
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				} else {
					ToastUtil.showMessage(mContext, R.string.error_internet);
				}

			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (null != mContext) {

				switch ((Integer) this.getUserTag()) {
				// 作业列表
				case Constants.WORKOL_GetStuHWList:
					// 练习列表
				case Constants.WORKOL_GetStuHWListPage:
					// 完成状况
				case Constants.WORKOL_GetCompleteStatusHW:
					// 错题本
				case Constants.WORKOL_GetStuErr:
					dealResponseInfo(arg0.result, this.getUserTag());
					break;
				// 所选章节下是否有试题
				case Constants.WORKOL_GetStuHWQs:
					dealResponseInfo(arg0.result, this.getUserTag(),
							mStuErrorModel);
					break;
				// 学力值信息
				case Constants.WORKOL_GetStuEduLevel:
					dealResponseInfo(arg0.result, this.getUserTag(),
							this.getEduLevelParentID());
					break;

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

		public String getEduLevelParentID() {
			return eduLevelParentID;
		}

		public void setEduLevelParentID(String eduLevelParentID) {
			this.eduLevelParentID = eduLevelParentID;
		}

		public void setStuErrorModel(StuErrorModel stuErrorModel) {
			mStuErrorModel = stuErrorModel;
		}

	}

	/**
	 * 处理信息
	 * 
	 * @param result
	 * @param userTag
	 */

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constants.WORKOL_getGenInfo:
			GenInfo genInfo = GsonUtil.GsonToObject(result, GenInfo.class);
			post.add(genInfo);
			break;

		case Constants.WORKOL_GetStuHWListPage:
		case Constants.WORKOL_GetStuHWList:
			ArrayList<StuHW> list = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<StuHW>>() {
					}.getType());
			post.add(list);
			break;
		case Constants.WORKOL_GetCompleteStatusHW:
			ArrayList<HWStatus> list1 = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<HWStatus>>() {
					}.getType());
			post.add(list1);
			break;
		case Constants.WORKOL_GetStuEduLevel:
			ArrayList<EduLevel> level = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<EduLevel>>() {
					}.getType());
			post.add(level);
			break;
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
		case Constants.WORKOL_GetStuHWQs:
			if (result != null) {
				StuErrDetailModel detailModel = GsonUtil.GsonToObject(result,
						StuErrDetailModel.class);
				post.add(detailModel);
				post.add(stuErrorModel);
			}
			break;
		default:
			break;
		}
		EventBusUtil.post(post);

	}

	/**
	 * 处理学力值
	 * 
	 * @param result
	 * @param userTag
	 * @param parentID
	 */

	private void dealResponseInfo(String result, Object userTag, String parentID) {
		ArrayList<Object> post = new ArrayList<Object>();
		post.add(userTag);
		switch ((Integer) userTag) {
		case Constants.WORKOL_GetStuEduLevel:
			// 获取各章节学力值信息
			ArrayList<EduLevel> level = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<EduLevel>>() {
					}.getType());
			//
			if (level != null) {
				for (EduLevel item : level) {
					item.setParentID(parentID);
					item.setLevelID(parentID + "|" + item.getID());
					Log.d("ParentId", parentID);
					String[] tags = parentID.split("\\|");
					String[] itemTags = item.getLevelID().split("\\|");
					item.setPadding(tags.length);

					System.out.println("-----padding:" + tags.length);
					// 获取下级菜单
					if (itemTags.length == 3) {
						mUid = Integer.parseInt(itemTags[1]);
						mChapterid = Integer.parseInt(itemTags[2]);
					} else if (itemTags.length == 2) {
						mUid = Integer.parseInt(itemTags[1]);
						mChapterid = 0;
					} else if (itemTags.length == 1) {
						mUid = 0;
						mChapterid = 0;
					} else if (itemTags.length > 3) {
						// 四级级以上
						mUid = Integer.parseInt(itemTags[1]);
						mChapterid = Integer
								.parseInt(itemTags[itemTags.length - 1]);
					}
					RequestParams params = new RequestParams();
					params.addBodyParameter("StuId",
							String.valueOf(GenCheckWorkActivity.StuId));
					params.addBodyParameter("uId", String.valueOf(mUid));
					params.addBodyParameter("chapterid",
							String.valueOf(mChapterid));
					CallBack1 requestCallBack = new CallBack1();
					requestCallBack.setUserTag(Constants.WORKOL_GetStuEduLevel);
					requestCallBack.setItem(item);
					HttpUtil.InstanceSend(Constants.GetStuEduLevel, params,
							requestCallBack);
				}
			}

			post.add(level);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}

	/**
	 * 获取学力值的下级的下级数据
	 * 
	 * @author admin
	 * 
	 */
	private class CallBack1 extends RequestCallBack<String> {
		private EduLevel item;

		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {
			String result = responseInfo.result;
			dealResponseInfo1(result, this.getUserTag(), item.getLevelID());
		}

		@Override
		public void onFailure(HttpException e, String s) {

		}

		public void setItem(EduLevel item) {
			this.item = item;
		}

		private void dealResponseInfo1(String result, Object userTag,
				String parentID) {

			ArrayList<EduLevel> level = GsonUtil.GsonToList(result,
					new TypeToken<ArrayList<EduLevel>>() {
					}.getType());
			ArrayList<Object> post = new ArrayList<Object>();
			if (level == null || level.size() == 0) {
				Log.e("result", result);
				item.setHaveChild(false);

				post.add(Constants.WORKOL_Notify_nochild);

			} else {
				for (EduLevel item : level) {
					item.setParentID(parentID);
					item.setLevelID(parentID + "|" + item.getID());
					String[] tags = parentID.split("\\|");
					item.setPadding(tags.length);
				}

				post.add(level);
			}
			EventBusUtil.post(post);
		}
	}

	//
}
