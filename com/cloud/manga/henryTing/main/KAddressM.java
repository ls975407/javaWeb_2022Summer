package com.cloud.manga.henryTing.main;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/main/KAddressM.java
*/

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEHandle;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.Log;
import com.cloud.manga.henryTing.unit.HolderEnum;
import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.consts.ThreadWork;
import com.cloud.manga.henryTing.thread.ThreadImp;
import com.cloud.manga.henryTing.data.*;
import com.cloud.manga.henryTing.consts.CloudM;
import com.cloud.manga.henryTing.tool.RandomM;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.holder.HolderM;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;

public class KAddressM {
	public final static Log _log = new Log("KAddressM");
	public static Setting _setting = null;
	private final GUIInterface _guiInterface;
	private final String _dirWork;
	private KAddressM(GUIInterface guiInterface, String dirWork) {
		_guiInterface = guiInterface;
		_dirWork = dirWork;
	}
	private static KAddressM _ptr = null;
	public synchronized static KAddressM ptr(GUIInterface guiInterface, String dirWork) {
		if (_ptr == null) {
			_ptr = new KAddressM(guiInterface, dirWork);
		}
		if (_ptr._guiInterface != guiInterface || !_ptr._dirWork.equals(dirWork)) {
			_ptr = new KAddressM(guiInterface, dirWork);
		}
		return _ptr;
	}
	public KAddress readKAddressbyPath(String localPath) throws SDEBase {
		_log.p("readKAddressbyPath", "localPath", localPath);
		boolean state = false;
		for (String ext: _setting.getLegalJsonExtension()) {
			if (localPath.endsWith(ext)) {
				state = true; break;
			}
		}
		if (!state) {
			SDEBase e = SDEError.cKAddressMReadExtensionFail(localPath);
			_log.e("readKAddressbyPath localPath", localPath);
			throw e;
		}
		
		String content = ThreadWork.readString(localPath);
		KAddress ad = readKAddressbyContent(content);
		return ad;
	}
	public KAddress readKAddressbyContent(String content) throws SDEBase {
		_log.p("readKAddressbyContent", "content", content);
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (JSONException e1) {
			SDEBase e = SDEError.cKAddressMToJSONObject(content);
			_log.e("JSONException", e.toString());
			throw e;
		}
		return readKAddress(json);
	}
	public KAddress readKAddress(JSONObject json) throws SDEBase {
		try {
			return KAddress.parseJson(json);
		} catch (JSONException e1) {
			SDEBase e = SDEError.cKAddressMParseJson(json.toString());
			_log.e("JSONException" + e.toString() + e1.toString());
			throw e;
		}
	}
	public KAddress inputLocalRandom(TokenS tokenS) throws SDEBase {
		_log.t("inputLocalRandom", tokenS.toString());
		int num = _setting.getRandomIdNum();
		List<KIdT> buf = new ArrayList<>();
		final int size = tokenS.size();
		if (size == 0) {
			SDEBase e = SDEError.cKAddressMInputLocalRandom();
			_log.e("inputLocalRandom size == 0", e.toString());
			throw e;
		}
		if (size <= num) {
			for (Token token: tokenS) {
				buf.add(new KIdT(token));
			}
			Collections.shuffle(buf);
		} else {
			Integer[] indexs = RandomM.randomWitNoDuplicates(num, size);
			for (Integer index: indexs) {
				buf.add(new KIdT(tokenS.get(index)));
			}
		}
		_log.t("inputLocalRandom", buf.toString());
		return new KAddress(buf.toArray(new KIdT[0]));
	}
	
	// change HolderM
	// initailize

	public KAddress inputLocalHistory() throws SDEBase {
		try {
			return readKAddressbyPath(_setting.getTxtHistory());
		} catch (SDEBase e) {
			// maybe there is no history file or something wrong
			// we hable this by open the history dir folder
			_log.w("read history fail");
		}
		return openLocalDefault();
	}
	public KAddress inputLocalRandom() throws SDEBase {
		_log.p("inputLocalRandom");
		String localPath = _setting.getPathLPicture();
		String[] names = ThreadWork.listDirectory(localPath);
		if (names.length == 0) {
			throw SDEError.cKAddressMLocalNameNull(localPath);
		}
		return inputLocalRandom(new TokenS(names, false)); 
	}
	public KAddress inputCloudHistory() throws SDEBase {
		return inputLocalHistory();
	}
	public KAddress inputCloudRandom() throws SDEBase {
		_log.p("inputCloudRandom");
		return inputLocalRandom(CloudM.ptr().getTokenSFolder(_setting.getNodeCPicture())); 
	}
	public KAddress inputCloudFile(int index) throws SDEBase {
		_log.p("inputCloudFile", "index", Integer.valueOf(index).toString());
		TokenUS tokenUS = CloudM.ptr().getTokenUSFile(_setting.getNodeCJson()).filterExtension(_setting.getLegalJsonExtension());
		if (tokenUS.size() < index) {
			SDEBase e = SDEError.cKAddressMInputCloudFileIndex(tokenUS.size(), index);
			_log.e("tokenUS.size() < index", e.toString());
			throw e;
		}
		_log.t("inputCloudFile download file");
		TokenU tokenU = tokenUS.get(index);
		String content = null;
		try {
			content = ThreadWork.downloadFile(_setting.getPathSynJson() + "/" + tokenU._keyName, tokenU._getUrl).toTxt();
		} catch (IOException e1) {
			SDEBase e = SDEError.cKAddressMInputCloudFileFormat(tokenU._keyName);
			_log.e("IOException", e.toString());
			throw e;
		}
		return readKAddressbyContent(content);
	}
	
