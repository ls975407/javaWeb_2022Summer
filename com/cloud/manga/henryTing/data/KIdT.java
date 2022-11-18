package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KIdT.java
*/

import com.cloud.manga.henryTing.unit.SDEBase;

public class KIdT extends KId implements LocalInfor {
	public final Long _key;
	public KIdT(KId kid, Long key) {
		super(kid);
		_key = key;
	}
	public KIdT(KId kid) {
		super(kid);
		_key = Long.valueOf(0);
	}
	public KIdT(Token token) {
		super(new KId(token._keyName));
		_key = token._key;
	}
	public Token toToken() {
		return new Token(_key, _keyName);
	}
	public boolean equals(Object obj) {
        if(obj instanceof KIdT) {
            KIdT k = (KIdT) obj;
            return _key.equals(k._key) && super.equals((KId) k);
        }
		return false;
	}
	public boolean isLocal(){
		return _key != null && _key == 0;
	} 
	public boolean isCloud(){
		return  _key != null && _key != 0;
	} 
}