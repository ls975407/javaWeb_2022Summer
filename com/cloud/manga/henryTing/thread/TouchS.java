package com.cloud.manga.henryTing.thread;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/thread/TouchS.java
*/

import java.util.ArrayList;
import java.util.List;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.Point;
import com.cloud.manga.henryTing.unit.Log;

public class TouchS<T> {
	
	// public static Log _log = new Log("TouchS");
	
	public interface Actor<T> {
		void summitAction(List<T> points);
		void reportException(SDEBase e);
	}
	
	private TouchUnit<T> _lastOne = null;
	List<TouchUnit<T>> _list = new ArrayList<TouchUnit<T>>();
	public void add(TouchUnit<T> touchUnit) {
		if (_lastOne != null) {
			if (_lastOne.isNotOnTime(touchUnit)) {
				// _log.w("fail to add " + _lastOne.toType().toString());
				return;
			}
		}
		_lastOne = touchUnit;
		_list.add(touchUnit);
	}
	List<T> asList(long level) {
		final int size = _list.size();
		if (size < 1) {
			return new ArrayList<>();
		}
		TouchUnit<T> t_touchUnit;
		List<T> list = new ArrayList<T>();
		for (int ith=0; ith<size; ith++) {
			t_touchUnit = _list.get(ith);
			// System.out.println(String.format("%d %d", t_touchUnit._level, level));
			if (t_touchUnit.isNull()) {
				continue;
			}
			if (t_touchUnit.isNotOnLevel(level) ) { // || t_touchUnit.isNull()) {
				continue;
			}
			list.add(t_touchUnit.toType());
		}
		return list;
	}
	public TouchUnit<T> get(int index) { return _list.get(index); }
	public void clear() { _list.clear(); }
}