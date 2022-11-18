package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/FileB.java
*/
import com.cloud.manga.henryTing.tool.FileM;

public class FileB {
	public final byte[] _bytes;
	public FileB(byte[] bytes) {
		_bytes = bytes;
	}
	public String toTxt() throws java.io.IOException {
		return FileM.bytes2String(_bytes);
	}
}