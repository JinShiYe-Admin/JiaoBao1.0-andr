package com.jsy_jiaobao.po.qiuzhi;

//被观察者  
public interface Watched {
	public void addWatcher(Watcher watcher);

	public void removeWatcher(Watcher watcher);
	/***<pre>
	 * RecommentIndexItem 推荐
	 * QuestionIndexItem 首页和教学后
	 * @param rEntity
	 * @param qEntity
	 */
	public void notifyWatcher(QuestionIndexItem qEntity);
	
}