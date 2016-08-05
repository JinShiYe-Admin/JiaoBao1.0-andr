package com.jsy_jiaobao.main.schoolcircle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;

import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.HanyuPinyin;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.PublicMethod;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.NoticeGetUnitClass;
import com.jsy_jiaobao.po.personal.NoticeGetUnitInfo;
import com.jsy_jiaobao.po.personal.UnitClass;
import com.jsy_jiaobao.po.personal.UnitInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 下级单位列表界面
 * 
 * @author admin
 * 
 */
public class NoticeJuniorListActivity extends BaseActivity implements
		PublicMethod {
	@ViewInject(R.id.layout_listview)
	private ListView listView;
	private NoticeUnitsAdapter listAdapter;// 列表Adapter
	private Context mContext;
	private int UnitID = 0;// 单位Id
	private int UnitType = 0;// 1教育局2学校3班级

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			UnitID = savedInstanceState.getInt("UnitID");
			UnitType = savedInstanceState.getInt("UnitType");
		} else {
			initPassData();
		}
		initViews();
		initListener();
	}

	/**
	 * 获取Intent携带的信息
	 */
	@Override
	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				UnitID = bundle.getInt("UnitID");
				UnitType = bundle.getInt("UnitType");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("UnitID", UnitID);
		outState.putInt("UnitType", UnitType);
	}

	/**
	 * 加载界面
	 */
	@Override
	public void initViews() {
		setContentLayout(R.layout.layout_listview);
		ViewUtils.inject(this);
		mContext = this;
		NoticeJuniorListActivityController.getInstance().setContext(this);
		listAdapter = new NoticeUnitsAdapter(mContext, 0);
		listView.setAdapter(listAdapter);
		if (UnitType == 1) {
			setActionBarTitle(R.string.lower_unit);
			NoticeJuniorListActivityController.getInstance().getMySubUnitInfo(
					UnitID);
		} else {
			setActionBarTitle(R.string.all_class);
			NoticeJuniorListActivityController.getInstance()
					.getSchoolClassInfo(UnitID);
		}
	}

	@Override
	public void initDeatilsData() {
	}

	@Override
	public void initListener() {
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

	// 单位信息排序
	HanyuPinyin hanYu = new HanyuPinyin();
	Comparator<UnitInfo> comparator = new Comparator<UnitInfo>() {
		public int compare(UnitInfo s1, UnitInfo s2) {
			String s1name = hanYu.getStringPinYin(s1.getUintName());
			String s2name = hanYu.getStringPinYin(s2.getUintName());
			// 按姓名排序
			if (!s1name.equals(s2name)) {
				return s1name.compareTo(s2name);
			} else {
				// 姓名也相同则按学号排序
				return s1.getArtUpdate() - s2.getArtUpdate();
			}
		}
	};
	// 单位班级排序
	Comparator<UnitClass> comparator1 = new Comparator<UnitClass>() {
		public int compare(UnitClass s1, UnitClass s2) {
			String s1name = hanYu.getStringPinYin(s1.getClsName());
			String s2name = hanYu.getStringPinYin(s2.getClsName());
			// 按姓名排序
			if (!s1name.equals(s2name)) {
				return s1name.compareTo(s2name);
			} else {
				// 姓名也相同则按学号排序
				return s1.getArtUpdate() - s2.getArtUpdate();
			}
		}
	};

	/**
	 * EventBus 功能组件
	 * 
	 * @功能 获取信息并处理
	 * @param list
	 */
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list) {
		int tag = (Integer) list.get(0);
		switch (tag) {
		case Constant.msgcenter_notice_getMySubUnitInfo:// 下级单位
			NoticeGetUnitInfo getMySubUnitInfo = (NoticeGetUnitInfo) list
					.get(1);
			List<UnitInfo> list1 = getMySubUnitInfo.getList();
			Collections.sort(list1, comparator);
			listAdapter.setData(list1);
			listAdapter.notifyDataSetChanged();
			break;
		case Constant.msgcenter_notice_getSchoolClassInfo:// 获取指定学校的所有班级基础数据
			NoticeGetUnitClass getUnitClass = (NoticeGetUnitClass) list.get(1);
			List<UnitClass> classList = getUnitClass.getList();
			Collections.sort(classList, comparator1);
			listAdapter.setData(classList);
			listAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	/**
	 * 系统返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
