package com.cloud.manga.henryTing.holder;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/holder/MapL.java
*/

import com.cloud.manga.henryTing.unit.SDEBase;
public interface MapL<K,V> extends java.util.Map<K,OrderEle<V>> {
	V putEle(K key, V value);
	OrderEle<V> getEle(K key) throws SDEBase;
	OrderEle<V> getEle(K key, boolean needSave) throws SDEBase;
	V getEleV(K key) throws SDEBase;
	V getEleV(K key, boolean needSave) throws SDEBase;
	void removeEle(K key) throws SDEBase;
}