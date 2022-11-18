package com.cloud.manga.henryTing.main.settingWork;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import java.util.ArrayMap;
import java.util.Map;

/*
javac -encoding utf8  ./com/cloud/manga/henryTing/main/settingWork/SettingAdInput.java
*/

public abstract class SettingAdInput implements com.cloud.manga.henryTing.main.Setting 
 ,com.cloud.manga.henryTing.data.Setting
 ,com.cloud.manga.henryTing.consts.Setting
 ,com.cloud.manga.henryTing.infor.Setting
 ,com.cloud.manga.henryTing.holder.Setting
 ,com.cloud.manga.henryTing.thread.Setting
{
	// data.PathGet
	public String getPathLPicture();
	public String getPathCPicture();
	public String getPathLRecord();
	public String getPathCRecord();
	// data.InforCh
	public int mark2Order(String mark);
	public String markToName(String mark);
	// data.DateT
	public long getDiffMaxDay();
	// consts.IdTokenS
	public Long getNode();
	public String getPathTokenS();
	// consts.CloudM
	public String getCloudToken();
	// consts.ThreadWork
	public String[] getLegalImageExtension();
	// holder.holderId
	public int mapIdIndexPKPgS();
	// holder.holderCh
	public int mapKIdTHolderCh();
	public int mapChIndexPKPgS();
	// holder.holderPg
	public int mapKIdTHolderPg();
	public int mapPgIndexPKPgS();
	public int mapPgIndexChKPgSF();
	public int mapPgIndexChInteger();
	// holder.holderBase
	public int getPressAmount();
	// infor.FrameM
	public String getDFKeyName();
	public String[] getLegalImageExtension();
	public String getPathLFImage();
	// infor.LabelBase
	public String getFontTypes(int index);
	public String getErrorFontType();
	public int getErrorFontSize();
	// infor.LabelM
	public int getErrorLabelIndex();
	// infor.FrameInfor
	public double getFontScaleValue();
	public double getGeoScaleValue();
	public int getLabelMarge();
	public int getScreenX();
	public int getScreenY();
	// thread.ThreadImp
	public int getRangeNextPg();
	public int getRangePrevPg();
	public int getRangeNextCh();
	public int getRangePrevCh();
	public int getNumThread();
	// thread.TouchDelayer
	public long getCmdTimeMax();
	// thread.TouchUnit
	public long getCmdTimeMin();
	// main.KAddreddM
	public int getRandomIdNum();
	public String getTxtHistory();
	public Long getNodeCPicture();
	public Long getNodeCJson();
	public String getPathLPicture();
	public String getPathSynJson();
	public String[] getLegalJsonExtension();
	public String[] getFileManagerList();
	public String getPathSynCId();
	public String getPathSynLId();
	public String getLocalDefault();
	// main.Manga
	public FrameM findFrameObj(String keyString);
	public FrameM getDefaultFrame();
	public FrameM getDefaultIdFrame();
	public FrameM getDefaultChFrame();
	public FrameM getDefaultPgFrame();
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
	static private String[] _iniAS2S(JSONArray jsonA) throws JSONException {
		final int size = jsonA.length();
		String[] buf = new String[size];
		for (int ith=0; ith<size; ith++) {
			buf[ith] = jsonA.getString(ith);
		}
		return buf;
	}
	static private Map<String, String> _iniTS2S(JSONArray jsonA) throws JSONException {
		Map<String, String> reMap = new ArrayMap<>();
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
	static private Map<String, String> _iniTS2I(JSONArray jsonA) throws JSONException {
		Map<String, Integer> reMap = new ArrayMap<>();
		final int size = jsonA.length();
		JSONArray t_jsonA;
		for (int ith=0; ith<size; ith++) {
			t_jsonA = jsonA.getJSONArray(ith);
			reMap.put(
				t_jsonA.getString(0),
				new Integer(t_jsonA.getInt(1))
			);
		}
		return reMap;
	}
	static private Map<String, FrameM> _iniTS2F(JSONArray jsonA) throws JSONException {
		FrameM._frameIni = frameIni;
		Map<String, FrameM> reMap = new ArrayMap<>();
		final int size = jsonA.length();
		FrameM frameM;
		for (int ith=0; ith<size; ith++) {
			frameM = new FrameM(jsonA.getJSONObject(ith));
			reMap.put( frameM._keyName, frameM );
		}
		return reMap;
	}
}