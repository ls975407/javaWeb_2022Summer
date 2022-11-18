package com.cloud.manga.henryTing.test;
/*
javac -encoding utf8  com/cloud/manga/henryTing/test/Key2CmdAbcE.java
*/

import java.awt.event.KeyEvent;

import com.cloud.manga.henryTing.infor.GeoGet;
import com.cloud.manga.henryTing.infor.CmdM;
import com.cloud.manga.henryTing.unit.FrameEnum;
import com.cloud.manga.henryTing.unit.HolderEnum;
import com.cloud.manga.henryTing.unit.CmdEnum;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.ActionM;
import com.cloud.manga.henryTing.unit.CmdAbcE;
import com.cloud.manga.henryTing.unit.Point;
import com.cloud.manga.henryTing.unit.Log;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.thread.TouchUnit;
import com.cloud.manga.henryTing.main.GUIInterface;
import com.cloud.manga.henryTing.main.SettingAd;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class Key2CmdAbcE extends com.cloud.manga.henryTing.main.MangaMain {
	public final static Log _log = new Log("Key2CmdAbcE");
	
	// private final GetFName _getFName;
	private final static Map<Integer, CmdAbcE> table = _tableIni();
	
	private static Map<Integer, CmdAbcE> _tableIni() {
		Map<Integer, CmdAbcE> table = new HashMap<>();
		table.put(KeyEvent.VK_Q, CmdAbcE.NextChapter); 
		table.put(KeyEvent.VK_A, CmdAbcE.NextChapter); 
		table.put(KeyEvent.VK_Z, CmdAbcE.NextChapter); 

		table.put(KeyEvent.VK_R, CmdAbcE.PrevChapter); 
		table.put(KeyEvent.VK_F, CmdAbcE.PrevChapter); 
		table.put(KeyEvent.VK_V, CmdAbcE.PrevChapter); 

		table.put(KeyEvent.VK_X, CmdAbcE.NextPage); 
		table.put(KeyEvent.VK_C, CmdAbcE.PrevPage); 
		table.put(KeyEvent.VK_S, CmdAbcE.NextTwoPage); 
		table.put(KeyEvent.VK_D, CmdAbcE.PrevTwoPage); 
		table.put(KeyEvent.VK_W, CmdAbcE.NextMark); 
		table.put(KeyEvent.VK_E, CmdAbcE.PrevMark); 
		table.put(KeyEvent.VK_CLOSE_BRACKET, CmdAbcE.GoFirst); 
		table.put(KeyEvent.VK_OPEN_BRACKET, CmdAbcE.GoLast); 
		table.put(KeyEvent.VK_O, CmdAbcE.TxtCurrntFolder); 
		table.put(KeyEvent.VK_P, CmdAbcE.TxtDefaultFolder); 
		table.put(KeyEvent.VK_Y, CmdAbcE.SetIni); 
		table.put(KeyEvent.VK_U, CmdAbcE.SetPathUser); 
		table.put(KeyEvent.VK_I, CmdAbcE.SetCloudStorage); 
		table.put(KeyEvent.VK_O, CmdAbcE.SetLocalStorage); 
		table.put(KeyEvent.VK_0, CmdAbcE.Show); 
		table.put(KeyEvent.VK_9, CmdAbcE.None); 
		table.put(KeyEvent.VK_1, CmdAbcE.Switch); 
		table.put(KeyEvent.VK_EQUALS , CmdAbcE.TxtDefaultFolder); 
		table.put(KeyEvent.VK_B, CmdAbcE.NextUpPage); 
		table.put(KeyEvent.VK_N, CmdAbcE.PrevUpPage); 
		table.put(KeyEvent.VK_G, CmdAbcE.NextLeftPage); 
		table.put(KeyEvent.VK_H, CmdAbcE.PrevLeftPage); 
		table.put(KeyEvent.VK_ESCAPE , CmdAbcE.Exit); 
		
		table.put(KeyEvent.VK_BACK_SPACE, CmdAbcE.Back); 
		table.put(KeyEvent.VK_ENTER, CmdAbcE.Enter); 
		
		table.put(KeyEvent.VK_HOME, CmdAbcE.GoFirst); 
		table.put(KeyEvent.VK_END, CmdAbcE.GoLast); 
		table.put(KeyEvent.VK_PAGE_UP, CmdAbcE.PrevPage); 
		table.put(KeyEvent.VK_PAGE_DOWN, CmdAbcE.NextPage); 
		table.put(KeyEvent.VK_UP, CmdAbcE.PrevPage); 
		table.put(KeyEvent.VK_RIGHT, CmdAbcE.PrevPage); 
		table.put(KeyEvent.VK_DOWN, CmdAbcE.NextPage); 
		table.put(KeyEvent.VK_LEFT, CmdAbcE.NextPage); 
		
		table.put(KeyEvent.VK_NUMPAD0, CmdAbcE.NextPage); 
		table.put(KeyEvent.VK_NUMPAD1, CmdAbcE.NextChapter); 
		table.put(KeyEvent.VK_NUMPAD2, CmdAbcE.Switch); 
		table.put(KeyEvent.VK_NUMPAD3, CmdAbcE.PrevChapter); 
		table.put(KeyEvent.VK_PERIOD, CmdAbcE.PrevPage); 
		
		table.put(KeyEvent.VK_NUMPAD4, CmdAbcE.Back); 
		table.put(KeyEvent.VK_NUMPAD5, CmdAbcE.NextUpPage); 
		table.put(KeyEvent.VK_NUMPAD6, CmdAbcE.Show); 
		table.put(KeyEvent.VK_NUMPAD7, CmdAbcE.NextLeftPage); 
		table.put(KeyEvent.VK_NUMPAD8, CmdAbcE.PrevUpPage); 
		table.put(KeyEvent.VK_NUMPAD9, CmdAbcE.PrevLeftPage); 
		
		table.put(KeyEvent.VK_SLASH, CmdAbcE.GoLast); 
		table.put(KeyEvent.VK_SEPARATOR, CmdAbcE.GoLast); 
		table.put(KeyEvent.VK_MULTIPLY, CmdAbcE.GoFirst); 
		
		table.put(KeyEvent.VK_MINUS, CmdAbcE.Exit); 
		table.put(KeyEvent.VK_PLUS, CmdAbcE.Back);
		table.put(KeyEvent.VK_ENTER, CmdAbcE.Enter);  
		
		return table;
	}
	// public Key2CmdAbcE(GeoGet geoGet, GUIInterface guiInterface, GetFName getFName) throws JSONException {
	public Key2CmdAbcE(GeoGet geoGet, GUIInterface guiInterface, String localPath) throws SDEBase {
		super(geoGet, guiInterface);
		
		String dirSystem = ".", dirWork = ".";
		
		// super.initializeWithOutFile(".", ".");
		
		byte[] byteData;
		JSONObject json = null;
		try {
			byteData = FileM.read(localPath);
			json = new JSONObject(FileM.bytes2String(byteData));
		} catch (IOException | JSONException e) {
			byteData = null;
		}

		SettingAd settingAd = null;
		if (byteData != null) {
			try {
				settingAd = new SettingAd(dirSystem, dirWork);
				settingAd.parseJson(json);
			} catch (JSONException | SDEBase e) {
				_log.w("setting read fail ", e.toString());
				settingAd = null;
				// return;
			}
		}
		if (settingAd == null) {
			_log.p("initializeWithByteData");
			super.initializeWithJsonData(dirSystem, dirWork, json);
		} else {
			_log.p("initializeWithOutFile");
			super.initializeWithOutFile(settingAd);
		}
	}
	public Key2CmdAbcE(GeoGet geoGet, GUIInterface guiInterface) throws SDEBase {
		super(geoGet, guiInterface);
		
		String dirSystem = ".", dirWork = ".";
		_log.p("initializeWithOutFile");
		super.initializeWithOutFile(dirSystem, dirWork);
	}
	
	public void sendAKey(KeyEvent key) {
		CmdAbcE cmdAbcE = table.get(key.getKeyCode());
		if (cmdAbcE == null) {
			return;
		}
		sendACmd(cmdAbcE);
	}
	// ----------------------------------------- //
	public void closeTheProgram() {
		super._closeTheProgram(false);
	}
}