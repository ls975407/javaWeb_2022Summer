package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/ImageGet.java
*/

import com.cloud.manga.henryTing.data.FileB;
public interface ImageGet {
	int size();
	int lenX(int index);
	int lenY(int index);
	FileB Bytes(int index);
} 

