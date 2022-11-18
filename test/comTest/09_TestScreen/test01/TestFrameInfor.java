/*
javac -encoding utf8  TestFrameInfor.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;

import com.cloud.manga.henryTing.infor.FrameInfor;
import com.cloud.manga.henryTing.infor.GeoGet;
import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.infor.LabelGet;
import com.cloud.manga.henryTing.infor.FrameM;
import com.cloud.manga.henryTing.data.KPg;
import com.cloud.manga.henryTing.data.FileB;
import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.SDEBase;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

public class TestFrameInfor {
	public static Setting initailizeSetting() {
		final String path = "MangaResouceIni.json";
		String content = null;
		try {
			content = FileM.readString(path);
		} catch (IOException e) {
			println(e.toString()); return null;
		}
		JSONObject json = null;
		SettingAd settingAd = new SettingAd(".", ".");
		try {
			json = new JSONObject(content);
			settingAd.parseJson(json);
		} catch (JSONException e) {
			println(e.toString()); return null;
		}
		return settingAd;
	}
	public static void println(String content) {
		System.out.println(content);
	}
	
	private static class GeoGetAd implements GeoGet {
		final int _x, _y;
		GeoGetAd(int x, int y) { _x = x; _y = y; }
		public int screenX() { return _x; }
		public int screenY() { return _y; }
	}
	private static class ImageGetAd implements ImageGet {
		final int _x, _y, _s;
		ImageGetAd(int x, int y, int s) { _x = x; _y = y; _s = s; }
		public int lenX(int index) { return _x; }
		public int lenY(int index) { return _y; }
		public int size() { return _s; }
		public FileB Bytes(int index) { return null; }
	}
	private static class LabelGetAd implements LabelGet {
		public KPg KPg() { return KPg.testCreate(); }
		public int Len() throws SDEBase { return 999; }
		public IndexP IndexP() { return new IndexP(0); }
		public String Error() { return "Error"; }
	}
	// java TestFrameInfor 600 900 1404 1872 Setting01Single
	public static void main(String[] arg) {
		if (arg.length != 5) {
			println("Error: arg.length() != 5");
			for (int ith=0; ith<arg.length; ith++) {
				println(String.format("%d: %s", ith, arg[ith]));
			}
			return;
		}
		
		Setting setting = initailizeSetting();
		com.cloud.manga.henryTing.infor.Infor.initialize(setting);
		GeoGet geoGet = new GeoGetAd(Integer.parseInt(arg[0]), Integer.parseInt(arg[1]));
		FrameM frameM = setting.findFrameObj(arg[4]);
		if (frameM == null) {
			println("Error: frameM == null frameM=" + arg[4]);
			return;
		}
		ImageGet imageGet = new ImageGetAd(Integer.parseInt(arg[2]), Integer.parseInt(arg[3]),frameM.isOnePage()? 1: 2);
		LabelGetAd labelGet = new LabelGetAd();
		
		FrameInfor frameInfor = new FrameInfor(frameM, geoGet, labelGet, imageGet);
		println(frameInfor.toString());
	}
}