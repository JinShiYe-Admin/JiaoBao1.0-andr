package com.jsy.xuezhuli.utils;

import com.jsy_jiaobao.main.BaseActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class HttpUtil {
    private HttpUtil() { }
    
    /**
     * Initializes HttpUtil.
     *
     * HttpUtilHolder is loaded on the first execution of HttpUtil.getInstance()
     * or the first access to HttpUtilHolder.INSTANCE, not before.
     */
    private static class HttpUtilHolder {
        private static final HttpUtils INSTANCE = new HttpUtils(30000);
        private static final HttpUtils INSTANCE_SESSION = new HttpUtils(30000);
    }

    public static HttpUtils getInstance() {
        return HttpUtilHolder.INSTANCE_SESSION;
    }
    public static HttpUtils getInstanceNew() {
    	return HttpUtilHolder.INSTANCE;
    }
    public static HttpHandler<String> InstanceSend(String url,RequestParams params,RequestCallBack<String> callback){
    	BaseActivity.HttpPost.clear();
    	BaseActivity.HttpPost.add(0,url);
		BaseActivity.HttpPost.add(1,params);
		BaseActivity.HttpPost.add(2,callback);
    	return HttpUtilHolder.INSTANCE_SESSION.send(HttpRequest.HttpMethod.POST, url, params, callback);
    }
    public static HttpHandler<String> InstanceNewSend(String url,RequestParams params,RequestCallBack<String> callback){
    	BaseActivity.HttpPost.clear();
    	BaseActivity.HttpPost.add(0,url);
		BaseActivity.HttpPost.add(1,params);
		BaseActivity.HttpPost.add(2,callback);
    	return HttpUtilHolder.INSTANCE.send(HttpRequest.HttpMethod.POST, url, params, callback);
    }
}
