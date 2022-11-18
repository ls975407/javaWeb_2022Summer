package com.cloud.manga.henryTing.thread;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/thread/TouchDelayer.java
*/

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.SDEHandle;


public class TouchDelayer<T> {
	public static Setting _setting;
	public volatile boolean WORKING = true;
	public volatile TouchS.Actor<T> _touchActor;
	private final LinkedBlockingQueue<TouchUnit<T>> queue = new LinkedBlockingQueue<>();
	private volatile boolean isStart = false;
	private final ExecutorService _service = Executors.newSingleThreadExecutor();
	private TouchS<T> _touchS = new TouchS<>();
	public static <T> TouchDelayer<T> create() {
		TouchDelayer<T> ptr = new TouchDelayer<>();
		ptr.start();
		return ptr;
	}
	private void start() {
		if (isStart) { return; }
		_service.execute( new Runnable() { public void run() {
			collectTouch();
		}});
		isStart = true;
	}
	private void reportInterrupted() {
		_touchActor.reportException(
			SDEError.cTouchDelayerInterrupted()
		);
		isStart = false;
	}
	private void reportClose() {
		WORKING = false;
		_touchActor.reportException(
			SDEHandle.cTouchDelayerClose()
		);
		isStart = false;
	}
	
	private boolean _isLegal(TouchUnit<T> touchUnit) {
		return touchUnit.isActive() && WORKING;
	}
	
	private void collectTouch() {
		TouchUnit<T> touchUnit;
		while(true) {
			try {
				touchUnit = queue.take();
			} catch(InterruptedException e) {
				reportInterrupted(); isStart = false; return;
			}
			if (!_isLegal(touchUnit)) {
				reportClose(); continue;
			}
			if (touchUnit.isNull()) { continue; }
			break;
		}
		long level = touchUnit._level;
		do {
			level = Math.max(level, touchUnit._level);
			_touchS.add(touchUnit);
			try {
				touchUnit = queue.poll(
					_setting.getCmdTimeMax(), 
					TimeUnit.MILLISECONDS 
				);
			} catch(InterruptedException e) {
				reportInterrupted(); return;
			}
		} while(touchUnit != null && _isLegal(touchUnit)); // && !touchUnit.isNull());
		if (touchUnit != null && !_isLegal(touchUnit)) {
			reportClose(); return;
		}
		
		// summitAction
		try {
			_touchActor.summitAction(_touchS.asList(level));
		} catch (Exception e) {
			e.printStackTrace();
		}

		_touchS.clear();
		
		isStart = false;
		start();
	}
	public boolean sendATouch(TouchUnit<T> touchUnit) {
		if (!WORKING) { return false; }
		start();
		try {
			queue.put(touchUnit);
		} catch(InterruptedException e) {
			reportInterrupted(); return false;
		}
		return true;
	}
	public boolean sendASkip(long level) {
		return sendATouch(new TouchUnit<T>(null, level));
	}
	public void shutdown() {
		TouchUnit<T> touchUnit = new TouchUnit<>(null, 0);
		touchUnit.setActiveOff();
		sendATouch(touchUnit);
//		_service.shutdown();
	}
}

