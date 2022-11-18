package com.cloud.manga.henryTing.holder;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/holder/HolderM.java
*/

import com.cloud.manga.henryTing.data.KeyStringBase;
import com.cloud.manga.henryTing.data.KPg;
import com.cloud.manga.henryTing.data.KPgS;
import com.cloud.manga.henryTing.data.KIdT;
import com.cloud.manga.henryTing.data.KIdTS;
import com.cloud.manga.henryTing.data.PathGet;
import com.cloud.manga.henryTing.data.FileB;
import com.cloud.manga.henryTing.infor.LabelGet;

import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.IndexEnum;
import com.cloud.manga.henryTing.unit.HolderEnum;
import com.cloud.manga.henryTing.consts.ThreadWork;

import com.cloud.manga.henryTing.infor.FrameM;
import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.infor.LabelGet;
import com.cloud.manga.henryTing.unit.Log;

import java.util.Map;
import java.util.HashMap;

public class HolderM {
	public final static Log _log = new Log("HolderM");
	private static class LableMark implements LabelGet {
		private final HolderBase _holder;
		private final KPgS _kpgs;
		private final IndexP _indexP;
		LableMark(HolderBase holder, IndexP indexP, KPgS kpgs) {
			_holder = holder; _indexP = indexP; _kpgs = kpgs; 
		}
		public KPg KPg() { return _kpgs.toKPg(); }
		public int Len() throws SDEBase { return _holder.getLen(_indexP); }
		public IndexP IndexP() { return _indexP; }
		public String Error() { return ""; }
	}
	
	private static class LableError implements LabelGet {
		private final String _error;
		LableError(String error) {
			_error = error; 
		}
		public KPg KPg() { return KPg.testCreate(); }
		public int Len() throws SDEBase { return 0; }
		public IndexP IndexP() { return new IndexP(0); }
		public String Error() { return _error; }
	}
	private static class ImageError implements ImageGet {
		public int size() { return 0; }
		public int lenX(int index) { return 0; }
		public int lenY(int index) { return 0; }
		public FileB Bytes(int index) { return null; }
	}
	
	public LabelGet getLabelGet(IndexP indexP, KPgS kpgs) {
		return new LableMark(_holder, indexP, kpgs);
	}
	public static LabelGet getLabelGet(String error) {
		return new LableError(error);
	}
	public static ImageGet getImageGet() {
		return new ImageError();
	}

	private final Map<KIdT,KPgS> _cacheMap = new HashMap<>();
	private final HolderId _holderId;
	private HolderBase _holder;
	private KIdT _kidt;
	
	public KIdTS getKIdTS() { return _holderId._kidts; }
	public KIdT getKIdT() { return _kidt; }
	
	public HolderEnum getHolderType() {
		return _holder._type;
	}
	
	public HolderM(HolderId holderId) {
		_log.p("ini");
		_holderId = holderId;
		_holder = holderId;
		_kidt = holderId.getKIdT();
	}
	public IndexP nextMark(IndexP indexP, int clickTime) throws SDEBase {
		if(_holder._type != HolderEnum.Ch) {
			SDEBase e = SDEError.cHolderMNextMark(_holder._type.toString());
			_log.e("_holder._type != HolderEnum.Ch", e.toString());
			throw e;
		}
		return HolderCh.get(_kidt).nextMark(indexP, clickTime);
	}
	public IndexP prevMark(IndexP indexP, int clickTime) throws SDEBase {
		if(_holder._type != HolderEnum.Ch) {
			SDEBase e = SDEError.cHolderMNextMark(_holder._type.toString());
			_log.e("_holder._type != HolderEnum.Ch", e.toString());
			throw e;
		}
		return HolderCh.get(_kidt).prevMark(indexP, clickTime);
	}
	public IndexP getIniIndexP() throws SDEBase {
		_log.p("getIniIndexP");
		return _holder.getIniIndexP();
	}
	// public IndexP getStartIndexP() {
		// return _holder.getStartIndexP();
	// }
	public IterUnit getIterUnit() { return _holder; }

