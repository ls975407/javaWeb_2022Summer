package com.cloud.manga.henryTing.main;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.infor.FrameM;
import com.cloud.manga.henryTing.consts.ThreadWork;

/*
javac -encoding utf8  ./com/cloud/manga/henryTing/main/settingWork/SettingAd.java
*/

public abstract class SettingAd implements com.cloud.manga.henryTing.main.Setting 
 ,com.cloud.manga.henryTing.data.Setting
 ,com.cloud.manga.henryTing.consts.Setting
 ,com.cloud.manga.henryTing.infor.Setting
 ,com.cloud.manga.henryTing.holder.Setting
 ,com.cloud.manga.henryTing.thread.Setting
{
	// data.PathGet
	public abstract String getPathLPicture();
	public abstract String getPathCPicture();
	public abstract String getPathLRecord();
	public abstract String getPathCRecord();
	public abstract String getPathLIdJson();
	public abstract String getPathCIdJson();
	// data.InforCh
	public abstract int mark2Order(String mark);
	public abstract String markToName(String mark);
	// data.DateT
	public abstract long getDiffMaxDay();
	// consts.IdTokenS
	public abstract Long getNode();
	public abstract String getPathTokenS();
	// CloudMBase
	public abstract String getCloudToken();
	// CloudM
	public abstract String getCFolderEndsWith();
	public abstract String getCFileEndsWith();
	// consts.ThreadWork
	public abstract String[] getLegalImageExtension();
	// holder.holderId
	public abstract int mapIdIndexPKPgS();
	// holder.holderCh
	public abstract int mapKIdTHolderCh();
	public abstract int mapChIndexPKPgS();
	// holder.holderPg
	public abstract int mapKIdTHolderPg();
	public abstract int mapPgIndexPKPgS();
	public abstract int mapPgIndexChKPgSF();
	public abstract int mapPgIndexChInteger();
	// holder.holderBase
	public abstract int getPressAmount();
	// infor.FrameM
	public abstract String getDFKeyName();
	// public abstract String[] getLegalImageExtension();
	public abstract String getPathLFImage();
	// infor.LabelBase
	public abstract String getFontTypes(int index);
	public abstract int getFontSizes(int index);
	public abstract String getErrorFontType();
	public abstract int getErrorFontSize();
	// infor.LabelM
	public abstract int getErrorLabelIndex();
	// infor.FrameInfor
	public abstract double getFontScaleValue();
	public abstract double getGeoScaleValue();
	public abstract int getLabelMarge();
	public abstract int getScreenX();
	public abstract int getScreenY();
	// thread.ThreadImp
	public abstract int getRangeNextPg();
	public abstract int getRangePrevPg();
	public abstract int getRangeNextCh();
	public abstract int getRangePrevCh();
	public abstract int getNumThread();
	// thread.TouchDelayer
	public abstract long getCmdTimeMax();
	// thread.TouchUnit
	public abstract long getCmdTimeMin();
	// main.KAddreddM
	public abstract int getRandomIdNum();
	public abstract String getTxtHistory();
	public abstract Long getNodeCPicture();
	public abstract Long getNodeCJson();
	public abstract String getPathSynCId();
	public abstract String getPathSynLId();
	// public abstract String getPathLPicture();
	public abstract String getPathSynJson();
	public abstract String[] getLegalJsonExtension();
	public abstract String[] getFileManagerList();
	public abstract String getLocalDefault();
	// main.Manga
	public abstract FrameM findFrameObj(String keyString);
	public abstract FrameM getDefaultFrame();
	public abstract FrameM getDefaultIdFrame();
	public abstract FrameM getDefaultChFrame();
	public abstract FrameM getDefaultPgFrame();
	
	private final String INI_SETTING_FILE = "MangaResouceIni.json";
	private final String INI_SETTING_FILE_PATH;
	private final String INI_SETTING_DIR;
	private final String _dirWork;
	private String _dirUser = "";
	private String _dirCloudStorage = "";
	private String _dirLocalStorage = "";

	public SettingAd(String dirSystem, String dirWork) { //, String filename) {
		// INI_SETTING_FILE = filename;
		INI_SETTING_DIR = dirSystem;
		INI_SETTING_FILE_PATH = INI_SETTING_DIR + "/" + INI_SETTING_FILE;
		_dirWork = dirWork;
	}
	public void parseJson() throws SDEBase {
		parseJson(ThreadWork.readJson(INI_SETTING_FILE_PATH));
	}
	public void parseJson(String externalFile) throws SDEBase {
		JSONObject json = ThreadWork.readJson(externalFile);
		try {
			parseJson(json);
		} catch(JSONException e) {
			throw SDEError.cSettingAdParseJson(e.toString()); 
		}		
		ThreadWork.write(INI_SETTING_FILE_PATH, json.toString());
	}
	public void setPathUser(String localPath){
		_dirUser = localPath;
		updateIniFile("DirUser", _dirUser);
	}
	public void setCloudStorage(String localPath){
		_dirCloudStorage = localPath;
		updateIniFile("DirCloudStorage", _dirCloudStorage);
	}
	public void setLocalStorage(String localPath){
		_dirLocalStorage = localPath;
		updateIniFile("DirLocalStorage", _dirLocalStorage);
	}
	private void updateIniFile(String key, String value) {
		JSONObject json = ThreadWork.readJson(INI_SETTING_FILE_PATH);
		json.put(key, value);
		ThreadWork.write(INI_SETTING_FILE_PATH, json.toString());
	}
	
	static private String[] _iniAS2S(JSONArray jsonA) throws JSONException {
		final int size = jsonA.length();
		String[] buf = new String[size];
		for (int ith=0; ith<size; ith++) {
			buf[ith] = jsonA.getString(ith);
		}
		return buf;
	}
	static private Map<String, String> _iniTS2S(JSONArray jsonA) throws JSONException {
		Map<String, String> reMap = new HashMap<>();
		final int size = jsonA.length();
		JSONArray t_jsonA;
		for (int ith=0; ith<size; ith++) {
			t_jsonA = jsonA.getJSONArray(ith);
			reMap.put(
				t_jsonA.getString(0),
				t_jsonA.getString(1)
			);
		}
		return reMap;
	}
	static private Map<String, Integer> _iniTS2I(JSONArray jsonA) throws JSONException {
		Map<String, Integer> reMap = new HashMap<>();
		final int size = jsonA.length();
		JSONArray t_jsonA;
		for (int ith=0; ith<size; ith++) {
			t_jsonA = jsonA.getJSONArray(ith);
			reMap.put(
				t_jsonA.getString(0),
				Integer.valueOf(t_jsonA.getInt(1))
			);
		}
		return reMap;
	}
	static private Map<String, FrameM> _iniTS2F(JSONArray jsonA) throws JSONException {
		Map<String, FrameM> reMap = new HashMap<>();
		final int size = jsonA.length();
		FrameM frameM;
		for (int ith=0; ith<size; ith++) {
			frameM = new FrameM(jsonA.getJSONObject(ith));
			reMap.put( frameM._keyName, frameM );
		}
		return reMap;
	}
	static private Map<Integer, Integer> _iniTI2I(JSONArray jsonA) throws JSONException {
		Map<Integer, Integer> reMap = new HashMap<>();
		final int size = jsonA.length();
		JSONArray t_jsonA;
		for (int ith=0; ith<size; ith++) {
			t_jsonA = jsonA.getJSONArray(ith);
			reMap.put(
				Integer.valueOf(t_jsonA.getInt(0)),
				Integer.valueOf(t_jsonA.getInt(1))
			);
		}
		return reMap;
	}
	static private Map<Integer, String> _iniTI2S(JSONArray jsonA) throws JSONException {
		Map<Integer, String> reMap = new HashMap<>();
		final int size = jsonA.length();
		JSONArray t_jsonA;
		for (int ith=0; ith<size; ith++) {
			t_jsonA = jsonA.getJSONArray(ith);
			reMap.put(
				Integer.valueOf(t_jsonA.getInt(0)),
				t_jsonA.getString(1)
			);
		}
		return reMap;
	}
	public void parseJson(JSONObject json) throws JSONException {
		try {
			_dirUser = json.getString("DirUser");
		} catch(JSONException e) {
			_dirUser = _dirWork + "/amanga/user";
		}
		try {
			_dirCloudStorage = json.getString("DirCloudStorage");
		} catch(JSONException e) {
			_dirCloudStorage = _dirWork + "/amanga/cloudstorage";
		}
		try {
			_dirLocalStorage = json.getString("DirLocalStorage");
		} catch(JSONException e) {
			_dirLocalStorage = _dirWork + "/amanga/localstorage";
		}
	}
}