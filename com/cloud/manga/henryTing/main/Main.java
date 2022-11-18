package com.cloud.manga.henryTing.main;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/main/Main.java
*/

public class Main {
	public static void initialize(Setting setting) {
		com.cloud.manga.henryTing.data.Data.initialize(setting);
		com.cloud.manga.henryTing.consts.Consts.initialize(setting);
		com.cloud.manga.henryTing.infor.Infor.initialize(setting);
		com.cloud.manga.henryTing.holder.Holder.initialize(setting);
		com.cloud.manga.henryTing.thread.Thread.initialize(setting);
		
		KAddressM._setting = setting;
	}
}