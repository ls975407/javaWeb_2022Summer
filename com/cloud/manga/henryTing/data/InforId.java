package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/InforId.java
*/

import com.cloud.manga.henryTing.unit.Log;

public class InforId {
	public final static Log _log = new Log("InforId");
	public final KId _kid;
	public final String _name;
	public final int _id;
	public InforId(KId kid) {
		_kid = kid; 
		String folderName = kid._keyName;
		String[] tokens = folderName.split("_");
		if (tokens.length < 2) {
			_log.w("illegal format", "tokens.length < 2");
			_name = folderName; _id = 9999; return;
		}
		int id_1; //, id_2;
		try {
			id_1 = Integer.parseInt(tokens[0]);
		} catch (NumberFormatException e) {
			_log.w("illegal format", "NumberFormatException");
			_name = folderName; _id = 9999; return;
		}
		// if (id_1 != id_2) {
			// _name = folderName; _id = 9999; return;
		// }
		_name = tokens[1]; _id = id_1;
	}
}