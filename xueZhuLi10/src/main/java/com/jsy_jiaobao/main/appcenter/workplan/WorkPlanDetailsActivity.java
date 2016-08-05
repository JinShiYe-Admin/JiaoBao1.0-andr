package com.jsy_jiaobao.main.appcenter.workplan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.app.workplan.GetWorkPlanInfo;
import com.jsy_jiaobao.po.app.workplan.WorkPlanInfo;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONObject;

import java.util.List;

public class WorkPlanDetailsActivity extends BaseActivity {

	@ViewInject(R.id.workplan_listview)ListView listView;
	@ViewInject(R.id.workplan_tv_time)TextView tv_selectedtime;
//	@ViewInject(R.id.workplan_sp_unit)Spinner sp_unit;
	
	private String str_selecttime;
	private Context mContext;
	private WorkPlanListAdapter adapter;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			str_selecttime = savedInstanceState.getString("selecttime");
		}else{
			Intent getTime = getIntent();
			if (getTime != null) {
				Bundle bundle = getTime.getExtras();
				if (bundle != null) {
					str_selecttime = bundle.getString("selecttime");
				}
			}
		}
		setContentLayout(R.layout.ui_workplandetails);
		ViewUtils.inject(this);
		mContext = this;
		sp = getSharedPreferences(Constant.SP_TB_USER, MODE_PRIVATE);
		adapter = new WorkPlanListAdapter(mContext);
		listView.setAdapter(adapter);
		initParentView();
		getWorkPlan();
	}
	private void initParentView(){
		
		setActionBarTitle("日程记录");
		
		tv_selectedtime.setText("已选时间:"+str_selecttime);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("selecttime", str_selecttime);
	}
	/**
	 * 查询日程
	 */
	private void getWorkPlan() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("UnitID", sp.getInt("UnitID", 0)+"");
		params.addBodyParameter("UserID", sp.getInt("UserID", 0)+"");
		params.addBodyParameter("WorkPlanDate", str_selecttime);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, ACache.get(mContext.getApplicationContext()).getAsString("RiCUrl")+selectWorkPlanDay, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				BaseUtils.shortToast(mContext, getResources().getString(R.string.error_serverconnect));
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					JSONObject jsonObj = new JSONObject(arg0.result);
					String ResultCode = jsonObj.getString("ResultCode");
					if ("0".equals(ResultCode)) {
						GetWorkPlanInfo getWorkPlanList = GsonUtil.GsonToObject(arg0.result, GetWorkPlanInfo.class);
						List<WorkPlanInfo> list_workplan = getWorkPlanList.getData();
						adapter.setListData(list_workplan);
						adapter.notifyDataSetChanged();
					}else{
						BaseUtils.shortToast(mContext, "获取失败");
					}
				} catch (Exception e) {
					BaseUtils.shortToast(mContext, "获取失败1002");
				}
			}
		});
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
