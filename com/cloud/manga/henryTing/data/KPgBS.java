package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KPgBS.java
*/

import java.util.Iterator;
public class KPgBS {
	private final KPgB[] _kpgBs;
	public KPgBS(KPgB[] kpgBs) {
		_kpgBs = kpgBs;
	}
	public int size() { return _kpgBs.length; }
    public Iterator<KPgB> iterator() {
        Iterator<KPgB> iterator = new Iterator<KPgB>() {
            private int index = 0;
            public boolean hasNext() { return index < _kpgBs.length;}
            public KPgB next() { return (KPgB)_kpgBs[index++]; }
            public void remove() {}
        };
        return iterator;
    }
	public KPgB get(int index) { return _kpgBs[index]; }
}