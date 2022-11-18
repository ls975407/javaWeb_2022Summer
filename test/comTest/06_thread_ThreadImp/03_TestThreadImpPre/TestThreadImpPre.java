/*
javac -encoding utf8  TestThreadImpPre.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;



import com.cloud.manga.henryTing.data.*;


import com.cloud.manga.henryTing.holder.IterUnit;

import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.IndexCh;
import com.cloud.manga.henryTing.unit.IndexPg;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.consts.CloudM;
import com.cloud.manga.henryTing.consts.IdTokenS;
import com.cloud.manga.henryTing.consts.ThreadWork;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.tool.ThreadLocal;
import com.cloud.manga.henryTing.thread.ThreadImp;
import com.cloud.manga.henryTing.holder.MapL;
import com.cloud.manga.henryTing.holder.MapLAd;
import com.cloud.manga.henryTing.holder.OrderEle;
import com.cloud.manga.henryTing.holder.EleC;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;
import java.util.Scanner;

public class TestThreadImpPre implements IterUnit {	
	public static void println(String content) {
		System.out.println(content);
	}
	private KPg getKPg(IndexPg indexPg) throws SDEBase {
		IndexCh indexCh = indexPg._indexCh;
		int index = indexPg._index;
		if (!indexCh.equals(_indexCh)) {
			throw new SDEError("!indexCh.equals(_indexCh)");
		}
		return _kpgsf.getKPg(index);
	}
	private final KPgSF _kpgsf;
	private final IndexCh _indexCh;
	private final IndexP _indexP;
	private TestThreadImpPre() {
		KId kid = new KId("0039_涼風_0039_scaled");
		KIdT kidt = IdTokenS.ptr().find(kid);
		PathGet pathGet = PathGet.get(kidt);
		KCh2D kch2d = ThreadWork.readKCh2D(kidt);
		// println(kch2d.toString());
		
		// _indexCh = new IndexCh(int type, int index)
		_indexCh = new IndexCh(0, 2);
		KChT kcht = kch2d.get(_indexCh);
		_kpgsf = ThreadWork.getKPgSF(pathGet, kcht);
		_indexP = new IndexP(_indexCh.toIndexP(), 50);
	}
	
	private class MapTestClass extends EleC<IndexP, OrderEle<KPgS>> {
		MapTestClass(int limit) { super(limit); }
		public OrderEle<KPgS> createEle(IndexP indexP) throws SDEBase {
			IndexPg indexPg = indexP.toPg();
			KPg kpg_0 = getKPg(indexPg);
			indexPg = addPage( 1, indexP, false).toPg();
			KPg kpg_1 = getKPg(indexPg);
			return new OrderEle<KPgS>(new KPgS(kpg_0, kpg_1));
		}
	}
	private MapL<IndexP,KPgS> _cacheMap = new MapLAd<>(new MapTestClass(20));
	public void run() {
		SDEBase e = null;
		Scanner scanner = new Scanner(System.in);
		while(true) {
			println("pre start");
			ThreadImp.ptr().prepareImage(_indexP, this);
			scanner.next();
			
			println("pre stop\n");
			e = ThreadImp.ptr().joinPrepareImage();
			if (e != null) {
				println(e.toString());
			}
			printTable();
			println("\n");
			scanner.next();
		}
	}
	public KPgS getKPgS(IndexP indexP) throws SDEBase {
		return _cacheMap.getEleV(indexP);
	}
	public void printTable() {
		println("printTable");
		int count=0;
		for (int ith=10; ith<90; ith++) {
			if (_kpgsf.getKPg(ith)._isExist == null) {
				System.out.println(String.format("ith = %02d (null)", ith));
			} else if (_kpgsf.getKPg(ith)._isExist) {
				System.out.println(String.format("ith = %02d", ith)); count++;
			}
		}
		System.out.println(String.format("count = %02d", count));
	}
	private final MapL<IndexCh,Integer> _mapSize = new MapLAd<>(new MapPgIndexChInteger(10));
	private class MapPgIndexChInteger extends EleC<IndexCh, OrderEle<Integer>> {
		MapPgIndexChInteger(int limit) { super(limit); }
		public OrderEle<Integer> createEle(IndexCh indexCh) throws SDEBase {
			if (!indexCh.equals(_indexCh)) {
				throw new SDEError("!indexCh.equals(_indexCh)");
			}
			return new OrderEle<>(_kpgsf.size());
		}
	}
	
	
	public IndexP nextPage(IndexP indexP, int count) throws SDEBase { return null; }
	public IndexP nextChapter(IndexP indexP, int count) throws SDEBase { return null; }
	public IndexP prevPage(IndexP indexP, int count) throws SDEBase { return null; }
	public IndexP prevChapter(IndexP indexP, int count) throws SDEBase { return null; }
	public IndexP goFirst(IndexP indexP) throws SDEBase { return null; }
	public IndexP goLast(IndexP indexP) throws SDEBase { return null; }
	
	
	private int getPressAmount() {
		return 10;
	}
	
	private IndexP addPage(int amount, IndexP indexP, boolean isBound) throws SDEBase {
		IndexPg indexPg = indexP.toPg();
		return indexPg.add( amount, _mapSize, 3, isBound);
	}
	public IndexP pnextPage(IndexP indexP) throws SDEBase {
		return addPage( 2, indexP, false);
	}
	public IndexP pnextChapter(IndexP indexP) throws SDEBase {
		return addPage( getPressAmount(), indexP, false);
	}
	public IndexP pprevPage(IndexP indexP) throws SDEBase {
		return addPage(-2, indexP, false);
	}
	public IndexP pprevChapter(IndexP indexP) throws SDEBase {
		return addPage(-getPressAmount(), indexP, false);
	}
	public synchronized final void saveKPgS(IndexP indexP, KPgS kpgs) throws SDEBase {
		_cacheMap.putEle(indexP, kpgs);
		KPg kpg_0 = kpgs.get(0), kpg_1 = kpgs.get(1);
		if (!_kpgsf.update(kpg_0)) {
			throw new SDEError("_kpgsf.update(kpg_0) " + kpg_0._keyName);
		}
		if (!_kpgsf.update(kpg_1)) {
			throw new SDEError("_kpgsf.update(kpg_1) " + kpg_1._keyName);
		}
	}
	public synchronized final void clearKPgS(IndexP indexP, KPgS kpgs) throws SDEBase {
		_cacheMap.removeEle(indexP);
	}
	public synchronized final void resetKPgS(IndexP indexP, KPgS kpgS) throws SDEBase { 
		final int size = kpgS.size();
		KPg[] kpgs = new KPg[size];
		for (int ith=0; ith<size; ith++) {
			kpgs[ith] = new KPg(kpgS.get(ith), null);
		}
		saveKPgS(indexP, new KPgS(kpgs));
	}

	public static Setting initailizeSetting() {
		final String path = "IniSetting_ver01.json";
		String content = null;
		try {
			content = FileM.readString(path);
		} catch (IOException e) {
			println(e.toString()); return null;
		}
		JSONObject json = null;
		SettingAd settingAd = new SettingAd(".", ".");
		try {
			json = new JSONObject(content);
			settingAd.parseJson(json);
		} catch (JSONException e) {
			println(e.toString()); return null;
		}
		return settingAd;
	}
	public static void main(String[] arg) {
		
		java.util.Locale.setDefault(java.util.Locale.TAIWAN);
		Setting setting = initailizeSetting();
		if (setting == null) {
			System.out.println("fail to initailizeSetting");
			return;
		}
		com.cloud.manga.henryTing.consts.Consts.initialize(setting);
		com.cloud.manga.henryTing.data.Data.initialize(setting);
		com.cloud.manga.henryTing.thread.Thread.initialize(setting);
		com.cloud.manga.henryTing.holder.Holder.initialize(setting);
		new TestThreadImpPre().run();
		ThreadImp.ptr().shutdown();
		ThreadLocal.shutdown();
	}
}