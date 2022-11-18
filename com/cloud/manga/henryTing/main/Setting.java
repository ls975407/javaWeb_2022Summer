package com.cloud.manga.henryTing.main;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/main/Setting.java
*/

import com.cloud.manga.henryTing.infor.FrameM;

public interface Setting  
 extends com.cloud.manga.henryTing.data.Setting
 ,com.cloud.manga.henryTing.consts.Setting
 ,com.cloud.manga.henryTing.infor.Setting
 ,com.cloud.manga.henryTing.holder.Setting
 ,com.cloud.manga.henryTing.thread.Setting
{
	// KAddreddM
	int getRandomIdNum();
	String getTxtHistory();
	Long getNodeCPicture();
	Long getNodeCJson();
	String getPathLPicture();
	String getPathSynJson();
	String[] getLegalJsonExtension();
	String[] getFileManagerList();
	String getPathSynCId();
	String getPathSynLId();
	String getPathSynCCh();
	String getPathSynLCh();
	String getLocalDefault();
	// Manga
	FrameM findFrameObj(String keyString);
	FrameM getDefaultFrame();
	FrameM getDefaultIdFrame();
	FrameM getDefaultChFrame();
	FrameM getDefaultPgFrame();
	String getPIPagePNext();
	String getPIPagePPrev();
	String getPIPageCNext();
	String getPIPageCPrev();

	void setPathUser(String localPath);
	void setCloudStorage(String localPath);
	void setLocalStorage(String localPath);
} 