package com.jsy_jiaobao.main.schoolcircle;

import java.util.ArrayList;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.actionbarsherlock.view.MenuItem;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.Photo;
import com.umeng.analytics.MobclickAgent;

/**
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
 * 相册列表
 */
public class UnitSpacePhotoActivity extends BaseActivity {

	private LinearLayout layout_body;
	private Context mContext;
	private String NameStr;
	private String GroupInfo;
	private int UnitID;
	private int TabID;
	private int CreateByjiaobaohao;
	// private ACache mCache;
	private GridView gridView;
	private UnitSpacePhotoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			TabID = savedInstanceState.getInt("TabID");
			GroupInfo = savedInstanceState.getString("TabID");
			UnitID = savedInstanceState.getInt("UnitID");
			CreateByjiaobaohao = savedInstanceState
					.getInt("CreateByjiaobaohao");
			NameStr = savedInstanceState.getString("NameStr");
		} else {
			initPass();
		}
		initViews();
		initDatas();
	}

	/**
	 * 获取Intent携带的数据
	 */
	private void initPass() {

		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				TabID = bundle.getInt("TabID");
				GroupInfo = bundle.getString("TabID");
				UnitID = bundle.getInt("UnitID");
				CreateByjiaobaohao = bundle.getInt("CreateByjiaobaohao");
				NameStr = bundle.getString("NameStr");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("NameStr", NameStr);
		outState.putString("TabID", GroupInfo);
		outState.putInt("UnitID", UnitID);
		outState.putInt("TabID", TabID);
		outState.putInt("CreateByjiaobaohao", CreateByjiaobaohao);
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		setContentLayout(R.layout.activity_unitspace_photo);
		layout_body = (LinearLayout) findViewById(R.id.unitspace_layout_body);
		mContext = this;
		mCache = ACache.get(getApplicationContext(), "chat");
		UnitSpaceExpActivityController.getInstance().setContext(this);
		setActionBarTitle(NameStr);
		gridView = new GridView(this);
		gridView.setNumColumns(3);
		layout_body.addView(gridView);
		adapter = new UnitSpacePhotoAdapter(mContext);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (getPgroupList.size() > 0) {
					MobclickAgent.onEvent(mContext, mContext.getResources()
							.getString(R.string.UnitSpacePhotoActivity_photo));
					Intent intent = new Intent(mContext,
							UnitSpacePhotoExpActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("position", position);
					bundle.putSerializable("photoList", getPgroupList);
					// 名字
					bundle.putString("NameStr", NameStr);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
	}

	/**
	 * 加载数据
	 */
	private void initDatas() {
		if (UnitID == 0) {
			UnitSpaceActivityController.getInstance().GetPhotoByGroup(
					CreateByjiaobaohao, GroupInfo);
		} else {
			UnitSpaceActivityController.getInstance().GetUnitPhotoByGroupID(
					UnitID, TabID);
		}
	}

	@Override
	public void onResume() {
		EventBusUtil.register(this);
		super.onResume();
	}

	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}

	private ArrayList<Photo> getPgroupList = new ArrayList<Photo>();

	/**
	 * EventBus 功能模块
	 * 
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_unitspace_GetUnitPhotoByGroupID:
			// 通过单位相册Id 获取的照片列表
			getPgroupList = (ArrayList<Photo>) list.get(1);
			adapter.setData(getPgroupList);
			adapter.notifyDataSetChanged();

			break;
		case Constant.msgcenter_unitspace_GetPhotoByGroup:
			// 通过相册Id 获取的照片列表
			getPgroupList = (ArrayList<Photo>) list.get(1);
			adapter.setData(getPgroupList);
			adapter.notifyDataSetChanged();

			break;
		default:
			break;
		}
	}

	/**
	 * 系统返回键
	 * 
	 * @功能 结束当前Activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ActionBar的选择事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return true;
	}
}
