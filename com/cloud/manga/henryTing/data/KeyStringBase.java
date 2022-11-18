package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KeyStringBase.java
*/

public class KeyStringBase implements Comparable<KeyStringBase> {
	public final String _keyName;
	public KeyStringBase(String folderName) { _keyName = folderName; }
	public boolean equals(Object obj) {
        if(obj instanceof KeyStringBase) {
            KeyStringBase k = (KeyStringBase) obj;
            return this._keyName.equals(k._keyName);
        }
		return false;
	}
	public int hashCode() { return _keyName.hashCode(); }
	public String toString() { return _keyName; }
    public int compareTo(KeyStringBase other) {
		return this._keyName.compareTo(other._keyName);
    }
}