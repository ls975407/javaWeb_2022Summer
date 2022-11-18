package com.cloud.manga.henryTing.thread;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/thread/Thread.java
*/

public class Thread {
	public static void initialize(Setting setting) {
		ThreadImp._setting = setting;
		TouchDelayer._setting = setting;
		TouchUnit._setting = setting;
	}
}