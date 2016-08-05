package com.jsy_jiaobao.main.affairs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.ACache;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.PictureUtils;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy.xuezhuli.utils.adapter.ViewHolder;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.JSYApplication;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.po.personal.Attlist;
import com.jsy_jiaobao.po.personal.FeeBack;
import com.jsy_jiaobao.po.personal.GetWorkMsgDetails;
import com.jsy_jiaobao.po.personal.MySendMsg;
import com.jsy_jiaobao.po.personal.SendToMeMsg;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 一些修改1 2016-4-29 MSL<br>
 * 1.增加一些注释<br>
 * 2.修改附件的文件格式如果为图片，显示缩略图的逻辑
 */

/**
 * 事务详情展示Adapter（包括在事务信息详细界面下拉出现的列表）
 */
public class Work2DetailListAdapter<T> extends BaseAdapter {
    protected final static String TAG = "Work2DetailListAdapter";
    private final int WorkDetails = 1;
    private final int WorkList = 2;

    /**
     * 事务类型:::1:我发的；2:发给我的;31:我发的信息详情;32:发我的信息详情
     */
    private int WorkType = 1;
    private String mainURL;
    private String JiaoBaoHao;
    public BitmapUtils bitmapUtils;
    private List<T> mData;

    private Context mContext;
    private OnClickListener onclickListener;

    public Work2DetailListAdapter(Context mContext,
                                  OnClickListener onclickListener) {
        this.mContext = mContext;
        mainURL = ACache.get(mContext.getApplicationContext()).getAsString(
                "MainUrl");
        JiaoBaoHao = BaseActivity.sp.getString("JiaoBaoHao", "");
        bitmapUtils = new BitmapUtils(mContext);
        bitmapUtils.configDefaultLoadingImage(R.drawable.img_downloading);
        bitmapUtils
                .configDefaultLoadFailedImage(R.drawable.rc_image_download_failure);
        this.onclickListener = onclickListener;
    }

    public void setData(List<T> mData) {
        this.mData = mData;
    }

    public void setWorkType(int WorkType) {
        this.WorkType = WorkType;
    }


