package com.jsy_jiaobao.main.personalcenter;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;

import com.google.gson.reflect.TypeToken;
import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.GsonUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.jsy_jiaobao.po.qiuzhi.AnswerComment;
import com.jsy_jiaobao.po.qiuzhi.AnswerDetails;
import com.jsy_jiaobao.po.qiuzhi.GetAnswerComments;
import com.jsy_jiaobao.po.qiuzhi.QuestionDetails;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 求知答案详情的Controller
 *
 * @author admin
 */
public class QiuZhiQuestionAnswerDetailsActivityController implements
        ConstantUrl {
    private static QiuZhiQuestionAnswerDetailsActivityController instance;
    private Activity mContext;

    public static synchronized QiuZhiQuestionAnswerDetailsActivityController getInstance() {
        if (instance == null) {
            instance = new QiuZhiQuestionAnswerDetailsActivityController();
        }
        return instance;
    }

    public QiuZhiQuestionAnswerDetailsActivityController setContext(
            Activity pActivity) {
        mContext = pActivity;
        return this;
    }

    /**
     * <pre>
     * 功能：获取一个答案明细信息，包括问题内容;
     * 参数名称	是否必须	类型	描述
     * AId	是	int	答案ID
     */
    public void AnswerDetail(int AId) {
        DialogUtil.getInstance().getDialog(mContext,
                mContext.getResources().getString(R.string.public_loading));
        DialogUtil.getInstance().setCanCancel(false);
        RequestParams params = new RequestParams();
        params.addBodyParameter("AId", String.valueOf(AId));
        params.addBodyParameter("byUrl", "1");
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_qiuzhi_AnswerDetail);
        HttpUtil.InstanceSend(AnswerDetail, params, callback);
    }

    /**
     * 功能：获取一个问题明细信息，包括问题内容;
     * <p/>
     * <pre>
     * byUrl 0:1 ==获取Html:获取Url
     */
    public void QuestionDetail(int QId) {
        DialogUtil.getInstance().getDialog(mContext,
                mContext.getResources().getString(R.string.public_loading));
        RequestParams params = new RequestParams();
        params.addBodyParameter("QId", String.valueOf(QId));
        params.addBodyParameter("byUrl", "1");
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_qiuzhi_QuestionDetail);
        HttpUtil.InstanceSend(QuestionDetail, params, callback);
    }

    /**
     * <pre>
     * 功能：.获取答案的评论 列表;
     * 参数名称	是否必须	类型	描述
     * numPerPage	否	int	取回的记录数量，默认20
     * pageNum	否	int	第几页，默认为1
     * AId	是	int	答案Id
     * RowCount	是	int
     * @param AId aId
     */
    public void CommentsList(int AId, int pageNum, int RowCount) {
        if (!DialogUtil.getInstance().isDialogShowing()) {
            DialogUtil.getInstance().getDialog(mContext,
                    mContext.getResources().getString(R.string.public_loading));
            DialogUtil.getInstance().setCanCancel(false);
            System.out.println("-----comment list");
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("AId", String.valueOf(AId));
        params.addBodyParameter("pageNum", String.valueOf(pageNum));
        params.addBodyParameter("RowCount", String.valueOf(RowCount));
        params.addBodyParameter("numPerPage", String.valueOf(20));
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_qiuzhi_CommentsList);
        HttpUtil.InstanceSend(KnlCommentsList, params, callback);

    }

    /**
     * <pre>
     * 举报答案
     * 参数名称	是否必须	类型	描述
     * ansId	是	int	答案ID
     * repType  0=答案1=问题2=评论
     * @param AId a
     */
    public void ReportAns(int AId, int repType) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("ansId", String.valueOf(AId));
        params.addBodyParameter("repType", String.valueOf(repType));
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_qiuzhi_ReportAns);
        HttpUtil.InstanceSend(ReportAns, params, callback);
    }

    /**
     * <pre>
     * 评价答案，支持或反对;
     * 参数名称	是否必须	类型	描述
     * AId	是	int	答案ID
     * yesNoFlag	是	int	0=反对，1=支持
     */
    public void SetYes(int AId) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("AId", String.valueOf(AId));
        params.addBodyParameter("yesNoFlag", "1");
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_qiuzhi_SetYes);
        HttpUtil.InstanceSend(SetYesNo, params, callback);

    }

    /**
     * <pre>
     * 评价答案，支持或反对;
     * 参数名称	是否必须	类型	描述
     * AId	是	int	答案ID
     * yesNoFlag	是	int	0=反对，1=支持
     */
    public void SetNo(int AId) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("AId", String.valueOf(AId));
        params.addBodyParameter("yesNoFlag", "0");
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_qiuzhi_SetNo);
        HttpUtil.InstanceSend(SetYesNo, params, callback);

    }

    /**
     * <pre>
     * 对答案添加评论;
     * 参数名称	是否必须	类型	描述
     * AId	是	int	答案Id
     * comment	是	string	评论内容
     * RefID	否	string	引用评论ID
     */
    public void AddComment(int AId, String comment, int RefID) {
        DialogUtil.getInstance().getDialog(mContext,
                mContext.getResources().getString(R.string.public_loading));
        DialogUtil.getInstance().setCanCancel(false);
        RequestParams params = new RequestParams();
        params.addBodyParameter("RefID", String.valueOf(RefID));
        params.addBodyParameter("AId", String.valueOf(AId));
        params.addBodyParameter("comment", comment);
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_qiuzhi_AddComment);
        HttpUtil.InstanceSend(AddComment, params, callback);

    }

    /**
     * <pre>
     * 功能：对评论进行顶或踩的操作
     * 参数名称	是否必须	类型	描述
     * tabid	是	int	评论ID,不加密，直接是int
     * tp	是	int	顶=1，踩=0
     */
    public void AddScore(final AnswerComment item, final int tp) {
        DialogUtil.getInstance().getDialog(mContext, "正在更新数据");
        RequestParams params = new RequestParams();
        params.addBodyParameter("tabid", String.valueOf(item.getTabID()));
        params.addBodyParameter("tp", String.valueOf(tp));
        HttpUtil.InstanceSend(AddScoreKnl, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if (tp == 1) {
                            item.setLikeCount(item.getLikeCount() + 1);
                        } else {
                            item.setCaiCount(item.getCaiCount() + 1);
                        }
                        item.setAddScore(true);
                        ArrayList<Object> post = new ArrayList<>();
                        post.add(Constant.msgcenter_qiuzhi_AddScoreKnl);
                        post.add(item);
                        post.add(tp);
                        EventBusUtil.post(post);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dealResponseInfo("",
                                Constant.msgcenter_qiuzhi_AddScoreKnl);
                    }
                });

    }

    private class CallBack extends RequestCallBack<String> {

        @Override
        public void onFailure(HttpException arg0, String arg1) {
            if (null != mContext) {
                dealResponseInfo("", this.getUserTag());
                DialogUtil.getInstance().cannleDialog();
                if (BaseUtils.isNetworkAvailable(mContext)) {
                    ToastUtil.showMessage(mContext, R.string.phone_no_web);
                } else {
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.public_syserr));
                }
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> arg0) {
            if (null != mContext) {
                try {
                    JSONObject jsonObj = new JSONObject(arg0.result);
                    String ResultCode = jsonObj.getString("ResultCode");
                    if ("0".equals(ResultCode)) {
                        switch ((Integer) this.getUserTag()) {
                            case Constant.msgcenter_qiuzhi_AnswerDetail:
                            case Constant.msgcenter_qiuzhi_CommentsList:
                            case Constant.msgcenter_qiuzhi_SetYes:
                            case Constant.msgcenter_qiuzhi_SetNo:
                            case Constant.msgcenter_qiuzhi_ReportAns:
                            case Constant.msgcenter_qiuzhi_QuestionDetail:
                                dealResponseInfo(jsonObj.getString("Data"),
                                        this.getUserTag());
                                break;
                            case Constant.msgcenter_qiuzhi_AddComment:
                                dealResponseInfo("1", this.getUserTag());
                                ToastUtil.showMessage(mContext,
                                        jsonObj.getString("ResultDesc"));
                                break;
                            default:
                                String data = Des.decrypt(
                                        jsonObj.getString("Data"),
                                        BaseActivity.sp_sys.getString("ClientKey",
                                                ""));
                                dealResponseInfo(data, this.getUserTag());
                                break;
                        }
                    } else if ("8".equals(ResultCode)) {
                        dealResponseInfo("", this.getUserTag());
                        LoginActivityController.getInstance().helloService(
                                mContext);
                    } else {
                        switch ((Integer) this.getUserTag()) {
                            case Constant.msgcenter_qiuzhi_ReportAns:
                                dealResponseInfo(jsonObj.getString("Data"),
                                        this.getUserTag());
                                break;
                            default:
                                ToastUtil.showMessage(mContext,
                                        jsonObj.getString("ResultDesc"));
                                dealResponseInfo("", this.getUserTag());
                                break;
                        }
                    }
                } catch (Exception e) {
                    dealResponseInfo("", this.getUserTag());
                    ToastUtil.showMessage(mContext, mContext.getResources()
                            .getString(R.string.error_serverconnect) + "r1002");
                }
            }
        }

    }

    private void dealResponseInfo(String result, Object userTag) {
        ArrayList<Object> post = new ArrayList<>();
        post.add(userTag);
        switch ((Integer) userTag) {
            case Constant.msgcenter_qiuzhi_AnswerDetail:
                AnswerDetails answer = GsonUtil.GsonToObject(result,
                        AnswerDetails.class);
                post.add(answer);
                break;
            case Constant.msgcenter_qiuzhi_CommentsList:
                DialogUtil.getInstance().cannleDialog();
                GetAnswerComments comments = GsonUtil.GsonToObject(result,
                        GetAnswerComments.class);
                post.add(comments);
                break;
            case Constant.msgcenter_qiuzhi_ReportAns:
                DialogUtil.getInstance().cannleDialog();
                post.add(result);
                break;
            case Constant.msgcenter_qiuzhi_SetYes:
                post.add(result);
                break;
            case Constant.msgcenter_qiuzhi_SetNo:
                post.add(result);
                break;
            case Constant.msgcenter_qiuzhi_AddComment:
                post.add(result);
                break;
            case Constant.msgcenter_qiuzhi_QuestionDetail:
                DialogUtil.getInstance().cannleDialog();
                QuestionDetails questiondetails = GsonUtil.GsonToList(result,
                        new TypeToken<QuestionDetails>() {
                        }.getType());
                post.add(questiondetails);
                break;
            default:
                post.add("");
                break;
        }
        EventBusUtil.post(post);
    }
}
