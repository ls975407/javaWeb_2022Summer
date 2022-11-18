package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/Token.java
*/
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

public class Token extends KeyStringBase  implements LocalInfor {
	public final Long _key;
	public Token(String name) {
		super(name); _key = Long.valueOf(0);
	}
	public Token(Long key, String name) {
		super(name); _key = key;
	}
	public static Token parseJsonArray(JSONArray jsonA) throws JSONException {
		if (jsonA.length() != 2) {
			throw new JSONException("Token parseJson jsonA.length() != 2");
		}
		return new Token(jsonA.getLong(1), jsonA.getString(0));
	}
	public JSONArray toJSONArray() throws JSONException {
		JSONArray jsonA = new JSONArray();
		jsonA.put(_keyName);
		jsonA.put(_key);
		return jsonA;
	}
	public boolean isLocal(){
		return _key != null && _key == 0;
	} 
	public boolean isCloud(){
		return  _key != null && _key != 0;
	} 
}