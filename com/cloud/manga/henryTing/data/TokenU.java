package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/TokenU.java
*/

import java.net.URL;

public class TokenU extends KeyStringBase implements LocalInfor {
	public final boolean _isLocal;
	public final GetUrl _getUrl;
	
	private static GetUrl _getUrlNull = new GetUrl(){
		public java.net.URL getUrl() { return null; }
	};
	
	public TokenU(TokenU tokenU) {
		super(tokenU._keyName);
		_getUrl = tokenU._getUrl;
		_isLocal = tokenU._isLocal;
	}
	public TokenU(String name, GetUrl getUrl) {
		super(name);
		_isLocal = getUrl == null;
		if (_isLocal) {
			_getUrl = _getUrlNull;
		} else {
			_getUrl = getUrl;
		}
	}
	public TokenU(String name, boolean isLocal) {
		super(name);
		_isLocal = isLocal;
		_getUrl = _getUrlNull;
	}
	public boolean isLocal() {
		return _isLocal;
	}
	public boolean isCloud() {
		return _isLocal && _getUrl.equals(_getUrlNull);
	}
	public java.net.URL getUrl() {
		return _getUrl.getUrl();
	}
}