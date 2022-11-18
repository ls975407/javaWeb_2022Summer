package com.cloud.manga.henryTing.main;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/main/MangaMain.java
*/


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
import com.cloud.manga.henryTing.infor.CmdM; 
import com.cloud.manga.henryTing.infor.FrameInfor; 
import com.cloud.manga.henryTing.data.FileBS; 
import com.cloud.manga.henryTing.data.KAddressT; 
import com.cloud.manga.henryTing.data.KAddress; 
import com.cloud.manga.henryTing.data.PathGet; 
import com.cloud.manga.henryTing.data.KIdT; 
import com.cloud.manga.henryTing.data.KIdTS; 
import com.cloud.manga.henryTing.data.KPgS; 
import com.cloud.manga.henryTing.data.DateT; 
import com.cloud.manga.henryTing.unit.ActionM; 
import com.cloud.manga.henryTing.unit.SDEBase; 
import com.cloud.manga.henryTing.unit.SDEError; 
import com.cloud.manga.henryTing.unit.CmdEnum; 
import com.cloud.manga.henryTing.unit.HolderEnum; 
import com.cloud.manga.henryTing.unit.Point; 
import com.cloud.manga.henryTing.unit.CmdAbcE; 
import com.cloud.manga.henryTing.unit.Log;
import com.cloud.manga.henryTing.unit.FrameEnum;
import com.cloud.manga.henryTing.holder.HolderM; 
import com.cloud.manga.henryTing.holder.HolderBase; 
import com.cloud.manga.henryTing.holder.HolderId; 
import com.cloud.manga.henryTing.holder.HolderCh; 
import com.cloud.manga.henryTing.holder.HolderPg; 
import com.cloud.manga.henryTing.tool.FileM; 
import com.cloud.manga.henryTing.tool.ThreadLocal; 

import java.io.IOException;

public class MangaMain extends MangaBase {
	public final static Log _log = new Log("MangaMain");
	private static MangaMain ptr;
	public static MangaMain create(GeoGet geoGet, GUIInterface guiInterface) {
		if (ptr == null) {
			ptr = new MangaMain(geoGet, guiInterface);
		}
		return ptr;
	}
	public MangaMain(GeoGet geoGet, GUIInterface guiInterface) {
		super(geoGet, guiInterface);
		_log.p("create MangaMain");
		
		// ThreadWork._log.p_lock();
		// ThreadWork._log.t_lock();
		ThreadWork._log.lock();
		IdTokenS._log.lock();
		ThreadImp._log.lock();
		ThreadLocal._log.lock();
		HolderPg._log.lock();
		HolderCh._log.lock();
		HolderId._log.lock();
		HolderBase._log.lock();
		HolderM._log.lock();
		KAddressM._log.lock();
		DateT._log.lock();
	}
	
	// implements TouchS.TouchActor
	
	public boolean isPortrait() {
		return _geoGet.screenX() < _geoGet.screenY();
	}
	
	// TouchListener will use these methods
	public synchronized void sendAPoint(Point p1) {
		_log.p("sendAPoint", p1.toString());
		if (!isPortrait()) {
			if (getFrameM().getFrameType() == FrameEnum.dual) {
				p1 = new Point(_geoGet.screenY()-p1._y, _geoGet.screenX()-p1._x);
			} else {
				p1 = new Point(p1._y, p1._x);
			}
		} else {
			p1 = new Point(p1._x, _geoGet.screenY()-p1._y);
		}
		FrameM frameM = getFrameM();
		CmdM cmdM = frameM.parseClickCmdMs(p1, _geoGet);
		if (cmdM == null) {
			cmdM = frameM.parseCountCmdMs(p1, _geoGet);
			if (cmdM == null) {
				return;
			}
			sendATouch(new TouchUnit<CmdM>(cmdM, _level));
			return;
		}
		runClickActionM(cmdM.getActionM(1));
	}
	// SlideListener will use these methods
	// public synchronized void slideUp()  {
		// _log.p("slideUp");
		// runClickActionM(getFrameM().getSlideCmds().Up());
	// }
	// public synchronized void slideDown()  {
		// _log.p("slideDown");
		// runClickActionM(getFrameM().getSlideCmds().Down());
	// }
	// public synchronized void slideLeft()  {
		// _log.p("slideLeft");
		// runClickActionM(getFrameM().getSlideCmds().Left());
	// }
	// public synchronized void slideRight() {
		// _log.p("slideRight");
		// runClickActionM(getFrameM().getSlideCmds().Right());
	// }

	public synchronized void sendASlide(Point p1, Point p2) {
		_log.p(String.format("sendASlide %s->%s", p1.toString(),p2.toString()));
		if (!isPortrait()) {
			if (getFrameM().getFrameType() == FrameEnum.dual) {
				int tx = _geoGet.screenX();
				int ty = _geoGet.screenY();
				p1 = new Point(ty-p1._y, tx-p1._x);
				p2 = new Point(ty-p2._y, tx-p2._x);
			} else {
				p1 = new Point(p1._y, p1._x);
				p2 = new Point(p2._y, p2._x);
			}
		} else {
			int t = _geoGet.screenY();
			p1 = new Point(p1._x, t-p1._y);
			p2 = new Point(p2._x, t-p2._y);
		}
		ActionM actionM = getFrameM().parseSlideActionM(p1, p2, _geoGet);
		if (actionM == null) { return; }
		runClickActionM(actionM);
	}

	public synchronized void sendACmd(CmdAbcE cmdAbcE) {
		_log.p("sendACmd", cmdAbcE.toString());
		CmdM cmdM = CmdM.parseClickCmdMs(cmdAbcE);
		if (cmdM == null) {
			cmdM = CmdM.parseCountCmdMs(cmdAbcE);
			if (cmdM == null) {
				cmdM = getFrameM().parseCmdAbcE(cmdAbcE);
				if (cmdM == null) {
					_log.w("sendACmd not support", cmdAbcE.toString());
					_guiInterface.promptInformation(
						String.format("%s not support", cmdAbcE.toString())
					);
					return;
				}
				if (cmdM._isClickAction) {
					runClickActionM(cmdM);
					return;
				}				
			}
			sendATouch(new TouchUnit<CmdM>(cmdM, _level));
			return;
		}
		runClickActionM(cmdM);
	}

}