    @Override
    public int getCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position == mData.size() - 1 ? WorkDetails : WorkList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
                R.layout.work2details_item, position);
        LinearLayout replyLayout = viewHolder
                .getView(R.id.workdetails_layout_reply);// 回复区域布局
        LinearLayout attLayout = viewHolder
                .getView(R.id.workdetails_layout_att);// 附件区域布局
        TextView workcontent = viewHolder.getView(R.id.workdetails_content);// 信息内容
        TextView time = viewHolder.getView(R.id.workdetails_time);// 时间
        TextView unread = viewHolder.getView(R.id.workdetails_tv_unread);
        Button reply = viewHolder.getView(R.id.workdetails_btn_reply);// 回复按钮
        ImageView sendphoto = viewHolder.getView(R.id.workdetails_photo);// 发送者头像
        final ArrayList<TextView> recViews = new ArrayList<TextView>();
        attLayout.removeAllViews();
        replyLayout.removeAllViews();
        unread.setVisibility(View.GONE);
        viewHolder.getConvertView().setOnClickListener(null);
        if (getItemViewType(position) == WorkDetails) {// 事务详细内容
            final GetWorkMsgDetails work = (GetWorkMsgDetails) getItem(position);
            String url = mainURL + ConstantUrl.photoURL + "?AccID="
                    + work.getModel().getJiaoBaoHao();
            JSYApplication.getInstance().bitmap.display(sendphoto, url);
            workcontent.setText(work.getModel().getMsgContent());
            if (WorkType == 1) {// 我发的
                if (mData.size() == 1) {
                    time.setText(mContext.getResources().getString(
                            R.string.pullDown_toLoadMore)
                            + work.getModel().getRecDate());
                } else {
                    time.setText(work.getModel().getRecDate());
                }
                reply.setVisibility(View.GONE);
                // reply.setText("再发");
            } else if (WorkType == 2) {// 发给我的
                if (mData.size() == 1) {
                    time.setText(mContext.getResources().getString(
                            R.string.pullDown_toLoadMore)
                            + work.getModel().getRecDate());
                } else {
                    time.setText(work.getModel().getRecDate());
                }
                reply.setVisibility(View.GONE);
            } else if (WorkType == 31) {// 我发的信息详情
                time.setText(work.getModel().getRecDate());
                reply.setVisibility(View.GONE);
                // reply.setText("再发");
            } else if (WorkType == 32) {// 发给我的信息详情
                time.setText(work.getModel().getRecDate());
                reply.setVisibility(View.GONE);
            }
            final ArrayList<Attlist> attList = GsonUtil.GsonToList(work
                            .getModel().getAttList(),
                    new TypeToken<ArrayList<Attlist>>() {
                    }.getType());
            if (attList != null) {
                for (int i = 0; i < attList.size(); i++) {
                    final Attlist att = attList.get(i);
                    final TextView tv = new TextView(mContext);
                    tv.setTag(att);
                    tv.setOnClickListener(onclickListener);
                    tv.setPadding(0, 20, 0, 0);
                    tv.setTextColor(mContext.getResources().getColor(
                            R.color.file_name));
                    attLayout.addView(tv);
                    final String[] names = att.getOrgFilename().split("\\.");
                    // if (names.length==2) {
                    if (names[names.length - 1].equals("jpg")
                            || names[names.length - 1].equals("jpeg")
                            || names[names.length - 1].equals("png")
                            || names[names.length - 1].equals("bmp")) {// 图片
                        tv.setLayoutParams(new LinearLayout.LayoutParams(
                                mContext.getResources().getDimensionPixelSize(
                                        R.dimen.px_to_dip_100), mContext
                                .getResources().getDimensionPixelSize(
                                        R.dimen.px_to_dip_100)));
                        downLoadPic(tv, att);
                        tv.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (mMediaPlayer.isPlaying()) {// 关掉录音
                                    mMediaPlayer.stop();
                                    for (int j = 0; j < recViews.size(); j++) {
                                        recViews.get(j).setBackgroundResource(
                                                R.drawable.chatto_bg_normal);
                                    }
                                }
                                String filePath = JSYApplication.getInstance().FILE_PATH
                                        + att.getOrgFilename();
                                File file = new File(filePath);
                                if (!file.exists()) {
                                    downLoadPic(tv, att);
                                } else {
                                    Intent intent = new Intent(mContext,
                                            SinglePictureDisplayActivity.class);
                                    intent.putExtra("PhotoPath", filePath);
                                    // PictureUtils.openFile(mContext,
                                    // filePath);
                                    Log.d(TAG, filePath);
                                    mContext.startActivity(intent);
                                }
                            }
                        });
                    } else if (names[names.length - 1].equals("aac")
                            || names[names.length - 1].equals("amr")) {// 录音
                        tv.setText(att.getFileSize());
                        tv.setGravity(Gravity.CENTER_VERTICAL);
                        tv.setBackgroundResource(R.drawable.chatto_bg_normal);
                        tv.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.chatto_voice_playing, 0, 0, 0);
                        recViews.add(tv);
                        tv.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                String path = JSYApplication.getInstance().AV_PATH
                                        + att.getOrgFilename();
                                if (path.equals(playingName)) {
                                    if (mMediaPlayer.isPlaying()) {// 关掉录音
                                        mMediaPlayer.stop();
                                        tv.setBackgroundResource(R.drawable.chatto_bg_normal);
                                    } else {
                                        if (new File(path).exists()) {
                                            playMusic(path, tv);
                                        } else {
                                            downLoadAudio(att, tv);
                                        }
                                    }
                                } else {
                                    if (new File(path).exists()) {
                                        playMusic(path, tv);
                                    } else {
                                        downLoadAudio(att, tv);
                                    }
                                }
                            }
                        });
                    } else if (names[names.length - 1].equals("mp4")) {// 视频
                        tv.setLayoutParams(new LinearLayout.LayoutParams(
                                mContext.getResources().getDimensionPixelSize(
                                        R.dimen.px_to_dip_100), mContext
                                .getResources().getDimensionPixelSize(
                                        R.dimen.px_to_dip_100)));
                        tv.setBackgroundResource(R.drawable.play);
                        tv.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                if (mMediaPlayer.isPlaying()) {// 关掉录音
                                    mMediaPlayer.stop();
                                    for (int j = 0; j < recViews.size(); j++) {
                                        recViews.get(j).setBackgroundResource(
                                                R.drawable.chatto_bg_normal);
                                    }
                                }
                                final String filePath = JSYApplication
                                        .getInstance().AV_PATH
                                        + att.getOrgFilename();
                                File file = new File(filePath);
                                if (!file.exists()) {
                                    DialogUtil.getInstance().getDialog(
                                            mContext,
                                            R.string.video_loading_waiting);
                                    DialogUtil.getInstance()
                                            .setCanCancel(false);
                                    downLoadAudio(att, null);
                                } else {
                                    Intent intent = new Intent(
                                            Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file),
                                            "video/mp4");
                                    mContext.startActivity(intent);
                                }
                            }
                        });
                    } else {
                        tv.setText("附件:" + att.getOrgFilename() + "("
                                + att.getFileSize() + ")");
                        tv.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mMediaPlayer.isPlaying()) {// 关掉录音
                                    mMediaPlayer.stop();
                                    for (int j = 0; j < recViews.size(); j++) {
                                        recViews.get(j).setBackgroundResource(
                                                R.drawable.chatto_bg_normal);
                                    }
                                }
                                String filePath = JSYApplication.getInstance().FILE_PATH
                                        + att.getOrgFilename();
                                File file = new File(filePath);
                                if (!file.exists()) {
                                    dialog_down(att);
                                } else {
                                    PictureUtils.openFile(mContext, filePath);
                                }
                            }
                        });
                    }
                    // }else{
                    // tv.setText("附件:"+att.getOrgFilename()+"("+att.getFileSize()+")");
                    // }

                }
            }
            List<FeeBack> feeBackList = work.getFeebackList();
            if (feeBackList != null) {
                for (int i = 0; i < feeBackList.size(); i++) {
                    replyLayout
                            .addView(creatRefView(feeBackList.get(i), parent));
                }
            }
            reply.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,
                            WorkSendAgainActivity.class);
                    intent.putExtra("readlist", work.getModel().getReaderList());
                    mContext.startActivity(intent);
                }
            });
        } else if (getItemViewType(position) == WorkList) {// 事务列表（点击头像进入后，下拉出现）
            if (WorkType == 1) {// 我发的
                final MySendMsg work = (MySendMsg) getItem(position);
                workcontent.setText(work.getMsgContent());
                String url = mainURL + ConstantUrl.photoURL + "?AccID="
                        + JiaoBaoHao;
                JSYApplication.getInstance().bitmap.display(sendphoto, url);
                time.setText(work.getRecDate().replace("T", " "));
                reply.setVisibility(View.VISIBLE);
                reply.setBackgroundResource(R.drawable.icon_workdetails_open);// 详情按钮
                if (work.getNoReadCount() > 0) {
                    unread.setVisibility(View.VISIBLE);
                    unread.setText(String.valueOf(work.getNoReadCount()));
                }
                reply.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(mContext,
                                Work2DetailsListItemActivity.class);
                        intent.putExtra("type", WorkType);
                        intent.putExtra("TabIDStr", work.getTabIDStr());
                        intent.putExtra("MsgRecDate", work.getRecDate());
                        intent.putExtra("ReadFlag", work.getNoReadCount());
                        mContext.startActivity(intent);
                    }
                });
                viewHolder.getConvertView().setOnClickListener(
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(mContext,
                                        Work2DetailsListItemActivity.class);
                                intent.putExtra("type", WorkType);
                                intent.putExtra("TabIDStr", work.getTabIDStr());
                                intent.putExtra("MsgRecDate", work.getRecDate());
                                mContext.startActivity(intent);
                            }
                        });
            } else if (WorkType == 2) {// 发给我的
                final SendToMeMsg work = (SendToMeMsg) getItem(position);
                workcontent.setText(work.getMsgContent());
                String url = mainURL + ConstantUrl.photoURL + "?AccID="
                        + work.getJiaoBaoHao();
                JSYApplication.getInstance().bitmap.display(sendphoto, url);
                time.setText(work.getRecDate().replace("T", " "));
                if (work.getReadFlag() < 2) {// 0未读，1已读未回复，2已回复
                    reply.setVisibility(View.VISIBLE);

                    reply.setBackgroundResource(R.drawable.icon_workdetails_uncomment);//待回复
                } else if (work.getReadFlag() == 2) {// 0未读，1已读未回复，2已回复
                    reply.setVisibility(View.VISIBLE);
                    reply.setBackgroundResource(R.drawable.icon_workdetails_open);//详情
                }
                reply.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(mContext,
                                Work2DetailsListItemActivity.class);
                        intent.putExtra("type", WorkType);
                        intent.putExtra("TabIDStr", work.getTabIDStr());
                        intent.putExtra("MsgRecDate", work.getRecDate());
                        mContext.startActivity(intent);
                    }
                });
                viewHolder.getConvertView().setOnClickListener(
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setClass(mContext,
                                        Work2DetailsListItemActivity.class);
                                intent.putExtra("type", WorkType);
                                intent.putExtra("TabIDStr", work.getTabIDStr());
                                intent.putExtra("MsgRecDate", work.getRecDate());
                                mContext.startActivity(intent);
                            }
                        });
            }
        }
        return viewHolder.getConvertView();
    }

    /**
     * 确认下载附件提示框
     *
     * @param att 附件
     */
    protected void dialog_down(final Attlist att) {
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.hint);
        builder.setIcon(android.R.drawable.ic_dialog_info).setMessage(
                mContext.getResources().getString(
                        R.string.beSure_toLoad_enclosure)
                        + att.getOrgFilename() + "？");
        builder.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // HttpHandler handler =
                        Work2DetailsListActivityController.getInstance()
                                .downloadAtt(att);
                        // httpDownList.add(handler);
                    }
                });
        builder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    ArrayList<HttpHandler> httpHandlerList = new ArrayList<>();

    private View creatRefView(FeeBack feeBack, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.work2details_reply, parent,
                false);
        ImageView photo = (ImageView) layout
                .findViewById(R.id.workdetails_reply_photo);
        TextView content = (TextView) layout
                .findViewById(R.id.workdetails_reply);
        TextView author = (TextView) layout
                .findViewById(R.id.workdetails_reply_author);
        TextView time = (TextView) layout
                .findViewById(R.id.workdetails_reply_time);
        content.setText(feeBack.getFeeBackMsg().trim());
        author.setText(feeBack.getUserName());
        time.setText(feeBack.getRecDate().replace("T", " "));
        String url = mainURL + ConstantUrl.photoURL + "?AccID="
                + feeBack.getJiaobaohao();
        JSYApplication.getInstance().bitmap.display(photo, url);
        return layout;
    }

    /**
     * 下载图片
     *
     * @param view f
     * @param att  c
     */
    private void downLoadPic(final View view, Attlist att) {
        final String filePath = JSYApplication.getInstance().FILE_PATH
                + att.getOrgFilename();
        File file = new File(filePath);
        if (!file.exists()) {
            boolean inList = false;
            for (int i = 0; i < httpHandlerList.size(); i++) {
                HttpHandler handler = httpHandlerList.get(i);
                try {
                    if (handler.getRequestCallBack().getRequestUrl()
                            .equals(att.getDlurl())) {
                        inList = true;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!inList) {
                DialogUtil.getInstance().getDialog(mContext, R.string.loading);
                HttpHandler handler = HttpUtil.getInstanceNew().download(
                        att.getDlurl(), filePath, new RequestCallBack<File>() {
                            private String TAG = "0000000";

                            @Override
                            public void onSuccess(ResponseInfo<File> arg0) {
                                Log.d(TAG, "success");
                                DialogUtil.getInstance().cannleDialog();
                                bitmapUtils.display(view, filePath);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onLoading(long total, long current,
                                                  boolean isUploading) {
                                super.onLoading(total, current, isUploading);

                            }

                            @Override
                            public void onFailure(HttpException arg0,
                                                  String arg1) {
                                bitmapUtils.display(view, filePath);
                                notifyDataSetChanged();
                            }
                        });
                httpHandlerList.add(handler);
            }
        } else {
            bitmapUtils.display(view, filePath);
        }
    }

    /**
     * 下载视频或录音
     *
     * @param att d
     * @param tv  d
     */
    private void downLoadAudio(Attlist att, final TextView tv) {
        final String filePath = JSYApplication.getInstance().AV_PATH
                + att.getOrgFilename();
        File file = new File(filePath);
        if (!file.exists()) {
            boolean inList = false;
            for (int i = 0; i < httpHandlerList.size(); i++) {
                HttpHandler handler = httpHandlerList.get(i);
                try {
                    if (handler.getRequestCallBack().getRequestUrl()
                            .equals(att.getDlurl())) {
                        inList = true;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!inList) {
                HttpHandler handler = HttpUtil.getInstanceNew().download(
                        att.getDlurl(), filePath, new RequestCallBack<File>() {

                            @Override
                            public void onSuccess(ResponseInfo<File> arg0) {
                                ToastUtil.showMessage(mContext, "下载成功");
                                if (tv != null) {
                                    playMusic(filePath, tv);
                                } else {
                                    File file2 = new File(filePath);
                                    DialogUtil.getInstance().cannleDialog();
                                    Intent intent = new Intent(
                                            Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(file2),
                                            "video/mp4");
                                    mContext.startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(HttpException arg0,
                                                  String arg1) {
                                DialogUtil.getInstance().cannleDialog();
                                ToastUtil.showMessage(mContext,
                                        R.string.load_failed);
                            }
                        });
                httpHandlerList.add(handler);
            }
        }
    }

    public MediaPlayer mMediaPlayer = new MediaPlayer();
    private String playingName;
    private TextView lastTextView;

    /**
     * 播放录音
     *
     * @param name n
     * @param tv   tv
     */
    private void playMusic(String name, final TextView tv) {
        try {
            if (null != lastTextView) {
                lastTextView.setBackgroundResource(R.drawable.chatto_bg_normal);
            }
            tv.setBackgroundResource(R.drawable.chatto_bg_pressed);
            lastTextView = tv;
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(name);
            playingName = name;
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    tv.setBackgroundResource(R.drawable.chatto_bg_normal);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
