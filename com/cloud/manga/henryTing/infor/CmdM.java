package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/CmdM.java
*/

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import com.cloud.manga.henryTing.unit.Point;
import com.cloud.manga.henryTing.unit.ActionM;
import com.cloud.manga.henryTing.unit.CmdEnum;
import com.cloud.manga.henryTing.unit.CmdAbcE;
import com.cloud.manga.henryTing.unit.SDEBase;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


public class CmdM implements Comparable<CmdM> {
	private static final int orderDefault = 5;

	public final boolean _isClickAction;
	protected final String[] _Args;
	protected final CmdEnum[] _Type;
	final int _order;
	
	final double _scale_minX;
	final double _scale_minY;
	final double _scale_lenX;
	final double _scale_lenY;
	
	public int size() {
		return _Type.length;
	}
	
	private static CmdM _cmdmNone = null;
	private CmdM() {
		_isClickAction = true;
		_Args = new String[]{""};
		_Type = new CmdEnum[]{CmdEnum.None};
		_scale_minX = 0; _scale_lenX = 1;
		_scale_minY = 0; _scale_lenY = 1;
		_order = orderDefault;
	}
	public static CmdM getNone() {
		if (_cmdmNone == null) {
			_cmdmNone = new CmdM();
		}
		return _cmdmNone;
	}
	
