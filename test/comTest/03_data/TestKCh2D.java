/*
javac -encoding utf8  TestKCh2D.java
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

public class TestKCh2D {
	public static boolean createTestFile(String testFileName) {
		Long node = Long.valueOf("11008705442");
		TokenS tokenS = null;
		try {
			tokenS = CloudM.ptr().getTokenSFolder(node);
		} catch (SDEBase e) {
			System.out.println("fail to CloudM.ptr().getTokenSFolder");
			return false;
		}
		try {
			FileM.write(testFileName, tokenS.toString());
		} catch (IOException e) {
			System.out.println("fail to FileM.write");
			return false;
		}
		return true;
	}
		
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
		String testFileName = "testKCh2DTokenS.txt";
		String testFileNameOut = "testKCh2DTokenSOut.txt";
		String content = null;
		try {
			content = FileM.readString(testFileName);
		} catch (IOException e1) {
			if(!createTestFile(testFileName)) {
				System.out.println("fail to createTestFile");
				return;
			}
			try {
				content = FileM.readString(testFileName);
			} catch (IOException e2) {
				System.out.println("fail to FileM.readString " + e2.toString());
				return;
			}
		}
		TokenS tokenS = TokenS.create(content);
		if (tokenS == null) {
			System.out.println("fail to TokenS.create ");
			return;
		}
		KCh2D kch2d = KCh2D.create(new KIdT(new KId("kid")), tokenS);
		try {
			FileM.write(testFileNameOut, kch2d.toString());
		} catch (IOException e) {
			System.out.println("fail to FileM.write " + e.toString());
			return;
		}
	}
}