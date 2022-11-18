package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/FrameM.java
*/

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import com.cloud.manga.henryTing.data.KeyStringBase;
import com.cloud.manga.henryTing.data.KPg;
import com.cloud.manga.henryTing.data.InforCh;
import com.cloud.manga.henryTing.data.InforId;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.tool.RandomM;
import com.cloud.manga.henryTing.tool.FrameMethod;

import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.Point;
import com.cloud.manga.henryTing.unit.ActionM;
import com.cloud.manga.henryTing.unit.CmdEnum;
import com.cloud.manga.henryTing.unit.HolderEnum;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.FrameEnum;
import com.cloud.manga.henryTing.unit.CmdAbcE;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class FrameM extends KeyStringBase {
	
	public static Setting _setting = null;
	
	// use external class to record these functions
	public final FrameM _FrameM_Dual;
	private final SlideCmds _slideCmds;
	private final CmdM[] _cmdMs_count;
	private final CmdM[] _cmdMs_click;
	private final Map<CmdAbcE, CmdM> _cmdAbsMap;
	private final LabelM[] _lableMs;
	
	// use enum to get the attribute
	private final HolderEnum _holderType;
	private final FrameEnum _frameType;
	

	private final String _pathLFImage;
	private final String[] _pathNames;
	
	
	
	public final boolean isPortait() {
		return FrameMethod.isPortait(_frameType);
	}
	public final boolean isOnePage() {
		return FrameMethod.isOnePage(_frameType);
	}

	// Error message frame
	public FrameM (FrameM fmRef) {
		super(_setting.getDFKeyName());
		_FrameM_Dual = null;
		_cmdAbsMap = new HashMap<>();
		_slideCmds = null; // SlideCmds.getNone();
		_cmdMs_count = new CmdM[0];
		_cmdMs_click = new CmdM[]{ CmdM.getNone() };
		_lableMs = new LabelM[]{ LabelM.getNone(fmRef._frameType) };
		
		_frameType = fmRef._frameType;
		_holderType = fmRef._holderType;
		
		// _counter = new int[1];
		_pathLFImage = null;
		_pathNames = new String[0];
	}
	public FrameM (FrameM fm, FrameM frameM_Dual) {
		super(fm._keyName);
		_FrameM_Dual = frameM_Dual;
		_cmdAbsMap = fm._cmdAbsMap;
		_slideCmds = fm._slideCmds;
		_cmdMs_count = fm._cmdMs_count;
		_cmdMs_click = fm._cmdMs_click;
		_lableMs = fm._lableMs;

		_frameType = fm._frameType;
		_holderType = fm._holderType;
		
		// _counter = fm._counter;
		_pathLFImage = fm._pathLFImage;
		_pathNames = fm._pathNames;
	}
	
	public String getKeyName() {
		if (_FrameM_Dual == null) {
			return _keyName;
		} else {
			return _FrameM_Dual._keyName;
		}
	}
	
	public HolderEnum getHolderType() {
		if (_FrameM_Dual == null) {
			return _holderType;
		} else {
			return _FrameM_Dual._holderType;
		}
	}
	public FrameEnum getFrameType() {
		if (_FrameM_Dual == null) {
			return _frameType;
		} else {
			return _FrameM_Dual._frameType;
		}
	}
	public boolean isIniState() {
		return getHolderType() == HolderEnum.Ini;
	}
	// public boolean isBothIniState() {
		// System.out.println(getKeyName());
		// System.out.println(_keyName);
		// return getHolderType() == HolderEnum.Ini && _holderType == HolderEnum.Ini;
	// }
	
	public SlideCmds getSlideCmds() {
		if (_FrameM_Dual == null) {
			return _slideCmds;
		} else {
			return _FrameM_Dual._slideCmds;
		}
	}
	public FrameM getFrameM(FrameM frameM) throws SDEBase {
		if (frameM != null && !frameM.isIniState()) {
			throw SDEError.cFrameMTypeNotIni(frameM._keyName);
		}
		return new FrameM(this, frameM);
	}
	public FrameM getFrameM() {
		if (_FrameM_Dual == null) {
			return this;
		} else {
			return _FrameM_Dual;
		}
	}
	public String getPathImage() throws SDEError {
		String pathLFImage;
		String[] pathNames;
		if (_FrameM_Dual == null) {
			pathLFImage = _pathLFImage ;
			pathNames = _pathNames;
		} else {
			pathLFImage = _FrameM_Dual._pathLFImage;
			pathNames = _FrameM_Dual._pathNames;
		}
		if (pathNames.length == 0) {
			return null;
		}
		return pathLFImage + "/" + pathNames[RandomM.getNextInt(pathNames.length)];
	}
	
	
	private CmdM parseSingleCmdM(JSONObject json_t) throws JSONException {
		// json_t = jsonA.getJSONObject(ith);
		String cmdAbcName;
		JSONArray jsonA_t; CmdM cmdM; CmdAbcE cmdAbcE;
		try {
			jsonA_t = json_t.getJSONArray("CmdAbcE");
		} catch (JSONException e) {
			try {
				cmdAbcName = json_t.getString("CmdAbcE");
			} catch (JSONException e1) {
				// cmdMs[ith] = new CmdM(json_t);
				// continue;
				cmdM = new CmdM(json_t);
				return cmdM;
			}
			try {
				cmdAbcE = Enum.valueOf(
					CmdAbcE.class, 
					cmdAbcName
				);
			} catch (IllegalArgumentException e1) {
				throw new JSONException(e1.toString());
			}
			CmdM cmdM_t = CmdM.parseCmdMs(cmdAbcE);
			if (cmdM_t == null) {
				// cmdMs[ith] = new CmdM(json_t);
				cmdM = new CmdM(json_t);
				// _cmdAbsMap.put(cmdAbcE, cmdMs[ith]);
				_cmdAbsMap.put(cmdAbcE, cmdM);
			} else {
				// cmdMs[ith] = new CmdM(json_t, cmdM_t);
				cmdM = new CmdM(json_t, cmdM_t);
			}
			return cmdM;
		}

		List<CmdAbcE> cmdAbcEs = new ArrayList<>();
		List<CmdM> cmdMs = new ArrayList<>();
		int jth=0;
		try {
			String tStr; // CmdM cmdM = cmdMs[ith];
			for (; jth<jsonA_t.length();jth++) {
				tStr = jsonA_t.getString(jth);
				cmdAbcE = Enum.valueOf(
					CmdAbcE.class, 
					tStr
				);
				
				CmdM cmdM_t = CmdM.parseCmdMs(cmdAbcE);
				if (cmdM_t != null) { cmdMs.add(cmdM_t); }
				// _cmdAbsMap.put(cmdAbcE, cmdM.getCmdM(jth));
				cmdAbcEs.add(cmdAbcE);
			}
		} catch (IllegalArgumentException | JSONException e1) {
			throw new JSONException(String.format("%s jth=%d", e1.toString(), jth));
		}
		if (cmdMs.size() == cmdAbcEs.size()) {
			return new CmdM(json_t, cmdMs.toArray(new CmdM[0]));
		}
		
		// cmdMs[ith] = new CmdM(json_t);
		cmdM = new CmdM(json_t);
		// if (jsonA_t.length() != cmdMs[ith].size()) {
		if (jsonA_t.length() != cmdM.size()) {
			throw new JSONException(
				String.format("jsonA_t.length() != cmdMs[ith].size()")
			);
		}
		
		for (jth=0; jth<jsonA_t.length();jth++) {
			_cmdAbsMap.put(cmdAbcEs.get(jth), cmdM.getCmdM(jth));
		}
		
		return cmdM;
	}

	public FrameM (JSONObject json, Setting setting) throws JSONException {
		super(json.getString("Name"));
		
		_cmdAbsMap = new HashMap<>();
		_FrameM_Dual = null;
		JSONArray jsonA;
		try {
			jsonA = json.getJSONArray("CmdMs");
		} catch (JSONException e) {
			throw new JSONException(String.format("CmdMs fail %s", e.toString()));
		}
		
		
		CmdM[] cmdMs = new CmdM[jsonA.length()];
		CmdAbcE cmdAbcE = null;
		{
			int ith=0;
			try {
				JSONObject json_t; String cmdAbcName;
				for ( ;ith<jsonA.length(); ith++) {
					cmdMs[ith] = parseSingleCmdM(jsonA.getJSONObject(ith));
				}
			} catch (JSONException e) {
				throw new JSONException(String.format("CmdMs fail ith = %d: %s",ith,e.toString()));
			}
		}

		// java.util.Arrays.sort(cmdMs);
		List<CmdM> countCmds = new ArrayList<>();
		List<CmdM> clickCmds = new ArrayList<>();
		for (CmdM cmdM: cmdMs) {
			if (cmdM._isClickAction) { 
				clickCmds.add(cmdM); 
			} else { countCmds.add(cmdM); }
		}
		_cmdMs_count = countCmds.toArray(new CmdM[0]);
		_cmdMs_click = clickCmds.toArray(new CmdM[0]);


		try {
			_holderType = Enum.valueOf(
				HolderEnum.class, 
				json.getString("HolderType")
			);
		} catch (IllegalArgumentException e) {
			throw new JSONException(String.format("unknowned holder type %s", json.getString("HolderType")));
		} catch (JSONException e) {
			throw new JSONException(String.format("HolderType fail: %s",e.toString()));
		}

		try {
			_frameType = Enum.valueOf(
				FrameEnum.class, 
				json.getString("FrameType")
			);
		} catch (IllegalArgumentException e) {
			throw new JSONException(String.format("unknowned frame type %s", json.getString("FrameType")));
		} catch (JSONException e) {
			throw new JSONException(String.format("FrameType fail: %s",e.toString()));
		}

		try {
			jsonA = json.getJSONArray("LabelMs");
		} catch (JSONException e) {
			throw new JSONException(String.format("LabelMs fail: %s",e.toString()));
		}
		{
			LabelM labelM;
			LabelM[] lableMs = new LabelM[9];
			for (int ith=0; ith<lableMs.length; ith++) {
				lableMs[ith] = null;
			}
			{
				int ith=0;
				try {
					for (; ith<jsonA.length(); ith++) {
						labelM = new LabelM(jsonA.getJSONArray(ith), _frameType);
						lableMs[labelM._posiIndex] = labelM;
					}
				} catch (JSONException e) {
					throw new JSONException(String.format("LabelMs fail ith = %d: %s",ith,e.toString()));
				}
			}
			List<LabelM> buf = new ArrayList<>();
			int count = 0;
			for (int ith=0; ith<lableMs.length; ith++) {
				if (lableMs[ith] != null) { buf.add(lableMs[ith]); }
			}
			_lableMs = buf.toArray(new LabelM[0]);
		}
		// _lableMs = null;
		
		try {
			// jsonA = json.getJSONArray("SlideCmds");
			// JSONObject json_t; // CmdAbcE cmdAbcE;
			// String cmdAbcName;
			// List<ActionM> list_ = new ArrayList<>();
			// ActionM actionM;
			// for (int ith=0; ith<jsonA.length(); ith++) {
				// json_t = jsonA.getJSONObject(ith);
				// actionM = SlideCmds.actionMIni(json_t);
				// list_.add(actionM);
				// try {
					// cmdAbcName = json_t.getString("CmdAbcE");
				// } catch (JSONException e) {
					// continue;
				// }
				// try {
					// cmdAbcE = Enum.valueOf(
						// CmdAbcE.class, 
						// cmdAbcName
					// );
				// } catch (IllegalArgumentException e) {
					// throw new JSONException(String.format("%s ith=%d", e.toString(), ith));
				// }
				// _cmdAbsMap.put(cmdAbcE, new CmdM(actionM._cmdEnum, actionM._arg, true));
			// }
			// _slideCmds = new SlideCmds(list_.toArray(new ActionM[0]));
			JSONObject json_t = null; SlideCmds slideCmds = null;
			try {
				json_t = json.getJSONObject("SlideCmds");
			} catch (JSONException e) {
				slideCmds = null;
			}
			if (json_t != null) {
				CmdM cmdM_t = parseSingleCmdM(json_t);
				slideCmds = new SlideCmds(cmdM_t);
			}
			_slideCmds = slideCmds;
		} catch (JSONException e) {
			throw new JSONException(String.format("SlideCmds fail: %s",e.toString()));
		}
		
		// _counter = new int[_cmdMs_count.length];
		// for (int ith=0; ith<_cmdMs_count.length; ith++) {
			// _counter[ith] = 0;
		// }
		String pathLFImage = null; String[] pathNames = null;
		try {
			pathLFImage = setting.getPathLFImage() + '/' + json.getString("FolderImage");
		} catch (JSONException e) {}
		try {
			if(pathLFImage != null) {
				pathNames = FileM.listFile(pathLFImage, setting.getLegalImageExtension());
			}
		} catch (java.io.IOException e) {}
		if(pathLFImage == null || pathNames == null) {
			_pathLFImage = null; _pathNames = new String[0];
		} else {
			_pathLFImage = pathLFImage; _pathNames = pathNames;
		}
	}
	public Iterable<LabelM> getLabelMs() {
		LabelM[] lableMs;
		if (_FrameM_Dual == null) {
			lableMs = _lableMs;
		} else {
			lableMs = _FrameM_Dual._lableMs;
		}
		return Arrays.asList(_lableMs);
	}
	// public static Iterable<LableM> getErrorLabelMs(FrameM frameM) {
		// List<LabelM> list_ = new ArrayList<>();
		// list_.add(LabelM.getNone(frameM));
		// return list_;
	// }
	
	private static int[] formatGeoGet(GeoGet geoGet) {
		final int SX = geoGet.screenX();
		final int SY = geoGet.screenY();
		if (SX < SY) {
			return new int[] {SX, SY};
		} else {
			return new int[] {SY, SX};
		}
	}
	
	public CmdM parseClickCmdMs(Point point, GeoGet geoGet) {
		CmdM[] cmdMs_click;
		if (_FrameM_Dual == null) {
			cmdMs_click = _cmdMs_click;
		} else {
			cmdMs_click = _FrameM_Dual._cmdMs_click;
		}
		
		int[] screenXY = formatGeoGet(geoGet);
		for (CmdM cmdM_click: cmdMs_click) {
			if (cmdM_click.isInRange(point, screenXY)) {
				return cmdM_click;
			}
		}
		return null;
	}
	public CmdM parseCountCmdMs(Point point, GeoGet geoGet) {
		CmdM[] cmdMs_count;
		if (_FrameM_Dual == null) {
			cmdMs_count = _cmdMs_count;
		} else {
			cmdMs_count = _FrameM_Dual._cmdMs_count;
		}
		
		int[] screenXY = formatGeoGet(geoGet);
		for (CmdM cmdM_click: cmdMs_count) {
			if (cmdM_click.isInRange(point, screenXY)) {
				return cmdM_click;
			}
		}
		return null;
	}
	public CmdM parseCmdAbcE(CmdAbcE cmdAbsE) {
		Map<CmdAbcE, CmdM> cmdAbsMap;
		if (_FrameM_Dual == null) {
			cmdAbsMap = _cmdAbsMap;
		} else {
			cmdAbsMap = _FrameM_Dual._cmdAbsMap;
		}
		return _cmdAbsMap.get(cmdAbsE);
	}
	public ActionM parseSlideActionM(Point p1, Point p2, GeoGet geoGet) {
		if (_slideCmds == null) { return null; }
		return _slideCmds.parseActionM(p1, p2, geoGet, _frameType);
	}
}