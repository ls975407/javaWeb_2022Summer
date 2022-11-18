import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import java.util.ArrayMap;
import java.util.Map;

/*
javac -encoding utf8  ./SettingAd.java
*/

public class SettingAd implements Setting 
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
	// data.InforCh
	public int mark2Order(String key) { return _Tmark2Order.find(key); }
	public String markToName(String key) { return _TmarkToName.find(key); }
	// data.DateT
	public long getDiffMaxDay() { return _diffMaxDay; }
	// consts.IdTokenS
	public Long getNode() { return _node; }
	public String getPathTokenS() { return _pathTokenS; }
	// consts.CloudM
	public String getCloudToken() { return _cloudToken; }
	// consts.ThreadWork
	public String[] getLegalImageExtension() { return _legalImageExtension; }
	// holder.holderId
	public int getIdIndexPKPgS() { return _idIndexPKPgS; }
	// holder.holderCh
	public int getKIdTHolderCh() { return _kIdTHolderCh; }
	public int getChIndexPKPgS() { return _chIndexPKPgS; }
	// holder.holderPg
	public int getKIdTHolderPg() { return _kIdTHolderPg; }
	public int getPgIndexPKPgS() { return _pgIndexPKPgS; }
	public int getPgIndexChKPgSF() { return _pgIndexChKPgSF; }
	public int getPgIndexChInteger() { return _pgIndexChInteger; }
	// holder.holderBase
	public int getPressAmount() { return _pressAmount; }
	// infor.FrameM
	public String getDFKeyName() { return _dFKeyName; }
	public String[] getLegalImageExtension() { return _legalImageExtension; }
	public String getPathLFImage() { return _pathLFImage; }
	// infor.LabelBase
	public String getFontTypes(String key) { return _TgetFontTypes.find(key); }
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
	public String getPathLPicture() { return _pathLPicture; }
	public String getPathSynJson() { return _pathSynJson; }
	public String[] getLegalJsonExtension() { return _legalJsonExtension; }
	public String[] getFileManagerList() { return _fileManagerList; }
	public String getPathSynCId() { return _pathSynCId; }
	public String getPathSynLId() { return _pathSynLId; }
	public String getLocalDefault() { return _localDefault; }
	// main.Manga
	public FrameM findFrameObj(String key) { return _TfindFrameObj.find(key); }
	public FrameM getDefaultFrame() { return findFrameObj(_defaultFrame); }
	public FrameM getDefaultIdFrame() { return findFrameObj(_defaultIdFrame); }
	public FrameM getDefaultChFrame() { return findFrameObj(_defaultChFrame); }
	public FrameM getDefaultPgFrame() { return findFrameObj(_defaultPgFrame); }
	public void setPathUser(String localPath){ _dirUser = localPath; }
	public void setCloudStorage(String localPath){ _dirCloudStorage = localPath; }
	public void setLocalStorage(String localPath){ _dirLocalStorage = localPath; }

	private final String INI_SETTING_FILE = "MangaResouceIni.json";
	private final String INI_SETTING_FILE_PATH;
	private final String INI_SETTING_DIR;
	private String _dirUser = "";
	private String _dirCloudStorage = "";
	private String _dirLocalStorage = "";

	public SettingAd(String dirSystem) { //, String filename) {
		// INI_SETTING_FILE = filename;
		INI_SETTING_DIR = dirSystem;
		INI_SETTING_FILE_PATH = INI_SETTING_DIR + "/" + INI_SETTING_FILE;
	}
	public void parseJson() throws SDEBase {
		try {
			parseJson(ThreadWork.readJson(INI_SETTING_FILE_PATH));
		} catch(JSONException e) {
			throw SDEError.c.........; 
		}
	}
	public void parseJson(String externalFile) throws SDEBase {
		try {
			parseJson(ThreadWork.readJson(externalFile));
		} catch(JSONException e) {
			throw SDEError.c.........; 
		}		
		ThreadWork.write(INI_SETTING_FILE_PATH, json.toString());
	}
}

	private void parseJson(JSONObject json) throws JSONException {
		_pathLPicture = json.getString("PathLPicture");
		_pathCPicture = json.getString("PathCPicture");
		_pathLRecord = json.getString("PathLRecord");
		_pathCRecord = json.getString("PathCRecord");
		_Tmark2Order = _iniTS2I(json.getJSONArray("Mark2Order"));
		_TmarkToName = _iniTS2S(json.getJSONArray("MarkToName"));
		_diffMaxDay = json.getLong("DiffMaxDay");
		_node = json.getLong("Node");
		_pathTokenS = json.getString("PathTokenS");
		_cloudToken = json.getString("CloudToken");
		_legalImageExtension = _iniTS2S(json.getJSONArray("LegalImageExtension"));
		_idIndexPKPgS = json.getInt("IdIndexPKPgS");
		_kIdTHolderCh = json.getInt("KIdTHolderCh");
		_chIndexPKPgS = json.getInt("ChIndexPKPgS");
		_kIdTHolderPg = json.getInt("KIdTHolderPg");
		_pgIndexPKPgS = json.getInt("PgIndexPKPgS");
		_pgIndexChKPgSF = json.getInt("PgIndexChKPgSF");
		_pgIndexChInteger = json.getInt("PgIndexChInteger");
		_pressAmount = json.getInt("PressAmount");
		_dFKeyName = json.getString("DFKeyName");
		_legalImageExtension = _iniTS2S(json.getJSONArray("LegalImageExtension"));
		_pathLFImage = json.getString("PathLFImage");
		_TgetFontTypes = _iniTS2S(json.getJSONArray("GetFontTypes"));
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
		_pathLPicture = json.getString("PathLPicture");
		_pathSynJson = json.getString("PathSynJson");
		_legalJsonExtension = _iniTS2S(json.getJSONArray("LegalJsonExtension"));
		_fileManagerList = _iniTS2S(json.getJSONArray("FileManagerList"));
		_pathSynCId = json.getString("PathSynCId");
		_pathSynLId = json.getString("PathSynLId");
		_localDefault = json.getString("LocalDefault");
		_TfindFrameObj = _iniTS2F(json.getJSONArray("FindFrameObj"));
		_defaultFrame = json.getString("DefaultFrame"));
		_defaultIdFrame = json.getString("DefaultIdFrame"));
		_defaultChFrame = json.getString("DefaultChFrame"));
		_defaultPgFrame = json.getString("DefaultPgFrame"));
	}
	private String _pathLPicture = null;
	private String _pathCPicture = null;
	private String _pathLRecord = null;
	private String _pathCRecord = null;
	private Map<String, int> _Tmark2Order = null;
	private Map<String, String> _TmarkToName = null;
	private long _diffMaxDay = 0;
	private Long _node = null;
	private String _pathTokenS = null;
	private String _cloudToken = null;
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
	private String[] _legalImageExtension = null;
	private String _pathLFImage = null;
	private Map<String, String> _TgetFontTypes = null;
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
	private String _pathLPicture = null;
	private String _pathSynJson = null;
	private String[] _legalJsonExtension = null;
	private String[] _fileManagerList = null;
	private String _pathSynCId = null;
	private String _pathSynLId = null;
	private String _localDefault = null;
	private Map<String, FrameM> _TfindFrameObj = null;
	private String _defaultFrame = null;
	private String _defaultIdFrame = null;
	private String _defaultChFrame = null;
	private String _defaultPgFrame = null;
