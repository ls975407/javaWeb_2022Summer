package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/FileBS.java
*/

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
public class FileBS implements Iterable<FileB> {
	private final FileB[] _fileBs;
	public FileBS(FileB[] kpgBs) {
		_fileBs = kpgBs;
	}
	public FileBS(int count) {
		List<FileB> buf = new ArrayList<>();
		for(int ith=0; ith<count; ith++) {
			buf.add(null);
		}
		_fileBs = buf.toArray(new FileB[0]);
	}
	public FileBS() {
		_fileBs = new FileB[0];
	}
	public int size() { return _fileBs.length; }
    public Iterator<FileB> iterator() {
        Iterator<FileB> iterator = new Iterator<FileB>() {
            private int index = 0;
            public boolean hasNext() { return index < _fileBs.length;}
            public FileB next() { return (FileB)_fileBs[index++]; }
            public void remove() {}
        };
        return iterator;
    }
	public FileB get(int index) { return _fileBs[index]; }
}