package com.cloud.manga.henryTing.holder;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/holder/HolderCh.java
*/

import com.cloud.manga.henryTing.data.KCh;
import com.cloud.manga.henryTing.data.KPg;
import com.cloud.manga.henryTing.data.KPgS;
import com.cloud.manga.henryTing.data.PathGet;
import com.cloud.manga.henryTing.data.KIdT;
import com.cloud.manga.henryTing.data.KChT;
import com.cloud.manga.henryTing.data.KCh2D;
import com.cloud.manga.henryTing.data.InforCh;

import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.IndexCh;
import com.cloud.manga.henryTing.unit.IndexPg;
import com.cloud.manga.henryTing.unit.SDEBase;

import com.cloud.manga.henryTing.unit.HolderEnum;
import com.cloud.manga.henryTing.unit.Log;

public class HolderCh extends HolderBase {

	public final static Log _log = new Log("HolderCh");
	public final KIdT _kidt;
	public final KCh2D _kch2d;
	
	// private static class HolderPgD {
		// static HolderPgD get(KIdT kidt) { return new HolderPgD(); }
		// static boolean isExist(KIdT kidt) { return true; }
		// KPgS getKPgS(IndexP indexP) { return null; }
		// void updateKPgS(IndexP indexP, KPgS kpgs) {}
		// void removeKPgS(IndexP indexP, KPgS kpgs) {}
		// IndexP getIniIndexP() { return new IndexP(-1); }
		// IndexP getIndexP(KPg kpg) { return null; }
		
	// }
	
	private static MapL<KIdT,HolderCh> _mapClass = null;
	public static HolderCh get(KIdT kidt) throws SDEBase {
		if (_mapClass == null) {
			_mapClass = new MapLAd<>(new MapKIdTHolderCh(_setting.mapKIdTHolderCh()));
		}
		return _mapClass.getEleV(kidt);
	}
	public static boolean isExist(KIdT kidt) throws SDEBase {
		if (_mapClass == null) {
			_mapClass = new MapLAd<>(new MapKIdTHolderCh(_setting.mapKIdTHolderCh()));
		}
		return _mapClass.containsKey(kidt);
	}
	public static void clearMemory(KIdT kidt) {
		if (_mapClass != null) { _mapClass.removeEle(kidt); }
	}
	private static class MapKIdTHolderCh extends EleC<KIdT, OrderEle<HolderCh>> {
		MapKIdTHolderCh(int limit) { super(limit); }
		public OrderEle<HolderCh> createEle(KIdT kidt) throws SDEBase {
			_log.t("create HolderCh", kidt.toString());
			return new OrderEle<>(new HolderCh(kidt, HolderBase.readKCh2D(kidt)));
		}
	}

	private HolderCh(KIdT kidt, KCh2D kch2d) {
		super(HolderEnum.Ch, kidt._keyName);
		super.setCacheMap(new MapChIndexPKPgS(_setting.mapChIndexPKPgS()));
		_kidt = kidt; _kch2d = kch2d;
	}
	private class MapChIndexPKPgS extends EleC<IndexP, OrderEle<KPgS>> {
		MapChIndexPKPgS(int limit) { super(limit); }
		public OrderEle<KPgS> createEle(IndexP indexP) throws SDEBase {
			_log.t("create KPgS", indexP.toString());
			final KIdT kidt = _kidt;
			final KChT kcht = _kch2d.get(indexP);
			final KCh kch = kcht;
			
			// System.out.println("HolderCh MapChIndexPKPgS createEle");
			// indexP.print();
			// System.out.println("HolderCh mark 01");
			// System.out.println("kcht = " + kcht._keyName);
			
			// KPgS kpgs = HolderCh.super.getCasheKPgS(kidt, new InforCh(kch)._mark);
			KPgS kpgs;
			kpgs = HolderCh.super.getCasheKPgS(kidt, kch);
			if (kpgs == null) {
				kpgs = HolderCh.super.getCasheKPgS(kidt, new InforCh(kch)._mark);
				if (kpgs != null && !getIndexP(kpgs.toKPg()._kch).equals(indexP)) {
					kpgs = null;
				}
			}
			 // && getIndexP(kpgs).equals(indexP)
			if (kpgs != null) { return new OrderEle<>(kpgs); }
			// System.out.println("HolderCh mark 02");
			HolderPg holder = HolderPg.get(kidt);
			kpgs = holder.getKPgS(new IndexPg(indexP.toCh()).toIndexP());
			HolderCh.super._saveCover(PathGet.get(_kidt), kpgs);
			return new OrderEle<>(kpgs);
		}
	}

