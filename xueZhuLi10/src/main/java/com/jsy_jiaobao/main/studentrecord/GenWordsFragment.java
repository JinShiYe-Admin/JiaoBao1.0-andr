package com.jsy_jiaobao.main.studentrecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.customview.XListView;
import com.jsy_jiaobao.customview.XListView.IXListViewListener;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sturecord.BaseInfo;
import com.jsy_jiaobao.po.sturecord.GenWords;
import com.jsy_jiaobao.po.sturecord.StuRec_GenWord;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class GenWordsFragment extends Fragment implements IXListViewListener {

    @ViewInject(R.id.layout_xlistview)
    private XListView listView;
    private WordsListAdapter listAdapter;
    private ArrayList<StuRec_GenWord> genwordsList = new ArrayList<>();
    public static int isPack = 0;// 1:选择的孩子为档案包类型,0为学生类型
    public static int packid;// 档案包ID;
    public static int stuid;// 学生ID
    private int CurPage = 1;
    private boolean havemore = false;

    public static GenWordsFragment newInstance() {
        return new GenWordsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_xlistview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GenWordsFragmentController.getInstance().setContext(this);
        ViewUtils.inject(this, view);
        initData();
    }

    private void initData() {
        listAdapter = new WordsListAdapter(getActivity());
        listView.setAdapter(listAdapter);
        listView.setPullLoadEnable(true);
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(this);
        if (StudentRecordActivity.initBaseInfo) {
            isPack = StudentRecordActivity.isPack;
            packid = StudentRecordActivity.packid;
            stuid = StudentRecordActivity.stuid;
            onRefresh();
        }
    }

    private void StuOrPackGenWSch() {
        if (isPack == 1) {// DATA=Uid|Recid|MsgType|PageSize|CurPage|X
            String DATA = BaseActivity.sp.getString("JiaoBaoHao", "")
                    + "|"
                    + packid
                    + "|"
                    + getResources().getString(
                    R.string.record_function_parentsnotice).replace(
                    "\n", "") + "|20|" + CurPage + "|X";
            GenWordsFragmentController.getInstance().PackGenW(DATA);
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
        switch (tag) {
            case Constant.sturecord_home_BaseInfo:
                BaseInfo baseInfo = (BaseInfo) list.get(1);
                isPack = baseInfo.getIspack();
                packid = baseInfo.getPackid();
                stuid = baseInfo.getStuid();
                onRefresh();
                break;
            case Constant.sturecord_home_PackGenW:
                listView.stopRefresh();
                listView.stopLoadMore();
                GenWords genWords = (GenWords) list.get(1);
                ArrayList<StuRec_GenWord> wordsList = genWords.getList();

                if (wordsList.size() < 20) {
                    havemore = false;
                }
                genwordsList.addAll(wordsList);
                listAdapter.setData(genwordsList);
                listAdapter.notifyDataSetChanged();

                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        havemore = true;
        if (genwordsList != null) {
            genwordsList.clear();
        }
        CurPage = 1;
        StuOrPackGenWSch();
    }

    @Override
    public void onLoadMore() {
        if (havemore) {
            CurPage++;
            StuOrPackGenWSch();
        } else {
            ToastUtil.showMessage(getActivity(), R.string.no_more);
            listView.stopLoadMore();
        }
    }
}