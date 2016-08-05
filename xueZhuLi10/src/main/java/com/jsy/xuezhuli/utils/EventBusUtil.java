package com.jsy.xuezhuli.utils;


import org.greenrobot.eventbus.EventBus;



public class EventBusUtil {

	private static EventBus instance;

	private static EventBus getEventBus() {
		if (instance == null) {
			instance = new EventBus();
		}
		return instance;
	}

	public static void register(Object subscriber) {
		getEventBus().register(subscriber);
	}

	public static void post(Object obj) {
		getEventBus().post(obj);
	}


	public static void unregister(Object subscriber) {
		getEventBus().unregister(subscriber);
	}
	
}