	protected void updateKPgS(IndexP indexP, KPgS kpgs) throws SDEBase {
		_log.t("updateKPgS", indexP.toString(), kpgs.toKPg().toString());
		KIdT kidt = getKIdT();
		if (!HolderPg.isExist(kidt)) { return; }
		HolderPg holder = HolderPg.get(kidt);
		indexP = holder.getIndexP(kpgs.toKPg());
		holder.updateKPgS(indexP, kpgs);
	}
	protected void removeKPgS(IndexP indexP, KPgS kpgs) throws SDEBase {
		// _log.t("removeKPgS (do nothing)");
		// KIdT kidt = getKIdT();

		// if (!HolderPg.isExist(kidt)) { return; }
		// HolderPg holder = HolderPg.get(kidt);
		// indexP = holder.getIndexP(kpgs.toKPg());
		// holder.removeKPgS(indexP, kpgs);
		// since we use getUrl method to get url as soon as we need it
		// it is no need to recalculate the remote file 
	}

	public int getLen(IndexP indexP) throws SDEBase {
		return _kch2d.size(indexP);
	}

	public KIdT getKIdT() { return _kidt; }
	public String getKChMark() throws SDEBase {
		_log.t("getKChMark");
		IndexP indexP = _getIniIndexP();
		if (indexP != null) {
			_log.t("iniIndexP != null", indexP.toString());
			KChT kcht = _kch2d.get(indexP);
			if (kcht != null) {
				return new InforCh(kcht)._mark;
			}
			_log.w("getKChMark _getIniIndexP _kch2d.get = null");
			_setIniIndexP(null);
		}
		_log.t("iniIndexP == null");
		KCh _ini_kch = super.getDefaultKCh(_kidt);
		indexP = _kch2d.getIndexCh(_ini_kch);
		if (indexP == null) {
			throw super.ExceptionRecordNotMatch("DefaultKCh is unable to match local");
		}
		_setIniIndexP(indexP);
		return new InforCh(_ini_kch)._mark;
	}
	public IndexP getIndexP(KPg kpg) throws SDEBase { 
		_log.t("getIndexP", kpg.toString());
		// System.out.println("HolderKCh getIndexP KPg kpg");
		// try {
			return getIndexP(kpg._kch);
		// } catch (SDEBase e) {
			// throw HolderBase.ExceptionGetIndexP(kpg._keyName);
		// }
	}
	public IndexP getStartIndexP() { return new IndexCh().toIndexP(); }
	
	public boolean setIniIndexP(KCh kch) throws SDEBase {
		_log.t("setIniIndexP", kch.toString());
		IndexP indexP = getIndexP(kch);
		_setIniIndexP(indexP);
		if (indexP == null) {
			return false; // _log.w("indexP == null");
		}
		return true;
	}
	public IndexP getIndexP(KCh kch) throws SDEBase {
		_log.t("getIndexP", kch.toString());
		// System.out.println("HolderKCh getIndexP KCh kch");
		return _kch2d.getIndexCh(kch);
	}

	public IndexP nextPage(IndexP indexP, int count) throws SDEBase {
		IndexCh indexCh = indexP.toCh();
		return indexCh.add( count, _kch2d.size(indexCh));
	}
	public IndexP nextChapter(IndexP indexP, int count) throws SDEBase {
		IndexCh indexCh = indexP.toCh();
		return indexCh.add( count*getPressAmount(), _kch2d.size(indexCh));
	}
	public IndexP prevPage(IndexP indexP, int count) throws SDEBase {
		IndexCh indexCh = indexP.toCh();
		// System.out.println(String.format("Ch prevPage %d %s %d",count, indexP.toString(), _kch2d.size(indexCh)));
		return indexCh.add(-count, _kch2d.size(indexCh));
	}
	public IndexP prevChapter(IndexP indexP, int count) throws SDEBase {
		IndexCh indexCh = indexP.toCh();
		return indexCh.add(-count*getPressAmount(), _kch2d.size(indexCh));
	}
	public IndexP nextMark(IndexP indexP, int count) throws SDEBase {
		IndexCh indexCh = indexP.toCh();
		int type = indexCh.switchType(count, _kch2d.size());
		KCh kch = super.getDefaultKCh(_kidt, _kch2d.getMark(type));
		return getIndexP(kch);
	}
	public IndexP prevMark(IndexP indexP, int count) throws SDEBase {
		return nextMark(indexP, -count+_kch2d.size());
	}
	public IndexP goFirst(IndexP indexP) throws SDEBase {
		IndexCh indexCh = indexP.toCh();
		return indexCh.goFirst();
	}
	public IndexP goLast(IndexP indexP) throws SDEBase {
		IndexCh indexCh = indexP.toCh();
		return indexCh.goLast(_kch2d.size(indexP));
	}
}