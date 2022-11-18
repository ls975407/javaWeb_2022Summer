package com.cloud.manga.henryTing.holder;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/holder/HolderId.java
*/

import com.cloud.manga.henryTing.data.KeyStringBase;
import com.cloud.manga.henryTing.data.KId;
import com.cloud.manga.henryTing.data.KCh;
import com.cloud.manga.henryTing.data.KPg;
import com.cloud.manga.henryTing.data.KPgS;
import com.cloud.manga.henryTing.data.PathGet;
import com.cloud.manga.henryTing.data.TokenUS;
import com.cloud.manga.henryTing.data.KIdT;
import com.cloud.manga.henryTing.data.KIdTS;
import com.cloud.manga.henryTing.data.KAddressT;

import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.IndexId;
import com.cloud.manga.henryTing.unit.SDEBase;

import java.util.Iterator;
import com.cloud.manga.henryTing.unit.HolderEnum;
import com.cloud.manga.henryTing.unit.Log;

public class HolderId extends HolderBase implements Iterable<KIdT> {

	public final KIdTS _kidts;
	public final static Log _log = new Log("HolderId");
	public static String _getKeyName(KId[] kids) {
		StringBuilder buider = new StringBuilder();
		for (KId kid: kids) {
			buider.append(kid._keyName);
		}
		return buider.toString();
	}
	
	// private static class HolderChD {
		// static HolderChD get(KIdT kidt) { return new HolderChD(); }
		// static void clearMemory(KIdT kidt) {}
		// void setIniIndexP(KCh kch) {}
		// KPgS getKPgS(IndexP indexP) { return null; }
		// IndexP getIniIndexP() { return new IndexP(-1); }
	// }
	// private static class HolderPgD {
		// static HolderPgD get(KIdT kidt) { return new HolderPgD(); }
		// static boolean isExist(KIdT kidt) { return true; }
		// void updateKPgS(IndexP indexP, KPgS kpgs) {}
		// void removeKPgS(IndexP indexP, KPgS kpgs) {}
		// KPgS getKPgS(IndexP indexP) { return null; }
		// static void clearMemory(KIdT kidt) {}
		// void setIniIndexP(KPg kpg) {}
		// IndexP getIndexP(KPg kpg) { return null; }
	// }
	
	public final void clearMemory(KIdT kidt) {
		_log.t("clearMemory", kidt.toString());
		IndexP indexP = getIndexP(kidt);
		super.clear(indexP);
		// HolderPgD.clearMemory(kidt);
		// HolderChD.clearMemory(kidt);
		HolderPg.clearMemory(kidt);
		HolderCh.clearMemory(kidt);
	}
	public static void clearMemory(KPgS kpgs) {
		HolderPg.clearMemory(kpgs);
	}
	
	public final void clearMemory() {
		_log.t("clearMemory");
		super.clear();
		for (KIdT kidt: _kidts) {
			// HolderPgD.clearMemory(kidt);
			// HolderChD.clearMemory(kidt);
			HolderPg.clearMemory(kidt);
			HolderCh.clearMemory(kidt);
		}
	}
	
	// KIdT[] kidts
	public HolderId(KAddressT kadt) throws SDEBase {
		super(HolderEnum.Id, kadt.getKeyName());
		super.setCacheMap(new MapIdIndexPKPgS(_setting.mapIdIndexPKPgS()));
		_log.t("ini HolderId");
		_kidts = kadt.getKIdTS();
		
		
		KId kid = kadt.getKId();
		if (kid == null) {
			_log.t("use default id");
			_setIniIndexP(new IndexId().toIndexP());
			return;
		}
		setIniIndexP(kid);
		KIdT kidt = getKIdT();
		_log.t("use id ", kid.toString());
		
		KCh kch = kadt.getKCh(kidt);
		if (kch == null) {
			_log.t("use default ch");
			if (!HolderCh.isExist(kidt)) { return; }
			HolderCh.get(kidt)._setIniIndexP(null);
			return;
		}
		// HolderChD.get(kidt).setIniIndexP(kch);
		_log.t("use ch ", kch.toString());
		HolderCh.get(kidt).setIniIndexP(kch);
		
		KPg kpg = kadt.getKPg(kidt);
		if (kpg == null) {
			_log.t("use default pg");
			if (!HolderPg.isExist(kidt)) { return; }
			HolderPg.get(kidt)._setIniIndexP(null);
			return;
		}
		// HolderPgD.get(kidt).setIniIndexP(kpg);
		_log.t("use pg ", kpg.toString());
		HolderPg.get(kidt).setIniIndexP(kpg);
	}
    public Iterator<KIdT> iterator() { return _kidts.iterator(); }
	
	
	
