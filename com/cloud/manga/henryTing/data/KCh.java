package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KCh.java
*/
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

public class KCh extends KeyString {
	public final static String KEY = "KCh";
	public final KId _kid;
	public KCh(KId kid, String folderName) {
		super(folderName);
		_kid = kid;
	}
	public KCh(KCh kch) {
		super(kch._keyName);
		_kid = kch._kid;
	}
	public static KCh parseJson(JSONObject json) throws JSONException {
		return new KCh(
			KId.parseJson(json),
			json.getString(KEY)
		);
	}
	public void addJson(JSONObject json) throws JSONException {
		_kid.addJson(json);
		json.put(KEY, _keyName);
	}
}