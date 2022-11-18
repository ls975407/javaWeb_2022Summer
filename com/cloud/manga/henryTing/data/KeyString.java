package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KeyString.java
*/

import org.json.JSONObject;
import org.json.JSONException;

abstract public class KeyString extends KeyStringBase {
	KeyString(String keyString) {
		super(keyString);
	}
	public abstract void addJson(JSONObject json) throws JSONException;
}