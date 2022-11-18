package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KCh2D.java
*/
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.OrderObj;
import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.IndexCh;
import java.util.Iterator;

public class KCh2D implements Iterable<TokenS> {
	
	public final KId _kid;
	private TokenS[] _table2D = null;
	// private TokenS _table1D = null;
	private Map<Integer, Integer> _order2index;

	private KCh2D(KId kid, TokenS[] table2D) {
		_kid = kid;
		_table2D = table2D;
		// _table1D = _2Dto1D(_table2D);
		_order2index = calTableIndex(_table2D);
	}
	
	public static KCh2D create(KId kid, String content) throws JSONException {
		TokenS[] buf;
		JSONArray jsonA = new JSONArray(content);
		final int size = jsonA.length();
		buf = new TokenS[size];
		for (int ith=0; ith<size; ith++) {
			buf[ith] = TokenS.parseJSONArray(jsonA.getJSONArray(ith));
		}
		return new KCh2D(kid, buf);
	}
	
	public static KCh2D create(KIdT kidt, TokenS tokenS) throws JSONException {
		if (tokenS.size() < 1) {
			throw new JSONException("KCh2D create tokenS.size() < 1");
		}
		return new KCh2D(kidt, arrange(kidt, tokenS));
	}
	
	public static KCh2D create(KId kid, String[] names) {
		return new KCh2D(kid, arrange(kid, new TokenS(names, false)));
	}
	
	// private static void print(String content) {
		// System.out.print(content);
	// }
	
	public static KCh2D filterExistChInfor(KCh2D kch2dNew, KCh2D kch2dOld) throws SDEError {
		TokenS tableNew = _2Dto1D(kch2dNew._table2D);
		TokenS tableOld = _2Dto1D(kch2dOld._table2D);
		if (!kch2dNew._kid.equals(kch2dOld._kid)) {
			throw SDEError.cKCh2DFilterExistCh(kch2dNew._kid.toString(), kch2dOld._kid.toString());
		}
		
		List<Token> buf = new ArrayList<>();
		Token told=null, tnew=null;
		int oth=0, nth=0;
		for (oth=0, nth=0; oth<tableOld.size() && nth<tableNew.size(); oth++, nth++) {
			while(true) {
				told = tableOld.get(oth);
				tnew = tableNew.get(nth);
				// print(String.format("%03d-%s %03d-%s", oth, told, nth, tnew));
				int code = told.compareTo(tnew);
				if (code == 0) {
					// print(String.format(" ==\n"));
					break;
				} else if (code > 0){
					// print(String.format(" > \n"));
					buf.add(tnew);
					nth++;
				} else {
					// print(String.format(" < \n"));
					oth++;
				}
			}
		}
		for (; nth<tableNew.size(); nth++) {
			tnew = tableNew.get(nth);
			buf.add(tnew);
			// print(String.format("%03d-%s %03d-%s\n", oth, told, nth, tnew));
		}
		if (buf.size() == 0) { return null; }
		return new KCh2D(
			kch2dNew._kid, 
			arrange(
				kch2dNew._kid, 
				new TokenS(buf.toArray(new Token[0]), true)
			)
		);
	}
	
	 public String toJsonString() throws JSONException {
		 JSONArray jsonA = new JSONArray();
		 for (TokenS tokenS: _table2D) {
			jsonA.put(tokenS.toJSONArray());
		 }
		 return jsonA.toString();
	 }
	
	
    public Iterator<TokenS> iterator() {
        Iterator<TokenS> iterator = new Iterator<TokenS>() {
            private int index = 0;
            public boolean hasNext() { return index < _table2D.length;}
            public TokenS next() { return (TokenS)_table2D[index++]; }
            public void remove() {}
        };
        return iterator;
    }
	private static TokenS _2Dto1D(TokenS[] table2D) {
		List<TokenS> _table2DCopy = new ArrayList<TokenS>();
		int size = 0; int ith=0;
		for (TokenS tokenS: table2D) {
			_table2DCopy.add(tokenS);
		}
		Collections.sort(_table2DCopy);
		List<Token> buf = new ArrayList<Token>();
		for (TokenS tokenS: _table2DCopy) {
			for (Token token: tokenS) {
				buf.add(token);
			}
		}
		// print(String.format("size = %d\n", buf.size()));
		return new TokenS(buf.toArray(new Token[0]), true);		
	}
	
	public IndexP getIndexCh(KCh kch, int typeIndex) throws SDEBase {
		TokenS tokenS = _table2D[typeIndex];
		int index = tokenS.findIndex(kch._keyName);
		if (index < 0) {
			throw SDEError.cKCh2DGetIndexCh(kch.toString(), typeIndex);
		}
		return IndexCh.cIndexP(typeIndex, index);
	}
		
	public IndexP getIndexCh(KCh kch) throws SDEBase {
		int index = new InforCh(kch)._order;
		if (!_order2index.containsKey(Integer.valueOf(index))) {
			throw SDEError.cKCh2DGetIndexCh(kch.toString());
		}
		int typeIndex = _order2index.get(index);
		return getIndexCh(kch, typeIndex);
	}
	
	public KChT get() throws SDEBase {
		return new KChT(_kid, _table2D[0].get(0));
	}
	public KChT get(IndexP indexP) throws SDEBase {
		return get(indexP.toCh());
	}
	public KChT get(IndexCh indexCh) throws SDEBase {
		return new KChT(_kid, _table2D[indexCh._type].get(indexCh._index));
	}
	
	public TokenS get(int ith) {
		return _table2D[ith];
	}
	public int size() {
		return _table2D.length;
	}
	public String getMark(int ith) {
		return new InforCh(new KCh(_kid, get(ith).get(0)._keyName))._mark;
	}
	
	public int size(int typeIndex) { return _table2D[typeIndex].size(); }

	public int size(IndexCh indexCh) { return size(indexCh._type);  }
	public int size(IndexP indexP) throws SDEBase { return size(indexP.toCh()); }
	

	private Map<Integer, Integer> calTableIndex(TokenS[] tokenSs) {
		Integer ith = Integer.valueOf(0);
		Map<Integer, Integer> table = new HashMap<>();
		for (TokenS tokenS: tokenSs) {
			table.put(new InforCh(new KCh(_kid, tokenS.get(0)._keyName))._order, ith++);
		}
		return table;
	}

	private static TokenS[] arrange(KId kid, TokenS tokenS) throws SDEBase {
		OrderObj<List<Token>> temp;
		List<OrderObj<List<Token>>> buf = new ArrayList<>();
		List<Token> t_list = new ArrayList<>();
		Token token = tokenS.get(0);
		t_list.add(token); 
		int f_order, t_order;
		f_order = new InforCh(new KCh(kid, token._keyName))._order;
		for (int ith=1; ith<tokenS.size(); ith++) {
			token = tokenS.get(ith);
			t_order = new InforCh(new KCh(kid, token._keyName))._order;
			if (f_order == t_order) {
				t_list.add(token);
				continue;
			}
			buf.add(new OrderObj<List<Token>>(
					Integer.valueOf(f_order), 
					t_list
				)
			); 
			t_list = new ArrayList<>();
			t_list.add(token);
			f_order = t_order;
		}
		buf.add(new OrderObj<List<Token>>(
				Integer.valueOf(f_order), 
				t_list
			)
		);
		Collections.sort(buf); 
		final int size_ = buf.size();
		TokenS[] table = new TokenS[size_];
		for (int ith=0; ith<size_; ith++) {
			table[ith] = new TokenS(buf.get(ith)._list.toArray(new Token[0]), true);
		}
		return table;
	}
}