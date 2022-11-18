package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/GetUrl.java
*/
import java.net.URL;
import com.cloud.manga.henryTing.unit.SDEBase;

public interface GetUrl {
	URL getUrl() throws SDEBase;
}