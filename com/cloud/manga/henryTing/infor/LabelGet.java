package com.cloud.manga.henryTing.infor;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/infor/LabelGet.java
*/

import com.cloud.manga.henryTing.data.KPg;
import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.SDEBase;

public interface LabelGet {
	KPg KPg();
	int Len() throws SDEBase;
	IndexP IndexP();
	String Error();
}