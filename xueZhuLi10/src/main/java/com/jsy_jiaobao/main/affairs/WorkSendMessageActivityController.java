package com.jsy_jiaobao.main.affairs;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.jsy.xuezhuli.utils.BaseUtils;
import com.jsy.xuezhuli.utils.Constant;
import com.jsy.xuezhuli.utils.ConstantUrl;
import com.jsy.xuezhuli.utils.Des;
import com.jsy.xuezhuli.utils.DialogUtil;
import com.jsy.xuezhuli.utils.EventBusUtil;
import com.jsy.xuezhuli.utils.HttpUtil;
import com.jsy.xuezhuli.utils.ToastUtil;
import com.jsy_jiaobao.main.BaseActivity;
import com.jsy_jiaobao.main.R;
import com.jsy_jiaobao.main.system.LoginActivityController;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class WorkSendMessageActivityController implements ConstantUrl {
    private static WorkSendMessageActivityController instance;
    private Activity mcontext;
    private boolean msgall = false;

    public static synchronized WorkSendMessageActivityController getInstance() {
        if (instance == null) {
            instance = new WorkSendMessageActivityController();
        }
        return instance;
    }

    public WorkSendMessageActivityController setContext(Activity pActivity) {
        mcontext = pActivity;
        return this;
    }

    /**
     * 取接收人类表
     */
    public void GetMsgAllReviceUnitList() {
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_work_GetMsgAllReviceUnitList);
        HttpUtil.InstanceSend(GetMsgAllReviceUnitList, null, callback);

    }

    /**
     * 取接收人类表
     */
    public void CommMsgRevicerList(boolean msgall) {
        this.msgall = msgall;
        RequestParams params = new RequestParams();
        params.addBodyParameter("msgall", String.valueOf(msgall));
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_work_CommMsgRevicerList);
        HttpUtil.InstanceSend(CommMsgRevicerList, params, callback);

    }

    /**
     * 1.10 取接收人列表
     */
    public void CommMsgRevicerUnitList() {
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_work_CommMsgRevicerUnitList);
        HttpUtil.InstanceSend(CommMsgRevicerUnitList, null, callback);

    }

    /**
     * 功能：获取单位接收人
     *
     * @param params p
     * @param tag    t
     */
    public void GetUnitRevicer(RequestParams params, ArrayList<Object> tag) {
        int selit = (Integer) tag.get(1);
        GetReciverCallBack callback = new GetReciverCallBack();
        callback.setUserTag(tag);
        if (selit == 1) {
            HttpUtil.InstanceSend(GetUnitRevicer, params, callback);
        } else {
            HttpUtil.InstanceSend(GetUnitClassRevice, params, callback);

        }
    }

    /**
     * 发送信息
     *
     * @param params p
     */
    public void CreateCommMsg(RequestParams params) {
        DialogUtil.getInstance().getDialog(mcontext, mcontext.getResources().getString(R.string.public_later));
        DialogUtil.getInstance().setCanCancel(false);
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_work_CreateCommMsg);
        HttpUtil.InstanceSend(CreateCommMsg, params, callback);

    }

    /**
     * 功能：群发给下属单位的接收者，返回所有下属单位的管理员列表
     */
    public void GetMsgAllRevicer_toSubUnit() {
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_work_GetMsgAllRevicer_toSubUnit);
        HttpUtil.InstanceSend(GetMsgAllRevicer_toSubUnit, null, callback);

    }

    /**
     * 获取群发家长的接收对象  功能：群发给下属单位的接收者，返回所有下属单位的管理员列表
     */
    public void GetMsgAllRevicer_toSchoolGen() {
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_work_GetMsgAllRevicer_toSchoolGen);
        HttpUtil.InstanceSend(GetMsgAllRevicer_toSchoolGen, null, callback);

    }

    /**
     * 发送信息
     */
    public void SMSCommIndex() {
        CallBack callback = new CallBack();
        callback.setUserTag(Constant.msgcenter_work_SMSCommIndex);

        HttpUtil.InstanceSend(SMSCommIndex, null, callback);
    }

    private class CallBack extends RequestCallBack<String> {

        @Override
        public void onFailure(HttpException arg0, String arg1) {
            if (null != mcontext) {
                dealResponseInfo("", this.getUserTag());
                if (BaseUtils.isNetworkAvailable(mcontext)) {
                    ToastUtil.showMessage(mcontext, R.string.phone_no_web);
                } else {
                    ToastUtil.showMessage(mcontext, mcontext.getResources().getString(R.string.error_internet));

                }
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> arg0) {
            if (null != mcontext) {
                try {
                    JSONObject jsonObj = new JSONObject(arg0.result);
                    String ResultCode = jsonObj.getString("ResultCode");
                    if ("0".equals(ResultCode)) {
                        switch ((Integer) this.getUserTag()) {
                            case Constant.msgcenter_work_CommMsgRevicerUnitList:
                            case Constant.msgcenter_work_GetMsgAllRevicer_toSubUnit:
                            case Constant.msgcenter_work_GetMsgAllRevicer_toSchoolGen:
                            case Constant.msgcenter_work_GetMsgAllReviceUnitList:
                                dealResponseInfo(jsonObj.getString("Data"), this.getUserTag());
                                break;
                            default:
                                String data = Des.decrypt(jsonObj.getString("Data"), BaseActivity.sp_sys.getString("ClientKey", ""));
                                dealResponseInfo(data, this.getUserTag());
                                break;
                        }

                    } else if ("8".equals(ResultCode)) {
                        dealResponseInfo("", this.getUserTag());
                        LoginActivityController.getInstance().helloService(mcontext);
                    } else {
                        if ((Integer) getUserTag() != Constant.msgcenter_work_CreateCommMsg) {
                            ArrayList<Object> post = new ArrayList<>();
                            post.add(Constant.msgcenter_work_geterror);
                            EventBusUtil.post(post);
                        }
                        ToastUtil.showMessage(mcontext, jsonObj.getString("ResultDesc"));
                        dealResponseInfo("", this.getUserTag());
                    }
                } catch (Exception e) {
                    dealResponseInfo("", this.getUserTag());
                    ToastUtil.showMessage(mcontext, mcontext.getResources().getString(R.string.error_serverconnect) + "r1002");
                }
            }
        }

    }

    private class GetReciverCallBack extends RequestCallBack<String> {

        @Override
        public void onFailure(HttpException arg0, String arg1) {
            if (null != mcontext) {
                if (BaseUtils.isNetworkAvailable(mcontext)) {
                    ToastUtil.showMessage(mcontext, R.string.phone_no_web);
                } else {
                    ToastUtil.showMessage(mcontext, mcontext.getResources().getString(R.string.error_internet));

                }
            }
        }

        @Override
        public void onSuccess(ResponseInfo<String> arg0) {
            if (null != mcontext) {
                try {
                    JSONObject jsonObj = new JSONObject(arg0.result);
                    String ResultCode = jsonObj.getString("ResultCode");
                    if ("0".equals(ResultCode)) {
                        ArrayList<Object> post = new ArrayList<>();
                        post.add(Constant.msgcenter_work_GetUnitRevicer);
                        post.add(jsonObj.getString("Data"));
                        post.add(this.getUserTag());
                        EventBusUtil.post(post);
                    } else if ("8".equals(ResultCode)) {
                        LoginActivityController.getInstance().helloService(mcontext);
                    } else {
                        ToastUtil.showMessage(mcontext, jsonObj.getString("ResultDesc"));
                    }
                } catch (Exception e) {
                    ToastUtil.showMessage(mcontext, mcontext.getResources().getString(R.string.error_serverconnect) + "r1002");
                }
            }
        }

    }

    private void dealResponseInfo(String result, Object userTag) {
        ArrayList<Object> post = new ArrayList<>();
        switch ((Integer) userTag) {
            case Constant.msgcenter_work_CommMsgRevicerList:
                post.add(Constant.msgcenter_work_CommMsgRevicerList);
                if (msgall) {
                    BaseActivity.mCache.put("allUnitID", BaseActivity.sp.getInt("UnitID", 0) + "");
                    BaseActivity.mCache.put("allrevicerdata", result);
                    BaseActivity.mCache.put("AllJiaoBaoHao", BaseActivity.sp.getString("JiaoBaoHao", ""));
                } else {
                    BaseActivity.mCache.put("sbUnitID", BaseActivity.sp.getInt("UnitID", 0) + "");
                    BaseActivity.mCache.put("sbrevicerdata", result);
                    BaseActivity.mCache.put("SbJiaoBaoHao", BaseActivity.sp.getString("JiaoBaoHao", ""));
                }
                break;
            case Constant.msgcenter_work_CreateCommMsg:
                post.add(Constant.msgcenter_work_CreateCommMsg);
                DialogUtil.getInstance().cannleDialog();
                break;
            case Constant.msgcenter_work_SMSCommIndex:
                post.add(Constant.msgcenter_work_SMSCommIndex);
                result = "{\"list\":" + result + "}";
                BaseActivity.mCache.put("SMSUnitID", BaseActivity.sp.getInt("UnitID", 0) + "");
                BaseActivity.mCache.put("SMSrevicerdata", result);
                BaseActivity.mCache.put("SMSJiaoBaoHao", BaseActivity.sp.getString("JiaoBaoHao", ""));
                break;
            case Constant.msgcenter_work_CommMsgRevicerUnitList:
                post.add(Constant.msgcenter_work_CommMsgRevicerUnitList);
                post.add(result);
                break;
            case Constant.msgcenter_work_GetMsgAllRevicer_toSubUnit:
                post.add(Constant.msgcenter_work_GetMsgAllRevicer_toSubUnit);
                post.add(result);
                break;
            case Constant.msgcenter_work_GetMsgAllRevicer_toSchoolGen:
                post.add(Constant.msgcenter_work_GetMsgAllRevicer_toSchoolGen);
                post.add(result);
                break;
            case Constant.msgcenter_work_GetMsgAllReviceUnitList:
                post.add(Constant.msgcenter_work_GetMsgAllReviceUnitList);
                try {
                    JSONObject obj = new JSONObject(result);
                    HashMap<String, Boolean> map = new HashMap<>();
                    map.put("MsgAll_ToSubUnitMem", obj.getBoolean("MsgAll_ToSubUnitMem"));
                    map.put("MsgAll_ToGen", obj.getBoolean("MsgAll_ToGen"));
                    post.add(map);
                } catch (JSONException e) {
                    post.add(null);
                }
                break;
            default:
                break;
        }
        EventBusUtil.post(post);
    }
}
