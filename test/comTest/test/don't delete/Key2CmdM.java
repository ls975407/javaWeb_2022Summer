package com.cloud.manga.henryTing.test;
/*
javac -encoding utf8  com/cloud/manga/henryTing/test/Key2CmdM.java
*/

import java.awt.event.KeyEvent;

import com.cloud.manga.henryTing.infor.CmdM;
import com.cloud.manga.henryTing.unit.FrameEnum;
import com.cloud.manga.henryTing.unit.HolderEnum;
import com.cloud.manga.henryTing.unit.CmdEnum;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.GUIInterface;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class Key2CmdM extends MangaBaseDual {
	
	public void sendASkip() {
		_touchDelayer.sendASkip(++_level); 
	}
	public long getLevel() {
		return _level;
	}
	
	private final GetFName _getFName;
	private final static Map<Integer, KeyEnum> table = _tableIni();
	
	private static Map<Integer, KeyEnum> _tableIni() {
		Map<Integer, KeyEnum> table = new HashMap<>();
		table.put(KeyEvent.VK_Q, KeyEnum.NextChapter); 
		table.put(KeyEvent.VK_A, KeyEnum.NextChapter); 
		table.put(KeyEvent.VK_Z, KeyEnum.NextChapter); 

		table.put(KeyEvent.VK_R, KeyEnum.PrevChapter); 
		table.put(KeyEvent.VK_F, KeyEnum.PrevChapter); 
		table.put(KeyEvent.VK_V, KeyEnum.PrevChapter); 

		table.put(KeyEvent.VK_X, KeyEnum.NextPage); 
		table.put(KeyEvent.VK_C, KeyEnum.PrevPage); 
		table.put(KeyEvent.VK_S, KeyEnum.NextTwoPage); 
		table.put(KeyEvent.VK_D, KeyEnum.PrevTwoPage); 
		table.put(KeyEvent.VK_W, KeyEnum.NextMark); 
		table.put(KeyEvent.VK_E, KeyEnum.PrevMark); 
		table.put(KeyEvent.VK_PERIOD, KeyEnum.GoFirst); 
		table.put(KeyEvent.VK_COMMA, KeyEnum.GoLast); 
		table.put(KeyEvent.VK_O, KeyEnum.TxtCurrntFolder); 
		table.put(KeyEvent.VK_P, KeyEnum.TxtDefaultFolder); 
		table.put(KeyEvent.VK_Y, KeyEnum.SetIni); 
		table.put(KeyEvent.VK_U, KeyEnum.SetPathUser); 
		table.put(KeyEvent.VK_I, KeyEnum.SetCloudStorage); 
		table.put(KeyEvent.VK_O, KeyEnum.SetLocalStorage); 
		table.put(KeyEvent.VK_0, KeyEnum.Show); 
		table.put(KeyEvent.VK_9, KeyEnum.None); 
		table.put(KeyEvent.VK_BACK_SPACE, KeyEnum.Back); 
		table.put(KeyEvent.VK_ENTER, KeyEnum.Enter); 
		table.put(KeyEvent.VK_1, KeyEnum.Switch); 
		table.put(KeyEvent.VK_B, KeyEnum.NextHPage1); 
		table.put(KeyEvent.VK_N, KeyEnum.PrevHPage1); 
		table.put(KeyEvent.VK_G, KeyEnum.NextHPage2); 
		table.put(KeyEvent.VK_H, KeyEnum.PrevHPage2); 
		return table;
	}
	
	public Key2CmdM(GetFName getFName, GUIInterface guiInterface) throws JSONException {
		super._initialize(guiInterface);
		_getFName = getFName;
	}
	

	private static int getOrder(KeyEnum keyEnum) {
		switch (keyEnum) {
			case SetIni: case SetPathUser: case SetCloudStorage: case SetLocalStorage: 
			return 1;
			case NextPage: case PrevPage: case NextChapter: case PrevChapter: case NextTwoPage: 
			case PrevTwoPage: case NextMark: case PrevMark: 
			return 5;
			case TxtCurrntFolder: case TxtDefaultFolder:  
			case GoFirst: case GoLast: case Switch: case Show: 
			case NextHPage1: case PrevHPage1: case NextHPage2: case PrevHPage2: case None:
			return 2;
			case Back: case Enter:
			default: 
			return 3;
		}
	}
	
	public CmdMKey parseClickCmdMs(KeyEvent key) {
		KeyEnum keyEnum = table.get(key.getKeyCode());
		if (keyEnum == null) {
			return null;
		}
		int order = getOrder(keyEnum);
		if (order >= 3) { return null; }
		NameBuilder nameBuilder = new NameBuilder(_getFName.getFName());
		CmdEnum cmdEnum = nameBuilder.getCmd(keyEnum);
		return new CmdMKey(cmdEnum, order, keyEnum, nameBuilder);
	}
	
	public CmdMKey parseCountCmdMs(KeyEvent key) {
		KeyEnum keyEnum = table.get(key.getKeyCode());
		if (keyEnum == null) {
			return null;
		}
		int order = getOrder(keyEnum);
		if (order < 3) { return null; }
		NameBuilder nameBuilder = new NameBuilder(_getFName.getFName());
		CmdEnum cmdEnum = nameBuilder.getCmd(keyEnum);
		return new CmdMKey(cmdEnum, order, keyEnum, nameBuilder);
	}
}