//package com.jsy.xuezhuli.utils.adapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.widget.ListView;
//
//public class MainActivity extends Activity
//{
//
//	private ListView mListView;
//	private List<String> mDatas = new ArrayList<String>();
//
//	private CommonAdapter<Bean> mAdapter;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//
//		initDatas();
//
//		mListView = (ListView) findViewById(R.id.id_lv_main);
//
//		Aadapter ada =new Aadapter(getApplicationContext(), itemLayoutId);
//		ada.setData(mDatas);
//		// 设置适配器
////		mListView.setAdapter({
////			@Override
////			public void convert(ViewHolder helper, Bean item)
////			{
////				helper.setText(R.id.tv_title, item.getTitle());
////				helper.setText(R.id.tv_describe, item.getDesc());
////				helper.setText(R.id.tv_phone, item.getPhone());
////				helper.setText(R.id.tv_time, item.getTime());
////				
//////				helper.getView(R.id.tv_title).setOnClickListener(l)
////			}
////
////		});
//
//	}
//	class Aadapter extends CommonAdapter<String>{
//
//		public Aadapter(Context context, int itemLayoutId) {
//			super(context, itemLayoutId);
//		}
//
//		@Override
//		public void convert(ViewHolder helper, String item) {
//			
//			// TODO Auto-generated method stub
//			
//		}
//	}
//
//}
