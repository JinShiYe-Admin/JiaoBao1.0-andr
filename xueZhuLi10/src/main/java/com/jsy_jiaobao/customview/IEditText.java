package com.jsy_jiaobao.customview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

public class IEditText extends EditText {
	public IEditText(Context context) {
		super(context);
	}

	public IEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public String getTextString() {
		String str = super.getText().toString();
		if (TextUtils.isEmpty(str.trim())) {
			return "";
		}else{
			return slect(str);
		}
	}
	private String slect(String content){
		if (!TextUtils.isEmpty(content)) {
			if (content.startsWith(" ")) {
				content = content.substring(1);
				content = slect(content);
			}
			if (content.endsWith(" ")) {
				content = content.substring(0,content.length()-1);
				content = slect(content);
			}
		}
		return content;
			
	}
}
