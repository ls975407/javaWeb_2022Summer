package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/IndexCh.java
*/

public class IndexCh {
	public final Integer _type;
	public final Integer _index;
	
	public String toString() {
		return toIndexP().toString();
	}
	
	public boolean equals(Object obj) {
        if(obj instanceof IndexCh) {
            IndexCh k = (IndexCh) obj;
            return _type.equals(k._type) && _index.equals(_index);
        }
		return false;
	}
    public int hashCode() {
        return toIndexP().hashCode();
    }
	
	public IndexCh() {
		_type = 0;
		_index = 0;
	}
	public IndexCh(int type, int index) {
		_type = Integer.valueOf(type);
		_index = Integer.valueOf(index);
	}
	public IndexCh(IndexP indexP) throws SDEBase {
		if (indexP._type != IndexEnum.ch) { throw SDEError.cIndexPTypeError(1); }
		_type = indexP._ith;
		_index = indexP._jth;
	}
	public IndexP toIndexP() {
		return new IndexP(_type, _index);
	}
	public static IndexP cIndexP(int type, int index) {
		return new IndexP(type, index);
	}
	

	public IndexP goFirst() {
		return cIndexP(_type, 0);
	}
	public IndexP goLast(int size_) {
		return cIndexP(_type, size_-1);
	}
	public IndexCh addToCh(int amount, int maxIndex) {
		IndexP indexP = add(amount, maxIndex);
		if (indexP == null) { return null; }
		return indexP.toCh();
	}
	public IndexP add(int amount, int maxIndex) {
		int indexNew = _index + amount;
		if (IndexP.isInRange(indexNew, maxIndex)) {
			return cIndexP(_type, indexNew); 
		}
		if (amount > 0) {
			if (_index == maxIndex-1) {
				return null;
			} else {
				return cIndexP(_type, maxIndex-1);
			}
		} else {
			if (_index == 0) {
				return null;
			} else {
				return cIndexP(_type, 0);
			}
		}
	}
	public IndexCh addUnit() {
		return new IndexCh(_type, _index+1);
	}
	public int switchType(int amount, int maxIndex) {
		return (_type + amount) % maxIndex;
	}
}