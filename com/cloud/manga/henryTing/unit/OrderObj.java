package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/OrderObj.java
*/


public class OrderObj<T> implements Comparable<OrderObj<T>> {
	public final Integer _order; public final T _list;
	public OrderObj(Integer order, T list_) {
		_list = list_; _order = order;
	}
	public int hashCode() { return _order.hashCode(); }
	public int compareTo(OrderObj<T> other) {
		return this._order.compareTo(other._order);
	}
}
