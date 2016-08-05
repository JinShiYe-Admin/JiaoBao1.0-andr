package com.jsy_jiaobao.main.studentrecord;

import java.util.ArrayList;
import java.util.HashMap;

import org.greenrobot.eventbus.Subscribe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy_jiaobao.customview.PCWorkItemOneChildView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sturecord.BaseInfo;
import com.jsy_jiaobao.po.sturecord.MsgSch;
import com.jsy_jiaobao.po.sturecord.StuRecGenPackage;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


public class QipingRecordFragment extends Fragment {
	@ViewInject(R.id.studengrecord_layout)private LinearLayout layout_body;
	/** 1:选择的孩子为档案包类型,0为学生类型*/
	public static int isPack = 0;
    public static int packid;//档案包ID;
    public static int stuid;//学生ID
    private int CurPage = 1;
    private ArrayList<HashMap<String,String>> schoolList = new ArrayList<HashMap<String,String>>();
	private static QipingRecordFragment fragment;
	public static QipingRecordFragment newInstance() {
		if (fragment == null) {
			fragment = new QipingRecordFragment();
		}
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_studentrecord, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewUtils.inject(this,view);
		QipingRecordFragmentController.getInstance().setContext(this);
		initData();
	}
	private void initData() {
		if (StudentRecordActivity.initBaseInfo) {
			isPack = StudentRecordActivity.isPack;
			packid = StudentRecordActivity.packid;
			stuid = StudentRecordActivity.stuid;
			StuOrPackQpSch();
		}
	}
	private void StuOrPackQpSch(){
		String DATA = "";//Uid|Stuid|MsgType
		if (isPack == 0) {
			DATA = BaseActivity.sp.getString("JiaoBaoHao", "")+"|"+stuid+"|"+getResources().getString(R.string.record_function_appraise).replace("\n", "");
			QipingRecordFragmentController.getInstance().StuQpSch(DATA);
			
		}else if (isPack == 1) {
			DATA = BaseActivity.sp.getString("JiaoBaoHao", "")+"|"+packid+"|"+getResources().getString(R.string.record_function_appraise).replace("\n", "");
			QipingRecordFragmentController.getInstance().PackQpSch(DATA);
		}
	}
	@Override
	public void onResume() {
		EventBusUtil.register(this);
		super.onResume();
	}
	@Override
	public void onPause() {
		EventBusUtil.unregister(this);
		super.onPause();
	}
	@Subscribe
	public void onEventMainThread(ArrayList<Object> list){
		int tag = (Integer) list.get(0);
		MsgSch msgSch;
		switch (tag) {
		case Constant.sturecord_home_BaseInfo:
			BaseInfo baseInfo = (BaseInfo) list.get(1);
//			StuRecGenPackage stubase = baseInfo.getStubase();
			isPack = baseInfo.getIspack();
			packid = baseInfo.getPackid();
			stuid = baseInfo.getStuid();
			StuOrPackQpSch();
			break;
		case Constant.sturecord_home_PackQpSch:
			msgSch = (MsgSch) list.get(1);
			if (null != msgSch.getSchs()) {
				
				for (String sch : msgSch.getSchs()) {
					String[] schitem = sch.split("\\|");
					HashMap<String,String> map = new HashMap<String, String>();
					map.put("name", schitem[0]);
					map.put("number", schitem[1]);
					schoolList.add(map);
				}
				initSchoolList();
			}
			break;
		case Constant.sturecord_home_StuQpSch:
			msgSch = (MsgSch) list.get(1);
			if (null != msgSch.getSchs()) {
				
				for (String sch : msgSch.getSchs()) {
					String[] schitem = sch.split("\\|");
					HashMap<String,String> map = new HashMap<String, String>();
					map.put("name", schitem[0]);
					map.put("number", schitem[1]);
					schoolList.add(map);
				}
				initSchoolList();
			}
			break;
//		case Constant.sturecord_home_StuQp:
//			ArrayList<Object> stuMsgTag = (ArrayList<Object>) list.get(2);
//			break;
//		case Constant.sturecord_home_PackQp:
//			ArrayList<Object> packMsgTag = (ArrayList<Object>) list.get(2);
//			break;
		default:
			break;
		}
	}
	/**
	 * 生成学校列表
	 */
	private void initSchoolList() {
		layout_body.removeAllViews();
		for (HashMap<String,String> map : schoolList) {
			PCWorkItemOneChildView layout_item = new PCWorkItemOneChildView(getActivity(), null);
			layout_item.create(-1, map.get("name").toString());
			NoticeListAdapter adapter = new NoticeListAdapter(getActivity());
			ArrayList<Object> tag = new ArrayList<Object>();
			layout_item.listview1.setAdapter(adapter);
			tag.add(map.get("name").toString());
			tag.add(layout_item);
			tag.add(adapter);
			layout_item.layout_parent.setTag(tag);
			layout_item.more.setTag(tag);
			layout_item.layout_parent.setOnClickListener(layoutOnClickListener);
			layout_item.more.setOnClickListener(moreOnClickListener);
			int art = Integer.parseInt(map.get("number").toString());
			if (art>0) {
				layout_item.parent_art.setVisibility(0);
				layout_item.parent_art.setText(String.valueOf(art));
			}else{
				layout_item.parent_art.setVisibility(4);
			}
			layout_body.addView(layout_item);
		}
	}
	OnClickListener layoutOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			@SuppressWarnings("unchecked")
			ArrayList<Object> tag = (ArrayList<Object>) v.getTag();
			PCWorkItemOneChildView layoutitem  = (PCWorkItemOneChildView) tag.get(1); 
			String SchName = (String) tag.get(0);
			String DATA = "";
			if (layoutitem.listview1.getVisibility() == View.GONE) {
				layoutitem.setExpanChild(0);
				if (isPack == 0) {
					DATA = BaseActivity.sp.getString("JiaoBaoHao", "")+"|"+stuid+"|"+getResources().getString(R.string.record_function_appraise).replace("\n", "")+"|20|"+CurPage+"|"+SchName;
					QipingRecordFragmentController.getInstance().StuQp(tag,DATA);
				}else if (isPack == 1) {//Uid|Recid|MsgType|PageSize|CurPage|SchName
					DATA = BaseActivity.sp.getString("JiaoBaoHao", "")+"|"+packid+"|"+getResources().getString(R.string.record_function_appraise).replace("\n", "")+"|20|"+CurPage+"|"+SchName;
					QipingRecordFragmentController.getInstance().PackQp(tag,DATA);
				}
			}else{
				layoutitem.setExpanChild(8);
			}
		}
	};
	OnClickListener moreOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
		}
	};
}