package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/IndexP.java
*/

public class IndexP implements Comparable<IndexP> {
	public final IndexEnum _type;
	public final Integer _ith;
	public final Integer _jth;
	public final IndexP _indexP;
	public IndexP(int ith) {
		_type = IndexEnum.id;
		_ith = Integer.valueOf(ith);
		_jth = null;
		_indexP = null;
	}
	public IndexP(int ith, int jth) {
		_type = IndexEnum.ch;
		_ith = Integer.valueOf(ith);
		_jth = Integer.valueOf(jth);
		_indexP = null;
	}
	public IndexP(IndexP indexP, int ith) {
		_type = IndexEnum.pg;
		_ith = Integer.valueOf(ith);
		_jth = null;
		_indexP = indexP;
	}
	public void print() {
		StringBuilder _buider = new StringBuilder();
		switch (_type) {
			case id:
			_buider.append(String.format("indexId: %s\n", toString())); break;
			case ch:
			_buider.append(String.format("indexCh: %s\n", toString())); break;
			case pg:
			_buider.append(String.format("indexPg: %s\n", toString())); break;
		}
		System.out.println(_buider.toString());
	}
	public String toString() {
		StringBuilder _buider = new StringBuilder();
		switch (_type) {
			case id:
			_buider.append(String.format("(%d)", _ith)); break;
			case ch:
			_buider.append(String.format("(%d, %d)", _ith, _jth)); break;
			case pg:
			_buider.append(String.format("(%s, %d)",_indexP.toString() , _ith)); break;
		}
		return _buider.toString();
	}
	public static boolean isInRange(int indexNew, int maxSize) {
		return !(indexNew < 0 || indexNew >= maxSize);
	}
	
	public IndexId toId() throws SDEBase {
		return new IndexId(this);
	}
	public IndexCh toCh() throws SDEBase {
		return new IndexCh(this);
	}
	public IndexPg toPg() throws SDEBase {
		return new IndexPg(this);
	}
	public int getIndex() {
		switch(_type) {
			case id: 
			case pg: return _ith;
			default: break;
		}
		return _jth;
	}
	public int hashCode() {
		int result = (int) (_ith ^ (_ith >>> 32));
		int prime = 31;
		switch(_type) {
			case id:
				return result*prime;
			case ch:
				return result*prime + _jth + 1;
			case pg:
				return result*prime + _indexP.hashCode() + 1;
		}
		return result;
	}
	public boolean equals(Object o) {
		if (o instanceof IndexP) {
			IndexP indexP = (IndexP) o;
			if (this.compareTo(indexP) != 0) {
				return false;
			}
			return true;
		}
		return false;
	}
    public int compareTo(IndexP other) {
		int codeLeft = _type.ordinal();
		int codeRight = other._type.ordinal();
		if (codeLeft != codeRight) { return codeLeft - codeRight; }
		if (_ith != null) {
			if (!_ith.equals(other._ith)) {
				return _ith - other._ith;
			}
		}
		if (_jth != null) {
			if (!_jth.equals(other._jth)) {
				return _jth - other._jth;
			}
		}
		if (_indexP != null) {
			if (!_indexP.equals(other._indexP)) {
				return _indexP.compareTo(other._indexP);
			}
		}
		return 0;
    }
}