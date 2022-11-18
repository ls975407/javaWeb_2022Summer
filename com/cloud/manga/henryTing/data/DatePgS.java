package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/DatePgS.java
*/
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
public class DatePgS extends DateT {
	public final KPgS _kpgs;
	public DatePgS(DateT dataT, KPgS kpgs) { super(dataT); _kpgs = kpgs; }
	public DatePgS(KPgS kpgs) { super(new DateT()); _kpgs = kpgs; }
	public void addJson(JSONObject json) throws JSONException {
		super.addJson(json);
		_kpgs.addJson(json);
	}
	public static DatePgS parseJsonFrom(KId kid, JSONObject json, boolean isLocal) throws JSONException {
		return new DatePgS(
			DateT.parseJson(json), 
			KPgS.parseJsonFrom(kid, json, isLocal)
		);
	}
}