	public final KPgS getKPgS(IndexP indexP) throws SDEBase {
		_log.p("getKPgS");
		KPgS kpgs = _holder.getKPgS(indexP);
		if (indexP._type == IndexEnum.id ) {
			_kidt = _holderId.getKIdT(indexP._ith);
		}
		_cacheMap.put(_kidt, kpgs);
		return kpgs;
	}

	public final void clearMemory() { _holderId.clearMemory(_kidt); _cacheMap.remove(_kidt); }
	public final void clearAllMemory() { _holderId.clearMemory(); _cacheMap.clear(); }

	public IndexP setFrameM(FrameM frameM) {
		return setFrameM(frameM, null);
	}
	public IndexP setFrameM(FrameM frameM, IndexP indexP) {
		_log.p("setFrameM frameM", frameM.toString());
		HolderEnum typeNew = frameM.getHolderType();
		if (frameM.isIniState()) {
			SDEBase e = SDEError.cHolderMFrameTypeIni(frameM._keyName);
			_log.e("frameM.isIniState()", e.toString());
			throw e;
		}
		if (indexP == null && _holder._type!=HolderEnum.Id) {
			SDEBase e = SDEError.cHolderMFrameTypeIni(frameM._keyName+" is not idType");
			_log.e("frameM.isIniState()", e.toString());
			throw e;
		}
		HolderEnum typeOld = _holder._type;
		HolderBase holder = _holder;
		IndexP indexRe = indexP;
		switch(typeOld) {
			case Id:
			switch(typeNew) {
				case Ch: 
				holder = HolderCh.get(_kidt); 
				indexRe = holder.getIniIndexP();
				break;
				case Pg:
				holder = HolderPg.get(_kidt);
				indexRe = holder.getIniIndexP();
				break;
				default: 
				if (indexP == null) { indexRe =_holder.getIniIndexP(); }
				else { indexRe = indexP; } break;
			}break;
			case Ch:
			switch(typeNew) {
				case Id: {
					indexRe = _holderId.getIndexP(_kidt);
					KPgS kpgs = _cacheMap.get(_kidt);
					if (kpgs == null) { break; }
					((HolderCh) holder).setIniIndexP(kpgs.toKPg()._kch);
					holder = _holderId;
					holder.saveKPgS(kpgs);
					ThreadWork.saveMarkHistory(PathGet.get(_kidt), kpgs.toKCh());
					break;
				}
				case Pg: 
				KPgS kpgs = _holder.getKPgS(indexP);
				holder = HolderPg.get(_kidt);
				indexRe = holder.getIndexP(kpgs.toKPg());
				break;
				default: break;
			}break;
			case Pg:
			switch(typeNew) {
				case Id: {
					indexRe = _holderId.getIndexP(_kidt);
					KPgS kpgs = _cacheMap.get(_kidt);
					if (kpgs == null) { break; }
					HolderPg.get(_kidt).setIniIndexP(kpgs.toKPg());
					HolderCh.get(_kidt).setIniIndexP(kpgs.toKPg()._kch);
					HolderCh.get(_kidt).saveKPgS(kpgs);
					holder = _holderId;
					holder.saveKPgS(kpgs);
					ThreadWork.saveHistory(PathGet.get(_kidt), kpgs);
					ThreadWork.saveMarkHistory(PathGet.get(_kidt), kpgs.toKCh());
					break;
				}
				case Ch: {
					HolderBase t_holder = HolderCh.get(_kidt);
					indexRe = indexP.toPg().toIndexCh();
					KPgS kpgs = _cacheMap.get(_kidt);
					if (kpgs == null) { break; }
					HolderPg.get(_kidt).setIniIndexP(kpgs.toKPg());
					holder = t_holder;
					holder.saveKPgS(kpgs);
					ThreadWork.saveMarkHistory(PathGet.get(_kidt), kpgs.toKCh());
					break;
				}
				default: break;
			}break;
		}
		_holder = holder;
		return indexRe;
	}
	
	public void close() throws SDEBase {
		_log.p("close");
		KPgS kpgs = _cacheMap.get(_kidt);
		if (kpgs == null) { return; }
		ThreadWork.saveHistory(PathGet.get(_kidt), kpgs);
		ThreadWork.saveMarkHistory(PathGet.get(_kidt), kpgs.toKCh());
	}
}