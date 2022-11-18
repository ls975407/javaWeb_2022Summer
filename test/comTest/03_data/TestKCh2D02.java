/*
javac -encoding utf8  TestKCh2D02.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;

import com.cloud.manga.henryTing.infor.FrameInfor;
import com.cloud.manga.henryTing.infor.GeoGet;
import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.infor.LabelGet;
import com.cloud.manga.henryTing.infor.FrameM;
import com.cloud.manga.henryTing.data.TokenS;
import com.cloud.manga.henryTing.data.KCh2D;
import com.cloud.manga.henryTing.data.KId;
import com.cloud.manga.henryTing.data.KIdT;
import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.unit.SDEBase;

import com.cloud.manga.henryTing.consts.CloudM;
import com.cloud.manga.henryTing.tool.FileM;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

public class TestKCh2D02 {
	public static Setting initailizeSetting() {
		final String path = "IniSetting_ver01.json";
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
	
	public static void main(String[] arg) {
		Setting setting = initailizeSetting();
		if (setting == null) {
			System.out.println("fail to initailizeSetting");
			return;
		}
		com.cloud.manga.henryTing.consts.Consts.initialize(setting);
		com.cloud.manga.henryTing.data.Data.initialize(setting);
		String testFileNameOut = "testKCh2DTokenSOut.txt";
		String testFileNameOutSmall = "testKCh2DTokenSOutSmall.txt";
		String testFileNameOutMinus = "testKCh2DTokenSOutMinus.txt";
		String testFileNameOutMinusTest = "testKCh2DTokenSOutMinusTest.txt";
		String content_01 = null;
		String content_02 = null;
		try {
			content_01 = FileM.readString(testFileNameOut);
		} catch (IOException e) {
			System.out.println("fail to FileM.readString 01 " + e.toString());
			return;
		}
		try {
			content_02 = FileM.readString(testFileNameOutSmall);
		} catch (IOException e) {
			System.out.println("fail to FileM.readString 02 " + e.toString());
			return;
		}
		KCh2D kch2d_01 = KCh2D.create(new KIdT(new KId("kid")), content_01);
		KCh2D kch2d_02 = KCh2D.create(new KIdT(new KId("kid")), content_02);
		KCh2D ttt = null;
		// try {
			// ttt = kch2d_01;
			// FileM.write("test__bud.txt", ttt.toString());
		// } catch (IOException e) {
			// System.out.println("ttt " + e.toString());
			// return;
		// }
		// try {
			// ttt = KCh2D.filterExistChInfor(kch2d_01, kch2d_02);
			// if (ttt == null) {
				// System.out.println("kch2d_01, kch2d_02 => null");
			// } else {
				// FileM.write(testFileNameOutMinus, ttt.toString());
			// }
		// } catch (IOException e) {
			// System.out.println("fail to FileM.write " + e.toString());
			// return;
		// }
		try {
			ttt = KCh2D.filterExistChInfor(kch2d_02, kch2d_01);
			if (ttt == null) {
				System.out.println("kch2d_02, kch2d_01 => null");
			} else {
				FileM.write(testFileNameOutMinusTest, ttt.toString());
			}
		} catch (IOException e) {
			System.out.println("fail to FileM.write " + e.toString());
			return;
		}
	}
}