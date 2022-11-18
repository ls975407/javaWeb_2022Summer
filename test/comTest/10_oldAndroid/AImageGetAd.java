package com.student.tool.MangaServer;
/*
javac -encoding utf8  com/cloud/manga/henryTing/test/ImageGetAd.java
*/


import android.graphics.BitmapFactory;

import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.data.FileB;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class AImageGetAd implements ImageGet {

	final int _size;
	final int[] _lenX;
	final int[] _lenY;
	final FileBS _fileBS;
	public ImageGetAd(FileBS fileBS) throws IOException {
		_fileBS = fileBS;
		_size = fileBS.size();
		_lenX = new int[_size];
		_lenY = new int[_size];
		FileB fileB = null;
		for (int ith=0; ith<_size; ith++) {
			fileB = _fileBS.get(ith);
			if (fileB == null) {
				_lenX[ith] = 0;
				_lenY[ith] = 0;
				continue;
			}
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(fileB._bytes, 0, fileB._bytes.length, option);
			_lenX[ith] = option.outWidth;
			_lenY[ith] = option.outHeight;
		}
	}
	public int size() { return _size; }
	public int lenX(int index) { return _lenX[index]; }
	public int lenY(int index) { return _lenY[index]; }
	public FileB Bytes(int index) { return _fileBS.get(index); }
}