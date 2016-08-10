package com.jsy_jiaobao.po.qiuzhi;

//观察者  
public interface Watcher {
	void update(QuestionIndexItem qEntity);
	void update(AnswerItem answer);
}