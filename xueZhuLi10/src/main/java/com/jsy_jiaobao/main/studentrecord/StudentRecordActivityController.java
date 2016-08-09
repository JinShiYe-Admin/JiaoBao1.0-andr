package com.jsy_jiaobao.main.studentrecord;

import android.app.Activity;
import android.content.Context;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

public class StudentRecordActivityController implements ConstantUrl{
	private static StudentRecordActivityController instance;
	private Activity mcontext;
	private Context mContext;
	public static synchronized  StudentRecordActivityController getInstance() {
		if (instance == null) {
			instance = new StudentRecordActivityController();
		}
		return instance;
	}

	public StudentRecordActivityController setContext(Activity pActivity) {
		mcontext = pActivity;
		mContext=pActivity;
		return this;
	}
	
	public void BaseInfo (){
		String DATA = BaseActivity.sp.getString("JiaoBaoHao", "")+"|"+BaseActivity.sp.getInt("UnitType", 1)+"|"+BaseActivity.sp.getInt("UnitID", 0);
		RequestParams params = new RequestParams();//CMD=BaseInfo&DATA=5150001|2|991
		params.addBodyParameter("CMD","BaseInfo");
		params.addBodyParameter("DATA",DATA);
		CallBack callback = new CallBack();
		callback.setUserTag(Constant.sturecord_home_BaseInfo);
		HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, StuRecordDataGet, params, callback);
	}
	private class CallBack extends RequestCallBack<String>{

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			if(mContext!=null){
				if (!mcontext.isFinishing()) {
					DialogUtil.getInstance().cannleDialog();
				}
				if(BaseUtils.isNetworkAvailable(mContext)){
					ToastUtil.showMessage(mContext,R.string.phone_no_web);
				}
			}
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			if (!mcontext.isFinishing()) {
				DialogUtil.getInstance().cannleDialog();
				dealResponseInfo(arg0.result,this.getUserTag());
			}
		}
	}

	private void dealResponseInfo(String result, Object userTag) {
		ArrayList<Object> post = new ArrayList<>();
		switch ((Integer)userTag) {
		case Constant.sturecord_home_BaseInfo:
			post.add(Constant.sturecord_home_BaseInfo);
			com.jsy_jiaobao.po.sturecord.BaseInfo baseInfo = GsonUtil.GsonToObject(result,com.jsy_jiaobao.po.sturecord.BaseInfo.class);
			post.add(baseInfo);
			break;
		default:
			break;
		}
		EventBusUtil.post(post);
	}
}