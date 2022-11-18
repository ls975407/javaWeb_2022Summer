package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/IndexId.java
*/

public class IndexId {
	public final Integer _index;
	public String toString() {
		return toIndexP().toString();
	}
	public boolean equals(Object obj) {
        if(obj instanceof IndexId) {
            IndexId k = (IndexId) obj;
            return _index.equals(k._index);
        }
		return false;
	}
    public int hashCode() {
        return new IndexP(_index).hashCode();
    }
	public IndexId() {
		_index = 0;
	}
	public IndexId(IndexP indexP) throws SDEBase {
		if (indexP._type != IndexEnum.id) { throw SDEError.cIndexPTypeError(0); }
		_index = indexP._ith;
	}
	public IndexP toIndexP() {
		return new IndexP(_index);
	}
	public static IndexP cIndexP(int index) {
		return new IndexP(index);
	}
	public IndexP goFirst() {
		return cIndexP(0);
	}
	public IndexP goLast(int size_) {
		return cIndexP(size_-1);
	}
	public IndexP add(int amount, int maxIndex) {
		int indexNew = _index + amount;
		if (IndexP.isInRange(indexNew, maxIndex)) {
			return cIndexP(indexNew); 
		}
		if (amount > 0) {
			if (_index == maxIndex-1) {
				return null;
			} else {
				return cIndexP(maxIndex-1);
			}
		} else {
			if (_index == 0) {
				return null;
			} else {
				return cIndexP(0);
			}
		}
	}
}