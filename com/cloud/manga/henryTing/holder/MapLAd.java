package com.cloud.manga.henryTing.holder;

/*
javac -encoding utf8  ./com/cloud/manga/henryTing/holder/MapLAd.java
*/

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Collection;
import com.cloud.manga.henryTing.unit.SDEBase;

import java.util.concurrent.ConcurrentHashMap;

// public class MapLAd<K, V> implements java.util.Map<K,OrderEle<V>> {
public class MapLAd<K, V> implements MapL<K,V> {

	private HashMap<K,OrderEle<V>> oldMap = new HashMap<>();
	private HashMap<K,OrderEle<V>> newMap = new HashMap<>();
	
	private final EleC<K,OrderEle<V>> _eleCreator;
	private final int _limit;
	private volatile int _size = 0;
	public MapLAd(EleC<K,OrderEle<V>> eleCreator) {
		_eleCreator = eleCreator; _limit = Math.max(1, eleCreator._limit);
	}
	
	public V putEle(K key, V value) {
		return put(key, new OrderEle<V>(value))._ele;
	}
	
	public int size() { return _size; }
	public synchronized OrderEle<V> put(K key, OrderEle<V> value) {
		if (_size > _limit) {
			HashMap<K,OrderEle<V>> t = oldMap;
			oldMap.clear(); oldMap = newMap;
			newMap = t; _size = 0;
		}
		newMap.put(key, value);
		_size += value._order;
		return value;
	}
	// private OrderEle<V> debugVlaue = null;
	public synchronized OrderEle<V> getEle(K key, boolean needSave) throws SDEBase {
		// if (debugVlaue != null) {
			// return debugVlaue;
		// }
		
		OrderEle<V> reV = null;
		
		// System.out.println(newMap);
		// System.out.println(key);
		reV = newMap.get(key);
		if (reV != null) { return reV; }
		reV = oldMap.remove(key);
		if (reV != null) {
			newMap.put(key, reV);
			return reV;
		}
		reV = _eleCreator.createEle(key);
		
		// if (debugVlaue == null) {
			// debugVlaue = reV;
		// }
		
		if (needSave) { return put(key, reV); }
		return reV;
	}
	public synchronized OrderEle<V> getEle(K key) throws SDEBase {
		return getEle(key, true);
	}
	public synchronized V getEleV(K key) throws SDEBase {
		return getEle(key)._ele;
	}
	public synchronized V getEleV(K key, boolean needSave) throws SDEBase {
		return getEle(key, needSave)._ele;
	}
	public void clear() { oldMap.clear(); newMap.clear(); _size = 0; }
	public void removeEle(K key) throws SDEBase {
		newMap.remove(key);
		oldMap.remove(key);
	}
	
	
	
	public OrderEle<V> get(Object key) {
		return null;
	}
	public OrderEle<V> remove(Object key) {
		OrderEle<V> value = null;
		value = newMap.remove(key);
		if (value != null) { return value; }
		return oldMap.remove(key);
	}
	
	
	public boolean containsKey(Object key) {
		if (newMap.containsKey(key) || oldMap.containsKey(key) ) {
			return true;
		} return false;
	}
	public boolean containsValue(Object value) { return false; }
	public boolean equals(Object o) { return false; }
	public int hashCode() { return newMap.hashCode(); }
	public boolean isEmpty() { return _size == 0; }
	public Set<K> keySet() {
		Set<K> setNew = newMap.keySet();
		setNew.addAll(oldMap.keySet());
		return setNew;
	}
	public Set<Map.Entry<K,OrderEle<V>>> entrySet() {
		Set<Map.Entry<K,OrderEle<V>>> setNew = newMap.entrySet();
		setNew.addAll(oldMap.entrySet());
		return setNew;
	}
	public void putAll(Map<? extends K,? extends OrderEle<V>> m) {}
	public Collection<OrderEle<V>> values() { 
		Collection<OrderEle<V>> eles = newMap.values();
		eles.addAll(oldMap.values());
		return eles;
	}
}