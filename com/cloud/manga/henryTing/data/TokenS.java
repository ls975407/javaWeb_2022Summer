package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/TokenS.java
*/
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.Iterator;
import java.util.Scanner;

public class TokenS extends KeyString implements Iterable<Token> {
	public final static String KEY = "TokenS";
	private static String sortAndGetKeyName(Object[] tokenUs, boolean isNotSort) {
		if (isNotSort) {
			java.util.Arrays.sort(tokenUs);
		}
		return tokenUs[0].toString();
	}
	protected final Token[] _tokens;
	public TokenS(Token[] tokens, boolean isSort) {
		super(sortAndGetKeyName(tokens, !isSort));
		_tokens = tokens;
	}
	public TokenS(String[] names, boolean isSort) {
		super(sortAndGetKeyName(names, !isSort));
		final int size = names.length;
		Token[] tokens = new Token[size];
		for (int ith=0; ith<size; ith++) {
			tokens[ith] = new Token(names[ith]);
		}
		_tokens = tokens;
	}
	
	public TokenS(TokenS tokens) {
		super(tokens._keyName);
		_tokens = tokens._tokens;
	}

	public static TokenS parseJSONArray(JSONArray jsonA) throws JSONException {
		final int len = jsonA.length();
		if (len < 1) {
			throw new JSONException("TokenS parseJson len < 1");
		}
		Token[] tokens = new Token[len];
		for (int ith=0; ith<len; ith++) {
			tokens[ith] = Token.parseJsonArray(jsonA.getJSONArray(ith));
		}
		return new TokenS(tokens, true);
	}
	public static TokenS parseJson(JSONObject json) throws JSONException {
		JSONArray jsonA = json.getJSONArray(KEY);
		return parseJSONArray(jsonA);
	}
	public void addJson(JSONObject json) throws JSONException {
		json.put(KEY, toJSONArray());
	}
	public JSONArray toJSONArray() throws JSONException {
		JSONArray jsonA = new JSONArray();
		for (Token token: _tokens) {
			jsonA.put(token.toJSONArray());
		}
		return jsonA;
	}


	public Token get(int index) { return _tokens[index]; }
	public int size() { return _tokens.length; }
	
	public int findIndex(String keyName) {
		// for (Token token: _tokens) {
			// if (keyName.equals(token._keyName)) {
				// return token;
			// }
		// }
		// return null;
		return java.util.Collections.binarySearch(
			java.util.Arrays.asList(_tokens),
			new KeyStringBase(keyName)
		);
	}
	
	public java.util.List<Token> asList() {
		return java.util.Arrays.asList(_tokens);
	}
	
	public Token find(String keyName) {
		int index = findIndex(keyName);
		if (index < 0) { return null; }
		return get(index);
	}
    public Iterator<Token> iterator() {
        Iterator<Token> iterator = new Iterator<Token>() {
            private int index = 0;
            public boolean hasNext() { return index < _tokens.length;}
            public Token next() { return (Token)_tokens[index++]; }
            public void remove() {}
        };
        return iterator;
    }
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("%d\n", size()));
		for (Token token: this) {
			builder.append(String.format("%s %d\n", token._keyName, token._key));
		}
		return builder.toString();
	}
	public static TokenS create(String content) {
		Scanner sc = new Scanner(content);
		int size = sc.nextInt();
		Token[] tokens = new Token[size];
		String name; Long key;
		for (int ith=0; ith<size; ith++) {
			if (!sc.hasNext()) { return null; }
			name = sc.next();
			if (!sc.hasNextLong()) { return null; }
			key = sc.nextLong();
			tokens[ith] = new Token(key, name);
		}
		return new TokenS(tokens, true);
	}
}