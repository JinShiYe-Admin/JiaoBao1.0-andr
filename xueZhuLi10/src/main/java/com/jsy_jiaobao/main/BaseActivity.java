package com.jsy_jiaobao.main;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * <pre>
 *                   _ooOoo_
 *                  o8888888o
 *                  88" . "88
 *                  (| -_- |)
 *                  O\  =  /O
 *               ____/`---'\____
 *             .'  \\|     |//  `.
 *            /  \\|||  :  |||//  \
 *           /  _||||| -:- |||||-  \
 *           |   | \\\  -  /// |   |
 *           | \_|  ''\---/''  |   |
 *           \  .-\__  `-`  ___/-. /
 *         ___`. .'  /--.--\  `. . __
 *      ."" '<  `.___\_<|>_/___.'  >'"".
 *     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *     \  \ `-.   \_ __\ /__ _/   .-` /  /
 *======`-.____`-.___\_____/___.-`____.-'======
 *                   `=---='
 *^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *         		    佛祖保佑       永无BUG
 */
public class BaseActivity extends SherlockFragmentActivity implements ConstantUrl{
	private Context mContext;
	LinearLayout ly_content;
	// 内容区域的布局
	private View contentView;

	public static SharedPreferences sp_sys,sp;
	public static Editor editor;
	public static ACache mCache,artCache;
//	DefaultHttpClient httpClient;
	
	private TextView title;
	public ImageView back;
	public static ArrayList<Object> HttpPost = new ArrayList<Object>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_base);
		JSYApplication.getInstance().addActivity(this);
		mContext = this;
		sp_sys = getSharedPreferences("sp_tb_sys", MODE_PRIVATE);
		sp = getSharedPreferences("sp_tb_user", MODE_PRIVATE);
		editor = sp.edit();
		mCache = ACache.get(getApplicationContext(), "worksend");
		artCache = ACache.get(mContext, "art");
		ly_content = (LinearLayout) findViewById(R.id.base_body_content);
	
		getSupportActionBar().show();
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getSupportActionBar().setCustomView(R.layout.actionbar);
		title = (TextView) findViewById(R.id.actionbar_title);
		back = (ImageView) findViewById(R.id.actionbar_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResultForLastActivity();
				finish();
			}
		});
		title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				finish();
			}
		});
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	public void setActionBarTitle(int id){
		String titlestr=getResources().getString(id);
		title.setText(titlestr);
	}
	public void setActionBarTitle(String titlestr){
		title.setText(titlestr);
	}
	public TextView getActionBarTitleView(){
		return title;
	}
	public ImageView getActionBarBackView(){
		return back;
	}
	public void setResultForLastActivity() {
		
	}
	/***
	 * 设置内容区域
	 * 
	 * @param resId
	 *            资源文件ID
	 */
	public void setContentLayout(int resId) {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contentView = inflater.inflate(resId, null);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		contentView.setLayoutParams(layoutParams);
		contentView.setBackgroundDrawable(null);
		if (null != ly_content) {
			ly_content.addView(contentView);
		}

	}

	/***
	 * 设置内容区域
	 * 
	 * @param view
	 * View对象
	 */
	public void setContentLayout(View view) {
		if (null != ly_content) {
			ly_content.addView(view);
		}
	}
	

	/**
	 * 得到内容的View
	 * 
	 * @return
	 */
	public View getLyContentView() {

		return contentView;
	}
	@Override
	protected void onResume() {
		super.onResume(); 
		if (HttpPost == null) {
			HttpPost = new ArrayList<Object>();
		}else{
			HttpPost.clear();
		}
		System.out.println("------------------onResume");
		sp_sys = getSharedPreferences("sp_tb_sys", MODE_PRIVATE);
		sp = getSharedPreferences("sp_tb_user", MODE_PRIVATE);
		editor = sp.edit();
		mCache = ACache.get(getApplicationContext(), "worksend");
		artCache = ACache.get(mContext, "art");
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
//			if((System.currentTimeMillis()-exitTime) > 2000){  
//				Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();                                
//				exitTime = System.currentTimeMillis();   
//			} else {
//				System.exit(0);
//			}
//            return true;   
//		}
//        return super.onKeyDown(keyCode, event);
		return false;
	}
	public void httpLogout(){
		try {
			HttpUtil.getInstance().send(HttpRequest.HttpMethod.POST, ConstantUrl.user_logout, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
//					BaseUtils.shortToast(mContext,getResources().getString(R.string.error_serverconnect));
				}
				
				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					
//					try {
//						JSONObject jsonObj = new JSONObject(arg0.result);
//						String ResultCode = jsonObj.getString("ResultCode");
//						
//						if ("0".equals(ResultCode)) {
//							BaseUtils.shortToast(mContext,jsonObj.getString("ResultDesc"));
//							finish();
//						} else {
//							BaseUtils.shortToast(mContext,jsonObj.getString("ResultDesc"));
//						}
//					} catch (Exception e) {
//						BaseUtils.shortToast(mContext, getResources().getString(R.string.login_error_login)+"r1002");
//					} 
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onDestroy() {
		JSYApplication.getInstance().removeActivity(this);
		super.onDestroy();
	}
	/**
	 * 重写TouchEvent
	 * 控制软键盘
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	        View v = getCurrentFocus();
	        if ( v instanceof EditText) {
	            Rect outRect = new Rect();
	            v.getGlobalVisibleRect(outRect);
	            if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
	                v.clearFocus();
	                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	            }
	        }
	    }
	    return super.dispatchTouchEvent( event );
	}

}
