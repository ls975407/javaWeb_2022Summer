package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KPgS.java
*/

import java.util.Iterator;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;



public class KPgS extends KeyString implements Iterable<KPg> {
	private final KPg[] _kpgs;
	private boolean[] chIndexs = new boolean[]{false, false};
	public KPgS(KPg[] kpgs) {
		super(kpgs[0]._keyName); 
		assert kpgs.length == 2: "KPgS size != 2"; 
		_kpgs = kpgs;
	}
	public KPgS(KPg kpg_0, KPg kpg_1) {
		super(kpg_0._keyName);
		_kpgs = new KPg[]{kpg_0, kpg_1};
	}
	public boolean isOnePage() { 
		return _kpgs[1] == null;
	}
	public boolean isChange() { 
		for(int ith=0; ith<2; ith++) { 
			if(chIndexs[ith]) {return true; } 
		} 
		return false;
	}
	public void set(int index, KPg kpg) { _kpgs[index] = kpg; chIndexs[index] = true; }
	public KPg get(int index) { return _kpgs[index]; }
    public Iterator<KPg> iterator() {
        Iterator<KPg> iterator = new Iterator<KPg>() {
            private int index = 0;
            public boolean hasNext() { return index < _kpgs.length;}
            public KPg next() { return (KPg)_kpgs[index++]; }
            public void remove() {}
        };
        return iterator;
    }
	public int size() { return _kpgs.length; }
	public final static String KEY = "KPgS";
	public static KPgS parseJsonFrom(KId kid, JSONObject json, boolean isLocal) throws JSONException {
		JSONArray jsonA = json.getJSONArray(KEY);
		final int len = jsonA.length();
		if (len < 2) {
			throw new JSONException("KPgS parseJson len < 2");
		}
		KPg[] kpgs = new KPg[len];
		for (int ith=0; ith<len; ith++) {
			kpgs[ith] = KPg.parseJsonArray(kid, jsonA.getJSONArray(ith), isLocal);
		}
		return new KPgS(kpgs);
	}
	public void addJson(JSONObject json) throws JSONException {
		JSONArray jsonA = new JSONArray();
		jsonA.put(_kpgs[0].toJSONArray());
		if(_kpgs[1] != null) { jsonA.put(_kpgs[1].toJSONArray()); }
		json.put(KEY, jsonA);
	}
	public KCh toKCh() {
		return toKPg()._kch;
	}
	public KPg toKPg() {
		return get(0);
	}
	public boolean isExist() {
		return _kpgs[1].isExist() && _kpgs[0].isExist();
	}
}