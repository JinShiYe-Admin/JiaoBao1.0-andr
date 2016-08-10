package com.jsy_jiaobao.po.qiuzhi;

//被观察者  
public interface Watched {
	void addWatcher(Watcher watcher);

	void removeWatcher(Watcher watcher);
	/***<pre>
	 * RecommentIndexItem 推荐
	 * QuestionIndexItem 首页和教学后
	 */
	void notifyWatcher(QuestionIndexItem qEntity);
	
}