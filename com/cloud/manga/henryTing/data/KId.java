package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KId.java
*/
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

public class KId extends KeyString {
	public final static String KEY = "KId";
	public KId(String folderName) {
		super(folderName);
	}
	public KId(KId kid) {
		super(kid._keyName);
	}
	public static KId parseJson(JSONObject json) throws JSONException {
		return new KId(json.getString(KEY));
	}
	public void addJson(JSONObject json) throws JSONException {
		json.put(KEY, _keyName);
	}
}