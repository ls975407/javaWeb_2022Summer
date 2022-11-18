package com.cloud.manga.henryTing.consts;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/consts/ThreadWork.java
*/
import com.cloud.manga.henryTing.unit.ResultEn;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.SDEHandle;
import com.cloud.manga.henryTing.unit.IndexCh;
import com.cloud.manga.henryTing.unit.Log;

import com.cloud.manga.henryTing.data.*;

import com.cloud.manga.henryTing.tool.ThreadLocal;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.tool.ArrayCaches;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreadWork {
	public static Setting _setting;
	public final static Log _log = new Log("ThreadWork");
	
	public static String[] listDirectory(String localPath) throws SDEBase {
		_log.r("listDirectory localPath", localPath);
		_log.r_lock();
		ResultEn<String[]> result = ThreadLocal.listDirectory(localPath);
		_log.r_release();
		if (result.isNotSuccess()) {
			_log.e(result._exception.toString());
			throw result._exception;
		}
		return result._result;
	}
	public static String[] listFile(String localPath) throws SDEBase {
		_log.r("listFile localPath", localPath);
		_log.r_lock();
		ResultEn<String[]> result = ThreadLocal.listFile(localPath);
		_log.r_release();
		if (result.isNotSuccess()) {
			_log.e(result._exception.toString());
			throw result._exception;
		}
		return result._result;
	}
	
	public static boolean isExist(String localPath) throws SDEBase {
		_log.r("isExist localPath", localPath);
		_log.r_lock();
		ResultEn<Boolean> result = ThreadLocal.isExist(localPath);
		_log.r_release();
		if (result.isNotSuccess()) {
			_log.e(result._exception.toString());
			throw result._exception;
		}
		return result._result;
	}
	
	public static byte[] readBinary(String localPath) throws SDEBase {
		_log.r("readBinary localPath", localPath);
		_log.r_lock();
		ResultEn<byte[]> result = ThreadLocal.read(localPath);
		_log.r_release();
		if (result.isNotSuccess()) {
			_log.e(result._exception.toString());
			throw result._exception;
		}
		return result._result;
	}
	
	public static String readString(String localPath) throws SDEBase {
		return FileM.bytes2String(readBinary(localPath));
	}
	
	public static JSONObject readJson(String localPath) throws SDEBase {
		String content = readString(localPath);
		try {
			return new JSONObject(content);
		} catch (JSONException e) {
			_log.e(e.toString());
			throw SDEError.cThreadWorkReadJson(localPath);
		}
	}
	public static JSONArray readJsonArray(String localPath) throws SDEBase {
		String content = readString(localPath);
		try {
			return new JSONArray(content);
		} catch (JSONException e) {
			_log.e(e.toString());
			throw SDEError.cThreadWorkReadJson(localPath);
		}
	}
	public static void write(String localPath, byte[] bytes) throws SDEBase {
		_log.r("write", "localPath", localPath);
		_log.r_lock();
		SDEBase exception = ThreadLocal.write(localPath, bytes);
		_log.r_release();
		if (exception != null) {
			_log.e(exception.toString());
			throw exception;
		}
	}
	public static void write(String localPath, String content) throws SDEBase {
		write(localPath, FileM.string2Bytes(content));
	}
	public static void write(String localPath, KeyString keyString) throws SDEBase {
		JSONObject json = new JSONObject();
		try {
			keyString.addJson(json);
		} catch (JSONException e) {
			SDEBase exception = SDEError.cThreadWorkWriteAddJson(keyString.toString() + " " + e.toString());
			_log.e(exception.toString());
			throw exception;
		}
		write(localPath, json.toString());
	}
	
	// --------------------------------------------------------------------------------------------- //
	
	public static KCh getDefaultKCh(PathGet pathGet, KId kid) {
		return getDefaultKCh(pathGet, kid, null);
	}
	public static KCh getDefaultKCh(PathGet pathGet, KId kid, String mark) {
		_log.p("getDefaultKCh");
		DateCh dataCh = null;
		_log.e_lock();
		try {
			if (mark == null) {
				dataCh = DateCh.parseJson(ThreadWork.readJson(pathGet.RecordH(kid)));
			} else {
				dataCh = DateCh.parseJson(ThreadWork.readJson(pathGet.RecordH(kid, mark)));
			}
		} catch(JSONException | SDEBase e) {
			_log.w("readJson(pathGet.RecordH(..)", e.toString());
		}
		_log.e_release();
		if (dataCh != null && dataCh.isOnDay()) {
			return dataCh._kch;
		}
		_log.e_lock();
		try {
			if (mark == null) {
				return KCh.parseJson(ThreadWork.readJson(pathGet.RecordC(kid)));
			} else {
				return KCh.parseJson(ThreadWork.readJson(pathGet.RecordC(kid, mark)));
			}
		} catch (JSONException | SDEError e) {
			_log.w("readJson(pathGet.RecordC(..)", e.toString());
			// throw SDEError.cThreadWorkGetDefaultKCh(pathGet.RecordC(kid));
			return null;
		} finally {
			_log.e_release();
		}
	}
	
	public static KPgS getCasheKPgS(PathGet pathGet, KId kid) {
		KCh kch = getDefaultKCh(pathGet, kid);
		if (kch == null) { return null; }
		return getCasheKPgS(pathGet, kch);
	}
	public static KPgS getCasheKPgS(PathGet pathGet, KId kid, String mark) {
		KCh kch = getDefaultKCh(pathGet, kid, mark);
		if (kch == null) { return null; }
		return getCasheKPgS(pathGet, kch);
	}
	public static KPgS getCasheKPgS(PathGet pathGet, KCh kch) {
		_log.p("getCasheKPgS", kch.toString());
		KId kid = kch._kid;
		DatePgS dataPgS = null;
		_log.e_lock();
		try {
			dataPgS = DatePgS.parseJsonFrom(
				kid, 
				ThreadWork.readJson(pathGet.RecordH(kch)), 
				pathGet._isLocal
			);
		} catch(JSONException | SDEBase e) {
			_log.w("readJson(pathGet.RecordH(kch))", e.toString());
		}
		_log.e_release();
		if (dataPgS != null && dataPgS.isOnDay()) {
			return dataPgS._kpgs;
		}
		_log.e_lock();
		try {
			return KPgS.parseJsonFrom(
				kid, 
				ThreadWork.readJson(pathGet.RecordC(kch)),
				pathGet._isLocal
			);
		} catch(JSONException | SDEBase  e) {
			_log.w("readJson(pathGet.RecordC(kch))", e.toString());
			return null;
		} finally {
			_log.e_release();
		}	
	}

	public static KPgSF getKPgSF(PathGet pathGet, KChT kcht) throws SDEBase {
		_log.p("getKPgSF", kcht.toString());
		String[] names = ThreadWork.listFile(pathGet.Image(kcht));
		TokenUS tExist = new TokenUS();
		String[] exts = _setting.getLegalImageExtension();
		if (names.length != 0) {
			tExist = new TokenUS(names, false, pathGet._isLocal).filterExtension(exts);
		}
		if (pathGet._isLocal) {
			if (tExist.size() < 1) {
				SDEBase e = SDEError.cThreadWorkGetKPgSFTExistNull(pathGet.Image(kcht));
				_log.e(e.toString());
				throw e;
			}
			return new KPgSF(kcht, tExist, true);
		} else {
			TokenUS record;
			_log.e_lock();
			try {
				record = TokenUS.parseJson(
					ThreadWork.readJson(pathGet.RecordT(kcht)),
					pathGet._isLocal
				);
			} catch (JSONException | SDEBase e) {
				_log.w("readJson(pathGet.RecordT(kcht))", e.toString());
				record = new TokenUS();
			}
			_log.e_release();
			if (tExist.equals(record) && tExist.size() != 0) {
				return new KPgSF(kcht, tExist, true);
			}
			TokenUS tCloud = CloudM.ptr().getTokenUSFile(kcht._key).filterExtension(exts);
			
			if (!record.equals(tCloud)) {
				ThreadWork.write( pathGet.RecordT(kcht), tCloud );
			}
			if (tExist.size() < 1) {
				return new KPgSF(kcht, tCloud, false);
			} else {
				return new KPgSF(kcht, tCloud, tExist);
			}
		}
	}
	
	// --------------------------------------------------------------------------------------------- //
	
	public static void saveMarkHistory(PathGet pathGet, KCh kch) throws SDEBase {
		_log.p("saveMarkHistory", kch.toString());
		DateCh dataCh = new DateCh(kch);
		ThreadWork.write( pathGet.RecordH(kch._kid, new InforCh(kch)._mark), dataCh);
		ThreadWork.write( pathGet.RecordH(kch._kid), dataCh);
	}
	public static void saveMarkCover(PathGet pathGet, KCh kch) throws SDEBase {
		_log.p("saveMarkCover", kch.toString());
		ThreadWork.write( pathGet.RecordC(kch._kid, new InforCh(kch)._mark), kch);
	}
	public static void saveHistory(PathGet pathGet, KPgS kpgs) throws SDEBase {
		KCh kch = kpgs.toKCh();
		ThreadWork.write( pathGet.RecordH(kch), new DatePgS(kpgs) );
	}
	public static void saveCover(PathGet pathGet, KCh kch) throws SDEBase {
		_log.p("saveCover kch", kch.toString());
		ThreadWork.write( pathGet.RecordC(kch._kid), kch );
	}
	public static void saveCover(PathGet pathGet, KPgS kpgs) throws SDEBase {
		_log.p("saveCover kpgs", kpgs.toKPg().toString());
		ThreadWork.write( pathGet.RecordC(kpgs.toKCh()), kpgs );
	}
	
	// --------------------------------------------------------------------------------------------- //
	
	public static void writeKCh2D(KIdT kidt, KCh2D kch2d) throws SDEBase {
		_log.p("writeKCh2D", kch2d._kid.toString());
		try {
			ThreadWork.write( PathGet.get(kidt).RecordT(kidt), kch2d.toJsonString() );
		} catch (JSONException e) {
			throw SDEError.cThreadWorkWriteAddJson("kch2d " + kch2d._kid.toString() + " " + e.toString());
		}
	}
		
	public static KCh2D readKCh2D(KIdT kidt) throws SDEBase {
		_log.p("readKCh2D", kidt.toString());
		PathGet pathGet = PathGet.get(kidt);
		String content = ThreadWork.readString(pathGet.RecordT(kidt));
		try {
			return KCh2D.create(kidt, content);
		} catch (JSONException e) {
			SDEBase e1 = SDEError.cThreadWorkReadKCh2DFile(pathGet.RecordT(kidt));
			_log.e(e1.toString());
			throw e1;
		}
	}
	
	public static KCh2D getKCh2D(KIdT kidt) throws SDEBase {
		_log.p("getKCh2D", kidt.toString());
		KCh2D kch2d;
		if (kidt.isLocal()) {
			String localPath = PathGet.get(kidt).Image(kidt);
			String[] names = ThreadWork.listDirectory(localPath);
			if (names.length < 1) {
				SDEBase e = SDEError.cThreadWorkReadJson(localPath + "(kidt.isLocal() names.length < 1)");
				_log.e(e.toString());
				throw e;
			}
			kch2d = KCh2D.create(kidt, names);
		} else {
			Token token = kidt.toToken();
			TokenS tokenS = CloudM.ptr().getTokenSFolder(token);
			try {
				kch2d = KCh2D.create(kidt, tokenS);
			} catch (JSONException e1) {
				SDEBase e = SDEError.cThreadWorkReadJson(e1.toString());
				_log.e(e.toString());
				throw e;
			}
		}
		return kch2d;
	}
	
	public static <T extends KeyStringBase> List<Boolean> isExistFolder(String path, Iterable<T> keys) throws SDEBase {
		String[] names = ThreadWork.listDirectory(path);
		Arrays.sort(names);
		
		List<Boolean> list = new ArrayList<>();
		for (T key: keys) {
			if (Arrays.binarySearch(names, key._keyName) >= 0) {
				list.add(Boolean.valueOf(true));
			} else {
				list.add(Boolean.valueOf(false));
			}
		}
		return list;
	}
	
	public static <T extends KeyStringBase> List<Boolean> isExistFile(String path, Iterable<T> keys, String ext) throws SDEBase {
		String[] names = ThreadWork.listFile(path);
		Arrays.sort(names);
		
		List<Boolean> list = new ArrayList<>();
		for (T key: keys) {
			if (Arrays.binarySearch(names, key._keyName + ext) >= 0) {
				list.add(Boolean.valueOf(true));
			} else {
				list.add(Boolean.valueOf(false));
			}
		}
		return list;
	}
	
	public static <T> List<T> filterEle(List<T> work, List<Boolean> states, boolean filterFalse) {
		List<T> buf = new ArrayList<T>();
		if (filterFalse) {
			for (int ith=0; ith<states.size(); ith++) {
				if (states.get(ith)) { buf.add(work.get(ith)); }
			}
		} else {
			for (int ith=0; ith<states.size(); ith++) {
				if (!states.get(ith)) { buf.add(work.get(ith)); }
			}
		}
		return buf;
	}
	public static KIdTS filterExistIdFolder(KIdTS kidts) throws SDEBase {
		_log.p("filterExistIdFolder");
		List<KIdT> kidt_local = new ArrayList<KIdT>();
		List<KIdT> kidt_cloud = new ArrayList<KIdT>();
		for (KIdT kidt: kidts) {
			if (kidt.isLocal()) { kidt_local.add(kidt); }
			else if (kidt.isCloud()) { kidt_cloud.add(kidt); }
			else {
				SDEBase e = SDEError.cThreadWorkIdFolderLongNull(kidt._keyName);
				_log.e(e.toString());
				throw e;
			}			
		}
		if (kidt_local.size() != 0) {
			kidt_local = filterEle(
				kidt_local,
				isExistFolder(PathGet.get(kidt_local.get(0)).Record(), kidt_local),
				false
			);
		}
		if (kidt_cloud.size() != 0) {
			kidt_local.addAll(filterEle(
				kidt_cloud,
				isExistFolder(PathGet.get(kidt_cloud.get(0)).Record(), kidt_cloud),
				false
			));
		}
		return new KIdTS(kidt_local.toArray(new KIdT[0]));
	}
	
	private static void iniChFolder(PathGet pathGet, KCh2D kch2d) throws SDEBase {
		_log.t("iniChFolder");
		KChT kcht;
		for (TokenS tokenS: kch2d) {
			kcht = new KChT(kch2d._kid, tokenS.get(0));
			ThreadWork.saveMarkCover(pathGet, kcht);
			// ThreadWork.saveMarkHistory(pathGet, kcht);
		}
	}
		
	public static void iniIdFolder(KIdT kidt) throws SDEBase {
		_log.p("iniChFolder");
		KCh2D kch2d = getKCh2D(kidt);
		iniChFolder(PathGet.get(kidt), kch2d);
		KChT kcht = new KChT(kidt,  kch2d.get(0).get(0));
		ThreadWork.saveCover(PathGet.get(kidt), kcht);
		ThreadWork.writeKCh2D( kidt, kch2d );
	}
	
	public static void updateIdFolder(KIdT kidt) throws SDEBase {
		_log.p("iniChFolder");
		KCh2D kch2dNew, kch2dOld, kch2dNeed;
		try {
			kch2dNew = getKCh2D(kidt);
		} catch (SDEBase e) {
			if (kidt.isLocal()) { 
				_log.e(e.toString());
				throw e;
			}
			_log.w("getKCh2D(kidt)", e.toString());
			return;
		}
		kch2dOld = readKCh2D(kidt);
		kch2dNeed = KCh2D.filterExistChInfor(kch2dNew, kch2dOld);
		if (kch2dNeed == null) { return; }
		iniChFolder(PathGet.get(kidt), kch2dNeed);
		KChT kcht = new KChT(kidt,  kch2dNew.get(0).get(0));
		ThreadWork.saveCover(PathGet.get(kidt), kcht);
		ThreadWork.writeKCh2D( kidt, kch2dNew );
	}
	
	public static void updateOrCreateIdFolder(KIdT kidt) {
		String localPath = PathGet.get(kidt).RecordT(kidt);
		if (ThreadWork.isExist(localPath)) {
			ThreadWork.updateIdFolder(kidt);
		} else {
			ThreadWork.iniIdFolder(kidt);
		}
	}
	
	// --------------------------------------------------------------------------------------
	
	public static boolean isLocked() {
		return ThreadLocal.isLocked();
	}
	
	public static FileB downloadFile(String localPath, GetUrl getUrl) throws SDEBase {
		_log.p("downloadFile");
		FileB fileB = readCloudFile(getUrl);
		ThreadWork.write(localPath, fileB._bytes);
		return fileB;
	}
	private static FileB readCloudFile(GetUrl getUrl) throws SDEBase {
		_log.p("readCloudFile");
		try {
			java.net.URL url = getUrl.getUrl();
			if (url == null) {
				SDEBase e = SDEError.cThreadWorkUrlIsNull("readCloudFile");
				_log.e(e.toString());
				throw e;
			}
			return new FileB(FileM.read(url)); 
		} catch (java.io.IOException e1) {
			SDEBase e = SDEHandle.cThreadWorkDownloadFile("");
			_log.e(e.toString());
			throw e;
		}
	}
	public static FileB readCloudFile(KPg kpg) throws SDEBase {
		try {
			return readCloudFile(kpg.getUrl());
		} catch (SDEBase e) {
			throw SDEHandle.cThreadWorkDownloadFile(kpg.toString());
		}
	}
	public static FileB downloadFile(String localPath, KPg kpg) throws SDEBase {
		return downloadFile(localPath, kpg.getUrl());
	}
	public static FileB downloadFile(PathGet pathGet, KPg kpg) throws SDEBase {
		return downloadFile(pathGet.Image(kpg), kpg);
	}
	
	public static FileBS getImage(String localPath) throws SDEBase {
		return getImage(new String[]{localPath});
	}
	public static FileBS getImage(String[] localPaths) throws SDEBase {
		_log.p("getImage String[] localPaths");
		final int size = localPaths.length;
		FileB[] files = new FileB[size];
		for (int ith=0; ith<size; ith++) {
			files[ith] = new FileB(ThreadWork.readBinary(localPaths[ith]));
		}
		return new FileBS(files);
	}
	

	private final static ArrayCaches<String, FileB> _byteCaches = new ArrayCaches<>(2);
	public static FileBS getImage(KPgS kpgs, int num) throws SDEBase {
		_log.p("getImage KPgS kpgs, int num");
		assert num <= kpgs.size(): 
			String.format(
			"ThreadWork getImage num <= kpgs.size() num=%d kpgs.size()=%d"
			, num, kpgs.size()
		);
		String localPath;
		PathGet pathGet = null;
		KPg kpg; FileB fileB;
		FileB[] table = new FileB[num];
		for (int ith=0; ith<num; ith++) {
			kpg = kpgs.get(ith);
			pathGet = PathGet.get(kpg);
			localPath = pathGet.Image(kpg);
			fileB = _byteCaches.get(localPath);
			if (fileB != null) {
				// _log.p_release();
				_log.p("getImage", "_byteCaches");
				// _log.p_lock();
				table[ith] = fileB;
				continue;
			}
			if (kpg._isExist == null) {
				kpg = new KPg(kpg, ThreadWork.isExist(localPath));
				kpgs.set(ith, kpg);
			}
			if (pathGet._isLocal) {
				if (kpg._isExist) {
					fileB = new FileB(ThreadWork.readBinary(localPath));
				} else {
					throw SDEError.cThreadWorkGetImageIsLocalNotExist(localPath);
				}
			} else {
				fileB = null;
				if (kpg._isExist) {
					try {
						fileB = new FileB(ThreadWork.readBinary(localPath));
					} catch (SDEBase e) {
						if (ThreadWork.isExist(localPath)) {
							throw e;
						}
					}
				} if (fileB == null) {
					if (ThreadWork.isExist(localPath)) {
						// debug test
						_log.w("donwload twice", localPath);
						fileB = new FileB(ThreadWork.readBinary(localPath));
					} else {
						fileB = downloadFile(localPath, kpg);
						kpgs.set(ith, new KPg(kpg, true));
					}
				}
			}
			_byteCaches.set(localPath, fileB);
			table[ith] = fileB;
		}
		return new FileBS(table);
	}
		
	public static void prepareImage(KPgS kpgs) throws SDEBase {
		_log.p("prepareImage");
		PathGet pathGet = null;
		for (int ith=0; ith<kpgs.size(); ith++) {
			KPg kpg = kpgs.get(ith);
			pathGet = PathGet.get(kpg);
			if (pathGet._isLocal) { continue; }
			if (kpg._isExist == null) {
				kpg = new KPg(kpg, ThreadWork.isExist(pathGet.Image(kpg)));
				kpgs.set(ith, kpg);
			}
			if (kpg._isExist) { continue; }
			try {
				ThreadWork.downloadFile(pathGet, kpg);
			} catch (SDEHandle e) {
				kpgs.set(ith, new KPg(kpg, null));
				throw e;
			}
			kpgs.set(ith, new KPg(kpg, true));
		}
	}
	
	
}