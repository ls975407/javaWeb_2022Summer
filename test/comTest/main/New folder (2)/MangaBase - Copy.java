package com.cloud.manga.henryTing.main;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/main/MangaBase.java
*/


import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.List;
import java.util.Collections;

import com.cloud.manga.henryTing.infor.GeoGet;
import com.cloud.manga.henryTing.infor.CmdM;
import com.cloud.manga.henryTing.infor.ImageGet;

import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.ExceptionJoin;
import com.cloud.manga.henryTing.unit.ActionM;
import com.cloud.manga.henryTing.unit.CmdEnum;
import com.cloud.manga.henryTing.unit.Point;
import com.cloud.manga.henryTing.tool.ThreadLocal;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.infor.FrameM;
import com.cloud.manga.henryTing.thread.ThreadImp;
import com.cloud.manga.henryTing.thread.TouchS;
import com.cloud.manga.henryTing.holder.HolderM;


import com.cloud.manga.henryTing.consts.ThreadWork; 
import com.cloud.manga.henryTing.consts.IdTokenS; 
import com.cloud.manga.henryTing.thread.ThreadImp; 
import com.cloud.manga.henryTing.thread.TouchS; 
import com.cloud.manga.henryTing.thread.TouchUnit; 
import com.cloud.manga.henryTing.thread.TouchDelayer; 
import com.cloud.manga.henryTing.infor.SlideCmds; 
import com.cloud.manga.henryTing.infor.FrameM; 
import com.cloud.manga.henryTing.infor.LabelGet; 
import com.cloud.manga.henryTing.infor.GeoGet; 
import com.cloud.manga.henryTing.infor.FrameInfor; 
import com.cloud.manga.henryTing.data.FileBS; 
import com.cloud.manga.henryTing.data.KAddressT; 
import com.cloud.manga.henryTing.data.KAddress; 
import com.cloud.manga.henryTing.data.PathGet; 
import com.cloud.manga.henryTing.data.KIdT; 
import com.cloud.manga.henryTing.data.KIdTS; 
import com.cloud.manga.henryTing.data.KPgS; 
import com.cloud.manga.henryTing.unit.ActionM; 
import com.cloud.manga.henryTing.unit.SDEBase; 
import com.cloud.manga.henryTing.unit.SDEError; 
import com.cloud.manga.henryTing.unit.CmdEnum; 
import com.cloud.manga.henryTing.unit.HolderEnum; 
import com.cloud.manga.henryTing.unit.Point; 
import com.cloud.manga.henryTing.holder.HolderM; 
import com.cloud.manga.henryTing.holder.HolderId; 
import com.cloud.manga.henryTing.tool.FileM; 


import java.io.IOException;
import org.json.JSONException;

public abstract class MangaBase implements TouchS.Actor<CmdM> {
	protected final ExecutorService _LFast = Executors.newSingleThreadExecutor();
	
	
	protected TouchDelayer<CmdM> _touchDelayer = TouchDelayer.create();
	protected final GeoGet _geoGet;
	protected final GUIInterface _guiInterface;
	
	protected ThreadImp _threadImp = null;
	
	protected boolean _lockAction = true;
	protected String _dirWork = null;
	protected String _dirSystem = null;
	protected KAddressM _KAddressM = null;
	
	protected IndexP _indexP = null, _indexP_t = null;
	protected FrameM _frameM = null, _frameM_t = null;
	protected HolderM _holderM = null, _holderM_t = null;
	protected Setting _setting = null;
	
	public MangaBase(GeoGet geoGet, GUIInterface guiInterface) {
		_geoGet = geoGet;
		_guiInterface = guiInterface;
	}
	

	protected void clearLocalCaches() {
		if (_holderM != null) { _holderM.clearAllMemory(); }
		ThreadLocal.forceClearCashes();
	}
	protected volatile ExceptionJoin eJoiner = new ExceptionJoin();
	@Override
	public synchronized void reportException(SDEBase e) {
		eJoiner.append(e, false);
	}


	protected synchronized void readExternalSettingFile() throws SDEBase { readExternalSettingFile(true); }
	protected synchronized void readExternalSettingFile(boolean isReleaseLock) throws SDEBase {
		_lockAction = true;
		String filePath = null;
		if (_setting != null) {
			filePath = _guiInterface.openAFile(_dirWork, _setting.getFileManagerList());
		} else {
			filePath = _guiInterface.openAFile(_dirWork, new String[0]);
		}
		if(filePath == null) {
			throw SDEError.cMangaBaseOpenAFile();
		}
		SettingAd settingAd = new SettingAd(_dirSystem, _dirWork);
		try {
			settingAd.parseJson(filePath);
		} catch (JSONException e) {
			throw SDEError.cMangaBaseReadFormat(filePath);
		}
		_setting = settingAd;
		Main.initialize(_setting);
		// you need to clear the memory in the holderM or those job in the threadLocal
		clearLocalCaches();
		if (isReleaseLock) { _lockAction = false; }
	}

