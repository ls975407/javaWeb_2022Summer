package com.cloud.manga.henryTing.holder;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/holder/EleC.java
*/
import com.cloud.manga.henryTing.unit.SDEBase;
public abstract class EleC<K,V> {
	public final int _limit;
	public EleC(int limit) {
		_limit = limit;
	}
	public abstract V createEle(K key) throws SDEBase;
}