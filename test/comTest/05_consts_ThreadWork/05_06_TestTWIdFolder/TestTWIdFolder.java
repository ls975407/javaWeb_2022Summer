/*
javac -encoding utf8  TestTWIdFolder.java
*/

import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.tool.ThreadLocal;
import com.cloud.manga.henryTing.main.SettingAd;
import com.cloud.manga.henryTing.main.Setting;


import com.cloud.manga.henryTing.data.Token;
import com.cloud.manga.henryTing.data.KIdT;
import com.cloud.manga.henryTing.data.KId;
import com.cloud.manga.henryTing.data.PathGet;
import com.cloud.manga.henryTing.data.KeyStringBase;
import com.cloud.manga.henryTing.data.KAddress;
import com.cloud.manga.henryTing.data.KAddressT;
import com.cloud.manga.henryTing.data.KCh2D;


import com.cloud.manga.henryTing.consts.ThreadWork;
import com.cloud.manga.henryTing.consts.IdTokenS;
import com.cloud.manga.henryTing.tool.FileM;

import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;
import org.json.JSONArray;

public class TestTWIdFolder {	
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
	// java TestTWIdFolder local 0
	public static void main(String[] arg) {
		if (arg.length != 2) {
			println("error arg.length != 2");
			for (int ith=0; ith<arg.length; ith++) {
				println(String.format("arg %02d: %s\n", ith, arg[ith]));
			}
			return;
		}
		final String filePath = arg[0].equals("local")? "testLocal.json": "testCloud.json";
		final int methodChoose = Integer.parseInt(arg[1]);
		
		Setting setting = initailizeSetting();
		if (setting == null) {
			System.out.println("fail to initailizeSetting");
			return;
		}
		com.cloud.manga.henryTing.consts.Consts.initialize(setting);
		com.cloud.manga.henryTing.data.Data.initialize(setting);


		KAddress KAddress_t = null;
		KAddressT KAddressT_t = null;
		String content;
		try {
			content = FileM.readString(filePath);
		} catch(IOException e) {
			println(String.format("fail to read filePath = %s", filePath)); return;
		}
		try {
			KAddress_t = KAddress.parseJson(new JSONObject(content));
		} catch (JSONException e) {
			println(String.format("fail to read KAddress_t %s", e.toString())); return;
		}
		// KAddress_t.print();
		KAddressT_t = IdTokenS.kAddressToT(KAddress_t);
		KIdT kidt = KAddressT_t.get(0);
		switch(methodChoose) {
			case 0:
			println("iniIdFolder");
			ThreadWork.iniIdFolder(kidt);
			break;
			case 1:
			println("updateIdFolder");
			ThreadWork.updateIdFolder(kidt);
			break;
			default:
			println("updateOrCreateIdFolder");
			ThreadWork.updateOrCreateIdFolder(kidt);
			break;
		}
		ThreadLocal.shutdown();
	}
}