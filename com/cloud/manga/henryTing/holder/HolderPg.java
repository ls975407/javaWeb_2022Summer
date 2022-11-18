package com.cloud.manga.henryTing.holder;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/holder/HolderPg.java
*/

import com.cloud.manga.henryTing.data.KPg;
import com.cloud.manga.henryTing.data.KPgS;
import com.cloud.manga.henryTing.data.KPgSF;
import com.cloud.manga.henryTing.data.KIdT;
import com.cloud.manga.henryTing.data.KChT;
import com.cloud.manga.henryTing.data.PathGet;
import com.cloud.manga.henryTing.data.KCh2D;

import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.IndexCh;
import com.cloud.manga.henryTing.unit.IndexPg;
import com.cloud.manga.henryTing.unit.SDEBase;

import com.cloud.manga.henryTing.unit.HolderEnum;
import com.cloud.manga.henryTing.unit.Log;


public class HolderPg extends HolderBase {

	public final static Log _log = new Log("HolderPg");
	private final HolderCh _holderCh;
	private final MapL<IndexCh,KPgSF> _mapKPgSF;
	private final MapL<IndexCh,Integer> _mapSize;

	private static MapL<KIdT,HolderPg> _mapClass = null;
	public static HolderPg get(KIdT kidt) throws SDEBase {
		if (_mapClass == null) {
			_mapClass = new MapLAd<>(new MapKIdTHolderPg(_setting.mapKIdTHolderPg()));
		}
		return _mapClass.getEleV(kidt);
	}
	public static boolean isExist(KIdT kidt) throws SDEBase {
		if (_mapClass == null) {
			_mapClass = new MapLAd<>(new MapKIdTHolderPg(_setting.mapKIdTHolderPg()));
		}
		return _mapClass.containsKey(kidt);
	}
	public static void clearMemory(KIdT kidt) {
		if (_mapClass != null) { _mapClass.removeEle(kidt); }
	}
	public static void clearMemory(KPgS kpgs) {
		_log.t("clearMemory()");
		KIdT kidt = new KIdT(kpgs.toKPg()._kch._kid);
		HolderPg holderPg = HolderPg.get(kidt);
		IndexCh indexCh = holderPg.getIndexP(kpgs.toKPg()).toPg()._indexCh;
		holderPg._mapKPgSF.removeEle(indexCh);
		holderPg._mapSize.removeEle(indexCh);
	}
	private static class MapKIdTHolderPg extends EleC<KIdT, OrderEle<HolderPg>> {
		MapKIdTHolderPg(int limit) { super(limit); }
		public OrderEle<HolderPg> createEle(KIdT kidt) throws SDEBase {
			_log.t("create HolderPg", kidt.toString());
			return new OrderEle<HolderPg>(new HolderPg(HolderCh.get(kidt)));
		}
	}
	private HolderPg(HolderCh holderCh) {
		super(HolderEnum.Pg, holderCh._keyName);
		super.setCacheMap(new MapPgIndexPKPgS(_setting.mapPgIndexPKPgS()));
		_holderCh = holderCh;
		_mapKPgSF = new MapLAd<>(new MapPgIndexChKPgSF(_setting.mapPgIndexChKPgSF()));
		_mapSize = new MapLAd<>(new MapPgIndexChInteger(_setting.mapPgIndexChInteger()));
	}
	
	private KPg getKPg(IndexPg indexPg) {
		// _log.t("getKPg", indexPg.toString());
		IndexCh indexCh = indexPg._indexCh;
		int index = indexPg._index;
		return _mapKPgSF.getEleV(indexCh).getKPg(index);
	}
	
	private class MapPgIndexPKPgS extends EleC<IndexP, OrderEle<KPgS>> {
		MapPgIndexPKPgS(int limit) { super(limit); }
		public OrderEle<KPgS> createEle(IndexP indexP) throws SDEBase {
			_log.t("create KPgS", indexP.toString());
			IndexPg indexPg = indexP.toPg();
			KPg kpg_0 = getKPg(indexPg);
			IndexP indexP_t = addPage( 1, indexP, false);
			if (indexP_t == null) {
				return new OrderEle<KPgS>(new KPgS(kpg_0, null));
			}
			indexPg = indexP_t.toPg();
			KPg kpg_1 = getKPg(indexPg);
			return new OrderEle<KPgS>(new KPgS(kpg_0, kpg_1));
		}
	}

