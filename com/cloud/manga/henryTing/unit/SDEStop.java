package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/SDEStop.java
*/

public class SDEStop extends SDEBase {
	public SDEStop(String infor) {
		super(SDEEnum.Stop, infor);
	}
	static public SDEStop cThreadLocal() {
		return new SDEStop("ThreadLocal");
	}
	static public SDEStop cThreadImpIniIdFolders() {
		return new SDEStop("cThreadImpIniIdFolders");
	}
}