package com.jsy_jiaobao.po.qiuzhi;

import java.util.LinkedList;
import java.util.List;

public class WatchedEntityIndexItem implements Watched {
	private static WatchedEntityIndexItem instance;

	public static synchronized final WatchedEntityIndexItem getInstance() {
		if (instance == null) {
			instance = new WatchedEntityIndexItem();
		}
		return instance;
	}
	private List<Watcher> list = new LinkedList<Watcher>();

	@Override
	public void addWatcher(Watcher watcher) {
		list.add(watcher);
	}

	@Override
	public void removeWatcher(Watcher watcher) {
		try {
			list.remove(watcher);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void notifyWatcher(QuestionIndexItem qEntity) {
		if (list.size() == 0 || list == null)
			return;
		for (Watcher watcher : list) {
			watcher.update(qEntity);
		}
	}

	public void notifyAnswerList(AnswerItem answer){
		if (list.size() == 0 || list == null)
			return;
		for (Watcher watcher : list) {
			watcher.update(answer);
		}
		
	}
}