	public CmdM getCmdM(int index) {
		return new CmdM(_Type[index], _Args[index], _order);
	}
	public CmdM(CmdEnum cmdEnum, boolean isClickAction) {
		_Args = new String[]{""};
		_Type = new CmdEnum[]{cmdEnum};
		
		_isClickAction = isClickAction;
		_scale_minX = 0; _scale_lenX = 1;
		_scale_minY = 0; _scale_lenY = 1;
		_order = orderDefault;
	}
	public CmdM(CmdEnum cmdEnum, String arg, boolean isClickAction) {
		_Args = new String[]{arg};
		_Type = new CmdEnum[]{cmdEnum};
		
		_isClickAction = isClickAction;
		_scale_minX = 0; _scale_lenX = 1;
		_scale_minY = 0; _scale_lenY = 1;
		_order = orderDefault;
	}
	public CmdM(CmdEnum cmdEnum, String arg) {
		_Args = new String[]{arg};
		_Type = new CmdEnum[]{cmdEnum};
		
		_isClickAction = true;
		_scale_minX = 0; _scale_lenX = 1;
		_scale_minY = 0; _scale_lenY = 1;
		_order = orderDefault;
	}
	public CmdM(CmdEnum cmdEnum, String arg, int order) {
		_Args = new String[]{arg};
		_Type = new CmdEnum[]{cmdEnum};
		
		_isClickAction = true;
		_scale_minX = 0; _scale_lenX = 1;
		_scale_minY = 0; _scale_lenY = 1;
		_order = order;
	}
	CmdM(JSONObject json, CmdM cmdM) throws JSONException {
		_Args = cmdM._Args;
		_Type = cmdM._Type;
		_isClickAction = cmdM._isClickAction;
		JSONArray jsonA = json.getJSONArray("Range");
		_scale_minX = jsonA.getDouble(0);
		_scale_minY = jsonA.getDouble(1);
		_scale_lenX = jsonA.getDouble(2);
		_scale_lenY = jsonA.getDouble(3);
		int order;
		try {
			order = json.getInt("Order");
		} catch (JSONException e) {
			order = orderDefault;
		}
		if (order < -9) { order = -9; }
		else if (order > 9) { order = 9; }
		_order = orderDefault-order;
	}
	CmdM(JSONObject json, CmdM[] cmdMs) throws JSONException {
		final int size = cmdMs.length;
		_Args = new String[size];
		_Type = new CmdEnum[size];
		for (int ith=0; ith<size; ith++) {
			_Args[ith] = cmdMs[ith].getActionM(ith+1)._arg;
			_Type[ith] = cmdMs[ith].getActionM(ith+1)._cmdEnum;
		}
		_isClickAction = false;
		JSONArray jsonA = json.getJSONArray("Range");
		_scale_minX = jsonA.getDouble(0);
		_scale_minY = jsonA.getDouble(1);
		_scale_lenX = jsonA.getDouble(2);
		_scale_lenY = jsonA.getDouble(3);
		int order;
		try {
			order = json.getInt("Order");
		} catch (JSONException e) {
			order = orderDefault;
		}
		if (order < -9) { order = -9; }
		else if (order > 9) { order = 9; }
		_order = orderDefault-order;
	}
	CmdM(JSONObject json) throws JSONException {
		JSONArray jsonA;
		jsonA = json.getJSONArray("Range");
		_scale_minX = jsonA.getDouble(0);
		_scale_minY = jsonA.getDouble(1);
		_scale_lenX = jsonA.getDouble(2);
		_scale_lenY = jsonA.getDouble(3);
		
		boolean isArrayType = true;
		CmdEnum[] type_ = null;
		try {
			jsonA = json.getJSONArray("Type");
		} catch(JSONException e) {
			try {
				type_ = new CmdEnum[]{Enum.valueOf(
					CmdEnum.class, 
					json.getString("Type")
				)};
			} catch (IllegalArgumentException | JSONException e1) {
				throw new JSONException("new CmdM: _Type getString " + e1.toString());
			}
			isArrayType = false;
		}
		int ith;
		if (isArrayType) {
			type_ = new CmdEnum[jsonA.length()];
			ith=0;
			try {
				for (; ith<jsonA.length(); ith++) {
					type_[ith] = Enum.valueOf(
						CmdEnum.class, 
						jsonA.getString(ith)
					);
				} 
			} catch (IllegalArgumentException | JSONException e) {
				throw new JSONException(String.format("new CmdM: _Type %s %d", e.toString(), ith));
			}
		}
		_Type = type_;
		final int size = _Type.length;
		isArrayType = true;
		String[] args_ = null;
		try {
			jsonA = json.getJSONArray("Args");
		} catch(JSONException e) {
			if (size != 1) {
				throw new JSONException("new CmdM: Args getString " + e.toString());
			}
			try {
				args_ = new String[]{
					json.getString("Args")
				};
			} catch (IllegalArgumentException | JSONException e1) {
				throw new JSONException("new CmdM: Args getString " + e1.toString());
			}
			isArrayType = false;
		}
		if (isArrayType) {
			ith=0;
			args_ = new String[jsonA.length()];
			try {
				for (; ith<jsonA.length(); ith++) {
					args_[ith] = jsonA.getString(ith);
				}
			} catch (IllegalArgumentException | JSONException e) {
				throw new JSONException(String.format("new CmdM: _Args %s %d", e.toString(), ith));
			}
		}
		_Args = args_;
		int size_2 = _Args.length;
		if (size_2 != size || size < 1) {
			throw new JSONException(String.format("new CmdM: args type size not match %d %d", size, size_2));
		}

		int order;
		try {
			order = json.getInt("Order");
		} catch (JSONException e) {
			order = orderDefault;
		}
		if (order < -9) { order = -9; }
		else if (order > 9) { order = 9; }
		_order = orderDefault-order;
		
		boolean isClickAction;
		try {
			isClickAction = json.getBoolean("IsClickAction");
		} catch (JSONException e) {
			isClickAction = size==1? true: false;
		}
		_isClickAction = isClickAction;
	}
	
