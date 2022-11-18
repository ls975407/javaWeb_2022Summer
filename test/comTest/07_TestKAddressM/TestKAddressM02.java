/*
javac -encoding utf8  TestKAddressM02.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;
import com.cloud.manga.henryTing.main.KAddressM;


import com.cloud.manga.henryTing.data.KAddress;
import com.cloud.manga.henryTing.data.TokenS;
import com.cloud.manga.henryTing.data.FileBS;
import com.cloud.manga.henryTing.tool.FileM;

import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.SDEBase;

import com.cloud.manga.henryTing.main.GUIInterface;
import com.cloud.manga.henryTing.infor.ImageGet;
import com.cloud.manga.henryTing.infor.FrameInfor;
import com.cloud.manga.henryTing.consts.ThreadWork;



import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

public class TestKAddressM02 {	

	public static class GUIAd implements GUIInterface {
		public String openAFile(String openPath, String[] appList) throws SDEBase {
			System.out.println(String.format("localPath = %s", openPath));
			return openPath + "/input01.json";
		}
		public ImageGet decodeImages(FileBS fileBS) throws SDEBase {
			throw new SDEError("GUIInterface decodeImages");
		}
		public void closeTheProgram() {
			throw new SDEError("GUIInterface closeTheProgram");
		}
		public void updateScreen(FrameInfor frameInfor) throws SDEBase {
			throw new SDEError("GUIInterface updateScreen");
		}
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
		com.cloud.manga.henryTing.main.Main.initialize(setting);
		
		KAddressM kaddressm = KAddressM.ptr(new GUIAd(), ".");
		// kaddressm.inputCloudFile(1).print();
		kaddressm.openCloudFolder().print();
	}
}