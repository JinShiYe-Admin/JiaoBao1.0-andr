package com.jsy_jiaobao.main.studentrecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy_jiaobao.customview.PCWorkItemOneChildView;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sturecord.BaseInfo;
import com.jsy_jiaobao.po.sturecord.MsgSch;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

public class DailyExpressionFragment extends Fragment {
    @ViewInject(R.id.studengrecord_layout)
    private LinearLayout layout_body;
    public static int isPack = 0;//1:选择的孩子为档案包类型,0为学生类型
    public static int packId;//档案包ID;
    public static int stuId;//学生ID

    private ArrayList<HashMap<String, String>> schoolList = new ArrayList<>();

    public static DailyExpressionFragment newInstance() {
        return new DailyExpressionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_studentrecord, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this, view);
        DailyExpressionFragmentController.getInstance().setContext(this);
        initData();
    }

    private void initData() {
        if (StudentRecordActivity.initBaseInfo) {
            isPack = StudentRecordActivity.isPack;
            packId = StudentRecordActivity.packid;
            stuId = StudentRecordActivity.stuid;
            StuOrPackMsgSch();
        }
    }

    private void StuOrPackMsgSch() {
        schoolList.clear();
        String DATA;//Uid|Stuid|MsgType
        if (isPack == 0) {
            DATA = BaseActivity.sp.getString("JiaoBaoHao", "") + "|" + stuId + "|" + getResources().getString(R.string.record_function_dayshow).replace("\n", "");
            DailyExpressionFragmentController.getInstance().StuMsgSch(DATA);

        } else if (isPack == 1) {
            DATA = BaseActivity.sp.getString("JiaoBaoHao", "") + "|" + packId + "|" + getResources().getString(R.string.record_function_dayshow).replace("\n", "");
            DailyExpressionFragmentController.getInstance().PackMsgSch(DATA);
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
    public void onEventMainThread(ArrayList<Object> list) {
        int tag = (Integer) list.get(0);
        MsgSch msgSch;
        switch (tag) {
            case Constant.sturecord_home_BaseInfo:
                BaseInfo baseInfo = (BaseInfo) list.get(1);
                isPack = baseInfo.getIspack();
                packId = baseInfo.getPackid();
                stuId = baseInfo.getStuid();
                StuOrPackMsgSch();
                break;
            case Constant.sturecord_home_PackMsgSch_daily:
                msgSch = (MsgSch) list.get(1);
                if (null != msgSch.getSchs()) {
                    for (String sch : msgSch.getSchs()) {
                        String[] schitem = sch.split("\\|");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", schitem[0]);
                        map.put("number", schitem[1]);
                        schoolList.add(map);
                    }
                    initSchoolList();
                }
                break;
            case Constant.sturecord_home_StuMsgSch_daily:
                msgSch = (MsgSch) list.get(1);
                if (null != msgSch.getSchs()) {
                    for (String sch : msgSch.getSchs()) {
                        String[] schitem = sch.split("\\|");
                        HashMap<String, String> map = new HashMap<>();
                        map.put("name", schitem[0]);
                        map.put("number", schitem[1]);
                        schoolList.add(map);
                    }
                    initSchoolList();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 生成学校列表
     */
    private void initSchoolList() {
        layout_body.removeAllViews();
        for (HashMap<String, String> map : schoolList) {
            PCWorkItemOneChildView layout_item = new PCWorkItemOneChildView(getActivity(), null);
            layout_item.create(-1, map.get("name"));
            NoticeListAdapter adapter = new NoticeListAdapter(getActivity());
            ArrayList<Object> tag = new ArrayList<>();
            layout_item.listview1.setAdapter(adapter);
            tag.add(map.get("name"));
            tag.add(layout_item);
            tag.add(adapter);
            layout_item.layout_parent.setTag(tag);
            layout_item.more.setTag(tag);
            layout_item.layout_parent.setOnClickListener(layoutOnClickListener);
            layout_item.more.setOnClickListener(moreOnClickListener);
            int art = Integer.parseInt(map.get("number"));
            if (art > 0) {
                layout_item.parent_art.setVisibility(View.VISIBLE);
                layout_item.parent_art.setText(String.valueOf(art));
            } else {
                layout_item.parent_art.setVisibility(View.INVISIBLE);
            }
            layout_body.addView(layout_item);
        }
    }

    OnClickListener layoutOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            @SuppressWarnings("unchecked")
            ArrayList<Object> tag = (ArrayList<Object>) v.getTag();
            PCWorkItemOneChildView layoutitem = (PCWorkItemOneChildView) tag.get(1);
            String SchName = (String) tag.get(0);
            String DATA;
            int CurPage = 1;
            if (layoutitem.listview1.getVisibility() == View.GONE) {
                layoutitem.setExpanChild(0);
                if (isPack == 0) {
                    DATA = BaseActivity.sp.getString("JiaoBaoHao", "") + "|" + stuId + "|" + getResources().getString(R.string.record_function_dayshow).replace("\n", "") + "|20|" + CurPage + "|" + SchName;
                    DailyExpressionFragmentController.getInstance().StuMsg(tag, DATA);
                } else if (isPack == 1) {//Uid|Recid|MsgType|PageSize|CurPage|SchName
                    DATA = BaseActivity.sp.getString("JiaoBaoHao", "")
                            + "|"
                            + packId
                            + "|"
                            + getResources().getString(
                            R.string.record_function_dayshow).replace("\n", "") + "|20|" + CurPage + "|" + SchName;
                    DailyExpressionFragmentController.getInstance().PackMsg(tag, DATA);
                }
            } else {
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