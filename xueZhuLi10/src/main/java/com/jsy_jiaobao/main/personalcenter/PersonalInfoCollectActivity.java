package com.jsy_jiaobao.main.personalcenter;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;

/**
 * 类名：.class
 * 描述：
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */
public class PersonalInfoCollectActivity extends BaseActivity {
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.activity_personalinfocollect);
		mContext = this;
		setActionBarTitle(getResources().getString(R.string.function_personal_infocollect));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
