package com.cloud.manga.henryTing.thread;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/thread/TouchUnit.java
*/

public class TouchUnit<T> {
	
	public final T _point;
	public static Setting _setting;
	// millisecond
	final long _time = System.currentTimeMillis();
	final long _level;
	boolean _activeState = true;
	public TouchUnit(T point, long level) {
		_point = point;
		_level = level;
	}
	// public TouchUnit() {
		// super(-1, -1);
		// _level = 0;
	// }
	public boolean isActive() {
		return _activeState;
	}
	public void setActiveOff() {
		_activeState = false;
	}
	public boolean isNotOnTime(TouchUnit touchUnit) {
		return (touchUnit._time-_time) < _setting.getCmdTimeMin();
	}
	public boolean isNotOnLevel(TouchUnit touchUnit) {
		return isNotOnLevel(touchUnit._level);
	}
	public boolean isNotOnLevel(long level) {
		// System.out.print("level = ");
		// System.out.println(level);
		// System.out.print("_level = ");
		// System.out.println(_level);
		
		return level != _level;
	}
	public T toType() {
		return _point;
	}
	public boolean isNull() {
		return _point == null;
	}
}