package com.jsy.xuezhuli.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class WatchUtils {
	public static void edtviewAddWarcher(final Context context,
			EditText editText, final int size, final TextView tv_left) {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int left=size-s.length();
				tv_left.setText("还剩"+left+"字");
				if(left==0){
					ToastUtil.showMessage(context,"字数已达到" + size + "字限制");
				}
			}
		});
	}
}