	public KAddress openAFile(String localPath) throws SDEBase {
		_log.p("openAFile", "localPath", localPath);
		localPath = _guiInterface.openAFile(localPath, _setting.getFileManagerList());
		if (localPath == null) {
			// SDEBase e = SDEError.cKAddressMOpenAFile();
			SDEBase e = SDEHandle.cKAddressMOpenAFile();
			_log.e("localPath == null", e.toString());
			throw e;
		}
		return readKAddressbyPath(localPath);
	}
	public KAddress openCloudFolder() throws SDEBase {
		_log.p("openCloudFolder");
		TokenUS tokenUS = CloudM.ptr().getTokenUSFile(_setting.getNodeCJson()).filterExtension(_setting.getLegalJsonExtension());
		if (tokenUS.size() == 0) {
			SDEBase e = SDEError.cKAddressMOpenCloudFolder();
			_log.e("tokenUS.size()", e.toString());
			throw e;
		}
		_log.t("openCloudFolder download file");
		String pathSynJson = _setting.getPathSynJson();
		for (TokenU tokenU: tokenUS) {
			ThreadWork.downloadFile(pathSynJson + "/" + tokenU._keyName, tokenU._getUrl);
		}
		return openAFile(pathSynJson);
	}
	public KAddress openLocalCurrent() throws SDEBase {
		return openAFile(_dirWork);
	}
	public KAddress openLocalDefault() throws SDEBase {
		return openAFile(_setting.getLocalDefault());
	}
	
	public KAddress openLocalId() throws SDEBase {
		_log.p("openLocalId");
		String localPath = _setting.getPathSynLId();
		String[] names  = ThreadWork.listDirectory(PathGet.getLocal().Image());
		if (names.length != 0) {
			ThreadImp.ptr().synKAddress(
				new TokenS(names, false)
				, localPath
			);
		} else {
			try {
				// FileM.mkdirs(localPath);
				FileM.mkdirForFolderByLoop(localPath);
			} catch (IOException e) {
				throw SDEError.cKAddressMOpenLocalId(localPath);
			}			
		}
		return openAFile(localPath);
	}
	public KAddress openCloudId() throws SDEBase {
		_log.p("openCloudId");
		String localPath = _setting.getPathSynCId();
		ThreadImp.ptr().synKAddress(CloudM.ptr().getTokenSFolder(_setting.getNodeCPicture()), localPath); 
		return openAFile(localPath);
	}
	public KAddress openLocalCh(KIdT kidt) throws SDEBase {
		if (kidt.isLocal()) {
		} else if (kidt.isCloud()) {
			return openCloudCh(kidt);
		} else {
			SDEBase e = SDEError.cKAddressMOpenChKIdTOutOfState(kidt._keyName);
			_log.e("openCloudCh", e.toString());
			throw e;
		}
		_log.p("openLocalCh", kidt.toString());
		String localPath = _setting.getPathSynLCh()+"/"+kidt._keyName;
		ThreadImp.ptr().synKAddress(
			new TokenS(
				ThreadWork.listDirectory(PathGet.get(kidt).Image(kidt)), false
			), 
			localPath, 
			kidt
		);
		return openAFile(localPath);
	}
	public KAddress openCloudCh(KIdT kidt) throws SDEBase {
		if (kidt.isLocal()) {
			return openLocalCh(kidt);
		} else if (kidt.isCloud()) {
		} else {
			SDEBase e = SDEError.cKAddressMOpenChKIdTOutOfState(kidt._keyName);
			_log.e("openCloudCh", e.toString());
			throw e;
		}
		_log.p("openCloudCh", kidt.toString());
		String localPath = _setting.getPathSynCCh()+"/"+kidt._keyName;
		ThreadImp.ptr().synKAddress(CloudM.ptr().getTokenSFolder(kidt._key), localPath, kidt); 
		return openAFile(localPath);
	}

	// public void saveHistory(HolderM holderM, IndexP indexP) throws SDEBase {
		// _log.p("saveHistory");
		// String localPath = _guiInterface.saveAFile(_setting. getLocalDefault(), _setting.getFileManagerList());
		// if (localPath == null) {
			// // SDEBase e = SDEError.cKAddressMOpenAFile();
			// SDEBase e = SDEHandle.cKAddressMOpenAFile();
			// _log.e("localPath == null", e.toString());
			// throw e;
		// }
		// saveHistory(localPath, holderM, indexP);
	// }

	// public static void saveHistory(String localPath, HolderM holderM, IndexP indexP) throws SDEBase {
		// HolderEnum holderType = holderM.getHolderType();
		// KAddress ka;
		// KPg kpg = holderM.getKPgS(indexP).toKPg();
		// KIdT[] kidts = holderM.getKIdTS().toArray();
		// switch(holderType) {
			// case Id:
			// ka = new KAddress(kidts, kpg._kch._kid); break;
			// case Ch:
			// ka = new KAddress(kidts, kpg._kch); break;
			// case Pg:
			// ka = new KAddress(kidts, kpg); break;
			// default:
			// throw new SDEError("holderM.getHolderType() == Ini");
		// }
		// ThreadWork.write(localPath, ka);
	// }
}