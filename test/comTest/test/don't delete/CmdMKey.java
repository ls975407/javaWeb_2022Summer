package com.cloud.manga.henryTing.test;
/*
javac -encoding utf8  com/cloud/manga/henryTing/test/CmdMKey.java
*/

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import com.cloud.manga.henryTing.unit.Point;
import com.cloud.manga.henryTing.unit.ActionM;
import com.cloud.manga.henryTing.unit.CmdEnum;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.infor.CmdM;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


public class CmdMKey extends CmdM implements Comparable<CmdM> {
	final KeyEnum _keyEnum;
	final NameBuilder _nameBuilder;
	public CmdMKey(CmdEnum cmdEnum, int order, KeyEnum keyEnum, NameBuilder nameBuilder) {
		super(cmdEnum, "", order);
		_keyEnum = keyEnum;
		_nameBuilder = nameBuilder;
	}
	@Override
	public ActionM getActionM(int clickTimes) {
		return new ActionM (
			_Type[0], 
			_nameBuilder.getArg(_keyEnum, clickTimes), 
			clickTimes
		);
	}
	@Override
	public boolean equals(Object obj) {
        if(obj instanceof CmdMKey) {
            CmdMKey k = (CmdMKey) obj;
            return this._keyEnum == k._keyEnum;
        }
		return false;
	}
}