	private synchronized void _initialize(String dirSystem, String dirWork) throws SDEBase {
		_lockAction = true;
		_dirWork = dirWork;
		_dirSystem = dirSystem;
		SettingAd settingAd = new SettingAd(_dirSystem, _dirWork);
		try {
			settingAd.parseJson();
			_setting = settingAd;
			Main.initialize(_setting);
		} catch (SDEBase e1) {
			boolean code = true;
			while (code) {
				code = false;
				try {
					readExternalSettingFile(false);
				} catch (SDEBase e2) {
					System.out.println(e2.toString());
					code = true;
				}
			}
		}
		_frameM_t = _setting.getDefaultFrame();
		if (_frameM_t == null) {
			throw SDEError.cMangaBaseFrameDefaultFrameNull();
		}
		if (!_frameM_t.isIniState()) {
			throw SDEError.cMangaBaseFrameTypeNotIni(_frameM_t._keyName);
		}
		_frameM = _frameM_t;
		_threadImp = ThreadImp.ptr();
		_KAddressM = KAddressM.ptr(_guiInterface, _dirWork);
		_touchDelayer._touchActor = this;
	}
	public synchronized void initializeWithOutFile(String dirSystem, String dirWork) throws SDEBase {
		_initialize(dirSystem, dirWork);
		_LFast.execute( new Runnable() { public void run() {
			try {
				_runCommand(ActionM.getNone());
			} catch (SDEBase e) {
				reportException(e);
			}
		}});
		_lockAction = false;
	}
	public synchronized void initializeWithFilePath(String dirSystem, String filePath) throws SDEBase {
		String dirWork = null;
		try {
			dirWork = FileM.dirname(filePath);
		} catch (IOException e) {
			reportException(SDEError.cMangaBasePathDir(filePath));
			_closeTheProgram();
		}
		_initialize(dirSystem, dirWork);	
		_LFast.execute( new Runnable() { public void run() {
			try {
				_runCommand(new ActionM(CmdEnum.TxtRead, filePath));
			} catch (SDEBase e) {
				reportException(e);
			}
		}});
		_lockAction = false;
	}
	protected void _closeTheProgram() {
		// ....
		if (!_frameM.isIniState()) { _holderM.close(); }
		clearLocalCaches();
		SDEBase error = eJoiner.append(null, true);
		if (error != null) {
			System.out.println(error.toString());
		}
		_guiInterface.closeTheProgram();
	}
	
	// ------------------------------------------------------------------------------------------ //
	
	// after change to the new frame holder type
	// the level will be increase so as to set a flag for TouchS 
	// so that we can stop the old touches for previous holder type
	protected volatile long _level = 0;
	
	
	public String getFName() {
		return _frameM._keyName;
	}
	
	// after a while, a set of touches will be passed
	@Override
	public void summitAction(List<CmdM> cmdMs) {
		if (cmdMs.size() == 0) { return; }
		Collections.sort(cmdMs);
		CmdM cmdM = cmdMs.get(0);
		if (cmdM == null) { return; }
		int count = 1;
		for (int ith=1; ith<cmdMs.size(); ith++) {
			if (!cmdM.equals(cmdMs.get(ith))) {
				break;
			}
			count++;
		}
		runActionM(cmdM.getActionM(count));
	}
	// following mrthods use single-thread protocol to maintain thread-safe 
	//  _frameM has two type of commands
	// one is click and action(parseClickCmdMs)
	// one is click several times and wait a while and action(parseCountCmdMs)
	protected void runActionM(ActionM actionM) {
		if (_lockAction) { return; }
		_LFast.execute( new Runnable() { public void run() {
			try { _runCommand(actionM); } catch (SDEBase e) {
				reportException(e);
			}
		}});
	}
	private void _runCommand(ActionM actionM) throws SDEBase {
		FileBS fileBS = null;
		LabelGet labelGet = null;
		try {
			_indexP_t = _indexP; _frameM_t = _frameM.getFrameM(); _holderM_t = _holderM;
			if (_frameM.isIniState()) {
				_runCommandSwitchIni(actionM);
			} else {
				SDEBase error = eJoiner.append(null, true);
				if (error != null) { throw error; }
				_threadImp.joinPrepareImage();
				_runCommandSwitch(actionM);
			}
			if (!_frameM.isIniState() && _frameM_t._FrameM_Dual == null) {
				KPgS kpgs = _holderM_t.getKPgS(_indexP_t);
				fileBS = ThreadWork.getImage(
					kpgs, _frameM_t.isOnePage() ? 1: 2
				);
				_threadImp.prepareImage(_indexP_t, _holderM_t.getIterUnit());
				labelGet = _holderM_t.getLabelGet(_indexP_t, kpgs);
			} else {
				String localPath = _frameM_t.getPathImage();
				if (localPath != null ) {
					while(fileBS == null) {
						try {
							fileBS = ThreadWork.getImage(localPath);
						} catch (SDEBase e) {
							if (_frameM.isIniState()) {
								localPath = _guiInterface.openAFile(_dirWork, _setting.getFileManagerList());
							} else {
								throw e;
							}
						}	
					}			
				}
			}
			_indexP = _indexP_t; _frameM = getFrameM(_frameM, _frameM_t); 
			if (_holderM != _holderM_t) {
				if (!_frameM.isIniState()) { // _holderM != null) {
					_holderM.close();
				}
				_holderM = _holderM_t;
			}
		} catch (SDEBase e) {
			labelGet = HolderM.getLabelGet(e.toString());
			_frameM_t = new FrameM(_frameM);
		}
		if (fileBS == null) {
			fileBS = new FileBS(_frameM_t.isOnePage()?1:2);
		}
		_updateScreen(_guiInterface.decodeImages(fileBS), labelGet);
	}
	
