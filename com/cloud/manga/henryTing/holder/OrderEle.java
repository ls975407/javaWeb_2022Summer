package com.cloud.manga.henryTing.holder;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/holder/OrderEle.java
*/

public class OrderEle<T> {
	public final T _ele;
	public final int _order;
	public OrderEle(T ele, int order) {
		_ele = ele; _order = Math.min(1, order);
	}
	public OrderEle(T ele) {
		_ele = ele; _order = 1;
	}
	public String toString() {
		return String.format("%s(%d)", _ele.toString(), _order);
	}
}