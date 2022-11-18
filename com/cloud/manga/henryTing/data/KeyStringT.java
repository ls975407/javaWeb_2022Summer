package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KeyStringBase.java
*/

public class KeyStringT<T> extends KeyStringBase {
	public final T _value;
	public KeyStringT(String keyString, T value) {
		super(keyString);
		_value = value;
	}
}