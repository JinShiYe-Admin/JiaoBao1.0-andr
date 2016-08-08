package com.jsy_jiaobao.main.personalcenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DestroyInterface;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.customview.SlideShowView;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.qiuzhi.AnswerItem;
import com.jsy_jiaobao.po.qiuzhi.GetPicked;
import com.jsy_jiaobao.po.qiuzhi.PickedIndex;
import com.jsy_jiaobao.po.qiuzhi.QuestionIndexItem;
import com.jsy_jiaobao.po.qiuzhi.RecommentIndexItem;
import com.jsy_jiaobao.po.qiuzhi.WatchedEntityIndexItem;
import com.jsy_jiaobao.po.qiuzhi.Watcher;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 求知界面ListView的Adapter
 *
 * @param <T>
 * @author admin
 */

public class QiuZhiIndexListAdapter<T> extends BaseAdapter implements Watcher,
        DestroyInterface {
    final private static String TAG = "QiuZhiIndexListAdapter";//
    final private static int CATEGORY_INDEX = -1;// 首页
    final private static int CATEGORY_RECOMMENT = -2;// 推荐
    final private static int CATEGORY_SIFTION = -3;// 精选
    final private int TAG_INDEX = 1;
    final private int TAG_SUGGEST = 2;
    final private int TAG_SIFTION = 3;

    private Context mContext;
    private List<T> mData;

    private String[] str_todaytimes;
    private String mainURL;
    private boolean isIndex;
    private int CategoryID = 0;
    private ArrayList<Object> TopData;
    int type;

    public QiuZhiIndexListAdapter(Context mContext, boolean isIndex) {
        this.mContext = mContext;
        this.setIndex(isIndex);
        // 时间格式
        SimpleDateFormat dateFormat;
        Date today;
        String str_todaytime;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        today = new Date(System.currentTimeMillis());
        str_todaytime = dateFormat.format(today);
        str_todaytimes = str_todaytime.split(" ");
        mainURL = ACache.get(mContext.getApplicationContext()).getAsString(
                "MainUrl");
        WatchedEntityIndexItem.getInstance().addWatcher(this);
    }

    public void setData(List<T> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position > 0) {
            return mData.get(position - 1);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        switch (CategoryID) {
            case CATEGORY_INDEX:
                return TAG_INDEX;
            case CATEGORY_RECOMMENT:
                return TAG_SUGGEST;
            case CATEGORY_SIFTION:
                return TAG_SIFTION;
            default:
                return TAG_INDEX;
        }

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.item_qiuzhi_index_listview, position);
        LinearLayout layout_top = viewHolder
                .getView(R.id.qiuzhi_item_layout_top);
        LinearLayout layout_question = viewHolder
                .getView(R.id.qiuzhi_item_layout_question);
        LinearLayout layout_answer = viewHolder
                .getView(R.id.qiuzhi_item_layout_answer);
        LinearLayout layout_answercontent = viewHolder
                .getView(R.id.qiuzhi_item_layout_answercontent);
        RelativeLayout layout_questionmsg = viewHolder
                .getView(R.id.qiuzhi_item_layout_questionmsg);
        RelativeLayout layout_authormsg = viewHolder
                .getView(R.id.qiuzhi_item_layout_answerermsg);
        RelativeLayout layout_answermsg = viewHolder
                .getView(R.id.qiuzhi_item_layout_answermsg);
        View view_line = viewHolder.getView(R.id.qiuzhi_item_view_line);

        TextView tv_details = viewHolder.getView(R.id.qiuzhi_item_tv_details);
        TextView tv_question = viewHolder.getView(R.id.qiuzhi_item_tv_question);
        // TextView tv_0 = viewHolder.getView(R.id.qiuzhi_item_tv_0);
        TextView tv_topic = viewHolder.getView(R.id.qiuzhi_item_tv_topic);
        TextView tv_answernum = viewHolder
                .getView(R.id.qiuzhi_item_tv_answernum);
        TextView tv_attnum = viewHolder.getView(R.id.qiuzhi_item_tv_attnum);
        TextView tv_clicknum = viewHolder.getView(R.id.qiuzhi_item_tv_clicknum);
        TextView tv_likenum = viewHolder.getView(R.id.qiuzhi_item_tv_likenum);
        ImageView iv_top = viewHolder.getView(R.id.qiuzhi_item_iv_top);
        ImageView iv_photo = viewHolder.getView(R.id.qiuzhi_item_iv_photo);
        TextView tv_author = viewHolder.getView(R.id.qiuzhi_item_tv_author);
        TextView tv_answer = viewHolder.getView(R.id.qiuzhi_item_tv_answer);
        TextView tv_answer_ = viewHolder.getView(R.id.qiuzhi_item_tv_answer_);
        TextView tv_gist = viewHolder.getView(R.id.qiuzhi_item_tv_gist);
        ImageView tv_gist_ = viewHolder.getView(R.id.qiuzhi_item_tv_gist_);
        GridView gv_gallery = viewHolder.getView(R.id.qiuzhi_item_gv_gallery);
        TextView tv_time = viewHolder.getView(R.id.qiuzhi_item_tv_time);
        TextView tv_commentnum = viewHolder
                .getView(R.id.qiuzhi_item_tv_commentnum);

        layout_top.setVisibility(View.VISIBLE);
        layout_questionmsg.setVisibility(View.VISIBLE);
        layout_authormsg.setVisibility(View.VISIBLE);
        layout_answermsg.setVisibility(View.VISIBLE);
        tv_answer_.setVisibility(View.VISIBLE);
        tv_gist.setVisibility(View.VISIBLE);
        gv_gallery.setVisibility(View.VISIBLE);
        tv_gist_.setVisibility(View.VISIBLE);
        tv_question.setSingleLine(true);
        tv_answer.setSingleLine(true);
        layout_top.removeAllViews();
        layout_answer.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        layout_question.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        view_line.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 1));
        iv_top.setVisibility(View.GONE);
        tv_question.setOnClickListener(null);
        layout_answercontent.setOnClickListener(null);
        tv_details.setOnClickListener(null);
        layout_question.setOnClickListener(null);
        type = getItemViewType(position);
        tv_details.setVisibility(View.VISIBLE);
        switch (type) {
            case TAG_INDEX:
                // 首页和其他
                try {
                    if (position == 0) {
                        // 第一条数据
                        if (TopData != null && TopData.size() > 0) {
                            final QuestionIndexItem data = (QuestionIndexItem) TopData
                                    .get(position);
                            iv_top.setVisibility(View.VISIBLE);
                            layout_question.setVisibility(View.VISIBLE);
                            tv_question.setVisibility(View.VISIBLE);
                            view_line.setVisibility(View.VISIBLE);
                            tv_question.setText(data.getTitle());

                            tv_topic.setText(data.getCategorySuject());
                            tv_answernum.setText(String.valueOf(data
                                    .getAnswersCount()));
                            tv_attnum.setText(String.valueOf(data.getAttCount()));

                            tv_clicknum
                                    .setText(String.valueOf(data.getViewCount()));
                            AnswerItem answer = data.getAnswer();

                            if (answer == null) {
                                // 回答为空
                                layout_answer.setVisibility(View.GONE);
                                layout_answermsg.setVisibility(View.GONE);
                                tv_likenum.setVisibility(View.GONE);
                                gv_gallery.setVisibility(View.GONE);
                                tv_author.setText("");
                                tv_answer.setText(mContext.getResources()
                                        .getString(R.string.temporarily_noAnswer));
                                tv_gist.setText("");
                                tv_gist_.setImageResource(R.drawable.icon_qiuzhi_nocontent_);
                                layout_answercontent.setOnClickListener(null);
                            } else {
                                // 存在回答
                                layout_answer.setVisibility(View.VISIBLE);
                                tv_likenum.setVisibility(View.VISIBLE);
                                layout_answermsg.setVisibility(View.VISIBLE);
                                layout_answer.setVisibility(View.VISIBLE);
                                view_line.setVisibility(View.VISIBLE);
                                tv_likenum.setText(mContext.getString(R.string.number_like, data.getAnswer()
                                        .getLikeCount()));
                                if (TextUtils.isEmpty(data.getAnswer().getIdFlag())) {
                                    tv_author.setText(mContext.getResources()
                                            .getString(R.string.anonymity));
                                    iv_photo.setImageResource(R.drawable.photo);
                                } else {
                                    String url = mainURL + ConstantUrl.photoURL
                                            + "?AccID=" + answer.getJiaoBaoHao();
                                    JSYApplication.getInstance().bitmap.display(
                                            iv_photo, url);
                                    tv_author.setText(data.getAnswer().getIdFlag()
                                            .trim());
                                }
                                String[] str_times = data.getAnswer().getRecDate()
                                        .split("T");
                                if (str_times[0].equals(str_todaytimes[0])) {
                                    tv_time.setText(str_times[1]);
                                } else {
                                    tv_time.setText(str_times[0]);
                                }
                                tv_commentnum.setText(String.valueOf(data
                                        .getAnswer().getCCount()));

                                tv_answer.setText(data.getAnswer().getATitle());
                                // 求真标志0=无内容，1=有内容，2=有证据
                                int flag = answer.getFlag();
                                tv_gist.setVisibility(View.VISIBLE);
                                tv_gist.setTextColor(mContext.getResources()
                                        .getColor(R.color.black));
                                if (TextUtils.isEmpty(answer.getAbstracts())) {
                                    tv_gist.setVisibility(View.GONE);
                                    tv_gist_.setImageResource(R.drawable.icon_qiuzhi_nocontent_);
                                } else {
                                    if (flag == 0) {
                                        tv_gist.setVisibility(View.GONE);
                                        tv_gist_.setImageResource(R.drawable.icon_qiuzhi_nocontent_);
                                    } else if (flag == 1) {
                                        tv_gist_.setImageResource(R.drawable.icon_qiuzhi_content_);
                                        tv_gist.setText(data.getAnswer()
                                                .getAbstracts());
                                    } else if (flag == 2) {
                                        tv_gist_.setImageResource(R.drawable.icon_qiuzhi_gist_);
                                        tv_gist.setTextColor(mContext
                                                .getResources().getColor(
                                                        R.color.color_e67215));
                                        tv_gist.setText(data.getAnswer()
                                                .getAbstracts());
                                    }
                                }
                                String thumbnail = data.getAnswer().getThumbnail();
                                if (TextUtils.isEmpty(thumbnail)) {
                                    gv_gallery.setVisibility(View.GONE);
                                } else {
                                    gv_gallery.setVisibility(View.VISIBLE);
                                    JSONArray jsonarray = new JSONArray(thumbnail);
                                    PictureAdapter adapter = new PictureAdapter(
                                            mContext, jsonarray);
                                    gv_gallery.setAdapter(adapter);
                                }
                            }
                            // 详情点击事件
                            tv_details.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    MobclickAgent.onEvent(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.qiuzhi_list_details));
                                    Intent intent = new Intent(mContext,
                                            QiuZhiQuestionDetailsActivity.class);
                                    intent.putExtra("QuestionIndexItem", data);
                                    mContext.startActivity(intent);
                                }
                            });
                            // 问题点击事件
                            tv_question.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    MobclickAgent.onEvent(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.qiuzhi_list_question));
                                    Intent intent = new Intent(mContext,
                                            QiuZhiQuestionAnswerListActivity.class);
                                    intent.putExtra("QuestionIndexItem", data);
                                    mContext.startActivity(intent);
                                }
                            });
                            // 答案内容点击事件
                            layout_answercontent
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            MobclickAgent
                                                    .onEvent(
                                                            mContext,
                                                            mContext.getResources()
                                                                    .getString(
                                                                            R.string.qiuzhi_list_answerContent));
                                            Intent intent = new Intent(
                                                    mContext,
                                                    QiuZhiQuestionAnswerDetailsActivity.class);
                                            intent.putExtra("AnswerID", data
                                                    .getAnswer().getTabID());
                                            intent.putExtra("QuestionIndexItem",
                                                    data);
                                            mContext.startActivity(intent);
                                        }
                                    });
                        } else {
                            layout_answer
                                    .setLayoutParams(new LinearLayout.LayoutParams(
                                            LayoutParams.MATCH_PARENT, 0));
                            layout_question
                                    .setLayoutParams(new LinearLayout.LayoutParams(
                                            LayoutParams.MATCH_PARENT, 0));
                            view_line
                                    .setLayoutParams(new LinearLayout.LayoutParams(
                                            LayoutParams.MATCH_PARENT, 0));
                        }
                    } else {
                        // 第二条及以后数据
                        final QuestionIndexItem data = (QuestionIndexItem) mData
                                .get(position - 1);
                        layout_question.setVisibility(View.VISIBLE);
                        tv_question.setVisibility(View.VISIBLE);
                        tv_question.setText(data.getTitle());
                        tv_topic.setText(data.getCategorySuject());
                        tv_answernum
                                .setText(String.valueOf(data.getAnswersCount()));
                        tv_attnum.setText(String.valueOf(data.getAttCount()));
                        tv_clicknum.setText(String.valueOf(data.getViewCount()));
                        iv_photo.setScaleType(ScaleType.FIT_CENTER);
                        AnswerItem answer = data.getAnswer();
                        if (answer == null) {
                            layout_answer.setVisibility(View.GONE);
                            layout_answermsg.setVisibility(View.GONE);
                            tv_likenum.setVisibility(View.GONE);
                            gv_gallery.setVisibility(View.GONE);
                            tv_author.setText("");
                            tv_answer.setText(mContext.getResources().getString(
                                    R.string.temporarily_noAnswer));
                            tv_gist.setText("");
                            tv_gist_.setImageResource(R.drawable.icon_qiuzhi_nocontent_);
                            layout_answercontent.setOnClickListener(null);
                        } else {
                            layout_answer.setVisibility(View.VISIBLE);
                            tv_likenum.setVisibility(View.VISIBLE);
                            layout_answermsg.setVisibility(View.VISIBLE);
                            layout_answer.setVisibility(View.VISIBLE);
                            view_line.setVisibility(View.VISIBLE);
                            tv_answer.setVisibility(View.VISIBLE);

                            tv_likenum.setText(mContext.getString(R.string.number_like, data.getAnswer()
                                    .getLikeCount()));
                            if (TextUtils.isEmpty(data.getAnswer().getIdFlag())) {
                                tv_author.setText(mContext.getResources()
                                        .getString(R.string.anonymity));
                                // JSYApplication.getInstance().bitmap.display(
                                // iv_photo, "");
                                iv_photo.setImageResource(R.drawable.photo);

                            } else {
                                String url = mainURL + ConstantUrl.photoURL
                                        + "?AccID=" + answer.getJiaoBaoHao();
                                JSYApplication.getInstance().bitmap.display(
                                        iv_photo, url);
                                tv_author.setText(data.getAnswer().getIdFlag()
                                        .trim());
                            }
                            String[] str_times = data.getAnswer().getRecDate()
                                    .split("T");
                            if (str_times[0].equals(str_todaytimes[0])) {
                                tv_time.setText(str_times[1]);
                            } else {
                                tv_time.setText(str_times[0]);
                            }
                            tv_commentnum.setText(String.valueOf(data.getAnswer()
                                    .getCCount()));

                            tv_answer.setText(data.getAnswer().getATitle());

                            String thumbnail = data.getAnswer().getThumbnail();
                            int flag = answer.getFlag();
                            tv_gist.setVisibility(View.VISIBLE);
                            tv_gist.setTextColor(mContext.getResources().getColor(
                                    R.color.black));
                            if (flag == 0) {
                                tv_gist.setVisibility(View.GONE);
                                if (TextUtils.isEmpty(thumbnail)) {
                                    tv_gist_.setImageResource(R.drawable.icon_qiuzhi_nocontent_);
                                } else {
                                    tv_gist_.setImageResource(R.drawable.icon_qiuzhi_content_);
                                }
                            } else if (flag == 1) {
                                tv_gist_.setImageResource(R.drawable.icon_qiuzhi_content_);
                                tv_gist.setText(data.getAnswer().getAbstracts());
                            } else if (flag == 2) {
                                tv_gist_.setImageResource(R.drawable.icon_qiuzhi_gist_);
                                tv_gist.setTextColor(mContext.getResources()
                                        .getColor(R.color.color_e67215));
                                String abs = data.getAnswer().getAbstracts();
                                if (TextUtils.isEmpty(thumbnail)
                                        && TextUtils.isEmpty(abs)) {
                                    tv_gist.setText(mContext.getResources()
                                            .getString(R.string.qiuzhi_gist_revamp));
                                } else {
                                    tv_gist.setText(abs);
                                }
                            }
                            // }

                            if (TextUtils.isEmpty(thumbnail)) {
                                gv_gallery.setVisibility(View.GONE);
                            } else {
                                gv_gallery.setVisibility(View.VISIBLE);
                                JSONArray jsonarray = new JSONArray(thumbnail);
                                PictureAdapter adapter = new PictureAdapter(
                                        mContext, jsonarray);
                                gv_gallery.setAdapter(adapter);
                            }
                            // 答案内容点击事件
                            layout_answercontent
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            MobclickAgent
                                                    .onEvent(
                                                            mContext,
                                                            mContext.getResources()
                                                                    .getString(
                                                                            R.string.qiuzhi_list_answerContent));
                                            Intent intent = new Intent(
                                                    mContext,
                                                    QiuZhiQuestionAnswerDetailsActivity.class);
                                            intent.putExtra("Answer",
                                                    data.getAnswer());
                                            intent.putExtra("QuestionIndexItem",
                                                    data);
                                            mContext.startActivity(intent);
                                        }
                                    });
                        }
                        // 详情点击事件
                        tv_details.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                MobclickAgent.onEvent(
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.qiuzhi_list_details));
                                data.setViewCount((data.getViewCount() + 1));
                                ACache.get(mContext.getApplicationContext(),
                                        "qiuzhi").put("isOldList", "true");
                                Intent intent = new Intent(mContext,
                                        QiuZhiQuestionDetailsActivity.class);
                                Bundle args = new Bundle();
                                args.putSerializable("QuestionIndexItem", data);
                                intent.putExtras(args);
                                mContext.startActivity(intent);
                            }
                        });
                        // 问题标题点击事件
                        tv_question.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                MobclickAgent.onEvent(
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.qiuzhi_list_question));
                                data.setViewCount((data.getViewCount() + 1));
                                ACache.get(mContext.getApplicationContext(),
                                        "qiuzhi").put("isOldList", "true");
                                Intent intent = new Intent(mContext,
                                        QiuZhiQuestionAnswerListActivity.class);
                                intent.putExtra("QuestionIndexItem", data);
                                mContext.startActivity(intent);
                            }
                        });
                        // 问题点击事件 包括标题和详情
                        layout_question.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                MobclickAgent.onEvent(
                                        mContext,
                                        mContext.getResources().getString(
                                                R.string.qiuzhi_list_question));
                                data.setViewCount((data.getViewCount() + 1));
                                ACache.get(mContext.getApplicationContext(),
                                        "qiuzhi").put("isOldList", "true");
                                Intent intent = new Intent(mContext,
                                        QiuZhiQuestionAnswerListActivity.class);
                                intent.putExtra("QuestionIndexItem", data);
                                mContext.startActivity(intent);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case TAG_SUGGEST:
                // 推荐
                try {
                    if (position == 0) {
                        // 首条数据 隐藏
                        layout_answer
                                .setLayoutParams(new LinearLayout.LayoutParams(
                                        LayoutParams.MATCH_PARENT, 0));
                        layout_question
                                .setLayoutParams(new LinearLayout.LayoutParams(
                                        LayoutParams.MATCH_PARENT, 0));
                        view_line.setLayoutParams(new LinearLayout.LayoutParams(
                                LayoutParams.MATCH_PARENT, 0));

                    } else {
                        // 第二个cell 及以后
                        layout_question.setVisibility(View.VISIBLE);
                        final RecommentIndexItem data = (RecommentIndexItem) mData
                                .get(position - 1);
                        QuestionIndexItem question = data.getQuestion();
                        AnswerItem answer = data.getAnswer();
                        if (null == question) {
                            // 问题为空
                            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) layout_question
                                    .getLayoutParams(); // 取控件textView当前的布局参数
                            linearParams.height = 0;// 控件的高强制设成0
                            layout_question.setLayoutParams(linearParams);
                            LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) layout_answer
                                    .getLayoutParams(); // 取控件textView当前的布局参数
                            linearParams1.height = 0;// 控件的高强制设成0
                            layout_answer.setLayoutParams(linearParams1);
                            LinearLayout.LayoutParams linearParams11 = (LinearLayout.LayoutParams) view_line
                                    .getLayoutParams(); // 取控件textView当前的布局参数
                            linearParams11.height = 0;// 控件的高强制设成0
                            view_line.setLayoutParams(linearParams11);
                        } else {
                            // 问题不为空
                            layout_answer
                                    .setLayoutParams(new LinearLayout.LayoutParams(
                                            LayoutParams.MATCH_PARENT,
                                            LayoutParams.WRAP_CONTENT));
                            layout_question
                                    .setLayoutParams(new LinearLayout.LayoutParams(
                                            LayoutParams.MATCH_PARENT,
                                            LayoutParams.WRAP_CONTENT));
                            view_line
                                    .setLayoutParams(new LinearLayout.LayoutParams(
                                            LayoutParams.MATCH_PARENT, 1));
                            tv_answer.setVisibility(View.VISIBLE);
                            tv_question.setText(question.getTitle());
                            tv_topic.setText(question.getCategorySuject());
                            tv_answernum.setText(String.valueOf(question
                                    .getAnswersCount()));
                            tv_attnum
                                    .setText(String.valueOf(question.getAttCount()));
                            tv_clicknum.setText(String.valueOf(question
                                    .getViewCount()));
                            if (answer == null) {
                                // 答案为空
                                layout_answer.setVisibility(View.GONE);
                                layout_answermsg.setVisibility(View.GONE);
                                tv_likenum.setVisibility(View.GONE);
                                gv_gallery.setVisibility(View.GONE);
                                tv_author.setText("");
                                tv_answer.setText(mContext.getResources()
                                        .getString(R.string.temporarily_noAnswer));
                                tv_gist.setText("");
                                tv_gist_.setImageResource(R.drawable.icon_qiuzhi_nocontent_);
                                layout_answercontent.setOnClickListener(null);
                            } else {
                                // 答案不为空
                                layout_answer.setVisibility(View.VISIBLE);
                                tv_likenum.setVisibility(View.VISIBLE);
                                layout_answermsg.setVisibility(View.VISIBLE);
                                layout_answer.setVisibility(View.VISIBLE);
                                view_line.setVisibility(View.VISIBLE);
                                tv_likenum.setText(mContext.getString(R.string.number_like, data.getAnswer()
                                        .getLikeCount()));
                                if (TextUtils.isEmpty(data.getAnswer().getIdFlag())) {
                                    // 匿名
                                    tv_author.setText(mContext.getResources()
                                            .getString(R.string.anonymity));
                                    iv_photo.setImageResource(R.drawable.photo);
                                } else {
                                    // 其他
                                    String url1 = mainURL + ConstantUrl.photoURL
                                            + "?AccID=" + answer.getJiaoBaoHao();
                                    JSYApplication.getInstance().bitmap.display(
                                            iv_photo, url1);
                                    tv_author.setText(data.getAnswer().getIdFlag()
                                            .trim());
                                }
                                String[] str_times = data.getAnswer().getRecDate()
                                        .split("T");
                                if (str_times[0].equals(str_todaytimes[0])) {
                                    // 如果是当天
                                    tv_time.setText(str_times[1]);
                                } else {
                                    // 其他日子
                                    tv_time.setText(str_times[0]);
                                }
                                // 评论数目
                                tv_commentnum.setText(String.valueOf(data
                                        .getAnswer().getCCount()));

                                tv_answer.setText(data.getAnswer().getATitle());
                                String thumbnail = data.getAnswer().getThumbnail();
                                // 0无内容1有内容2有证据
                                int flag = answer.getFlag();
                                tv_gist.setVisibility(View.VISIBLE);
                                tv_gist.setTextColor(mContext.getResources()
                                        .getColor(R.color.black));
                                if (flag == 0) {
                                    tv_gist.setVisibility(View.GONE);
                                    if (TextUtils.isEmpty(thumbnail)) {
                                        tv_gist_.setImageResource(R.drawable.icon_qiuzhi_nocontent_);
                                    } else {
                                        tv_gist_.setImageResource(R.drawable.icon_qiuzhi_content_);
                                    }
                                } else if (flag == 1) {
                                    tv_gist_.setImageResource(R.drawable.icon_qiuzhi_content_);
                                    tv_gist.setText(data.getAnswer().getAbstracts());
                                } else if (flag == 2) {
                                    tv_gist_.setImageResource(R.drawable.icon_qiuzhi_gist_);
                                    tv_gist.setTextColor(mContext.getResources()
                                            .getColor(R.color.color_e67215));
                                    String abs = data.getAnswer().getAbstracts();
                                    if (TextUtils.isEmpty(thumbnail)
                                            && TextUtils.isEmpty(abs)) {
                                        tv_gist.setText(mContext
                                                .getResources()
                                                .getString(
                                                        R.string.qiuzhi_gist_revamp));
                                    } else {
                                        tv_gist.setText(abs);
                                    }
                                }
                                // }
                                if (TextUtils.isEmpty(thumbnail)) {
                                    gv_gallery.setVisibility(View.GONE);
                                } else {
                                    gv_gallery.setVisibility(View.VISIBLE);
                                    JSONArray jsonarray = new JSONArray(thumbnail);
                                    PictureAdapter adapter = new PictureAdapter(
                                            mContext, jsonarray);
                                    gv_gallery.setAdapter(adapter);
                                }
                                // 答案内容的点击事件
                                layout_answercontent
                                        .setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                MobclickAgent
                                                        .onEvent(
                                                                mContext,
                                                                mContext.getResources()
                                                                        .getString(
                                                                                R.string.qiuzhi_list_answerContent));
                                                Intent intent = new Intent(
                                                        mContext,
                                                        QiuZhiSuggestQuestionAnswerDetailsActivity.class);
                                                intent.putExtra(
                                                        "QuestionIndexItem", data);
                                                mContext.startActivity(intent);
                                            }
                                        });
                            }
                            // 详情点击事件
                            tv_details.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(
                                            mContext,
                                            QiuZhiSuggestShowRecommentActivity.class);
                                    intent.putExtra("QuestionIndexItem", data);
                                    mContext.startActivity(intent);
                                }
                            });
                            // 问题标题点击事件
                            tv_question.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    MobclickAgent.onEvent(
                                            mContext,
                                            mContext.getResources().getString(
                                                    R.string.qiuzhi_list_question));
                                    Intent intent = new Intent(
                                            mContext,
                                            QiuZhiSuggestShowRecommentActivity.class);
                                    intent.putExtra("QuestionIndexItem", data);
                                    mContext.startActivity(intent);
                                }
                            });
                            // 问题点击事件
                            layout_question
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            MobclickAgent
                                                    .onEvent(
                                                            mContext,
                                                            mContext.getResources()
                                                                    .getString(
                                                                            R.string.qiuzhi_list_question));
                                            Intent intent = new Intent(
                                                    mContext,
                                                    QiuZhiSuggestShowRecommentActivity.class);
                                            intent.putExtra("QuestionIndexItem",
                                                    data);
                                            mContext.startActivity(intent);
                                        }
                                    });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case TAG_SIFTION:
                // 精选
                tv_details.setVisibility(View.GONE);
                try {
                    if (position == 0) {
                        // 第一条 置顶
                        if (TopData != null && TopData.size() > 0) {
                            // 有内容
                            layout_question.setVisibility(View.GONE);
                            layout_answer.setVisibility(View.GONE);
                            view_line.setVisibility(View.GONE);
                            layout_top.setVisibility(View.VISIBLE);
                            View mConvertView = LayoutInflater.from(mContext)
                                    .inflate(R.layout.item_qiuzhi_siftion_top,
                                            parent, false);
                            SlideShowView vp_sifttop = (SlideShowView) mConvertView
                                    .findViewById(R.id.qiuzhi_item_siftiontop_slideShowView);
                            TextView tv_time1 = (TextView) mConvertView
                                    .findViewById(R.id.qiuzhi_item_siftiontop_tv_name);
                            TextView tv_moresift = (TextView) mConvertView
                                    .findViewById(R.id.qiuzhi_item_siftiontop_tv_more);
                            layout_top.addView(mConvertView);
                            try {
                                final GetPicked data = (GetPicked) TopData
                                        .get(position);
                                String imgs = data.getImgContent();
                                tv_time1.setText(data.getPTitle());
                                //往期精选
                                tv_moresift
                                        .setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                MobclickAgent
                                                        .onEvent(
                                                                mContext,
                                                                mContext.getResources()
                                                                        .getString(
                                                                                R.string.qiuzhi_list_morSift));
                                                mContext.startActivity(new Intent(
                                                        mContext,
                                                        QiuZhiSiftListActivity.class));
                                            }
                                        });
                                if (TextUtils.isEmpty(imgs)) {
                                    vp_sifttop.setVisibility(View.GONE);
                                } else {
                                    ArrayList<String> list;
                                    list = new ArrayList<>();
                                    vp_sifttop.setVisibility(View.VISIBLE);
                                    vp_sifttop.removeAllViews();
                                    final JSONArray jsonarray = new JSONArray(imgs);
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        jsonarray.put(
                                                i,
                                                ConstantUrl.jsyoa.replaceAll(
                                                        "jbclient", "")
                                                        + data.getBaseImgUrl()
                                                        + jsonarray.getString(i));
                                        list.add(jsonarray.getString(i));
                                    }
                                    vp_sifttop.setImageUrls(list);
                                    vp_sifttop.setCurrentItem(0);
                                    vp_sifttop.startPlay();
                                    //
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            // 无内容
                            layout_answer
                                    .setLayoutParams(new LinearLayout.LayoutParams(
                                            LayoutParams.MATCH_PARENT, 0));
                            layout_question
                                    .setLayoutParams(new LinearLayout.LayoutParams(
                                            LayoutParams.MATCH_PARENT, 0));
                            view_line
                                    .setLayoutParams(new LinearLayout.LayoutParams(
                                            LayoutParams.MATCH_PARENT, 0));
                        }
                    } else {
                        // 第二个及以后的cell
                        layout_questionmsg.setVisibility(View.GONE);
                        layout_authormsg.setVisibility(View.GONE);
                        layout_answermsg.setVisibility(View.GONE);
                        layout_answer.setVisibility(View.VISIBLE);
                        tv_gist.setVisibility(View.GONE);
                        tv_gist_.setVisibility(View.GONE);
                        tv_answer_.setVisibility(View.GONE);
                        tv_answer.setVisibility(View.VISIBLE);
                        final PickedIndex data = (PickedIndex) mData
                                .get(position - 1);
                        layout_question.setVisibility(View.VISIBLE);
                        view_line.setLayoutParams(new LinearLayout.LayoutParams(
                                LayoutParams.MATCH_PARENT, 1));
                        view_line.setVisibility(View.VISIBLE);
                        Log.d(TAG, position + "");

                        tv_question.setText(data.getTitle());
                        tv_answer.setSingleLine(false);
                        if (TextUtils.isEmpty(data.getAbstracts())) {
                            tv_answer.setVisibility(View.GONE);

                        } else {
                            tv_answer.setVisibility(View.VISIBLE);
                            tv_answer.setText(data.getAbstracts());
                        }
                        // 字符串数组 答案内容前三张图片地址
                        String thumbnail = data.getThumbnail();
                        if (TextUtils.isEmpty(thumbnail)) {
                            // 图片地址为空
                            gv_gallery.setVisibility(View.GONE);
                        } else {
                            // 有图片地址
                            gv_gallery.setVisibility(View.VISIBLE);
                            JSONArray jsonarray = new JSONArray(thumbnail);
                            PictureAdapter adapter = new PictureAdapter(mContext,
                                    jsonarray);
                            gv_gallery.setAdapter(adapter);
                        }
                        if (TextUtils.isEmpty(thumbnail)
                                && TextUtils.isEmpty(data.getAbstracts())) {
                            view_line.setVisibility(View.GONE);
                        }
                        //详情按钮
                        tv_details.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext,
                                        QiuZhiPickedBaseActivity.class);
                                intent.putExtra("PickedIndex", data);
                                mContext.startActivity(intent);
                            }
                        });
                        //问题点击事件
                        layout_question.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext,
                                        QiuZhiPickedBaseActivity.class);
                                intent.putExtra("PickedIndex", data);
                                mContext.startActivity(intent);
                            }
                        });
                        //问题标题 点击事件
                        tv_question.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext,
                                        QiuZhiPickedBaseActivity.class);
                                intent.putExtra("PickedIndex", data);
                                mContext.startActivity(intent);
                            }
                        });
                        //答案内容点击事件
