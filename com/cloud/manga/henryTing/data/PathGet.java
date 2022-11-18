package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/PathGet.java
*/

public class PathGet {
	private static String printMark_2 = "%s/%s";
	private static String printMark_3 = "%s/%s/%s";
	
	private static String txtToken = "token.json";
	private static String txtHistory = "history.json";
	private static String txtCover = "cover.json";
	private static String _markFolder = "zMark";
	public static Setting _setting;

	private String getPathLPicture(boolean isLocal) {
		if (isLocal) { return _setting.getPathLPicture(); }
		return _setting.getPathCPicture(); 
	}
	private String getPathLRecord(boolean isLocal) {
		if (isLocal) { return _setting.getPathLRecord(); }
		return _setting.getPathCRecord(); 
	}
	public String IdJson() {
		if (_isLocal) { return _setting.getPathLIdJson(); }
		return _setting.getPathCIdJson(); 
	}

	public final boolean _isLocal;
	private PathGet(int ith) { _isLocal = ith == 0? true: false; }
	private static PathGet[] _ptrs = new PathGet[] { null, null };
	public static PathGet get(LocalInfor infor) {
		int ith = infor.isLocal() ? 0: 1;
		
		assert infor.isLocal() || infor.isCloud(): 
			String.format(
			"infor.isLocal() || infor.isCloud()"
		);
		
		return get(ith);
	}
	private static PathGet get(int ith) {
		if (_ptrs[ith] == null) { _ptrs[ith] = new PathGet(ith); }
		return _ptrs[ith];
	}
	public static PathGet getCloud() { return get(1); }
	public static PathGet getLocal() { return get(0); }
	
	private static String join2(String left, String right) {
		return String.format(printMark_2, left, right);
	}
	private static String join3(String left, String mid, String right) {
		return String.format(printMark_3, left, mid, right);
	}
	private static String joinIdCh(KCh kch) {
		return join2(kch._kid._keyName, kch._keyName);
	}
	// {image} / {Id} / {ch} / {pg}
	public String Image() {
		return this.getPathLPicture(_isLocal);
	}
	public String Image(KId kid) {
		return join2(this.getPathLPicture(_isLocal), kid._keyName);
	}
	public String Image(KCh kch) {
		return join2(Image(kch._kid), kch._keyName);
	}
	public String Image(KPg kpg) {
		return join2(Image(kpg._kch._kid), kpg._keyName);
	}
	public static String FolderName(KCh kch, String keyName) {
		return join2(kch._keyName, keyName);
	}
	
	public String Record() {
		return this.getPathLRecord(_isLocal);
	}
	// {record} / Token.json
	// {record} / {Id} / Token.json
	// {record} / {Id} / {ch} / Token.json
	// public String RecordT() {
		// return join2(this.getPathLRecord(_isLocal), txtToken);
	// }
	private String _RecordT(String mid) {
		return join3(this.getPathLRecord(_isLocal), mid, txtToken);
	}
	public String RecordT(KId kid) {
		return _RecordT(kid._keyName);
	}
	public String RecordT(KCh kch) {
		return _RecordT(joinIdCh(kch));
	}
	
	// {record} / {Id} / History.json
	// {record} / {Id} / {mark} / History.json
	// {record} / {Id} / {ch} / History.json
	private String _RecordH(String mid) {
		return join3(this.getPathLRecord(_isLocal), mid, txtHistory);
	}
	public String RecordH(KId kid) {
		return _RecordH(kid._keyName);
	}
	public String RecordH(KId kid, String mark) {
		return _RecordH(join3(kid._keyName, _markFolder, mark));
	}
	public String RecordH(KCh kch) {
		return _RecordH(joinIdCh(kch));
	}
	
	// {record} / {Id} / Cover.json
	// {record} / {Id} / {mark} / Cover.json
	// {record} / {Id} / {ch} / Cover.json
	private String _RecordC(String mid) {
		return join3(this.getPathLRecord(_isLocal), mid, txtCover);
	}
	public String RecordC(KId kid) {
		return _RecordC(kid._keyName);
	}
	public String RecordC(KId kid, String mark) {
		return _RecordC(join3(kid._keyName, _markFolder, mark));
	}
	public String RecordC(KCh kch) {
		return _RecordC(joinIdCh(kch));
	}
}