package com.cloud.manga.henryTing.test;
/*
javac -encoding utf8  com/cloud/manga/henryTing/test/MangaBaseDual.java
*/

import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.infor.CmdM;
import com.cloud.manga.henryTing.unit.ActionM;
import com.cloud.manga.henryTing.unit.SDEBase;

import com.cloud.manga.henryTing.thread.TouchDelayer;
import com.cloud.manga.henryTing.thread.TouchS;
import com.cloud.manga.henryTing.thread.TouchUnit;

import com.cloud.manga.henryTing.main.GUIInterface;


import java.util.List;
import java.util.Collections;

public class MangaBaseDual implements TouchS.Actor<CmdM> {
	public final TouchDelayer<CmdM> _touchDelayer = TouchDelayer.create();
	protected GUIInterface _guiInterface;
	
	protected synchronized void _initialize(GUIInterface guiInterface) {
		_touchDelayer._touchActor = this;
		_guiInterface = guiInterface;
	}
	
	public void reportException(SDEBase e) {}
	
	// after change to the new frame holder type
	// the level will be increase so as to set a flag for TouchS 
	// so that we can stop the old touches for previous holder type
	protected volatile long _level = 0;
	
	public void _closeTheProgram() {
		TouchUnit<CmdM> touchUnit = new TouchUnit<CmdM>(null, 0);
		touchUnit.setActiveOff();
		_touchDelayer.sendATouch(touchUnit);
		_touchDelayer.shutdown();
		_guiInterface.closeTheProgram();
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
		try {
			runActionM(cmdM.getActionM(count));
		} catch (SDEBase e) {
			System.out.println(e.toString());
		}
	}
	
	public void runActionM(ActionM actionM) {
		actionM.print();
		System.out.println("");
	}
	
	public void _updateScreen(ImageGet imageGet) {
		// guiInterface.updateScreen();
	}
}