package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/TokenUS.java
*/
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class TokenUS extends KeyString implements Iterable<TokenU> {
	public final static String KEY = "TokenUS";
	private static String sortAndGetKeyName(Object[] tokenUs, boolean isNotSort) {
		if (isNotSort) {
			java.util.Arrays.sort(tokenUs);
		}
		return tokenUs[0].toString();
	}
	protected final TokenU[] _tokenUs;
	public TokenUS(TokenU[] tokenUs, boolean isSort) {
		super(sortAndGetKeyName(tokenUs, !isSort));
		_tokenUs = tokenUs;
	}
	public TokenUS() {
		super(""); _tokenUs = new TokenU[0];
	}
	public TokenUS(TokenUS tokenUS) {
		super(tokenUS._keyName);
		_tokenUs = tokenUS._tokenUs;
	}
	public TokenUS(String[] names, boolean isSort, boolean isLocal) {
		super(sortAndGetKeyName(names, !isSort));
		TokenU[] tokenUs = new TokenU[names.length];
		for (int ith=0; ith<names.length; ith++) {
			tokenUs[ith] = new TokenU(names[ith], isLocal);
		}
		_tokenUs = tokenUs;
	}
	public TokenU get(int index) { return _tokenUs[index]; }
	
		
	// public void print() {
		// for (TokenU ele: this) {
			// System.out.println(ele._keyName);
		// }
	// }
	
	public static TokenUS parseJson(JSONObject json, boolean isLocal) throws JSONException {
		JSONArray jsonA = json.getJSONArray(KEY);
		final int len = jsonA.length();
		if (len < 2) {
			throw new JSONException("TokenUS parseJson len < 2");
		}
		TokenU[] tokenUs = new TokenU[len];
		for (int ith=0; ith<len; ith++) {
			tokenUs[ith] = new TokenU(jsonA.getString(ith), isLocal);
		}
		return new TokenUS(tokenUs, true);
	}
	public void addJson(JSONObject json) throws JSONException {
		JSONArray jsonA = new JSONArray();
		for (TokenU tokenU: _tokenUs) {
			jsonA.put(tokenU.toString());
		}
		json.put(KEY, jsonA);
	}




	public int findIndex(String keyName) {
		// for (Token token: _tokens) {
			// if (keyName.equals(token._keyName)) {
				// return token;
			// }
		// }
		// return null;
		return java.util.Collections.binarySearch(
			java.util.Arrays.asList(_tokenUs),
			new KeyStringBase(keyName)
		);
	}
	public TokenU find(String keyName) {
		int index = findIndex(keyName);
		if (index < 0) { return null; }
		return get(index);
	}
	@Override
	public boolean equals(Object obj) {
        if(obj instanceof TokenUS) {
            TokenUS k = (TokenUS) obj;
			if (k.size() != size()) { return false; }
			for (int ith=0; ith<k.size(); ith++) {
				if (!get(ith).equals(k.get(ith))) { return false; }
			}
            return true;
        }
		return false;
	}
	public int size() { return _tokenUs.length; }
    public Iterator<TokenU> iterator() {
        Iterator<TokenU> iterator = new Iterator<TokenU>() {
            private int index = 0;
            public boolean hasNext() { return index < _tokenUs.length;}
            public TokenU next() { return (TokenU)_tokenUs[index++]; }
            public void remove() {}
        };
        return iterator;
    }
	
	public TokenUS filterExtension(String[] legalE) {
		List<TokenU> buf = new ArrayList<>();
		for (TokenU tokenU: _tokenUs) {
			for (String ex: legalE) {
				if (tokenU._keyName.endsWith(ex)) {
					buf.add(tokenU);
				}
			}
		}
		return new TokenUS(buf.toArray(new TokenU[0]), true);
	}

}