package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KAddressT.java
*/
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.Iterator;

public class KAddressT extends KeyStringBase implements Iterable<KIdT> {
	private final KIdTS _kidtS;
	private final KAddress _kAddress;
	public KAddressT(KAddress kAddress, Long[] keys) {
		super(kAddress._keyName);
		KIdT[] kidts = new KIdT[kAddress.size()];
		for (int ith=0; ith<kAddress.size(); ith++) {
			kidts[ith] = new KIdT(new KId(kAddress.getToken(ith)._keyName), keys[ith]);
		}
		_kAddress = new KAddress(kAddress, kidts);
		_kidtS = new KIdTS(kidts);
	}
    public Iterator<KIdT> iterator() {
        Iterator<KIdT> iterator = new Iterator<KIdT>() {
            private int index = 0;
            public boolean hasNext() { return index < size();}
            public KIdT next() { return get(index++); }
            public void remove() {}
        };
        return iterator;
    }
	public int size() { return _kAddress.size();}
	
	public KIdT get(int index) {
		return _kidtS.get(index);
	}
	public void addJson(JSONObject json) throws JSONException {
		_kAddress.addJson(json);
	}
	public KId getKId() {
		if (_kAddress._kidName == null) {
			return null;
		}
		return new KId(_kAddress._kidName);
	}
	public KCh getKCh(KId kid) {
		if (_kAddress._kchName == null) {
			return null;
		}
		return new KCh(kid, _kAddress._kchName);
	}
	public KPg getKPg(KId kid, boolean isLocal) {
		KCh kch = getKCh(kid);
		if (kch == null || _kAddress._kpgName == null) {
			return null;
		}
		return new KPg(kch, new TokenU(_kAddress._kpgName, isLocal));
	}
	public KPg getKPg(KIdT kidt) {
		return getKPg(kidt, kidt.isLocal());
	}
	public KIdTS getKIdTS() {
		return _kidtS;
	}
	public String getKeyName() {
		return _kAddress._keyName;
	}
	public void print() {
		_kAddress.print();
	}
}