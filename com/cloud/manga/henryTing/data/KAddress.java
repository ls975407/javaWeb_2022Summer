package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KAddress.java
*/
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.Iterator;

public class KAddress extends KeyString  {
	private static final String KEY_KIdS = "kids";
	private static final String KEY_LONGS = "longs";
	private static final String KEY_KId = "id";
	private static final String KEY_KCh = "ch";
	private static final String KEY_KPg = "pg";
	
	private static String _getKeyName(KId[] kids) {
		StringBuilder buider = new StringBuilder();
		for (KId kid: kids) {
			buider.append(kid._keyName);
		}
		return buider.toString();
	}
	private final KId[] _kids;
	private final Long[] _keys;
	public final String _kidName;
	public final String _kchName;
	public final String _kpgName;
	// public KAddress(KId kid) {
		// super(kid._keyName);
		// _kids = new KId[]{kid};
		// _keys = new Long[]{Long.valueOf(0)};
		// _kidName = kid._keyName;
		// _kchName = null;
		// _kpgName = null;
	// }
	public KAddress(KIdT kidt) {
		super(kidt._keyName);
		_kids = new KId[]{kidt};
		_keys = new Long[]{kidt._key};
		_kidName = kidt._keyName;
		_kchName = null;
		_kpgName = null;
	}
	public KAddress(KCh kch) {
		super(kch._kid._keyName);
		_kids = new KId[]{kch._kid};
		_keys = new Long[]{Long.valueOf(0)};
		_kidName = kch._kid._keyName;
		_kchName = kch._keyName;
		_kpgName = null;
	}
	public KAddress(KIdT kidt, String kchName) {
		super(kidt._keyName);
		_kids = new KId[]{kidt};
		_keys = new Long[]{kidt._key};
		_kidName = kidt._keyName;
		_kchName = kchName;
		_kpgName = null;
	}
	public KAddress(KPg kpg) {
		super(kpg._kch._kid._keyName);
		_kids = new KId[]{kpg._kch._kid};
		_keys = new Long[]{Long.valueOf(0)};
		_kidName = kpg._kch._kid._keyName;
		_kchName = kpg._kch._keyName;
		_kpgName = kpg._tokenU._keyName;
	}
	public KAddress(KIdT[] kidts) {
		super(_getKeyName(kidts));
		_kids = kidts;
		final int size = kidts.length;
		_keys = new Long[size];
		for (int ith=0; ith<size; ith++) {
			_keys[ith] = kidts[ith]._key;
		}
		if (size == 1) {
			_kidName = kidts[0]._keyName;
		} else {
			_kidName = null;
		}
		_kchName = null;
		_kpgName = null;
	}
	public KAddress(KIdT[] kidts, KId kid) {
		super(_getKeyName(kidts));
		_kids = kidts;
		final int size = kidts.length;
		_keys = new Long[size];
		for (int ith=0; ith<size; ith++) {
			_keys[ith] = kidts[ith]._key;
		}
		_kidName = kid._keyName;
		_kchName = null;
		_kpgName = null;
	}
	public KAddress(KIdT[] kidts, KCh kch) {
		super(_getKeyName(kidts));
		_kids = kidts;
		final int size = kidts.length;
		_keys = new Long[size];
		for (int ith=0; ith<size; ith++) {
			_keys[ith] = kidts[ith]._key;
		}
		_kidName = kch._kid._keyName;
		_kchName = kch._keyName;
		_kpgName = null;
	}
	public KAddress(KIdT[] kidts, KPg kpg) {
		super(_getKeyName(kidts));
		_kids = kidts;
		final int size = kidts.length;
		_keys = new Long[size];
		for (int ith=0; ith<size; ith++) {
			_keys[ith] = kidts[ith]._key;
		}
		_kidName = kpg._kch._kid._keyName;
		_kchName = kpg._kch._keyName;
		_kpgName = kpg._tokenU._keyName;
	}
	public KAddress(KAddress kaddress, KIdT[] kidts) {
		super(kaddress._keyName);
		_kids = new KId[kidts.length];
		_keys = new Long[kidts.length];
		for (int ith=0; ith<kidts.length; ith++) {
			_kids[ith] = kidts[ith];
			_keys[ith] = kidts[ith]._key;
		}
		_kidName = kaddress._kidName;
		_kchName = kaddress._kchName;
		_kpgName = kaddress._kpgName;
	}
	private KAddress(KId[] kids, Long[] keys, String... name) {
		super(KAddress._getKeyName(kids));
		_kids = kids;
		_keys = keys;
		switch (name.length) {
			case 0:
			if (kids.length == 1) {
				_kidName = kids[0]._keyName;
			} else {
				_kidName = null;
			}
			_kchName = null;
			_kpgName = null;
			break;
			case 1:
			_kidName = name[0];
			_kchName = null;
			_kpgName = null;
			break;
			case 2:
			_kidName = name[0];
			_kchName = name[1];
			_kpgName = null;
			break;
			default:
			_kidName = name[0];
			_kchName = name[1];
			_kpgName = name[2];
			break;
		}
	}
	public int size() {
		return _kids.length;
	}
	public Token getToken(int index) {
		return new Token(_keys[index], _kids[index]._keyName);
	}
	
