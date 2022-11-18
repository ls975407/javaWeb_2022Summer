package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/SDEHandle.java
*/

public class SDEHandle extends SDEBase {
	public SDEHandle(SDEHEnum type, String infor) {
		super(SDEEnum.Handle, type, infor);
	}

	// .thread.TouchDelayer
	static public SDEHandle cTouchDelayerClose() {
		return new SDEHandle(
			SDEHEnum.close, 
			"cTouchDelayerClose"
		);
	}
	// .consts.ThreadWork
	static public SDEHandle cThreadWorkDownloadFile(String localPath) {
		return new SDEHandle(
			SDEHEnum.download, 
			join_2("cThreadWorkDownloadFile", "localPath", localPath)
		);
	}
	// .consts.ThreadWork
	static public SDEHandle cNone() {
		return new SDEHandle(
			SDEHEnum.none, 
			""
		);
	}
	// .main.KAddressM
	static public SDEHandle cKAddressMOpenAFile() {
		return new SDEHandle(
			SDEHEnum.openAFile, 
			"cMangaBaseOpenAFile"
		);
	}
	// .main.MangaBase
	static public SDEHandle cMangaBaseOpenAFile() {
		return new SDEHandle(
			SDEHEnum.openAFile, 
			"cMangaBaseOpenAFile"
		);
	}
	static public SDEHandle cMangaBaseRefreshScreen() {
		return new SDEHandle(
			SDEHEnum.refreshScreen, 
			"cMangaBaseRefreshScreen"
		);
	}
}