	private volatile LabelGet _labelGet = null;
	public void _updateScreen(ImageGet imageGet, LabelGet labelGet) {
		_labelGet = labelGet;
		_updateScreen(imageGet);
	}
	public void _updateScreen(ImageGet imageGet) {
		_guiInterface.updateScreen(
			new FrameInfor(
				_frameM_t,
				_geoGet,
				_labelGet,
				imageGet
			)
		);
	}
	
	private FrameM getFrameM(FrameM frameM, FrameM frameM_t) {
		if (frameM._FrameM_Dual.equals(frameM_t)) { return new FrameM(frameM, null); }
		return frameM_t;
	}
	
	private void _runCommandSwitchIni(ActionM actionM) throws SDEBase {
		System.out.println("_runCommandSwitchIni start");
		switch(actionM._cmdEnum) {
			case NextPage: case PrevPage: case NextChapter: case PrevChapter:
			case NextTwoPage: case PrevTwoPage: case GoFirst: case GoLast:
			case NextMark: case Show: case To: case MinusTo: case AddTo:
			case UpdateId: case UpdateIdS: case CreateId: case CreateIdS:
			case TxtLocalSearchCh: case TxtCloudSearchCh:
			throw SDEError.cMangaMainActionMNotSupport(actionM._cmdEnum.toString());
			default:
			break;
		}
		_runCommandSwitch(actionM);
	}
	private void _runCommandSwitch(ActionM actionM) {
		switch(actionM._cmdEnum) {
			// change page 
			case None: return;
			case Exit: _closeTheProgram(); return;
			case NextPage: _indexP_t = _holderM_t.getIterUnit().nextPage(_indexP_t, actionM._clickTimes); return;
			case PrevPage: _indexP_t = _holderM_t.getIterUnit().prevPage(_indexP_t, actionM._clickTimes); return;
			case NextChapter: _indexP_t = _holderM_t.getIterUnit().nextChapter(_indexP_t, actionM._clickTimes); return;
			case PrevChapter: _indexP_t = _holderM_t.getIterUnit().prevChapter(_indexP_t, actionM._clickTimes); return;
			case NextTwoPage: _indexP_t = _holderM_t.getIterUnit().nextPage(_indexP_t, actionM._clickTimes*2); return;
			case PrevTwoPage: _indexP_t = _holderM_t.getIterUnit().prevPage(_indexP_t, actionM._clickTimes*2); return;
			case GoFirst: _indexP_t = _holderM_t.getIterUnit().goFirst(_indexP_t); return;
			case GoLast: _indexP_t = _holderM_t.getIterUnit().goLast(_indexP_t); return;
			// change page (only for holderCh)
			case NextMark: _indexP_t = _holderM_t.nextMark(_indexP_t, actionM._clickTimes); return;
			case PrevMark: _indexP_t = _holderM_t.prevMark(_indexP_t, actionM._clickTimes); return;
			// change frame
			case Show: 
			_frameM_t = _setting.findFrameObj(actionM._arg);
			// it will be throw in the _frameM if it is not ini frame
			_frameM_t = _frameM.getFrameM(_frameM_t);
			return;
			case To:
			_frameM_t = _setting.findFrameObj(actionM._arg);
			// it will be throw in the _holderM_t if it is a ini frame
			_holderM_t.setFrameM(_frameM_t);
			return;
			case MinusTo:
			_frameM_t = _setting.findFrameObj(actionM._arg);
			_holderM_t.setFrameM(_frameM_t);
			_indexP_t = _holderM_t.getIterUnit().prevPage(_indexP_t, 1);
			return;
			case AddTo:
			_frameM_t = _setting.findFrameObj(actionM._arg);
			_holderM_t.setFrameM(_frameM_t);
			_indexP_t = _holderM_t.getIterUnit().nextPage(_indexP_t, 1);
			return;
			default: break;
		}
		switch(actionM._cmdEnum) {
			case UpdateId: 
			ThreadWork.updateOrCreateIdFolder(_holderM_t.getKIdT());
			return;
			case UpdateIdS: 
			_threadImp.iniIdFolders(_holderM_t.getKIdTS());
			return;
			case CreateId: {
				KIdT kidt = _holderM_t.getKIdT();
				ThreadWork.write(PathGet.get(kidt).IdJson(), new KAddress(kidt));
			}
			return;
			case CreateIdS: {
				for (KIdT kidt: _holderM_t.getKIdTS()) {
					ThreadWork.write(PathGet.get(kidt).IdJson(), new KAddress(kidt));
				}
			}
			return;
			default: break;
		}
		KAddress kad = null;
		switch(actionM._cmdEnum) {
			case TxtLocalHistory:  kad = _KAddressM.inputLocalHistory(); break;
			case TxtLocalRandom:   kad = _KAddressM.inputLocalRandom(); break;
			case TxtCloudHistory:  kad = _KAddressM.inputCloudHistory(); break;
			case TxtCloudRandom:   kad = _KAddressM.inputCloudRandom(); break;
			case TxtCloud0:        kad = _KAddressM.inputCloudFile(0); break;
			case TxtCloud1:        kad = _KAddressM.inputCloudFile(1); break;
			case TxtCloudFolder:   kad = _KAddressM.openCloudFolder(); break;
			case TxtCurrntFolder:  kad = _KAddressM.openLocalCurrent(); break;
			case TxtDefaultFolder: kad = _KAddressM.openLocalDefault(); break;
			case TxtLocalSearchId: kad = _KAddressM.openLocalId(); break;
			case TxtLocalSearchCh: kad = _KAddressM.openLocalCh(_holderM_t.getKIdT()); break;
			case TxtCloudSearchId: kad = _KAddressM.openCloudId(); break;
			case TxtCloudSearchCh: kad = _KAddressM.openCloudCh(_holderM_t.getKIdT()); break;
			case TxtRead: kad = _KAddressM.readKAddressbyPath(actionM._arg); break;
			default: break;
		}
		if (kad != null) {
			KAddressT kAddressT = IdTokenS.kAddressToT(kad);
			_threadImp.iniIdFolders(kAddressT.getKIdTS());
			HolderEnum holderType = null;
			if (kad._kidName == null) {
				holderType = HolderEnum.Id;
			} else if (kad._kchName == null) {
				holderType = HolderEnum.Ch;
			} else {
				holderType = HolderEnum.Pg;
			}
			if (_frameM_t._holderType != holderType) {
				FrameM t = null;
				switch (holderType) {
					case Id: t = _setting.getDefaultIdFrame(); break;
					case Ch: t = _setting.getDefaultChFrame(); break;
					case Pg:
					default: t = _setting.getDefaultPgFrame(); break;
				}
				if (t == null) {
					throw SDEError.cMangaMainNoDefaultFrame();
				}
				_frameM_t = t;
			}
			_holderM_t = new HolderM(new HolderId(kAddressT),_frameM_t);
			_indexP_t = _holderM_t.getIniIndexP();
			return;	
		}
		switch(actionM._cmdEnum) {
			case SetIni: readExternalSettingFile(); break;
			case SetPathUser:
			case SetCloudStorage:
			case SetLocalStorage:
			updateSettingPath(actionM._cmdEnum); break;
			default: break;
		}
	}
	protected void updateSettingPath(CmdEnum cmdEnum) {
		String filePath;
		_lockAction = true;
		filePath = _guiInterface.openAFile(_dirWork, _setting.getFileManagerList());
		try {
			filePath = FileM.dirname(filePath);
		} catch (IOException e) {
			throw SDEError.cMangaMainCannotAccessDirName(filePath);
		}
		switch (cmdEnum) {
			case SetPathUser: _setting.setPathUser(filePath); break;
			case SetCloudStorage: _setting.setCloudStorage(filePath); break;
			default: _setting.setLocalStorage(filePath); break;
		}
		clearLocalCaches();
		_lockAction = false;
	}
}
	
	