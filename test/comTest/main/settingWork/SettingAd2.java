	// 雲端放照片的地方
	private Long _nodeCPicture;
	public Long getNodeCPicture() { return _nodeCPicture; }
	// 雲端放json的地方
	private Long _nodeCJson;
	public Long getNodeCJson() { return _nodeCJson; }
	
	// 本機存 Id轉json的地方(同步)
	private String _pathSynLId;
	public String getPathSynLId() { return _dirUser + _pathSynLId; }
	private String _pathSynCId;
	public String getPathSynCId() { return _dirUser + _pathSynCId; }
	// 本機存 Ch轉json的地方(同步)
	private String _pathSynLCh;
	public String getPathSynLCh() { return _dirUser + _pathSynLCh; }
	private String _pathSynCCh;
	public String getPathSynCCh() { return _dirUser + _pathSynCCh; }
	// 本機存 雲端json的地方(同步)
	private String _pathSynJson;
	public String getPathSynJson() { return _dirUser + _pathSynJson; }
	
	// 本機 創建單一json的地方
	private String _pathLIdJson;
	public String getPathLIdJson() { return _dirUser + _pathLIdJson; }
	private String _pathCIdJson;
	public String getPathCIdJson() { return _dirUser + _pathCIdJson; }
	
	// 本機 放照片的地方
	private String _pathLPicture;
	public String getPathLPicture() { return _dirStorage + _pathLPicture; }
	private String _pathCPicture;
	public String getPathCPicture() { return _dirStorage + _pathCPicture; }
	
	// 本機放置token 歷史的地方
	private String _pathLRecord;
	public String getPathLRecord() { return _dirStorage + _pathLRecord; }
	private String _pathCRecord;
	public String getPathCRecord() { return _dirStorage + _pathCRecord; }
	
	// 本機放置預設圖片的地方
	private String _pathLFImage;
	public String getPathLFImage() { return _dirWork + _pathLFImage; }
	
	// 本機的歷史文件檔
	private String _txtLHistory;
	public String getTxtLHistory() { return _dirUser + _txtLHistory; }
	private String _txtCHistory;
	public String getTxtCHistory() { return _dirUser + _txtCHistory; }
	
	//字體類型(0~7)
	private String _fontTypes = new String[8];
	public String getFontTypes(int index) { return _fontTypes[index]; }
	//字體大小(px)(0~7)
	private int[] _fontSizes = new int[8];
	public int getFontSizes(int index) { return _fontSize[index]; }
	//字體縮放值
	private double _fontScaleValue;
	public double getFontScaleValue() { return _fontScaleValue; }
	
	
	// 歷史所存的Id數量
	private int _numHistoryId;
	public int getNumHistoryId() { return _numHistoryId; }
	// 下載執行緒數量
	private int _numThread;
	public int getNumThread() { return _numThread; }
	// 下一章對映的張數
	private int _numUnitCh;
	public int getNumUnitCh() { return _numUnitCh; }
	// 預先下載下一章幾次
	private int _numLoopCh;
	public int getNumLoopCh() { return _numLoopCh; }
	// 預先下載下一頁幾次
	private int _numLoopPg;
	public int getNumLoopPg() { return _numLoopPg; }
	// 螢幕的X尺寸
	private int _screenX;
	public int getScreenX() { return _screenX; }
	// 螢幕的Y尺寸
	private int _screenY; 
	public int getScreenY() { return _screenY; }
	
	// 表格 chapterMark 對 類型
	Map<String, String> _tChMarkName;
	public String findChMarkName(String key) { return _tChMarkName.get(key); }
	// 表格 chapterMark 對 權重
	Map<String, Integer> _tChMarkOrder;
	public int findChMarkOrder(String key) { return _tChMarkOrder.get(key); }
	// 表格 frameName 對 frame obj
	Map<String, FrameM> _tFrameObj;
	public FrameM findFrameObj(String key) {
		FrameM fm = _tFrameObj.get(key);
		if (fm == null) { return null; }
		return fm.getFrameM();
	}
	
	private void parseJson(JSONObject json) throws JSONException {
		_dirWork = json.getString("DirWork");
		_dirStorage = json.getString("DirStorage");
		_dirUser = json.getString("DirUser");
		
		_pathCPicture = json.getString("PathCPicture");
		_pathCJson = json.getString("PathCJson");
		_pathLCIdJson = json.getString("PathLCIdJson");
		_pathLCChJson = json.getString("PathLCChJson");
		_pathLCJson = json.getString("PathLCJson");
		_pathLIdJson = json.getString("PathLIdJson");
		_pathLPicture = json.getString("PathLPicture");
		_pathLRecord = json.getString("PathLRecord");
		_pathLShowPicture = json.getString("PathLShowPicture");
		_txtLHistory = json.getString("TxtLHistory");
		_pathLFImage = json.getString("PathLFImage");
		
		_numHistoryId = json.getInt("NumHistoryId");
		_numThread = json.getInt("NumThread");
		_numUnitCh = json.getInt("NumUnitCh");
		_numLoopCh = json.getInt("NumLoopCh");
		_numLoopPg = json.getInt("NumLoopPg");
		_screenX = json.getInt("ScreenX");
		_screenY = json.getInt("ScreenY");
		
		
		
		_aLegalImageExtension = _iniAS2S(json.getJSONArray("ALegalImageExtension"));
		_tChMarkName = _iniTS2S(json.getJSONArray("TChMarkName"));
		_tChMarkOrder = _iniTS2I(json.getJSONArray("TChMarkOrder"));
		_tFrameObj = _iniTS2F(json.getJSONArray("TFrameObj"));
	}
	

	