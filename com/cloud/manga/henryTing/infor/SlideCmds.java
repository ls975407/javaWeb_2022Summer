package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/SlideCmds.java
*/

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import com.cloud.manga.henryTing.unit.ActionM;
import com.cloud.manga.henryTing.unit.CmdEnum;
import com.cloud.manga.henryTing.unit.Point;
import com.cloud.manga.henryTing.unit.FrameEnum;

import java.util.List;
import java.util.ArrayList;


public class SlideCmds {
	private final CmdM _cmdM;
	public static Setting _setting = null;
	
	// private static SlideCmds _slideNone = null;
	// public static SlideCmds getNone() {
		// if (_slideNone == null) {
			// _slideNone = new SlideCmds(CmdM.getNone(4));
		// }
		// return _slideNone;
	// }
	// public static ActionM actionMIni(JSONObject json) throws JSONException {
		// try {
			// return new ActionM(
				// Enum.valueOf(
					// CmdEnum.class, 
					// json.getString("Type")
				// ),  json.getString("Args")
			// );
		// } catch (IllegalArgumentException | JSONException e) {
			// try {
				// return new ActionM(
					// Enum.valueOf(
						// CmdEnum.class, 
						// json.getString("Type")
					// ),  ""
				// );
			// } catch (IllegalArgumentException | JSONException e1) {
				// throw new JSONException(String.format("unknowned frame type %s", e1.toString()));
			// }
		// }
	// }
	// SlideCmds(JSONArray jsonA) throws JSONException {
		// final int size = jsonA.length();
		// _actionM = new ActionM[size];
		// if (size != 4) {
			// throw new JSONException("new SlideCmds: size not 4");
		// }
		// int ith=0;
		// try {
			// for (; ith<size; ith++) {
				// _actionM[ith] = actionMIni(jsonA.getJSONObject(ith));
			// } 
		// } catch (JSONException e) {
			// throw new JSONException(String.format("SlideCmds fail ith = %d: %s",ith,e.toString()));
		// }
	// }
	// SlideCmds(ActionM[] actionMs) throws JSONException {
		// if (actionMs.length != 4) {
			// throw new JSONException("new SlideCmds: size not 4");
		// }
		// _actionM = actionMs;
	// }
	SlideCmds(CmdM cmdM) throws JSONException {
		if (cmdM.size() != 4) {
			throw new JSONException("new SlideCmds: size not 4");
		}
		_cmdM = cmdM;
	}
	// private ActionM Up() { return _cmdM.getActionM(0); }
	// private ActionM Down() { return _cmdM.getActionM(1); }
	// private ActionM Left() { return _cmdM.getActionM(2); }
	// private ActionM Right() { return _cmdM.getActionM(3); }
	
	
	
	
    private enum Direct {
        up, down, left, right, none
    }
    private static Direct getDirect(int x1, int y1, int x2, int y2) {
        int diffX = x2-x1;
        int diffY = y2-y1;
        final int amount = _setting.getMinSlideAmount();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (diffX > amount) {
                return Direct.right;
            }
            if (diffX < -amount) {
                return Direct.left;
            }
        } else {
            if (diffY > amount) {
                return Direct.up;
            }
            if (diffY < -amount) {
                return Direct.down;
            }
        }
        return Direct.none;
    }
	private static Direct rotateDirect(Direct direct) {
		switch(direct) {
			case up: return Direct.left;
			case down: return Direct.right;
			case left: return Direct.down;
			case right: return Direct.up;
			case none: default: return direct;
		}
	}
	private static Direct switchDirect(Direct direct) {
		switch(direct) {
			case up: return Direct.right;
			case down: return Direct.left;
			case left: return Direct.down;
			case right: return Direct.up;
			case none: default: return direct;
		}
	}
	public ActionM parseActionM(Point p1, Point p2, GeoGet geoGet, FrameEnum frameTpye) {
		if (p1._x == p2._x && p1._y == p2._y) { return null; }
		
		// formatGeoGet
		final int SX = geoGet.screenX();
		final int SY = geoGet.screenY();
		int[] screenXY;
		if (SX < SY) {
			screenXY = new int[] {SX, SY};
		} else {
			screenXY = new int[] {SY, SX};
		}
		if (!_cmdM.isInRange(p1, screenXY)) {
			return null;
		}
		Direct direct = getDirect(p1._x, p1._y, p2._x, p2._y);
		if (frameTpye == FrameEnum.dual) { direct = rotateDirect(direct); }
		// System.out.println("-----------------" + direct.toString());
		if (SX > SY) { direct = switchDirect(direct); }
		// System.out.println("-----------------" + direct.toString());
		
		return new ActionM(_cmdM.getActionM(direct.ordinal()+1), 1);
	}
	
	
	
	
	
	
	
	
	
	
}