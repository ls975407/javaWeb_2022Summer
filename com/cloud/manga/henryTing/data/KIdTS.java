package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KIdTS.java
*/

import java.util.Iterator;
import java.util.Arrays;

public class KIdTS implements Iterable<KIdT> {
	private final KIdT[] _kids;
	public KIdTS(KIdT[] kids) { _kids = kids; }
	public KIdT get(int index) { return _kids[index]; }
    public Iterator<KIdT> iterator() {
        Iterator<KIdT> iterator = new Iterator<KIdT>() {
            private int index = 0;
            public boolean hasNext() { return index < _kids.length;}
            public KIdT next() { return (KIdT)_kids[index++]; }
            public void remove() {}
        };
        return iterator;
    }
	public int size() { return _kids.length; }
	public KIdT[] toArray() {
		return Arrays.copyOf(_kids, _kids.length); 
	}
}