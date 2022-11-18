package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/LabelBase.java
*/

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import com.cloud.manga.henryTing.unit.FrameEnum;
import com.cloud.manga.henryTing.tool.FrameMethod;

public class LabelBase {
	public static Setting _setting;
	public final boolean _needRotate; 
	public final int _posiIndex;
	private final boolean _isError;
	private LabelBase(int posiIndex, boolean needRotate) {
		_posiIndex = posiIndex;
		_needRotate = needRotate;
		_isError = false;
	}
	public LabelBase(LabelBase lableBase) {
		_needRotate = lableBase._needRotate;
		_posiIndex = lableBase._posiIndex;
		_isError = false;
	}
	public LabelBase(FrameEnum frameType) {
		_needRotate = !FrameMethod.isPortait(frameType);
		_posiIndex = _setting.getErrorLabelIndex();
		_isError = true;
	}
	
	public String getFontType() {
		if (_isError) {
			return _setting.getErrorFontType();
		}
		return _setting.getFontTypes(_posiIndex);
	}
	public int getFontSize() {
		if (_isError) {
			return _setting.getErrorFontSize();
		}
		return _setting.getFontSizes(_posiIndex);
	}
	
	public LabelBase Rotate() {
		return new LabelBase(_posiIndex, !_needRotate);
	}
	public LabelBase(JSONArray jsonA, FrameEnum frameType) throws JSONException {
		final int size = jsonA.length();
		if (size != 3) {
			throw new JSONException(String.format("LabelBase size = %d != 3", size));
		} 
		int t_int;

		try {
			t_int = jsonA.getInt(1);
		} catch (JSONException e) {
			throw new JSONException(String.format("LabelBase _posiIndex fail: %s",e.toString()));
		}
		_posiIndex = t_int;
		if (_posiIndex < 0 || _posiIndex >=9) {
			throw new JSONException(String.format("LabelBase _posiIndex=%d < 0 || >=9", _posiIndex));
		}
		try {
			t_int = jsonA.getInt(2);
		} catch (JSONException e) {
			throw new JSONException(String.format("LabelBase _posiIndex fail: %s",e.toString()));
		}
		if (t_int < 0 || t_int >=3) {
			throw new JSONException(String.format("LabelBase _needRotate=%d < 0 || >=3", t_int));
		}
		boolean needRotate = true;
		switch(t_int) {
			case 0: needRotate = !FrameMethod.isPortait(frameType);
			case 1: needRotate = false;
			default: break;
		}
		_needRotate = needRotate;
		_isError = false;
	}
} 