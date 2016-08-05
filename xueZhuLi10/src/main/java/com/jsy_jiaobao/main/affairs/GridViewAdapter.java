package com.jsy_jiaobao.main.affairs;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.sys.SMSTreeUnitID;
import com.jsy_jiaobao.po.sys.Selit;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    /**
     * 事务详情的接收人列表
     */
    private JSONArray readerArray = null;
    private List<Selit> groupselit_selit = null;
    private List<Selit> checkedList = new ArrayList<>();
    private List<Boolean> mChecked;
    /**
     * 短信直通车
     */
    private List<SMSTreeUnitID> smsList = null;
    /**
     * 短信直通车
     */
    private List<SMSTreeUnitID> checkedSMSList = new ArrayList<>();
    private int uType;
    private String postTag = "selit";

    public GridViewAdapter(Context context) {
        this.context = context;
        mChecked = new ArrayList<>();

    }

    /**
     * 获取传出参数类型  学生老师家长selit
     */
    public String getPostTag() {
        return postTag;
    }

    /**
     * 设置传出参数类型  学生老师家长selit
     */
    public void setPostTag(String postTag) {
        this.postTag = postTag;
    }

    /**
     * 普通接收人
     *
     * @param groupselit_selit d
     */
    public void setReciver(List<Selit> groupselit_selit) {
        if (groupselit_selit != null) {
            this.groupselit_selit = groupselit_selit;
            if (mChecked.size() == 0) {
                for (int i = 0; i < groupselit_selit.size(); i++) {
                    mChecked.add(false);
                }
            }
        }
    }


    /**
     * 短信接收人
     *
     * @param smsList s
     */
    public void setSMSReciver(List<SMSTreeUnitID> smsList, int uType) {
        this.smsList = smsList;
        this.uType = uType;
        for (int i = 0; i < smsList.size(); i++) {
            mChecked.add(false);
        }
    }

    /**
     * 获取短信接收人
     *
     * @return map
     */
    public SparseArray<List<SMSTreeUnitID>> getCheckedSMS() {
        SparseArray<List<SMSTreeUnitID>> checked = new SparseArray<>();
        checkedSMSList.clear();
        for (int i = 0; i < mChecked.size(); i++) {
            checkedSMSList.add(smsList.get(i));
//            if (mChecked.get(i)) {
//
//            }
        }
        checked.put(uType, checkedSMSList);
        return checked;
    }

    public void setData(JSONArray readerArray) {
        this.readerArray = readerArray;
    }

    public List<Selit> getCheckedSelit() {
        checkedList.clear();
        for (int i = 0; i < mChecked.size(); i++) {
            if (mChecked.get(i)) {
                checkedList.add((Selit) getItem(i));
            }
        }
        return checkedList;
    }


    @Override
    public int getCount() {
        if (readerArray != null) {
            return readerArray.length();
        } else if (groupselit_selit != null) {
            return groupselit_selit.size();
        } else if (smsList != null) {
            return smsList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        return groupselit_selit.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 全选
     */
    public void setCheckAll() {
        for (int i = 0; i < mChecked.size(); i++) {
            mChecked.set(i, true);
        }
    }

    /**
     * 反选
     */
    public void setInverse() {
        for (int i = 0; i < mChecked.size(); i++) {
            if (mChecked.get(i)) {
                mChecked.set(i, false);
            } else {
                mChecked.set(i, true);
            }
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(context, convertView, parent, R.layout.gridview_reciver_item, position);
        CheckBox cb = viewHolder.getView(R.id.reciver_checkbox);
        TextView name = viewHolder.getView(R.id.reciver_textview);
        try {
            if (readerArray != null) {
                cb.setVisibility(View.GONE);
                JSONObject item = readerArray.getJSONObject(position);
                int mc = item.getInt("MCState");
                int pc = item.getInt("PCState");
                if (mc == 1 || pc == 1) {
                    name.setText(item.getString("TrueName") + "(已查看)");
                } else {
                    name.setText(item.getString("TrueName"));
                }

            } else if (smsList != null) {

                name.setText(smsList.get(position).getName().trim());
                if (uType != 1) {
                    if (smsList.get(position).getuType() == 1) {
                        cb.setEnabled(false);
                    }
                }
                if (cb.isEnabled()) {
                    cb.setChecked(mChecked.get(position));
                }
                cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mChecked.set(position, isChecked);
                    }
                });
            } else {
                if ("".equals(groupselit_selit.get(position).getSelit())) {
                    cb.setEnabled(false);
                }
                if (cb.isEnabled()) {
                    cb.setChecked(mChecked.get(position));
                }
                name.setText(groupselit_selit.get(position).getName().trim());
                cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mChecked.set(position, isChecked);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return viewHolder.getConvertView();
    }


}