	private class MapIdIndexPKPgS extends EleC<IndexP, OrderEle<KPgS>> {
		MapIdIndexPKPgS(int limit) { super(limit); }
		public OrderEle<KPgS> createEle(IndexP indexP) throws SDEBase {
			_log.t("create KPgS", indexP.toString());
			final KIdT kidt = getKIdT(indexP.toId()._index);

			KPgS kpgs = HolderId.super.getCasheKPgS(kidt);
			if (kpgs != null) { return new OrderEle<KPgS>(kpgs); }

			HolderBase holder = HolderCh.get(kidt);
			// HolderChD holder = HolderChD.get(kidt);
			kpgs = holder.getKPgS(holder.getIniIndexP());
			return new OrderEle<KPgS>(kpgs);
		}
	}
	
	protected void updateKPgS(IndexP indexP, KPgS kpgs) throws SDEBase {
		_log.t("updateKPgS", indexP.toString(), kpgs.toString());
		KIdT kidt = getKIdT(indexP.toId()._index);

		if (!HolderPg.isExist(kidt)) { return; }
		HolderPg holder = HolderPg.get(kidt);
		indexP = holder.getIndexP(kpgs.toKPg());
		holder.updateKPgS(indexP, kpgs);
	}
	protected void removeKPgS(IndexP indexP, KPgS kpgs) throws SDEBase {
		_log.t("removeKPgS", indexP.toString(), kpgs.toString());
		KIdT kidt = getKIdT(indexP.toId()._index);

		if (!HolderPg.isExist(kidt)) { return; }
		HolderPg holder = HolderPg.get(kidt);
		indexP = holder.getIndexP(kpgs.toKPg());
		holder.removeKPgS(indexP, kpgs);
	}

	public int getLen(IndexP indexP) throws SDEBase {
		return _kidts.size();
	}

	public KIdT getKIdT() {
		_log.t("getKIdT");
		KIdT kidt;
		IndexP indexP = _getIniIndexP();
		if (indexP == null) {
			kidt = _kidts.get(0);
		} else {
			kidt = _kidts.get(indexP.toId()._index);
		}
		_log.d("getKIdT", kidt.toString());
		return kidt;
	}
	public KIdT getKIdT(int index) { return _kidts.get(index); }
	public String getKChMark() throws SDEBase {
		_log.t("getKChMark");
		// return HolderCh.get(kidt).getKChMark();
		return null;
	}
	public IndexP getIndexP(KPg kpg) throws SDEBase 
	{ return getIndexP(kpg._kch._kid); }
	public IndexP getStartIndexP() { return new IndexId().toIndexP(); }
	
	public void setIniIndexP(KId kid) throws SDEBase {
		IndexP indexP = getIndexP(kid);
		if (indexP == null) {
			_log.w("setIniIndexP indexP == null", kid.toString());
		}
		_setIniIndexP(indexP);
	}
	public IndexP getIndexP(KId kid) throws SDEBase {
		_log.t("getIndexP");
		KId tkid;
		for (int ith=0; ith<_kidts.size(); ith++) {
			// System.out.println(ith);
			// System.out.println(_kidts.get(ith)._keyName);
			if (kid._keyName.equals(_kidts.get(ith)._keyName)) {
				return new IndexP(ith);
			}
		}
		// System.out.println(-1);
		// System.out.println(kid._keyName);
		throw HolderBase.ExceptionGetIndexP(kid._keyName);
	}
	
	public IndexP nextPage(IndexP indexP, int count) throws SDEBase {
		IndexId indexId = indexP.toId();
		return indexId.add( count, _kidts.size());
	}
	public IndexP nextChapter(IndexP indexP, int count) throws SDEBase {
		IndexId indexId = indexP.toId();
		return indexId.add( count*getPressAmount(), _kidts.size());
	}
	public IndexP prevPage(IndexP indexP, int count) throws SDEBase {
		IndexId indexId = indexP.toId();
		return indexId.add(-count, _kidts.size());
	}
	public IndexP prevChapter(IndexP indexP, int count) throws SDEBase {
		IndexId indexId = indexP.toId();
		return indexId.add(-count*getPressAmount(), _kidts.size());
	}
	public IndexP goFirst(IndexP indexP) throws SDEBase {
		IndexId indexId = indexP.toId();
		return indexId.goFirst();
	}
	public IndexP goLast(IndexP indexP) throws SDEBase {
		IndexId indexId = indexP.toId();
		return indexId.goLast(_kidts.size());
	}
}