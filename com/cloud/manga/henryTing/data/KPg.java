package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KPg.java
*/

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

public class KPg extends KeyStringBase implements LocalInfor {
	public final KCh _kch;
	public final TokenU _tokenU;
	public final Boolean _isExist;
	public KPg(KCh kch, TokenU tokenU) { 
		super(PathGet.FolderName(kch, tokenU._keyName)); 
		_kch = kch; 
		_tokenU = tokenU;
		_isExist = null;
	}
	public KPg(KCh kch, TokenU tokenU, Boolean isExist) { 
		super(PathGet.FolderName(kch, tokenU._keyName)); 
		_kch = kch; 
		_tokenU = tokenU;
		_isExist = isExist;
	}
	public KPg(KPg kpg, Boolean isExist) { 
		super(kpg._keyName); 
		_kch = kpg._kch; 
		_tokenU = kpg._tokenU;
		_isExist = isExist;
	}
	public static KPg testCreate() {
		return new KPg(new KCh(new KId("kid"), "kch"), new TokenU("kpg", true));
	}
	public static KPg parseJsonArray(KId kid, JSONArray jsonA, boolean isLocal) throws JSONException {
		if (jsonA.length() != 2) {
			throw new JSONException("KPg parseJson jsonA.length() != 2");
		}
		KCh kch = new KCh(kid, jsonA.getString(0));
		TokenU tokenU = new TokenU(jsonA.getString(1), isLocal);
		// return new KPg(kch, tokenU);
		return new KPg(kch, tokenU, true);
	}
	public JSONArray toJSONArray() throws JSONException {
		JSONArray jsonA = new JSONArray();
		jsonA.put(_kch._keyName);
		jsonA.put(_tokenU._keyName);
		return jsonA;
	}
	public GetUrl getUrl() {
		return _tokenU._getUrl;
	}
	public boolean isExist() {
		if (_isExist == null) { return false; }
		return _isExist;
	}
	public boolean isNotExist() {
		if (_isExist == null) { return false; }
		return _isExist;
	}
	public boolean isLocal() {
		return _tokenU.isLocal();
	}
	public boolean isCloud() {
		return _tokenU.isCloud();
	}
}