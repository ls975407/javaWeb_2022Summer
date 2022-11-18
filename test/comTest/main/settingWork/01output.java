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
	public String getPathLPicture() { return _pathLPicture; }
	public String getPathCPicture() { return _pathCPicture; }
	public String getPathLRecord() { return _pathLRecord; }
	public String getPathCRecord() { return _pathCRecord; }
	public String getPathLIdJson() { return _pathLIdJson; }
	public String getPathCIdJson() { return _pathCIdJson; }
	// data.InforCh
	public int mark2Order(String key) { return _Tmark2Order.get(key); }
	public String markToName(String key) { return _TmarkToName.get(key); }
	// data.DateT
	public long getDiffMaxDay() { return _diffMaxDay; }
	// consts.IdTokenS
	public Long getNode() { return _node; }
	public String getPathTokenS() { return _pathTokenS; }
	// CloudMBase
	public String getCloudToken() { return _cloudToken; }
	// CloudM
	public String getCFolderEndsWith() { return _cFolderEndsWith; }
	public String getCFileEndsWith() { return _cFileEndsWith; }
	// consts.ThreadWork
	public String[] getLegalImageExtension() { return _legalImageExtension; }
	// holder.holderId
	public int mapIdIndexPKPgS() { return _idIndexPKPgS; }
	// holder.holderCh
	public int mapKIdTHolderCh() { return _kIdTHolderCh; }
	public int mapChIndexPKPgS() { return _chIndexPKPgS; }
	// holder.holderPg
	public int mapKIdTHolderPg() { return _kIdTHolderPg; }
	public int mapPgIndexPKPgS() { return _pgIndexPKPgS; }
	public int mapPgIndexChKPgSF() { return _pgIndexChKPgSF; }
	public int mapPgIndexChInteger() { return _pgIndexChInteger; }
	// holder.holderBase
	public int getPressAmount() { return _pressAmount; }
	// infor.FrameM
	public String getDFKeyName() { return _dFKeyName; }
	// public abstract String[] getLegalImageExtension();
	public String getPathLFImage() { return _pathLFImage; }
	// infor.LabelBase
	public String getFontTypes(int key) { return _TgetFontTypes.get(key); }
	public int getFontSizes(int key) { return _TgetFontSizes.get(key); }
	public String getErrorFontType() { return _errorFontType; }
	public int getErrorFontSize() { return _errorFontSize; }
	// infor.LabelM
	public int getErrorLabelIndex() { return _errorLabelIndex; }
	// infor.FrameInfor
	public double getFontScaleValue() { return _fontScaleValue; }
	public double getGeoScaleValue() { return _geoScaleValue; }
	public int getLabelMarge() { return _labelMarge; }
	public int getScreenX() { return _screenX; }
	public int getScreenY() { return _screenY; }
	// thread.ThreadImp
	public int getRangeNextPg() { return _rangeNextPg; }
	public int getRangePrevPg() { return _rangePrevPg; }
	public int getRangeNextCh() { return _rangeNextCh; }
	public int getRangePrevCh() { return _rangePrevCh; }
	public int getNumThread() { return _numThread; }
	// thread.TouchDelayer
	public long getCmdTimeMax() { return _cmdTimeMax; }
	// thread.TouchUnit
	public long getCmdTimeMin() { return _cmdTimeMin; }
	// main.KAddreddM
	public int getRandomIdNum() { return _randomIdNum; }
	public String getTxtHistory() { return _txtHistory; }
	public Long getNodeCPicture() { return _nodeCPicture; }
	public Long getNodeCJson() { return _nodeCJson; }
	public String getPathSynCId() { return _pathSynCId; }
	public String getPathSynLId() { return _pathSynLId; }
	// public abstract String getPathLPicture();
	public String getPathSynJson() { return _pathSynJson; }
	public String[] getLegalJsonExtension() { return _legalJsonExtension; }
	public String[] getFileManagerList() { return _fileManagerList; }
	public String getLocalDefault() { return _localDefault; }
	// main.Manga
	public FrameM findFrameObj(String key) { return _TfindFrameObj.get(key); }
	public FrameM getDefaultFrame() { return findFrameObj(_defaultFrame); }
	public FrameM getDefaultIdFrame() { return findFrameObj(_defaultIdFrame); }
	public FrameM getDefaultChFrame() { return findFrameObj(_defaultChFrame); }
	public FrameM getDefaultPgFrame() { return findFrameObj(_defaultPgFrame); }
	
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