	private class MapPgIndexChKPgSF extends EleC<IndexCh, OrderEle<KPgSF>> {
		MapPgIndexChKPgSF(int limit) { super(limit); }
		public OrderEle<KPgSF> createEle(IndexCh indexCh) throws SDEBase {
			_log.t("create KPgSF", indexCh.toString());
			KChT kcht = _holderCh._kch2d.get(indexCh.toIndexP());
			// if (kcht == null) { impossiple
			KPgSF kpgsf = HolderPg.super.getKPgSF(PathGet.get(HolderPg.this.getKIdT()), kcht);
			return new OrderEle<>(kpgsf, kpgsf.size());
		}
	}
	private class MapPgIndexChInteger extends EleC<IndexCh, OrderEle<Integer>> {
		MapPgIndexChInteger(int limit) { super(limit); }
		public OrderEle<Integer> createEle(IndexCh indexCh) throws SDEBase {
			_log.t("create size", indexCh.toString());
			KPgSF kpgsf = _mapKPgSF.getEleV(indexCh);
			return new OrderEle<>(kpgsf.size());
		}
	}
	// size of kpgs must be two
	protected void updateKPgS(IndexP indexP, KPgS kpgs) throws SDEBase {
		_log.t("updateKPgS", indexP.toString(), kpgs.toKPg().toString());
		IndexCh indexCh = indexP.toPg()._indexCh;
		if (!_mapKPgSF.containsKey(indexCh)) { return; }
		KPgSF kpgsf = _mapKPgSF.getEleV(indexCh);
		
		KPg kpg_0 = kpgs.get(0), kpg_1 = kpgs.get(1);	
		if (!kpgsf.update(kpg_0)) {
			_log.w("updateKPgS fail", kpg_0.toString());
			// throw HolderBase.ExceptionUpdateKPgS(kpg_0.toString());
		}
		if (kpg_1 == null) { return; }
		if (kpg_0._kch == kpg_1._kch) {
			if (!kpgsf.update(kpg_1)) {
				_log.w("updateKPgS fail", kpg_1.toString());
				// throw HolderBase.ExceptionUpdateKPgS(kpg_1.toString());
			}
		} else {
			indexCh = indexCh.addUnit();
			if (_mapKPgSF.containsKey(indexCh)) {
				kpgsf = _mapKPgSF.getEleV(indexCh);
				if (!kpgsf.update(kpg_1)) {
					_log.w("updateKPgS fail", kpg_1.toString());
					// throw HolderBase.ExceptionUpdateKPgS(kpg_1.toString());
				}
			}
		}
	}
	protected void removeKPgS(IndexP indexP, KPgS kpgs) throws SDEBase {
		// _log.t("removeKPgS (do nothing)");
		// IndexCh indexCh = indexP.toPg()._indexCh;
		// KPg kpg_0 = kpgs.get(0), kpg_1 = kpgs.get(1);
		// if (kpg_0._kch == kpg_1._kch) {
			// _mapKPgSF.removeEle(indexCh);
		// } else {
			// _mapKPgSF.removeEle(indexCh);
			// _mapKPgSF.removeEle(indexCh.addUnit());
		// }
		// since we use getUrl method to get url as soon as we need it
		// it is no need to recalculate the remote file 
	}
	
	public int getLen(IndexP indexP) throws SDEBase {
		IndexCh indexCh = indexP.toPg()._indexCh;
		return _mapSize.getEleV(indexCh);
	}
	
	public KIdT getKIdT() { return _holderCh._kidt; }
	public String getKChMark() throws SDEBase {
		return _holderCh.getKChMark();
	}
	// public IndexP getIndexP(KPg kpg) throw SDEBase 
	// { return getIndexP(kpg._kch); }
	public IndexP getStartIndexP() { return new IndexPg().toIndexP(); }
	
	public void setIniIndexP(KPg kpg) throws SDEBase {
		IndexP indexP = getIndexP(kpg);
		_setIniIndexP(indexP);
	}
	public IndexP getIndexP(KPg kpg) throws SDEBase {
		_log.t("getIndexP", kpg.toString());
		IndexCh indexCh = _holderCh.getIndexP(kpg._kch).toCh();
		KPgSF kpgsf = _mapKPgSF.getEleV(indexCh);
		int index = kpgsf.findIndex(kpg._tokenU._keyName);
		if (index < 0) {
			SDEBase e = HolderBase.ExceptionGetIndexP(kpg.toString());
			_log.e("getIndexP", e.toString());
			throw e;
		}
		return IndexPg.cIndexP(indexCh, index);
	}	
	
	private IndexP addPage(int amount, IndexP indexP, boolean isBound) throws SDEBase {
		IndexPg indexPg = indexP.toPg();
		return indexPg.add( amount, _mapSize, _holderCh._kch2d.size(indexPg._indexCh), isBound);
	}
	public IndexP nextPage(IndexP indexP, int count) throws SDEBase {
		return addPage( count, indexP, true);
	}
	public IndexP nextChapter(IndexP indexP, int count) throws SDEBase {
		return addPage( count*getPressAmount(), indexP, true);
	}
	public IndexP prevPage(IndexP indexP, int count) throws SDEBase {
		return addPage(-count, indexP, true);
	}
	public IndexP prevChapter(IndexP indexP, int count) throws SDEBase {
		return addPage(-count*getPressAmount(), indexP, true);
	}
	@Override
	public IndexP pnextPage(IndexP indexP) throws SDEBase {
		return addPage( 2, indexP, false);
	}
	@Override
	public IndexP pnextChapter(IndexP indexP) throws SDEBase {
		return addPage( getPressAmount(), indexP, false);
	}
	@Override
	public IndexP pprevPage(IndexP indexP) throws SDEBase {
		return addPage(-2, indexP, false);
	}
	@Override
	public IndexP pprevChapter(IndexP indexP) throws SDEBase {
		return addPage(-getPressAmount(), indexP, false);
	}
	public IndexP goFirst(IndexP indexP) throws SDEBase {
		IndexPg indexPg = indexP.toPg();
		return indexPg.goFirst();
	}
	public IndexP goLast(IndexP indexP) throws SDEBase {
		IndexPg indexPg = indexP.toPg();
		return indexPg.goLast(_mapSize.getEleV(indexPg._indexCh));
	}
}