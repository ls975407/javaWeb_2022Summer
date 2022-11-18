package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/LabelM.java
*/

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import com.cloud.manga.henryTing.data.KPg;
import com.cloud.manga.henryTing.data.InforCh;
import com.cloud.manga.henryTing.data.InforId;

import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.FrameEnum;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class LabelM extends LabelBase {
	public static Setting _setting;
	enum LabelMarkEnum {
		__none__,
		__id__, __ch__, //__pg__,
		__index__, __page__,
		__len__,
		__name__, __idName__, __chName__, __chMarkName__, //__pgName__,
		__fName__, __idFName__, __chFName__, __pgFName__,
		__error__
	}
	private final String[] _names;
	private final LabelMarkEnum[] _types;

	public LabelM(FrameEnum frameType) {
		super(frameType);
		_types = new LabelMarkEnum[]{LabelMarkEnum.__error__};
		_names = new String[]{"__error__"};
	}
	public LabelM(LabelM lableM) {
		super(lableM);
		_names = lableM._names;
		_types = lableM._types;
	}
	
	private static Map<FrameEnum, LabelM> _mapError = new HashMap<>();
	public static LabelM getNone(FrameEnum frameType) {
		if (!_mapError.containsKey(frameType)) {
			LabelM labelM = new LabelM(frameType);
			_mapError.put(frameType, labelM);
			return labelM;
		}
		return (LabelM) _mapError.get(frameType);
	}
	
	public LabelM(JSONArray jsonA, FrameEnum frameType) throws JSONException {
		super(jsonA, frameType);
		try {
			jsonA = jsonA.getJSONArray(0);
		} catch (JSONException e) {
			throw new JSONException(String.format("LabelM jsonA.getJSONArray(0) fail: %s",e.toString()));
		}
		
		final int size = jsonA.length();
		_names = new String[size];
		_types = new LabelMarkEnum[size];
		LabelMarkEnum t_type;
		{
			int ith=0;
			try {
				for (; ith<size; ith++) {
					_names[ith] = jsonA.getString(ith);
					try {
						t_type = Enum.valueOf(
							LabelMarkEnum.class, 
							_names[ith]
						);	
					} catch (IllegalArgumentException e) {
						t_type = LabelMarkEnum.__none__;
					}
					_types[ith] = t_type;
				}	
			} catch (JSONException e) {
				throw new JSONException(String.format("LabelMs fail ith = %d: %s",ith,e.toString()));
			}
		}

	}
	private static String int2Mark_3(int int_) {
		return String.format("%03d", int_);
	}
	private static String int2Mark_4(int int_) {
		return String.format("%04d", int_);
	}
	public static class Infor extends LabelBase {
		public final String _content;
		Infor(LabelBase lableBase, String content) {
			super(lableBase);
			_content = content;
		}
	} 
	
	public Infor getILabel(LabelGet _get) {
		return new Infor(this, getLabel(_get));
	}
	private String getLabel(LabelGet _get) {
		StringBuilder buider = new StringBuilder();
		String mark = null;
		KPg kpg = _get.KPg();
		IndexP indexP = _get.IndexP();
		// System.out.println("getLabel");
		for (int ith=0; ith<_names.length; ith++) {
			// System.out.println(String.format("%02d %s", ith, _names[ith]));
			
			switch(_types[ith]) {
				case __none__: 
				mark = _names[ith]; break;
				case __id__:
				mark = int2Mark_4(new InforId(kpg._kch._kid)._id); break;
				case __ch__:
				mark = int2Mark_3(new InforCh(kpg._kch)._chapter); break;
				// case __pg__:
				// mark = int2Mark(kpg._kch._chapter); break;
				case __index__:
				mark = int2Mark_3(indexP.getIndex()); break;
				case __page__:
				mark = int2Mark_3(indexP.getIndex()+1); break;
				case __len__:
				try {
					mark = int2Mark_3(_get.Len());
				} catch (SDEBase e) {
					mark = "???";
				}
				break;
				case __name__:
					switch(indexP._type) {
						case ch: mark = new InforCh(kpg._kch)._name; break;
						default: mark = new InforId(kpg._kch._kid)._name; break;
					}
					break;
				case __idName__:
				mark = new InforId(kpg._kch._kid)._name; break;
				case __chName__:
				mark = new InforCh(kpg._kch)._name; break;
				case __chMarkName__:
				mark = new InforCh(kpg._kch)._markName; break;
				// case __pgName__:
				case __fName__:
					switch(indexP._type) {
						case id: mark = kpg._kch._kid._keyName; break;
						case ch: mark = kpg._kch._keyName; break;
						default: mark = kpg._keyName; break;
					}
					break;
				case __idFName__:
				mark = kpg._kch._kid._keyName; break;
				case __chFName__:
				mark = kpg._kch._keyName; break;
				case __pgFName__:
				mark = kpg._keyName; break;
				case __error__:
				mark = _get.Error();
				break;
			}
			buider.append(mark);
		}
		return buider.toString();
	}
} 