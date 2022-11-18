package com.cloud.manga.henryTing.main;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/main/GUIInterface.java
*/

import com.cloud.manga.henryTing.unit.SDEBase;
// import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.infor.FrameInfor;
import com.cloud.manga.henryTing.data.FileBS;

public interface GUIInterface {
	String openAFile(String openPath, String[] appList) throws SDEBase;
	String saveAFile(String openPath, String[] appList) throws SDEBase;
	// ImageGet decodeImages(FileBS fileBS) throws SDEBase;
	void closeTheProgram();
	void updateScreen(FrameInfor frameInfor) throws SDEBase;
	void updateScreenLabel(FrameInfor frameInfor) throws SDEBase;
	void promptInformation(String infor) throws SDEBase;
} 