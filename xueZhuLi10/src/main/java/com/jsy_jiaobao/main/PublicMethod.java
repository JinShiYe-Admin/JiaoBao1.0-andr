package com.jsy_jiaobao.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;

/**
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
public interface PublicMethod {
	/**
	 * 加载传递过来的数据
	 */
	public void initPassData();
	/**
	 * 初始化所有的控件
	 */
	public void initViews();
	/**
	 * 加载本页所需要的数据
	 */
	public void initDeatilsData();
	/**
	 * 注册所有的监听器
	 */
	public void initListener();
}
