package com.cloud.manga.henryTing.main;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.infor.FrameM;
import com.cloud.manga.henryTing.consts.ThreadWork;
import com.cloud.manga.henryTing.tool.PassWordTranfer;

/*
javac -encoding utf8  ./com/cloud/manga/henryTing/main/SettingAd.java
*/

public class SettingAd implements com.cloud.manga.henryTing.main.Setting 
 ,com.cloud.manga.henryTing.data.Setting
 ,com.cloud.manga.henryTing.consts.Setting
 ,com.cloud.manga.henryTing.infor.Setting
 ,com.cloud.manga.henryTing.holder.Setting
 ,com.cloud.manga.henryTing.thread.Setting
{
	// data.PathGet
	public String getPathLPicture() { return _dirLocalStorage; }
	public String getPathCPicture() { return _dirCloudStorage; }
	public String getPathLRecord() { return _dirUser + "/" + _pathLRecord; }
	public String getPathCRecord() { return _dirUser + "/" + _pathCRecord; }
	public String getPathLIdJson() { return _dirUser + "/" + _pathLIdJson; }
	public String getPathCIdJson() { return _dirUser + "/" + _pathCIdJson; }
	// data.InforCh
	public Integer mark2Order(String key) {
		// System.out.println("key = " + key);
		// if (_Tmark2Order == null) {
			// System.out.println("_Tmark2Order == null");
		// }
		// if(!containsKey(key)) { return null; }
		return _Tmark2Order.get(key);
	}
	public String markToName(String key) { return _TmarkToName.get(key); }
	// data.DateT
	public long getDiffMaxDay() { return _diffMaxDay; }
	// consts.IdTokenS
	public Long getNode() { return _nodeCPicture; }
	public String getPathTokenS() { return _dirUser + "/" + _pathTokenS; }
	// CloudM
	public String getCloudToken() { return PassWordTranfer.decoding(_cloudToken); }
	// CloudM
	// public String getCFolderEndsWith() { return _cFolderEndsWith; }
	// public String getCFileEndsWith() { return _cFileEndsWith; }
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
	// SlideCmds
	public int getMinSlideAmount() { return _minSlideAmount; }
	// public abstract String[] getLegalImageExtension();
	public String getPathLFImage() { return _dirUser + "/" + _pathLFImage; }
	// infor.LabelBase
	public String getFontTypes(int key) { return _TgetFontTypes.get(key); }
	public int getFontSizes(int key) {
		// System.out.println(key);
		// System.out.println(_TgetFontSizes.toString());
		
		return _TgetFontSizes.get(key);
	}
	public String getErrorFontType() { return _errorFontType;}
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
	public String getTxtHistory() { return _dirUser + "/" + _txtHistory; }
	public Long getNodeCPicture() { return _nodeCPicture; }
	public Long getNodeCJson() { return _nodeCJson; }
	public String getPathSynCId() { return _dirUser + "/" + _pathSynCId; }
	public String getPathSynLId() { return _dirUser + "/" + _pathSynLId; }
	public String getPathSynCCh() { return _dirUser + "/" + _pathSynCCh; }
	public String getPathSynLCh() { return _dirUser + "/" + _pathSynLCh; }
	// public abstract String getPathLPicture();
	public String getPathSynJson() { return _dirUser + "/" + _pathSynJson; }
	public String[] getLegalJsonExtension() { return _legalJsonExtension; }
	public String[] getFileManagerList() { return _fileManagerList; }
	public String getLocalDefault() { return _dirUser; }
	// main.Manga
	public FrameM findFrameObj(String key) { return _TfindFrameObj.get(key); }
	public FrameM getDefaultFrame() { return findFrameObj(_defaultFrame); }
	public FrameM getDefaultIdFrame() { return findFrameObj(_defaultIdFrame); }
	public FrameM getDefaultChFrame() { return findFrameObj(_defaultChFrame); }
	public FrameM getDefaultPgFrame() { return findFrameObj(_defaultPgFrame); }
	public String getPIPagePNext() { return _pIPagePNext; }
	public String getPIPagePPrev() { return _pIPagePPrev; }
	public String getPIPageCNext() { return _pIPageCNext; }
	public String getPIPageCPrev() { return _pIPageCPrev; }
	public String getExtSettingPath() { return _dirUser + "/" + _extSettingPath + "/" + INI_SETTING_FILE; }
	public final static String INI_SETTING_FILE = "MangaResouceIni.json";
	private final String INI_SETTING_FILE_PATH;
	public final String _dirSystem;
	public final String _dirWork;
	private String _dirUser = "";
	private String _dirCloudStorage = "";
	private String _dirLocalStorage = "";

	public SettingAd(String dirSystem, String dirWork) { //, String filename) {
		// INI_SETTING_FILE = filename;
		_dirSystem = dirSystem;
		INI_SETTING_FILE_PATH = _dirSystem + "/" + INI_SETTING_FILE;
		_dirWork = dirWork;
	}
	public void parseJson() throws SDEBase {
		try {
			_parseJson(ThreadWork.readJson(INI_SETTING_FILE_PATH));
		} catch (JSONException e) {
			throw SDEError.cSettingAdParseJson(e.toString()); 
		}
	}
	public void parseJson(String externalFile) throws SDEBase {
		JSONObject json = ThreadWork.readJson(externalFile);
		parseJson(json);
	}
	public void parseJson(JSONObject json) throws SDEBase {
		try {
			_parseJson(json);
		} catch(JSONException e) {
			throw SDEError.cSettingAdParseJson(e.toString()); 
		}		
		ThreadWork.write(INI_SETTING_FILE_PATH, json.toString());
		ThreadWork.write(getExtSettingPath(), json.toString());
	}
	public void setPathUser(String localPath) throws SDEBase {
		_dirUser = localPath;
		updateIniFile("DirUser", _dirUser);
	}
	public void setCloudStorage(String localPath) throws SDEBase {
		_dirCloudStorage = localPath;
		updateIniFile("DirCloudStorage", _dirCloudStorage);
	}
	public void setLocalStorage(String localPath) throws SDEBase {
		_dirLocalStorage = localPath;
		updateIniFile("DirLocalStorage", _dirLocalStorage);
	}
	private void updateIniFile(String key, String value) throws SDEBase {
		try {
			JSONObject json = ThreadWork.readJson(INI_SETTING_FILE_PATH);
			json.put(key, value);
			ThreadWork.write(INI_SETTING_FILE_PATH, json.toString());
			ThreadWork.write(getExtSettingPath(), json.toString());
		} catch (JSONException e) {
			throw SDEError.cSettingAdParseJson(e.toString()); 
		}
	}
	
	static public String[] _iniAS2S(JSONArray jsonA) throws JSONException {
		final int size = jsonA.length();
		String[] buf = new String[size];
		int ith=0;
		try {
			for (; ith<size; ith++) {
				buf[ith] = jsonA.getString(ith);
			}
		} catch (JSONException e) {
			throw new JSONException(String.format("_iniAS2S ith=%d %s", ith, e.toString()));
		}
		return buf;
	}
	static public Map<String, String> _iniTS2S(JSONArray jsonA) throws JSONException {
		Map<String, String> reMap = new HashMap<>();
		final int size = jsonA.length();
		JSONArray t_jsonA;
		int ith=0;
		try {
			for (; ith<size; ith++) {
				t_jsonA = jsonA.getJSONArray(ith);
				reMap.put(
					t_jsonA.getString(0),
					t_jsonA.getString(1)
				);
			}
		} catch (JSONException e) {
			throw new JSONException(String.format("_iniTS2S ith=%d %s", ith, e.toString()));
		}
		return reMap;
	}
	static public Map<String, Integer> _iniTS2I(JSONArray jsonA) throws JSONException {
		Map<String, Integer> reMap = new HashMap<>();
		final int size = jsonA.length();
		JSONArray t_jsonA;
		int ith=0;
		try {
			for (; ith<size; ith++) {
				t_jsonA = jsonA.getJSONArray(ith);
				reMap.put(
					t_jsonA.getString(0),
					Integer.valueOf(t_jsonA.getInt(1))
				);
			}
		} catch (JSONException e) {
			throw new JSONException(String.format("_iniTS2I ith=%d %s", ith, e.toString()));
		}
		return reMap;
	}
	static public Map<String, FrameM> _iniTS2F(JSONArray jsonA, com.cloud.manga.henryTing.infor.Setting setting) throws JSONException {
		Map<String, FrameM> reMap = new HashMap<>();
		final int size = jsonA.length();
		FrameM frameM;
		int ith=0;
		try {
			for (; ith<size; ith++) {
				// System.out.println("abc 01");
				// JSONObject json = jsonA.getJSONObject(ith);
				// System.out.println("abc 02");
				frameM = new FrameM(jsonA.getJSONObject(ith), setting);
				reMap.put( frameM._keyName, frameM );
			}
		} catch (JSONException e) {
			throw new JSONException(String.format("_iniTS2F ith=%d %s", ith, e.toString()));
		}

		return reMap;
	}
	static public Map<Integer, Integer> _iniTAI2I(JSONArray jsonA) throws JSONException {
		Map<Integer, Integer> reMap = new HashMap<>();
		final int size = jsonA.length();
		int ith=0;
		try {
			for (; ith<size; ith++) {
				reMap.put(
					Integer.valueOf(ith),
					Integer.valueOf(jsonA.getInt(ith))
				);
			}
		} catch (JSONException e) {
			throw new JSONException(String.format("_iniTAI2I ith=%d %s", ith, e.toString()));
		}
		return reMap;
	}
	static public Map<Integer, String> _iniTAI2S(JSONArray jsonA) throws JSONException {
		Map<Integer, String> reMap = new HashMap<>();
		final int size = jsonA.length();
		int ith=0;
		try {
			for (; ith<size; ith++) {
				reMap.put(
					Integer.valueOf(ith),
					jsonA.getString(ith)
				);
			}
		} catch (JSONException e) {
			throw new JSONException(String.format("_iniTAI2S ith=%d %s", ith, e.toString()));
		}

		return reMap;
	}
	public void _parseJson(JSONObject json) throws JSONException {
		try {
			_dirUser = json.getString("DirUser");
		} catch(JSONException e) {
			_dirUser = _dirWork + "/aapmanga/user";
		}
		try {
			_dirCloudStorage = json.getString("DirCloudStorage");
		} catch(JSONException e) {
			_dirCloudStorage = _dirWork + "/aapmanga/cloudstorage";
		}
		try {
			_dirLocalStorage = json.getString("DirLocalStorage");
		} catch(JSONException e) {
			_dirLocalStorage = _dirWork + "/aapmanga/localstorage";
		}
		// _pathLPicture = json.getString("PathLPicture");
		// _pathCPicture = json.getString("PathCPicture");
		_pathLRecord = json.getString("PathLRecord");
		_pathCRecord = json.getString("PathCRecord");
		_pathLIdJson = json.getString("PathLIdJson");
		_pathCIdJson = json.getString("PathCIdJson");
		_Tmark2Order = _iniTS2I(json.getJSONArray("Mark2Order"));
		_TmarkToName = _iniTS2S(json.getJSONArray("MarkToName"));
		_diffMaxDay = json.getLong("DiffMaxDay");
		// _node = json.getLong("Node");
		_pathTokenS = json.getString("PathTokenS");
		_cloudToken = json.getString("CloudToken");
		// _cFolderEndsWith = json.getString("CFolderEndsWith");
		// _cFileEndsWith = json.getString("CFileEndsWith");
		_legalImageExtension = _iniAS2S(json.getJSONArray("LegalImageExtension"));
		_idIndexPKPgS = json.getInt("IdIndexPKPgS");
		_kIdTHolderCh = json.getInt("KIdTHolderCh");
		_chIndexPKPgS = json.getInt("ChIndexPKPgS");
		_kIdTHolderPg = json.getInt("KIdTHolderPg");
		_pgIndexPKPgS = json.getInt("PgIndexPKPgS");
		_pgIndexChKPgSF = json.getInt("PgIndexChKPgSF");
		_pgIndexChInteger = json.getInt("PgIndexChInteger");
		_pressAmount = json.getInt("PressAmount");
		_dFKeyName = json.getString("DFKeyName");
		_minSlideAmount = json.getInt("MinSlideAmount");
		_pathLFImage = json.getString("PathLFImage");
		try {
			_TgetFontTypes = _iniTAI2S(json.getJSONArray("GetFontTypes"));
		} catch (JSONException e) {
			throw new JSONException(String.format("GetFontTypes fail: %s", e.toString()));
		}
		try {
			_TgetFontSizes = _iniTAI2I(json.getJSONArray("GetFontSizes"));
		} catch (JSONException e) {
			throw new JSONException(String.format("GetFontSizes fail: %s", e.toString()));
		}
		_errorFontType = json.getString("ErrorFontType");
		_errorFontSize = json.getInt("ErrorFontSize");
		_errorLabelIndex = json.getInt("ErrorLabelIndex");
		_fontScaleValue = json.getDouble("FontScaleValue");
		_geoScaleValue = json.getDouble("GeoScaleValue");
		_labelMarge = json.getInt("LabelMarge");
		_screenX = json.getInt("ScreenX");
		_screenY = json.getInt("ScreenY");
		_rangeNextPg = json.getInt("RangeNextPg");
		_rangePrevPg = json.getInt("RangePrevPg");
		_rangeNextCh = json.getInt("RangeNextCh");
		_rangePrevCh = json.getInt("RangePrevCh");
		_numThread = json.getInt("NumThread");
		_cmdTimeMax = json.getLong("CmdTimeMax");
		_cmdTimeMin = json.getLong("CmdTimeMin");
		_randomIdNum = json.getInt("RandomIdNum");
		_txtHistory = json.getString("TxtHistory");
		_nodeCPicture = json.getLong("NodeCPicture");
		_nodeCJson = json.getLong("NodeCJson");
		_pathSynCId = json.getString("PathSynCId");
		_pathSynLId = json.getString("PathSynLId");
		_pathSynCCh = json.getString("PathSynCCh");
		_pathSynLCh = json.getString("PathSynLCh");
		_pathSynJson = json.getString("PathSynJson");
		try {
			Set<String> buf = new HashSet<>(Arrays.asList(_iniAS2S(json.getJSONArray("LegalJsonExtension"))));
			buf.add(".json");
			_legalJsonExtension = buf.toArray(new String[0]);
		} catch (JSONException e) {
			throw new JSONException(String.format("LegalJsonExtension fail: %s", e.toString()));
		}
		try {
			_fileManagerList = _iniAS2S(json.getJSONArray("FileManagerList"));
		} catch (JSONException e) {
			throw new JSONException(String.format("FileManagerList fail: %s", e.toString()));
		}
		// _localDefault = json.getString("LocalDefault");
		try {
			_TfindFrameObj = _iniTS2F(json.getJSONArray("FindFrameObj"), this);
		} catch (JSONException e) {
			throw new JSONException(String.format("FindFrameObj fail: %s", e.toString()));
		}
		_defaultFrame = json.getString("DefaultFrame");
		_defaultIdFrame = json.getString("DefaultIdFrame");
		_defaultChFrame = json.getString("DefaultChFrame");
		_defaultPgFrame = json.getString("DefaultPgFrame");
		_pIPagePNext = json.getString("PIPagePNext");
		_pIPagePPrev = json.getString("PIPagePPrev");
		_pIPageCNext = json.getString("PIPageCNext");
		_pIPageCPrev = json.getString("PIPageCPrev");
		_extSettingPath = json.getString("ExtSettingPath");
		
		
		boolean state = false;
		String[] exts = _legalJsonExtension;
		for (String ext: exts) {
			if (_txtHistory.endsWith(ext)) {
				state = true; break;
			}
		}
		if (!state) {
			_txtHistory = _txtHistory + ".json";
		}
	}
	// private String _pathLPicture = null;
	// private String _pathCPicture = null;
	private String _pathLRecord = null;
	private String _pathCRecord = null;
	private String _pathLIdJson = null;
	private String _pathCIdJson = null;
	private Map<String, Integer> _Tmark2Order = null;
	private Map<String, String> _TmarkToName = null;
	private long _diffMaxDay = 0;
	// private Long _node = null;
	private String _pathTokenS = null;
	private String _cloudToken = null;
	private String _cFolderEndsWith = null;
	private String _cFileEndsWith = null;
	private String[] _legalImageExtension = null;
	private int _idIndexPKPgS = 0;
	private int _kIdTHolderCh = 0;
	private int _chIndexPKPgS = 0;
	private int _kIdTHolderPg = 0;
	private int _pgIndexPKPgS = 0;
	private int _pgIndexChKPgSF = 0;
	private int _pgIndexChInteger = 0;
	private int _pressAmount = 0;
	private String _dFKeyName = null;
	private int _minSlideAmount = 0;
	private String _pathLFImage = null;
	private Map<Integer, String> _TgetFontTypes = null;
	private Map<Integer, Integer> _TgetFontSizes = null;
	private String _errorFontType = null;
	private int _errorFontSize = 0;
	private int _errorLabelIndex = 0;
	private double _fontScaleValue = 0;
	private double _geoScaleValue = 0;
	private int _labelMarge = 0;
	private int _screenX = 0;
	private int _screenY = 0;
	private int _rangeNextPg = 0;
	private int _rangePrevPg = 0;
	private int _rangeNextCh = 0;
	private int _rangePrevCh = 0;
	private int _numThread = 0;
	private long _cmdTimeMax = 0;
	private long _cmdTimeMin = 0;
	private int _randomIdNum = 0;
	private String _txtHistory = null;
	private Long _nodeCPicture = null;
	private Long _nodeCJson = null;
	private String _pathSynCId = null;
	private String _pathSynLId = null;
	private String _pathSynCCh = null;
	private String _pathSynLCh = null;
	private String _pathSynJson = null;
	private String[] _legalJsonExtension = null;
	private String[] _fileManagerList = null;
	// private String _localDefault = null;
	private Map<String, FrameM> _TfindFrameObj = null;
	private String _defaultFrame = null;
	private String _defaultIdFrame = null;
	private String _defaultChFrame = null;
	private String _defaultPgFrame = null;
	private String _pIPagePNext = "";
	private String _pIPagePPrev = "";
	private String _pIPageCNext = "";
	private String _pIPageCPrev = "";
	private String _extSettingPath = "";
}