	public static KAddress parseJson(JSONObject json) throws JSONException {
		// System.out.println("ABC 01");
		JSONArray jsonA; KId[] kids = null;
		try {
			jsonA = json.getJSONArray(KEY_KIdS);
			int size = jsonA.length();
			kids = new KId[size];
			for (int ith=0; ith<size; ith++) {
				kids[ith] = new KId(jsonA.getString(ith));
			}
		} catch (JSONException e) {
			String str_ = json.getString(KEY_KIdS);
			kids = new KId[]{ new KId(str_) };
		}
		// System.out.println("ABC 02");
		final int size = kids.length;
		Long[] keys = null;
		try {
			Long long_ = json.getLong(KEY_LONGS);
			keys = new Long[size];
			for (int ith=0; ith<size; ith++) {
				keys[ith] = Long.valueOf(long_);
			}
		} catch (JSONException e) {}
		// System.out.println("ABC 03");
		if (keys == null) {
			jsonA = null;
			try {
				jsonA = json.getJSONArray(KEY_LONGS);
			} catch(JSONException e) {
				keys = new Long[size];
				for (int ith=0; ith<size; ith++) {
					keys[ith] = null;
				}
			}
			if (jsonA != null) {
				int size_t = jsonA.length();
				if (size_t != size) {
					throw new JSONException(String.format("KAddress parseJson size_t != size %d != %d", size_t, size));
				}
				keys = new Long[size];
				for (int ith=0; ith<size; ith++) {
					try {
						keys[ith] = jsonA.getLong(ith);
					} catch(JSONException e) {
						keys[ith] = null;
					}
				}
			}
		}
		// System.out.println("ABC 04");
		String kidName, kchName, kpgName;
		try { kidName = json.getString(KEY_KId); } catch(JSONException e) {
			return new KAddress(kids, keys);
		}
		try { kchName = json.getString(KEY_KCh); } catch(JSONException e) {
			return new KAddress(kids, keys, kidName);
		}
		try { kpgName = json.getString(KEY_KPg); } catch(JSONException e) {
			return new KAddress(kids, keys, kidName, kchName);
		}
		// System.out.println("ABC 05");
		return new KAddress(kids, keys, kidName, kchName, kpgName);
	}
	public void addJson(JSONObject json) throws JSONException {
		JSONArray jsonA = new JSONArray();
		for (KId kid: _kids) {
			jsonA.put(kid._keyName);
		}
		json.put(KEY_KIdS, jsonA);
		jsonA = new JSONArray();
		for (Long long_: _keys) {
			jsonA.put(long_);
		}
		json.put(KEY_LONGS, jsonA);
		if (_kidName == null) { return; }
		json.put(KEY_KId, _kidName);
		if (_kchName == null) { return; }
		json.put(KEY_KCh, _kchName);
		if (_kpgName == null) { return; }
		json.put(KEY_KPg, _kpgName);
	}
	public void print() {
		System.out.print("kids = ");
		for (KId kid: _kids) {
			System.out.print(String.format("%s ", kid.toString()));
		}
		System.out.print("\n");
		for (Long long_: _keys) {
			if (long_ == null) {
				System.out.print("null ");
			} else {
				System.out.print(String.format("%s ", long_.toString()));
			}
		}
		System.out.print("\n");
		System.out.print("_kidName = "); System.out.println(_kidName); 
		System.out.print("_kchName = "); System.out.println(_kchName); 
		System.out.print("_kpgName = "); System.out.println(_kpgName); 
		System.out.print("\n");
	}
}