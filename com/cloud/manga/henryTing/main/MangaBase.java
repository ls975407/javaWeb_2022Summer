package com.cloud.manga.henryTing.main;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/main/MangaBase.java
*/


import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;



import com.cloud.manga.henryTing.unit.ActionM; 
import com.cloud.manga.henryTing.unit.SDEBase; 
import com.cloud.manga.henryTing.unit.SDEError; 
import com.cloud.manga.henryTing.unit.HolderEnum; 
import com.cloud.manga.henryTing.unit.Point; 
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.SDEHandle;
import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.ExceptionJoin;
import com.cloud.manga.henryTing.unit.ActionM;
import com.cloud.manga.henryTing.unit.CmdEnum;
import com.cloud.manga.henryTing.unit.CmdAbcE;
import com.cloud.manga.henryTing.unit.Point;
import com.cloud.manga.henryTing.unit.Log;
import com.cloud.manga.henryTing.tool.ThreadLocal;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.infor.FrameM;
import com.cloud.manga.henryTing.thread.ThreadImp;
import com.cloud.manga.henryTing.thread.TouchS;
import com.cloud.manga.henryTing.holder.HolderM;
import com.cloud.manga.henryTing.holder.HolderId; 
import com.cloud.manga.henryTing.holder.HolderPg; 
import com.cloud.manga.henryTing.holder.HolderCh; 
import com.cloud.manga.henryTing.holder.HolderBase; 


import com.cloud.manga.henryTing.consts.ThreadWork; 
import com.cloud.manga.henryTing.consts.IdTokenS; 
import com.cloud.manga.henryTing.thread.ThreadImp; 
import com.cloud.manga.henryTing.thread.TouchS; 
import com.cloud.manga.henryTing.thread.TouchUnit; 
import com.cloud.manga.henryTing.thread.TouchDelayer; 
import com.cloud.manga.henryTing.infor.SlideCmds; 
import com.cloud.manga.henryTing.infor.FrameM; 
import com.cloud.manga.henryTing.infor.LabelGet; 
import com.cloud.manga.henryTing.infor.FrameInfor; 
import com.cloud.manga.henryTing.infor.GeoGet;
import com.cloud.manga.henryTing.infor.CmdM;
// import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.data.FileBS; 
import com.cloud.manga.henryTing.data.FileB; 
import com.cloud.manga.henryTing.data.KAddressT; 
import com.cloud.manga.henryTing.data.PathGet; 
import com.cloud.manga.henryTing.data.KIdT; 
import com.cloud.manga.henryTing.data.KIdTS; 
import com.cloud.manga.henryTing.data.KAddress;
import com.cloud.manga.henryTing.data.KPg; 
import com.cloud.manga.henryTing.data.KPgS; 


