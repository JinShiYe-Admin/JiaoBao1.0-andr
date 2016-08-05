package com.jsy.xuezhuli.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtil {

	private static DialogUtil instance;
	private ProgressDialog dialog;

	public static final DialogUtil getInstance() {

		if (instance == null) {
			instance = new DialogUtil();
		}
		return instance;
	}

	public boolean isDialogShowing(){
		return dialog ==null ?false:dialog.isShowing();
	}
	public void getDialog(Context pContext, int id) {
		String pMessage=pContext.getResources().getString(id);
		try {
			if (dialog != null) {
				if (!dialog.isShowing()) {
					dialog = new ProgressDialog(pContext);
					dialog.setMessage(pMessage);
					dialog.show();
				}
			}else{
				dialog = new ProgressDialog(pContext);
				dialog.setMessage(pMessage);
				dialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getDialog(Context pContext, String pMessage) {
		try {
			if (dialog != null) {
				if (!dialog.isShowing()) {
					dialog = new ProgressDialog(pContext);
					dialog.setMessage(pMessage);
					dialog.show();
				}
			}else{
				dialog = new ProgressDialog(pContext);
				dialog.setMessage(pMessage);
				dialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCanCancel(boolean can){
		try {
			if (dialog != null) {
				dialog.setCancelable(can);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void cannleDialog() {
		try {
			if (dialog != null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/*	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			cannleDialog();
		}
		
	};*/
}
