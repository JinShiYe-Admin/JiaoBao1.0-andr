package com.jsy_jiaobao.main.affairs;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import com.jsy_jiaobao.customview.IEditText;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class WorkSendAgainActivity extends BaseActivity {

	@ViewInject(R.id.layout_gridview)private GridView gridView;
	@ViewInject(R.id.article_edt_mywords)IEditText edt_keywords;
	@ViewInject(R.id.article_btn_send)Button btn_reply;
	private String readlist;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			readlist = savedInstanceState.getString("readlist");
		}
		initPassData();
		initViews();
	}

	private void initPassData() {
		Intent getPass = getIntent();
		if (getPass != null) {
			Bundle bundle = getPass.getExtras();
			if (bundle != null) {
				readlist = bundle.getString("readlist");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("readlist", readlist);
	}
	private void initViews() {
		setContentLayout(R.layout.activity_worksendagain);
		ViewUtils.inject(this);
		Context mContext;
		mContext =  this;
		setActionBarTitle(R.string.receiver_details);
		WorkSendAgainListAdapter adapter;
		adapter = new WorkSendAgainListAdapter(mContext);
		try {
			JSONArray readerArray = new JSONArray(readlist);
			adapter.setData(readerArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		gridView.setAdapter(adapter);
		
		edt_keywords.setHint(R.string.please_input_message_content);
		btn_reply.setText(R.string.send);
	}
}