	public ActionM getActionM(int clickTimes) {
		int ith = clickTimes - 1;
		if (_isClickAction) {
			ith = 0;
		}
		ith = Math.min(ith, _Type.length-1);
		ith = Math.max(ith, 0);
		return new ActionM(_Type[ith], _Args[ith], clickTimes);
	}
	private int[] XY = new int[] {0, 0};
	private int MinX=0, MinY=0, LenX=0, LenY=0;
	boolean isInRange(Point point, int[] xy) {
		if (XY[0] != xy[0] || XY[1] != xy[1]) {
			MinX = (int) (xy[0]*_scale_minX); 
			MinY = (int) (xy[1]*_scale_minY);
			LenX = (int) (xy[0]*_scale_lenX);
			LenY = (int) (xy[1]*_scale_lenY);
			XY[0] = xy[0]; XY[1] = xy[1]; 
		}
		if (point._x < MinX || point._y < MinY 
		 || point._x-MinX > LenX || point._y-MinY > LenY
		) { return false; }
		return true;
	}
	public int compareTo(CmdM other) {
		// if (other == null) {
			// System.out.println("other == null");
		// }
		
		return this._order - other._order;
	}
	public boolean equals(Object obj) {
        if(obj instanceof CmdM) {
            CmdM k = (CmdM) obj;
            return Arrays.equals(this._Args, k._Args) &&
				   Arrays.equals(this._Type, k._Type) &&
				   this._order == k._order;
        }
		return false;
	}
	
	
	public static CmdM parseClickCmdMs(CmdAbcE cmdAbcE) {
		switch(cmdAbcE) {
			case None:
			// case NextPage:
			// case PrevPage:
			// case NextChapter:
			// case PrevChapter:
			// case NextTwoPage:
			// case PrevTwoPage:
			// case NextMark:
			// case PrevMark:
			case GoFirst:
			case GoLast:
			case TxtCurrntFolder:
			case TxtDefaultFolder:
			case SetIni:
			case SetPathUser:
			case SetCloudStorage:
			case SetLocalStorage:
			// case Show:
			// case Back:
			// case Enter:
			// case Switch:
			// case NextHPage1:
			// case PrevHPage1:
			// case NextHPage2:
			// case PrevHPage2:
			case TxtCloudHistory: case TxtCloudRandom: 
			case TxtLocalHistory: case TxtLocalRandom: 
			case TxtCloud0: case TxtCloud1: case TxtCloudFolder: 
			case TxtCloudSearchId: case TxtCloudSearchCh:
			case TxtLocalSearchId: case TxtLocalSearchCh:
			case UpdateId: case UpdateIdS:
			case CreateId: case CreateIdS:
			case Exit:
			case Close:
			case Nothing:
			case SaveHistory:
			break;
			default: return null;
		}
		return new CmdM(
			Enum.valueOf(CmdEnum.class,cmdAbcE.toString()), true
		);
	}
	public static CmdM parseCountCmdMs(CmdAbcE cmdAbcE) {
		switch(cmdAbcE) {
			// case None:
			case NextPage:
			case PrevPage:
			case NextChapter:
			case PrevChapter:
			case NextTwoPage:
			case PrevTwoPage:
			case NextMark:
			case PrevMark:
			// case GoFirst:
			// case GoLast:
			// case TxtCurrntFolder:
			// case TxtDefaultFolder:
			// case SetIni:
			// case SetPathUser:
			// case SetCloudStorage:
			// case SetLocalStorage:
			// case Show:
			// case Back:
			// case Enter:
			// case Switch:
			// case NextHPage1:
			// case PrevHPage1:
			// case NextHPage2:
			// case PrevHPage2:
			// case TxtCloudHistory: case TxtCloudRandom: 
			// case TxtLocalHistory: case TxtLocalRandom: 
			// case TxtCloud0: case TxtCloud1: case TxtCloudFolder: 
			// case TxtCloudSearchId: case TxtCloudSearchCh:
			// case TxtLocalSearchId: case TxtLocalSearchCh:
			// case UpdateId: case UpdateIdS:
			// case CreateId: case CreateIdS:
			// case Exit:
			// case Close:
			// case Nothing:
			// case SaveHistory:
			break;
			default: return null;
		}
		return new CmdM(
			Enum.valueOf(CmdEnum.class,cmdAbcE.toString()), false
		);
	}
	public static CmdM parseCmdMs(CmdAbcE cmdAbcE) {
		CmdM cmdM = parseClickCmdMs(cmdAbcE);
		if (cmdM != null) return cmdM;
		return parseCountCmdMs(cmdAbcE);
	}
}