package com.jsy_jiaobao.po.qiuzhi;

//观察者  
public interface Watcher {
	public void update(QuestionIndexItem qEntity);
	public void update(AnswerItem answer);
}