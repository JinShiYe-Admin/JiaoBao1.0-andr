package com.jsy_jiaobao.main.studentrecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsy_jiaobao.main.R;
import com.lidroid.xutils.ViewUtils;


public class NewUpFragment extends Fragment {

	public static NewUpFragment newInstance() {
		NewUpFragment fragment = new NewUpFragment();
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.layout_studentrecord, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		ViewUtils.inject(this,view);
		super.onViewCreated(view, savedInstanceState);
	}
}