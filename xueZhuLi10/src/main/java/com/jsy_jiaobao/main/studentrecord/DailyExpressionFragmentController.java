package com.jsy_jiaobao.main.studentrecord;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sturecord.MsgSch;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class DailyExpressionFragmentController implements ConstantUrl {
	private static DailyExpressionFragmentController instance;
	private Fragment mcontext;
	private Context mContext;

	public static synchronized  DailyExpressionFragmentController getInstance() {
		if (instance == null) {
			instance = new DailyExpressionFragmentController();
		}
		return instance;
	}

	public DailyExpressionFragmentController setContext(Fragment noticeFragment) {
		mcontext = noticeFragment;
		mContext = noticeFragment.getActivity();
		return this;
	}

	/**
	 * 根据档案ID获取分学校未读数
	 */
	public void PackMsgSch(String DATA) {
		RequestParams params = new RequestParams();// CMD=BaseInfo&DATA=Uid|Recid|MsgType
		params.addBodyParameter("CMD", "PackMsgSch");
		params.addBodyParameter("DATA", DATA);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.sturecord_home_PackMsgSch_daily);
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
				StuRecordDataGet, params, callback);
	}

	/**
	 * 学生信息未读数
	 */
	public void StuMsgSch(String DATA) {
		RequestParams params = new RequestParams();// CMD=BaseInfo&DATA=Uid|Stuid|MsgType
		params.addBodyParameter("CMD", "StuMsgSch");
		params.addBodyParameter("DATA", DATA);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.sturecord_home_StuMsgSch_daily);
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
				StuRecordDataGet, params, callback);
	}

	/**
	 * 老师获取某学生的某项信息的基本信息
	 * 
	 * @param tag tag
	 */
	public void PackMsg(ArrayList<Object> tag, String DATA) {
		RequestParams params = new RequestParams();// Uid|Recid|MsgType|PageSize|CurPage|SchName
		params.addBodyParameter("CMD", "PackMsg");
		params.addBodyParameter("DATA", DATA);
		PackMsgCallBack callback = new PackMsgCallBack();
		callback.setUserTag(tag);
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
				StuRecordDataGet, params, callback);
	}

	private class PackMsgCallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mContext != null) {
				try {
					if (mcontext.isAdded() && !mcontext.isDetached()) {
						dealResponse("0", this.getUserTag());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (mcontext.isAdded() && !mcontext.isDetached()) {
				DialogUtil.getInstance().cannleDialog();
				dealResponse(arg0.result, this.getUserTag());
			}
		}

		private void dealResponse(String string, Object userTag) {
			ArrayList<Object> post = new ArrayList<>();
			post.add(Constant.sturecord_home_PackMsg_daily);
			MsgSch msgSch = GsonUtil.GsonToObject(string, MsgSch.class);
			post.add(msgSch);
			post.add(userTag);
			EventBusUtil.post(post);
		}
	}

	/**
	 * 返回第一页信息列表
	 * 
	 * @param tag tag
	 */
	public void StuMsg(ArrayList<Object> tag, String DATA) {
		RequestParams params = new RequestParams();// Uid|Stuid|MsgType|PageSize|CurPage
		params.addBodyParameter("CMD", "StuMsg");
		params.addBodyParameter("DATA", DATA);
		StuMsgCallBack callback = new StuMsgCallBack();
		callback.setUserTag(tag);
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST,
				StuRecordDataGet, params, callback);
	}

	private class StuMsgCallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mContext != null) {
				try {
					if (mcontext.isAdded() && !mcontext.isDetached()) {
						dealResponse("0", this.getUserTag());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);

				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (mcontext.isAdded() && !mcontext.isDetached()) {
				DialogUtil.getInstance().cannleDialog();
				dealResponse(arg0.result, this.getUserTag());
			}
		}

		private void dealResponse(String string, Object userTag) {
			ArrayList<Object> post = new ArrayList<>();
			post.add(Constant.sturecord_home_StuMsg_daily);
			MsgSch msgSch = GsonUtil.GsonToObject(string, MsgSch.class);
			post.add(msgSch);
			post.add(userTag);
			EventBusUtil.post(post);
		}
	}

	private class CallBack extends RequestCallBack<String> {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if (mContext != null) {
				try {
					if (mcontext.isAdded() && !mcontext.isDetached()) {
						dealResponseInfo("0", this.getUserTag());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (BaseUtils.isNetworkAvailable(mContext)) {
					ToastUtil.showMessage(mContext, R.string.phone_no_web);

				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (mcontext.isAdded() && !mcontext.isDetached()) {
				DialogUtil.getInstance().cannleDialog();
				dealResponseInfo(arg0.result, this.getUserTag());
			}
		}

	}

	private void dealResponseInfo(String result, Object userTag) {
		System.out.println("---daily" + result);
		ArrayList<Object> post = new ArrayList<>();
		MsgSch msgSch;
		switch ((Integer) userTag) {
		case Constant.sturecord_home_PackMsgSch_daily:
			post.add(Constant.sturecord_home_PackMsgSch_daily);
			msgSch = GsonUtil.GsonToObject(result, MsgSch.class);
			post.add(msgSch);
			break;
		case Constant.sturecord_home_StuMsgSch_daily:
			post.add(Constant.sturecord_home_StuMsgSch_daily);
			msgSch = GsonUtil.GsonToObject(result, MsgSch.class);
			post.add(msgSch);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}