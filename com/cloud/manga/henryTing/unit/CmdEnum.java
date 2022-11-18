package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/CmdEnum.java
*/
public enum CmdEnum {
	None,
    NextPage, PrevPage, 
	NextChapter, PrevChapter, 
	NextTwoPage, PrevTwoPage, 
	NextMark, PrevMark,
	Show, To, MinusTo, AddTo,
	TxtCloudHistory, TxtCloudRandom, 
	TxtLocalHistory, TxtLocalRandom, 
	TxtCloud0, TxtCloud1, TxtCloudFolder, 
	TxtCurrntFolder, TxtDefaultFolder,
	TxtCloudSearchId, TxtCloudSearchCh,
	TxtLocalSearchId, TxtLocalSearchCh,
	TxtRead,
	UpdateId, UpdateIdS,
	CreateId, CreateIdS,
	GoFirst, GoLast,
	SetIni, SetPathUser, SetCloudStorage, SetLocalStorage,
	Exit,
	Close, Nothing, SaveHistory
} 