//					layout_answercontent
//							.setOnClickListener(new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
//								}
//							});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        return viewHolder.getConvertView();
    }

    @Override
    public void update(QuestionIndexItem qEntity) {
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("unchecked")
    @Override
    public void update(AnswerItem answer) {
        if (answer != null) {
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i) instanceof RecommentIndexItem) {
                    RecommentIndexItem item = (RecommentIndexItem) mData.get(i);
                    AnswerItem a = item.getAnswer();
                    if (a != null) {
                        if (item.getAnswer().getTabID() == answer.getTabID()) {
                            item.setAnswer(answer);
                            mData.set(i, (T) item);
                        }
                    }
                } else if (mData.get(i) instanceof QuestionIndexItem) {
                    QuestionIndexItem item = (QuestionIndexItem) mData.get(i);
                    AnswerItem a = item.getAnswer();
                    if (a != null) {
                        if (a.getTabID() == answer.getTabID()) {
                            item.setAnswer(answer);
                            mData.set(i, (T) item);
                        }
                    }
                }
            }
            QiuZhiIndexListAdapter.this.notifyDataSetChanged();
        }
    }

    @Override
    public void Destroy() {
        WatchedEntityIndexItem.getInstance().removeWatcher(this);
    }

    public boolean isIndex() {
        return isIndex;
    }

    public void setIndex(boolean isIndex) {
        this.isIndex = isIndex;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public void setTopData(ArrayList<Object> topData) {
        TopData = topData;
    }

}
