package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KChT.java
*/

import com.cloud.manga.henryTing.unit.SDEBase;

public class KChT extends KCh {
	public final Long _key;
	public KChT(KCh kch, Long key) {
		super(kch);
		_key = key;
	}
	public KChT(KCh kch) {
		super(kch);
		_key = Long.valueOf(0);
	}
	public KChT(KId kid, Token token) {
		super(new KCh(kid, token._keyName));
		_key = token._key;
	}
	public Token toToken() {
		return new Token(_key, _keyName);
	}
}