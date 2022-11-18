package com.cloud.manga.henryTing.holder;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/holder/HolderBase.java
*/


import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;

import com.cloud.manga.henryTing.data.KeyStringBase;
import com.cloud.manga.henryTing.data.DateCh;
import com.cloud.manga.henryTing.data.DatePgS;
import com.cloud.manga.henryTing.data.KId;
import com.cloud.manga.henryTing.data.KCh;
import com.cloud.manga.henryTing.data.KPg;
import com.cloud.manga.henryTing.data.KPgS;
import com.cloud.manga.henryTing.data.KPgSF;
import com.cloud.manga.henryTing.data.PathGet;
import com.cloud.manga.henryTing.data.TokenUS;
import com.cloud.manga.henryTing.data.KIdT;
import com.cloud.manga.henryTing.data.KChT;
import com.cloud.manga.henryTing.data.KCh2D;

import com.cloud.manga.henryTing.consts.ThreadWork;
import com.cloud.manga.henryTing.consts.CloudM;
import com.cloud.manga.henryTing.unit.Log;

import org.json.JSONException;

import com.cloud.manga.henryTing.unit.HolderEnum;
public abstract class HolderBase extends KeyStringBase 
	implements IterUnit
{
	public final static Log _log = new Log("HolderBase");
	public final HolderEnum _type;
	public static Setting _setting;
	private MapL<IndexP,KPgS> _cacheMap;
	
	
	protected int getPressAmount() {
		return _setting.getPressAmount();
	}

	protected HolderBase(HolderEnum type, String keyName) {
		super(keyName);
		_type = type;
	}
	protected void setCacheMap(EleC<IndexP, OrderEle<KPgS>> eleCreator) {
		_cacheMap = new MapLAd<>(eleCreator);
	}
	public abstract IndexP nextPage(IndexP indexP, int count) throws SDEBase;
	public abstract IndexP nextChapter(IndexP indexP, int count) throws SDEBase;
	public abstract IndexP prevPage(IndexP indexP, int count) throws SDEBase;
	public abstract IndexP prevChapter(IndexP indexP, int count) throws SDEBase;
	public IndexP pnextPage(IndexP indexP) throws SDEBase { return nextPage(indexP, 1); }
	public IndexP pnextChapter(IndexP indexP) throws SDEBase { return nextChapter(indexP, 1); };
	public IndexP pprevPage(IndexP indexP) throws SDEBase { return prevPage(indexP, 1); };
	public IndexP pprevChapter(IndexP indexP) throws SDEBase { return prevChapter(indexP, 1); };
	public abstract IndexP goFirst(IndexP indexP) throws SDEBase;
	public abstract IndexP goLast(IndexP indexP) throws SDEBase;
	
	public boolean isLocal() {
		return getKIdT().isLocal();
	}
	public boolean isCloud() {
		return getKIdT().isCloud();
	}
	public abstract KIdT getKIdT();
	protected abstract String getKChMark() throws SDEBase;
	// read history or cover file to create KPgS
	protected KPgS getCasheKPgS(KIdT kidt) {
		return ThreadWork.getCasheKPgS(PathGet.get(kidt), kidt);
	}
	protected KPgS getCasheKPgS(KIdT kidt, String mark) {
		return ThreadWork.getCasheKPgS(PathGet.get(kidt), kidt, mark);
	}
	protected KPgS getCasheKPgS(KIdT kidt, KCh kch) {
		return ThreadWork.getCasheKPgS(PathGet.get(kidt), kch);
	}
	public abstract int getLen(IndexP indexP) throws SDEBase;
	protected abstract IndexP getIndexP(KPg kpg) throws SDEBase;
	protected abstract IndexP getStartIndexP();
	
	private IndexP _iniIndexP = null;
	protected void _setIniIndexP(IndexP iniIndexP) {
		_iniIndexP = iniIndexP;
	}
	protected IndexP _getIniIndexP() {
		return _iniIndexP;
	}
	// public synchronized void getIndexPAndSet(KPgS kpgs) throws SDEBase {
		// _iniIndexP = getIndexP(kpgs.toKPg());
		// // saveKPgS(_iniIndexP, kpgs);
	// }
	// if _iniIndexP == null
	// mean it use cover indexP and
	public IndexP getIniIndexP() throws SDEBase {
		_log.p("getIniIndexP()", _type.toString());
		if (_iniIndexP != null) { return _iniIndexP; }
		_log.t("getIniIndexP() _iniIndexP == null");
		
		if (_type == HolderEnum.Pg) {
			HolderBase holder = HolderCh.get(getKIdT());
			IndexP indexP = holder._getIniIndexP();
			if (indexP != null) {
				KPgS kpgs = holder.getKPgS(indexP);
				if (kpgs != null) {
					_iniIndexP = getIndexP(kpgs.toKPg());
					return _iniIndexP;
				}
			}
		}
		
		KPgS kpgs = getCasheKPgS(getKIdT(), getKChMark());
		_log.t("getIniIndexP() get kpgs");
		
		if (kpgs != null) {
			_log.t("getIniIndexP() ", kpgs.toKPg().toString());
			_iniIndexP = getIndexP(kpgs.toKPg());
		} else {
			_log.t("getIniIndexP() kpgs == null");
			_iniIndexP = getStartIndexP();	
		}
		return _iniIndexP;
	}

	public final KPgS getKPgS(IndexP indexP) throws SDEBase {
		// return _cacheMap.getEleV(indexP, false);
		return _cacheMap.getEleV(indexP);
	}
	// it will only be used by HolderId
	protected final void clear(IndexP indexP) {
		_cacheMap.removeEle(indexP);
	}
	// it will only be used by HolderId
	protected final void clear() {
		_cacheMap.clear();
	}
	// private IndexP _currentIndexP = null;
	// public synchronized final void saveKPgS(IndexP indexP, KPgS kpgs) throws SDEBase { 
		// _cacheMap.put(indexP, new OrderEle<KPgS>(kpgs));
		// // _currentIndexP = indexP;
	// }
	
	public synchronized final void resetKPgS(IndexP indexP, KPgS kpgS) throws SDEBase { 
		_log.p("resetKPgS");
		final int size = kpgS.size();
		KPg[] kpgs = new KPg[size];
		for (int ith=0; ith<size; ith++) {
			kpgs[ith] = new KPg(kpgS.get(ith), null);
		}
		saveKPgS(indexP, new KPgS(kpgs));
	}
	protected abstract void updateKPgS(IndexP indexP, KPgS kpgs) throws SDEBase;
	protected abstract void removeKPgS(IndexP indexP, KPgS kpgs) throws SDEBase;
	public synchronized final void clearKPgS(KPgS kpgs) throws SDEBase {		
		IndexP indexP = getIndexP(kpgs.toKPg());
		clearKPgS(indexP, kpgs);
	}
	public synchronized final void clearKPgS(IndexP indexP, KPgS kpgs) throws SDEBase { 
		_log.p("clearKPgS");	
		_cacheMap.removeEle(indexP);
		removeKPgS(indexP, kpgs);
	}
	public synchronized final void saveKPgS(KPgS kpgs) throws SDEBase { 
		IndexP indexP = getIndexP(kpgs.toKPg());
		saveKPgS(indexP, kpgs);
	}
	public synchronized final void saveKPgS(IndexP indexP, KPgS kpgs) throws SDEBase { 
		_log.p("saveKPgS");
		_cacheMap.putEle(indexP, kpgs);
		updateKPgS(indexP, kpgs);
	}
	
	protected KCh getDefaultKCh(KId kid) throws SDEBase {
		return ThreadWork.getDefaultKCh(PathGet.get(getKIdT()), kid);
	}
	protected KCh getDefaultKCh(KId kid, String mark) throws SDEBase {
		return ThreadWork.getDefaultKCh(PathGet.get(getKIdT()), kid, mark);
	}

	protected static KCh2D readKCh2D(KIdT kidt) throws SDEBase {
		return ThreadWork.readKCh2D(kidt);
	}

	static protected void _saveCover(PathGet pathGet, KPgS kpgs) throws SDEBase {
		ThreadWork.saveCover(pathGet, kpgs );
	}
	
	static protected KPgSF getKPgSF(PathGet pathGet, KChT kcht) throws SDEBase {
		return ThreadWork.getKPgSF(pathGet, kcht);
	}
	
	protected static SDEBase ExceptionUpdateKPgS(String keyName) {
		return SDEError.cHolderFailToUpdateKPgS(keyName);
	}
	protected static SDEBase ExceptionGetIndexP(String keyName) {
		return SDEError.cHolderFailToGetIndexP(keyName);
	}
	protected static SDEBase ExceptionRecordNotMatch(String content) {
		return SDEError.cHolderRecordNotMatch(content);
	}
}