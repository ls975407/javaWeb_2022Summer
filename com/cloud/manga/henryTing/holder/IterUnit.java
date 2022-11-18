package com.cloud.manga.henryTing.holder;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/holder/IterUnit.java
*/
import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.data.KPgS;
import com.cloud.manga.henryTing.data.LocalInfor;
public interface IterUnit extends LocalInfor {
	IndexP nextPage(IndexP indexP, int count) throws SDEBase;
	IndexP nextChapter(IndexP indexP, int count) throws SDEBase;
	IndexP prevPage(IndexP indexP, int count) throws SDEBase;
	IndexP prevChapter(IndexP indexP, int count) throws SDEBase;
	IndexP pnextPage(IndexP indexP) throws SDEBase;
	IndexP pnextChapter(IndexP indexP) throws SDEBase;
	IndexP pprevPage(IndexP indexP) throws SDEBase;
	IndexP pprevChapter(IndexP indexP) throws SDEBase;
	IndexP goFirst(IndexP indexP) throws SDEBase;
	IndexP goLast(IndexP indexP) throws SDEBase;
	KPgS getKPgS(IndexP indexP) throws SDEBase;
	void saveKPgS(IndexP indexP, KPgS kpgs) throws SDEBase;
	void clearKPgS(IndexP indexP, KPgS kpgs) throws SDEBase;
	void resetKPgS(IndexP indexP, KPgS kpgs) throws SDEBase;
}