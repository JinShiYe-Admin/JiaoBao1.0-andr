package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.CommMsg;
import com.jsy_jiaobao.po.personal.FeeBack;
import com.lidroid.xutils.BitmapUtils;

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
public class WorkListAdapter extends BaseAdapter {
	private Context mContext;
	private List<CommMsg> list= null;
	private List<FeeBack> list2=null;
	private boolean fromme = false;
	private SharedPreferences sp;
	private String jiaobaohao;
	private BitmapUtils bitmap;
	private String mainURL;
	public WorkListAdapter(Context mContext){
		this.mContext = mContext;
		mainURL = ACache.get(mContext.getApplicationContext()).getAsString("MainUrl");
		sp = mContext.getSharedPreferences(Constant.SP_TB_USER, mContext.MODE_PRIVATE);
		jiaobaohao = sp.getString("JiaoBaoHao", "");
		bitmap = JSYApplication.getInstance().bitmap;
	}
	public void setData(List<CommMsg> list){
		this.list = list;
	}
	public List<CommMsg> getData(){
		return list;
	}
	public void setFBData(List<FeeBack> list2) {
		this.list2 = list2;
	}
	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}else if(list2 != null){
			return list2.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if (list != null) {
			return list.get(arg0);
		}else if(list2 != null){
			return list2.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,R.layout.msgcenter_listview_item, position);
		ImageView photo = viewHolder.getView(R.id.msgcenter_listview_img_photo);
		if (list != null) {
			String url ="";
			final CommMsg item = (CommMsg) getItem(position);
			if (fromme) {
				url =  mainURL +ConstantUrl.photoURL+"?AccID="+jiaobaohao;
			}else{
				url =  mainURL +ConstantUrl.photoURL+"?AccID="+item.getJiaoBaoHao();
			}
//			viewHolder.setImageByUrl(R.id.msgcenter_listview_img_photo,url);
//			ImageLoader.getInstance().loadImage(url, photo);
			bitmap.display(photo, url);
			viewHolder.setText(R.id.msgcenter_listview_tv_author, item.getUserName());
			viewHolder.setText(R.id.msgcenter_listview_tv_content, item.getMsgContent());
			viewHolder.setText(R.id.msgcenter_listview_tv_time, item.getRecDate());
			viewHolder.getConvertView().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					ArrayList<Object> post = new ArrayList<Object>();
					post.add(WorkMoreActivity.CLICK_POSITION);
					post.add(item);
					EventBusUtil.post(post);
					Intent i = new Intent(mContext,WorkDetailsActivity.class);
					i.putExtra("TabIDStr", item.getTabIDStr());
					if (fromme) {
						i.putExtra("JiaoBaoHao", jiaobaohao);
					}else{
						i.putExtra("JiaoBaoHao", item.getJiaoBaoHao()+"");
						
					}
					System.out.println("---------"+jiaobaohao+item.getTabIDStr());
					mContext.startActivity(i);
				}
			});
		}else if(list2 != null){
			String url ="";
			final FeeBack item = (FeeBack) getItem(position);
			if (fromme) {
				url =  mainURL +ConstantUrl.photoURL+"?AccID="+jiaobaohao;
			}else{
				url = mainURL +ConstantUrl.photoURL+"?AccID="+item.getJiaobaohao();
			}
//			viewHolder.setImageByUrl(R.id.msgcenter_listview_img_photo,url);
//			ImageLoader.getInstance().loadImage(url, photo);
			bitmap.display(photo, url);			
			viewHolder.setText(R.id.msgcenter_listview_tv_author, item.getUserName());
			viewHolder.setText(R.id.msgcenter_listview_tv_content, item.getFeeBackMsg());
			viewHolder.setText(R.id.msgcenter_listview_tv_time, item.getRecDate());
			viewHolder.getConvertView().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					Intent i = new Intent(mContext,WorkDetailsActivity.class);
					i.putExtra("TabIDStr", item.getTabIDStr());
					if (fromme) {
						i.putExtra("JiaoBaoHao", jiaobaohao);
					}else{
						i.putExtra("JiaoBaoHao", item.getJiaobaohao()+"");
						
					}
					i.putExtra("MsgTabIDStr", item.getMsgTabIDStr());
					mContext.startActivity(i);
				}
			});
		}
		return viewHolder.getConvertView();
	}
	public void setFromMe(boolean b) {
		fromme = b;
	}

}
