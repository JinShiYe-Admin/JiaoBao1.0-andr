package com.jsy_jiaobao.main.affairs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.view.SubMenu;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.PublicMethod;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnit;
import com.jsy_jiaobao.po.personal.CommMsgRevicerUnitClass;

import java.util.ArrayList;

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
 * 下级单位
 */
public class WorkJuniorUnitListActivity extends BaseActivity implements PublicMethod{
	public static String TAG = "WorkJuniorUnitListActivity";
	private Context mContext;
	private ArrayList<Object> tag;
	ArrayList<Object> resulttag = new ArrayList<>();
	private Object checked;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			tag = (ArrayList<Object>) savedInstanceState.getSerializable("tag");
		}else{
			initPassData();
		}
		initViews();
		initListener();
	}
	@SuppressWarnings("unchecked")
	@Override
	public void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				tag = (ArrayList<Object>) bundle.getSerializable("tag");
			}
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("tag", tag);
	}
	@Override
	public void initViews() {
		mContext = this;
		ScrollView body = new ScrollView(mContext);
		RadioGroup radioGroup = new RadioGroup(mContext);
		radioGroup.setPadding(20, 0, 20, 0);
		try {
			ArrayList<CommMsgRevicerUnit> RevicerUnit = (ArrayList<CommMsgRevicerUnit>) tag.get(0);
			for (int i = 0; i < RevicerUnit.size(); i++) {
				RadioButton item = new RadioButton(mContext);
				item.setText(RevicerUnit.get(i).getUintName());
				item.setTag(RevicerUnit.get(i));
				radioGroup.addView(item);
			}
		} catch (Exception e) {
			ArrayList<CommMsgRevicerUnitClass> UnitClass = (ArrayList<CommMsgRevicerUnitClass>) tag.get(0);
			for (int i = 0; i < UnitClass.size(); i++) {
				RadioButton item = new RadioButton(mContext);
				item.setText(UnitClass.get(i).getClsName());
				item.setTag(UnitClass.get(i));
				radioGroup.addView(item);
			}
		}
		String name = (String) tag.get(2);
		for (int i = 0; i < radioGroup.getChildCount(); i++) {
			RadioButton item = (RadioButton) radioGroup.getChildAt(i);
			if (item.getText().equals(name)) {
				item.setChecked(true);
				checked = item.getTag();
			}
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton select = (RadioButton) findViewById(checkedId);
				checked = select.getTag();
			}
		});
		
		body.addView(radioGroup);
		setContentLayout(body);
		setActionBarTitle(R.string.choose_unit);
	}
	@Override
	public void initDeatilsData() {
	}
	@Override
	public void initListener() {
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       
        SubMenu sub_search = menu.addSubMenu(R.string.sure).setIcon(R.drawable.abs__ic_cab_done_holo_dark);
        sub_search.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        sub_search.getItem().setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent result = new Intent(mContext,WorkSendToSbActivity.class);
				resulttag.add(0,checked);
				resulttag.add(tag.get(1));
				result.putExtra("tag", resulttag);
				setResult(0,result);
	        	finish();
				return false;
			}
		});
        return true;
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
        	finish();		
		}
		return super.onKeyDown(keyCode, event);
	}
}
