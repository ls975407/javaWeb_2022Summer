package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/DateCh.java
*/
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
public class DateCh extends DateT {
	public final KCh _kch;
	public DateCh(DateT dataT, KCh kch) { super(dataT); _kch = kch; }
	public DateCh(KCh kch) { super(new DateT()); _kch = kch; }
	public void addJson(JSONObject json) throws JSONException {
		super.addJson(json);
		_kch.addJson(json);
	}
	public static DateCh parseJson(JSONObject json) throws JSONException {
		return new DateCh(
			DateT.parseJson(json), 
			KCh.parseJson(json)
		);
	}
}