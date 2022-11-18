package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/IndexPg.java
*/

import com.cloud.manga.henryTing.holder.MapL;

public class IndexPg {
	//public final static Log _log = new Log("IndexPg");
	
	public final IndexCh _indexCh;
	public final Integer _index;
	
	public IndexP toIndexCh() {
		return _indexCh.toIndexP();
	}

	public IndexP toIndexId() {
		return new IndexP(_index);
	}
	public String toString() {
		return toIndexP().toString();
	}
	public boolean equals(Object obj) {
        if(obj instanceof IndexPg) {
            IndexPg k = (IndexPg) obj;
            return _indexCh.equals(k._indexCh) && _index.equals(_index);
        }
		return false;
	}
    public int hashCode() {
        return toIndexP().hashCode();
    }
	public IndexPg() {
		_indexCh = new IndexCh();
		_index = 0;
	}
	public IndexPg(IndexCh indexCh) {
		// assert indexCh != null: "indexCh != null";
		_indexCh = indexCh;
		_index = 0;
	}
	public IndexPg(IndexP indexP) throws SDEBase {
		if (indexP._type != IndexEnum.pg) { throw SDEError.cIndexPTypeError(2); }
		_indexCh = indexP._indexP.toCh();
		_index = indexP._ith;
	}
	public IndexP toIndexP() {
		return cIndexP(_indexCh, _index);
	}
	public static IndexP cIndexP(IndexCh indexCh, int index) {
		return cIndexP(indexCh.toIndexP(), index);
	}
	public static IndexP cIndexP(IndexP indexP, int index) {
		return new IndexP(indexP, index);
	}
	
	public IndexP goFirst() {
		return cIndexP(_indexCh, 0);
	}
	public IndexP goLast(int size_) {
		return cIndexP(_indexCh, size_-1);
	}
	
	public IndexP add(int amount, MapL<IndexCh,Integer> mapSize, final int maxChSize, final boolean isBound) 
		throws SDEBase 
	{
		//_log.p("add");
		// int mapSize if it fail to get size, it will throw SDEBase
		int maxIndex = mapSize.getEleV(_indexCh);
		int newIndex = _index + amount;
		if (IndexP.isInRange(newIndex, maxIndex)) {
			return cIndexP(_indexCh, newIndex); 
		}
		IndexCh indexCh = null;
		IndexP indexP = null;
		if(isBound) {
			// boolean isUnitAmount = (int)(Math.abs(amount)) == 1;
			//_log.t("isBound");
			if (amount > 0) {
				//_log.t("amount > 0");
				if (_index == maxIndex-1) {
					//_log.t("last index");
					indexCh = _indexCh.addToCh(1, maxChSize);
					if (indexCh == null) { return null; }
					// maxIndex = mapSize.getEleV(indexCh);
					newIndex = 0;
				} else {
					//_log.t("other index");
					indexCh = _indexCh; newIndex = maxIndex-1; 
				}
			} else {
				//_log.t("amount <= 0");
				if (_index == 0) {
					//_log.t("first index _indexCh", _indexCh.toString());
					indexCh = _indexCh.addToCh(-1, maxChSize);
					if (indexCh == null) {
						//_log.t("indexCh == null maxChSize=", Integer.valueOf(maxChSize).toString());
						return null;
					}
					maxIndex = mapSize.getEleV(indexCh);
					newIndex = maxIndex-1;
				} else {
					//_log.t("other index");
					indexCh = _indexCh; newIndex = 0;
				}
			}
		} else {
			//_log.t("isNotBound");
			if (amount > 0) {
				//_log.t("amount > 0");
				do {
					indexP = _indexCh.add( 1, maxChSize);
					if (indexP == null || newIndex < 0) {
						return null;
					}
					indexCh = indexP.toCh();
					newIndex -= maxIndex;
					maxIndex = mapSize.getEleV(indexCh);	
				} while(!IndexP.isInRange(newIndex, maxIndex));
			} else {
				//_log.t("amount <= 0");
				do {
					indexP = _indexCh.add(-1, maxChSize);
					if (indexP == null || newIndex > 1000) {
						return null;
					}
					indexCh = indexP.toCh();
					newIndex += maxIndex;
					maxIndex = mapSize.getEleV(indexCh);	
				} while(!IndexP.isInRange(newIndex, maxIndex));
			}
		}
		return cIndexP(indexCh, newIndex);
	}
}