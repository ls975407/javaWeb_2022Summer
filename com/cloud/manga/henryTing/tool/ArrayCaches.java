package com.cloud.manga.henryTing.tool;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/tool/ArrayCaches.java
*/

import java.util.LinkedList;
import java.util.List;

public class ArrayCaches<T1, T2> {
	private LinkedList<T1> keys = new LinkedList<>();
	private LinkedList<T2> values = new LinkedList<>();
	private final int MAX_SIZE;
	private int size = 0;
	public ArrayCaches(int size) {
		MAX_SIZE = size;
	}
	private void updateList(int posi) {
		keys.addFirst(keys.remove(posi));
		values.addFirst(values.remove(posi));
	}
	synchronized public T2 get(T1 keyNeed) {
		for (int ith=0; ith<size; ith++) {
			// System.out.println(ith);
			// System.out.println(keyNeed);
			if (keys.get(ith).equals(keyNeed)) {
				updateList(ith);
				return values.get(0);
			}
		}
		return null;
	}
	synchronized public void set(T1 keyNeed, T2 valueSet) {
		int size_t = Math.min(size+1, MAX_SIZE);
		if (size_t > size) {
			keys.addFirst(keyNeed);
			values.addFirst(valueSet);
		} else {
			keys.set(size-1, keyNeed);
			values.set(size-1, valueSet);
			updateList(size-1);
		}
		size = size_t;
	}
	synchronized public void clear() {
		values.clear(); keys.clear(); size = 0;
	}
	public void print() {
		System.out.println("ArrayCaches print");
		System.out.println("keys=");
		for (T1 key: keys) {
			System.out.println(key);
		}
		System.out.println("values=");
		for (T2 value: values) {
			System.out.println(value);
		}
	}
}