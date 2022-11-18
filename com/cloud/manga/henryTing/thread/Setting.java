package com.cloud.manga.henryTing.thread;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/thread/Setting.java
*/
public interface Setting {
	// ThreadImp
	int getRangeNextPg();
	int getRangePrevPg();
	int getRangeNextCh();
	int getRangePrevCh();
	int getNumThread();
	// TouchDelayer
	long getCmdTimeMax();
	// TouchUnit
	long getCmdTimeMin();
} 