import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class MangaBase implements TouchS.Actor<CmdM> {
	public final static Log _log = new Log("MangaBase");
	protected final ExecutorService _LFast = Executors.newSingleThreadExecutor();
	
	
	private final TouchDelayer<CmdM> _touchDelayer = TouchDelayer.create();
	protected final GeoGet _geoGet;
	protected final GUIInterface _guiInterface;
	
	private ThreadImp _threadImp = null;
	
	private boolean _lockAction = true;
	private String _dirWork = null;
	private String _dirSystem = null;
	private KAddressM _KAddressM = null;
	
	private IndexP _indexP = null, _indexP_t = null;
	private FrameM _frameM = null, _frameM_t = null;
	private HolderM _holderM = null, _holderM_t = null;
	private Setting _setting = null;
	
	public MangaBase(GeoGet geoGet, GUIInterface guiInterface) {
		_geoGet = geoGet;
		_guiInterface = guiInterface;
	}
	

	protected void clearLocalCaches() {
		if (_holderM != null) { _holderM.clearAllMemory(); }
		try {
			ThreadLocal.forceSaveCashes();
		} catch (SDEBase e) {
			_log.w(e.toString());
		}
	}
	protected volatile ExceptionJoin eJoiner = new ExceptionJoin();
	@Override
	public synchronized void reportException(SDEBase e) {
		if (e instanceof SDEHandle) {
			_log.w("reportException SDEHandle", e.toString());
			return;
		}
		_log.w("reportException", e.toString());
		eJoiner.append(e, false);
	}
	public synchronized void reportExceptionOnScreen(SDEBase e) {
		if (e instanceof SDEHandle) {
			// _guiInterface.promptInformation(e.toString());
			return;
		}
		_log.p("_error_state start", e.toString());
		_error_state = true;
		e.printStackTrace();
		_guiInterface.updateScreen(
			new FrameInfor(
				new FrameM(_frameM),
				_geoGet,
				HolderM.getLabelGet(e.toString()),
				new FileBS()
			)
		);

	}


	protected synchronized void readExternalSettingFile() throws SDEBase { readExternalSettingFile(true); }
	protected synchronized void readExternalSettingFile(boolean isReleaseLock) throws SDEBase {
		_log.p("readExternalSettingFile");
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
		} catch (SDEBase e) {
			throw SDEError.cMangaBaseReadFormat(filePath + " " + e.toString());
		}
		_setting = settingAd;
		Main.initialize(_setting);
		// you need to clear the memory in the holderM or those job in the threadLocal
		clearLocalCaches();
		if (isReleaseLock) { _lockAction = false; }
	}

	private synchronized void _initialize(String dirSystem, String dirWork) throws SDEBase {
		// _log.p("_initialize");
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
		_initializeBack();
	}
	private synchronized void _initialize(SettingAd setting) throws SDEBase {
		
		_lockAction = true;
		_dirWork = setting._dirWork;
		_dirSystem = setting._dirSystem;
		SettingAd settingAd = new SettingAd(_dirSystem, _dirWork);
		try {
			settingAd.parseJson();
			_setting = settingAd;
			Main.initialize(_setting);
		} catch (SDEBase e1) {
			_guiInterface.promptInformation("fail to load setting file, use default one");
			_setting = setting;
			Main.initialize(_setting);
		}
		_initializeBack();
	}
	private void _initializeBack() {
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
		_log.p("initializeWithOutFile", dirSystem, dirWork);
		_initialize(dirSystem, dirWork);
		// get the image
		_fileBS = getTheImageByLoop(_frameM);
		_labelGet = HolderM.getLabelGet("");
		
		_updateScreen();
		_lockAction = false;
	}
	
	public synchronized void initializeWithOutFile(SettingAd _setting) throws SDEBase {
		_log.p("initializeWithOutFile _setting");
		_initialize(_setting);
		// get the image
		_fileBS = getTheImageByLoop(_frameM);
		
		_labelGet = HolderM.getLabelGet("");
		
		_updateScreen();
		_lockAction = false;
	}
	private void initializeWithByteDataBack(byte[] data) {
		initializeWithByteDataBack(FileM.bytes2String(data));
	}
	private void initializeWithByteDataBack(String data) {
		try {
			initializeWithByteDataBack(new JSONObject(data));
		} catch (JSONException e) {
			SDEBase e1 = SDEError.cKAddressMToJSONObject(data);
			_log.e("JSONException", e.toString());
			throw e1;
		}		
	}
	private void initializeWithByteDataBack(JSONObject json) {
		try {
			iniKAddressData(_KAddressM.readKAddress(json));
		} catch (SDEBase e) {
			_fileBS = getTheImageByLoop(_frameM);
			_labelGet = HolderM.getLabelGet("");
			_updateScreen();
			_lockAction = false;
			return;
		}
		_indexP = _indexP_t;  _frameM = _frameM_t; 
		if (_holderM != _holderM_t) {
			if (_holderM != null) { _holderM.close(); }
			_holderM = _holderM_t;
		}
		SDEBase error = null;
		try {
			error = _LFast.submit( new Callable<SDEBase>() { public SDEBase call() {
				try {
					_runCommand(new ActionM(CmdEnum.None, ""));
				} catch (SDEBase e1) {
					return e1;
				}
				return null;
			}}).get();
		} catch(InterruptedException e) {
			throw SDEError.cMangaBaseInterrupted("initializeWithByteData");
		} catch(ExecutionException e) {
			e.printStackTrace();
			throw SDEError.cMangaBaseExecution("initializeWithByteData", e.toString());
		}
		if(error != null) { throw error; }
		_lockAction = false;
	}
	public synchronized void initializeWithByteData(String dirSystem, String dirWork, byte[] data) throws SDEBase {
		_log.p("initializeWithByteData", dirSystem, dirWork);
		_initialize(dirSystem, dirWork);
		initializeWithByteDataBack(data);
	}
	public synchronized void initializeWithJsonData(String dirSystem, String dirWork, JSONObject json) throws SDEBase {
		_log.p("initializeWithByteData", dirSystem, dirWork);
		_initialize(dirSystem, dirWork);
		initializeWithByteDataBack(json);
	}
	// public synchronized void initializeWithByteData(SettingAd setting, byte[] data) throws SDEBase {
		// _log.p("initializeWithByteData setting");
		// _initialize(setting);
		// initializeWithByteDataBack(data);
	// }
	public synchronized void initializeWithFilePath(String dirSystem, String dirWorkPair, String filePath) throws SDEBase {
		_log.p("initializeWithFilePath", dirSystem, filePath);
		String dirWork = null;
		try {
			dirWork = FileM.dirname(filePath);
		} catch (IOException e) {
			// throw SDEError.cMangaBasePathDir(filePath);
			_log.w("SDEError.cMangaBasePathDir(filePath)", filePath);
			dirWork = dirWorkPair;
		}
		byte[] data;
		try {
			data = ThreadWork.readBinary(filePath);
		} catch (SDEBase e) {
			initializeWithOutFile(dirSystem, dirWork); return;
		}
		initializeWithByteData(dirSystem, dirWork, data);
	}
	protected void _closeTheProgram(boolean skip) {
		_log.p("_closeTheProgram");
		if (skip) { _guiInterface.closeTheProgram(); return; }
		if (_holderM != null) {
			// write history json
			_log.t("write history json");
			try {
				saveHistory(_setting.getTxtHistory(), _holderM, _indexP);
			} catch (SDEBase e) {
				_log.e(e.toString());
			}
		}
		// close the thread
		_log.t("start to close the threads");
		try {
			_touchDelayer.shutdown();
			_threadImp.shutdown();
			ThreadLocal.shutdown();
		} catch (SDEBase e) {
			_log.e(e.toString());
		}
		// collect exist exception
		SDEBase error = eJoiner.append(null, true);
		if (error != null) {
			_log.e(error.toString());
		}
		
		_guiInterface.closeTheProgram();
	}
	
	// ------------------------------------------------------------------------------------------ //
	
	public synchronized void sendATouch(TouchUnit<CmdM> touch) {
		if (_lockAction) { return; }
		if (!_frameM.isIniState()) {
			_threadImp.askJoinPrepareImage();
		}
		_touchDelayer.sendATouch(touch);
	}
	
	public synchronized void runClickActionM(CmdM cmdM) {
		_touchDelayer.sendASkip(++_level); 
		runActionM(cmdM.getActionM(1));
	}
	public synchronized void runClickActionM(ActionM actionM) {
		_touchDelayer.sendASkip(++_level); 
		runActionM(actionM);
	}
	
	// after change to the new frame holder type
	// the level will be increase so as to set a flag for TouchS 
	// so that we can stop the old touches for previous holder type
	protected volatile long _level = 0;
	protected volatile boolean _error_state = false;
	
	public FrameM getFrameM() {
		if (_error_state) {
			return new FrameM(_frameM);
		}
		return _frameM;
	}
	
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
	private void runActionM(final ActionM actionM) {
		if (_lockAction) { return; }
		_log.p("send ", actionM.toString());
		if (actionM._cmdEnum == CmdEnum.Nothing) { return; }
		_LFast.execute( new Runnable() { public void run() {
			try { _runCommand(actionM); } catch (SDEBase e) {
				reportExceptionOnScreen(e);
			}
		}});
	}
	
	private String[] openAFile() throws SDEBase {
		_log.p("openAFile");
		final String[] legalE = _setting.getLegalImageExtension();
		String localPath;
		while(true) {
			localPath = _guiInterface.openAFile(_dirWork, _setting.getFileManagerList());
			if (localPath == null) {
				_log.w("openAFile() null");
				throw SDEError.cMangaBaseOpenAFile(_dirWork);
			}
			for (String ex: legalE) {
				if (localPath.endsWith(ex)) {
					return new String[]{localPath, ex};
				}
			}
		}
	}
	private FileBS getTheImageByLoop(FrameM frameM) throws SDEBase {
		String localPath = frameM.getPathImage();
		if (localPath != null) {
			try {
				return ThreadWork.getImage(localPath);
			} catch (SDEBase e) {}
		}
		_log.t("getTheImageByLoop localPath", localPath);
		_log.p("getTheImageByLoop");
		{
			String[] names = ThreadWork.listFile(_dirSystem);
			List<String> nameNeed = new ArrayList<>();
			final String[] legalE = _setting.getLegalImageExtension();
			for (String name: names) {
				if (name.startsWith(frameM.getKeyName())) {
					for (String ex: legalE) {
						if (name.endsWith(ex)) {
							nameNeed.add(name);
						}
					}
				}
			}
			for (String name: nameNeed) {
				try {
					return ThreadWork.getImage(_dirSystem + "/" + name);	
				} catch (SDEBase e) {}
			}
		}
		FileBS fileBS = null;
		String[] bufs;
		bufs = openAFile();
		while(fileBS == null) {
			try {
				fileBS = ThreadWork.getImage(bufs[0]);
				break;
			} catch (SDEBase e) {
				bufs = openAFile();
			}	
		}
		String basename = FileM.basename(bufs[0]);
		if (basename == null) {
			throw SDEError.cMangaBasePathBase(bufs[0]);
		}
		if (bufs[1].startsWith(".")) {
			ThreadWork.write(_dirSystem+"/"+frameM.getKeyName()+bufs[1], fileBS.get(0)._bytes);
		} else {
			ThreadWork.write(_dirSystem+"/"+frameM.getKeyName()+"."+bufs[1], fileBS.get(0)._bytes);
		}
		return fileBS;	
	} 
	private void _runCommand(ActionM actionM) throws SDEBase {
		if (_error_state) {
			_log.t("_error_state end");
			_error_state = false;
			if (_fileBS != null) {
				_updateScreen(_fileBS);
			} else {
				_updateScreen();
			}
			return;
		}
		
		// check if there is any error before the cmd
		SDEBase error = eJoiner.append(null, true);
		if (error != null) { throw error; }
		
		// copy the state
		_indexP_t = _indexP; _frameM_t = _frameM.getFrameM(); _holderM_t = _holderM;
		
		FileBS fileBS=new FileBS(0); LabelGet labelGet; boolean isNotHandleRefreshScreen = true;
		// run command
		try {
			if (_frameM_t.isIniState()) {
				_runCommandSwitchIni(actionM);
			} else {
				_threadImp.joinPrepareImage();
				_runCommandSwitch(actionM);
			}
		} catch (SDEBase sdebase) {
			if (sdebase instanceof SDEHandle) {
				switch(sdebase._h_type) {
					case refreshScreen:
					isNotHandleRefreshScreen = false;
					break;
					default:
					throw sdebase;
				}
			} else { throw sdebase; }
		}
		// get the image
		_frameM_t = _getFrameM(_frameM, _frameM_t); 
		if (_frameM_t.isIniState()) {
			if (isNotHandleRefreshScreen) {
				fileBS = getTheImageByLoop(_frameM_t);
			}
			labelGet = HolderM.getLabelGet("");
		} else {
			if (_indexP_t == null) {
				handleIndexPNull(actionM);
				return;
			}
			// _indexP_t.print();
			KPgS kpgs = _holderM_t.getKPgS(_indexP_t);
			if (!_frameM_t.isOnePage() && kpgs.isOnePage()) {
				_guiInterface.promptInformation(_setting.getPIPagePNext());
				return;
			}
			if (isNotHandleRefreshScreen) {
				fileBS = ThreadWork.getImage(
					kpgs, _frameM_t.isOnePage() ? 1: 2
				);
			}
			labelGet = _holderM_t.getLabelGet(_indexP_t, kpgs);
			// _threadImp.prepareImage(_indexP_t, _holderM_t.getIterUnit());
			if (_holderM_t != null) {
				_threadImp.prepareImage(_indexP_t, _holderM_t.getIterUnit());
			}
		}

		// copy the state
		_indexP = _indexP_t;  _frameM = _frameM_t; 
		if (_holderM != _holderM_t) {
			if (_holderM != null) { _holderM.close(); }
			_holderM = _holderM_t;
		}
		if (isNotHandleRefreshScreen) {
			_updateScreen(fileBS, labelGet);
		} else {
			_updateScreenLabel(labelGet);
		}
	}
	private void handleIndexPNull(ActionM actionM) throws SDEBase {
		switch(actionM._cmdEnum) {
			case NextPage:
			_guiInterface.promptInformation(_setting.getPIPagePNext());
			return;
			case PrevPage: case MinusTo:
			_guiInterface.promptInformation(_setting.getPIPagePPrev());
			return;
			case NextChapter:
			_guiInterface.promptInformation(_setting.getPIPageCNext());
			return;
			case PrevChapter:
			_guiInterface.promptInformation(_setting.getPIPageCPrev());
			return;
			default:
			throw SDEError.cMangaMainHandleIndexPNull(actionM._cmdEnum.toString());
		}
	}
	private FrameM _getFrameM(FrameM frameM, FrameM frameM_t) {
		if (frameM._FrameM_Dual != null && frameM._FrameM_Dual.equals(frameM_t)) { return new FrameM(frameM, null); }
		return frameM_t;
	}
	
	
	private volatile LabelGet _labelGet = null;
	private volatile FileBS _fileBS = null;
	private void _updateScreenLabel(LabelGet labelGet) {
		_labelGet = labelGet;
		_guiInterface.updateScreenLabel(
			new FrameInfor(
				_frameM.getFrameM(),
				_geoGet,
				_labelGet,
				new FileBS()
			)
		);
	}
	private void _updateScreen(FileBS fileBS, LabelGet labelGet) {
		_fileBS = fileBS;
		_labelGet = labelGet;
		_updateScreen();
	}
	public void _updateScreen(FileBS fileBS) {
		_log.p("_updateScreen fileBS");
		_fileBS = fileBS;
		_guiInterface.updateScreen(
			new FrameInfor(
				_frameM.getFrameM(),
				_geoGet,
				_labelGet,
				fileBS
			)
		);
	}
	public void _updateScreen() {
		_log.p("_updateScreen");
		_guiInterface.updateScreen(
			new FrameInfor(
				_frameM.getFrameM(),
				_geoGet,
				_labelGet,
				_fileBS
			)
		);
	}
	

	
	private void _runCommandSwitchIni(ActionM actionM) throws SDEBase {
		if (_holderM == null || _indexP == null) {
			switch(actionM._cmdEnum) {
				case NextPage: case PrevPage: case NextChapter: case PrevChapter:
				case NextTwoPage: case PrevTwoPage: case GoFirst: case GoLast:
				case NextMark: case To: case MinusTo: case AddTo:
				case UpdateId: case UpdateIdS: case CreateId: case CreateIdS:
				case TxtLocalSearchCh: case TxtCloudSearchCh: case SaveHistory:
				throw SDEError.cMangaMainActionMNotSupport(actionM.toString());
				default:
				break;
			}
		}
		_runCommandSwitch(actionM);
	}
	
	// private final static ArrayCaches<IndexCh, IndexP> _FDSizeCaches = new ArrayCaches<>(2);
	private void _runCommandSwitch(ActionM actionM) throws SDEBase {
		switch(actionM._cmdEnum) {
			// change page 
			case None: return;
			case Exit:
			_closeTheProgram(false);
			throw SDEHandle.cMangaBaseRefreshScreen();
			case Close:
			_closeTheProgram(true);
			throw SDEHandle.cMangaBaseRefreshScreen();
			case NextPage: 
			_indexP_t = _holderM_t.getIterUnit().nextPage(_indexP_t, actionM._clickTimes);
			// if (_frameM_t._frameType == FrameEnum.dual && _frameM_t._holderType == HolderEnum.pg) {
				// IndexCh indexCh = _indexP_t.toPg().toIndexCh();
				// IndexP indexP = _FDSizeCaches.get();
				// if (indexP == null) {
					// indexP = _holderM_t.getIterUnit().goLast(_indexP_t);
					// _FDSizeCaches.set(indexCh, indexP);
				// }
				// if (indexP.equals(_indexP_t)) {
					// _indexP_t = _holderM_t.getIterUnit().nextPage(_indexP_t, 1);
				// }
			// }
			return;
			case PrevPage: 
			_indexP_t = _holderM_t.getIterUnit().prevPage(_indexP_t, actionM._clickTimes);
			return;
			case NextChapter: 
			_indexP_t = _holderM_t.getIterUnit().nextChapter(_indexP_t, actionM._clickTimes);
			return;
			case PrevChapter:
			_indexP_t = _holderM_t.getIterUnit().prevChapter(_indexP_t, actionM._clickTimes);
			return;
			case NextTwoPage: 
			_runCommandSwitch(new ActionM(CmdEnum.NextPage, actionM._arg, actionM._clickTimes*2));
			return;
			case PrevTwoPage:
			_runCommandSwitch(new ActionM(CmdEnum.PrevPage, actionM._arg, actionM._clickTimes*2));
			return;
			case GoFirst: 
			switch(_frameM_t.getFrameType()) {
				case single: case dual:
				_indexP_t = _holderM_t.getIterUnit().goFirst(_indexP_t);
				return;
				default: {
					CmdM cmdM = _frameM_t.parseCmdAbcE(CmdAbcE.PrevLeftPage);
					if (cmdM == null) {
						cmdM = _frameM_t.parseCmdAbcE(CmdAbcE.PrevUpPage);
						if (cmdM == null) {
							throw SDEError.cMangaMainActionMNotSupport(actionM.toString());
						}
					}
					_runCommandSwitch(cmdM.getActionM(1));
					return;
				}
			}
			case GoLast: 
			switch(_frameM_t.getFrameType()) {
				case single:
				_indexP_t = _holderM_t.getIterUnit().goLast(_indexP_t);
				return;
				case dual:
				_indexP_t = _holderM_t.getIterUnit().goLast(_indexP_t);
				_indexP_t = _holderM_t.getIterUnit().prevPage(_indexP_t, 1);
				return;
				default: {
					_indexP_t = _holderM_t.getIterUnit().goLast(_indexP_t);
					CmdM cmdM = _frameM_t.parseCmdAbcE(CmdAbcE.PrevLeftPage);
					if (cmdM == null) {
						cmdM = _frameM_t.parseCmdAbcE(CmdAbcE.PrevUpPage);
						if (cmdM == null) {
							throw SDEError.cMangaMainActionMNotSupport(actionM.toString());
						}
					}
					_runCommandSwitch(cmdM.getActionM(1));
					return;
				}
			}
			// change page (only for holderCh)
			case NextMark: {
				IndexP indexP_t = _holderM_t.nextMark(_indexP_t, actionM._clickTimes);
				if (indexP_t.equals(_indexP_t)) {
					throw SDEHandle.cMangaBaseRefreshScreen();
				}
				_indexP_t = indexP_t;
				return;
			}
			case PrevMark: {
				IndexP indexP_t = _holderM_t.prevMark(_indexP_t, actionM._clickTimes);
				if (indexP_t.equals(_indexP_t)) {
					throw SDEHandle.cMangaBaseRefreshScreen();
				}
				_indexP_t = indexP_t;
				return;
			}
			// change frame
			case Show: 
			_frameM_t = _setting.findFrameObj(actionM._arg);
			// it will be throw in the _frameM if it is not ini frame
			_frameM_t = _frameM.getFrameM(_frameM_t);
			return;
			case To: {
				_frameM_t = _setting.findFrameObj(actionM._arg);
				// it will be throw in the _holderM_t if it is a ini frame
				_indexP_t = _holderM_t.setFrameM(_frameM_t, _indexP_t);
				
				if (_frameM_t.getHolderType() != _frameM.getHolderType()) {
					boolean[] buf = new boolean[]{_frameM_t.isOnePage(), _frameM.isOnePage()};
					int count = 0;
					for (boolean b: buf) { if(b) { count++; }}
					if (count%2 == 0) {
						throw SDEHandle.cMangaBaseRefreshScreen();
					}
				}
				return;
			}
			case MinusTo:
			_frameM_t = _setting.findFrameObj(actionM._arg);
			_indexP_t = _holderM_t.setFrameM(_frameM_t, _indexP_t);
			_indexP_t = _holderM_t.getIterUnit().prevPage(_indexP_t, 1);
			return;
			case AddTo:
			_frameM_t = _setting.findFrameObj(actionM._arg);
			_indexP_t = _holderM_t.setFrameM(_frameM_t, _indexP_t);
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
				String localPath = PathGet.get(kidt).IdJson()+"/"+kidt._keyName+".json";
				ThreadWork.write(localPath, new KAddress(kidt));
			}
			return;
			case CreateIdS: {
				for (KIdT kidt: _holderM_t.getKIdTS()) {
					String localPath = PathGet.get(kidt).IdJson()+"/"+kidt._keyName+".json";
					ThreadWork.write(localPath, new KAddress(kidt));
				}
			}
			return;
			case SaveHistory: {
				// _KAddressM.inputLocalHistory(_holderM_t, _indexP_t);
				String localPath = _guiInterface.saveAFile(_setting.getLocalDefault(), _setting.getFileManagerList());
				if (localPath == null) {
					// SDEBase e = SDEError.cKAddressMOpenAFile();
					SDEBase e = SDEHandle.cKAddressMOpenAFile();
					_log.e("localPath == null", e.toString());
					throw e;
				}
				boolean notState = true;
				for (String ext: _setting.getLegalJsonExtension()) {
					if (localPath.endsWith(ext)) {
						notState = false;
						break;
					}
				}
				if (notState) {
					throw SDEError.cMangaBaseFileExtension(localPath);
				} 
				saveHistory(localPath, _holderM_t, _indexP_t);
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
			iniKAddressData(kad);
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
	
	private void iniKAddressData(KAddress kad) {
		KAddressT kAddressT = IdTokenS.kAddressToT(kad);
		_threadImp.iniIdFolders(kAddressT.getKIdTS());
		HolderEnum holderType = HolderEnum.Pg;
		if (kad._kidName == null) {
			holderType = HolderEnum.Id;
		} else if (kad._kchName == null) {
			holderType = HolderEnum.Ch;
		}
		if (_frameM_t.getHolderType() != holderType) {
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
		_holderM_t = new HolderM(new HolderId(kAddressT));
		_indexP_t = _holderM_t.setFrameM(_frameM_t);
		if (_indexP_t == null) {
			throw SDEError.cMangaBaseIniIndexP(_frameM_t.toString());
		}
	}
	
	protected void updateSettingPath(CmdEnum cmdEnum) {
		_log.p("updateSettingPath");
		String filePath;
		_lockAction = true;
		filePath = _guiInterface.openAFile(_dirWork, _setting.getFileManagerList());
		if (filePath == null) {
			_guiInterface.promptInformation("path == null");
			return;
		}
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

	public static void saveHistory(String localPath, HolderM holderM, IndexP indexP) throws SDEBase {
		HolderEnum holderType = holderM.getHolderType();
		KAddress ka;
		KPg kpg = holderM.getKPgS(indexP).toKPg();
		KIdT[] kidts = holderM.getKIdTS().toArray();
		switch(holderType) {
			case Id:
			ka = new KAddress(kidts, kpg._kch._kid); break;
			case Ch:
			ka = new KAddress(kidts, kpg._kch); break;
			case Pg:
			ka = new KAddress(kidts, kpg); break;
			default:
			throw new SDEError("holderM.getHolderType() == Ini");
		}
		// ka.print();
		// System.out.println(localPath);
		ThreadWork.write(localPath, ka);
	}
}
	
	