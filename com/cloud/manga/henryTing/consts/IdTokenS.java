package com.cloud.manga.henryTing.consts;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/consts/IdTokenS.java
*/
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import com.cloud.manga.henryTing.data.TokenS;
import com.cloud.manga.henryTing.data.Token;
import com.cloud.manga.henryTing.tool.FileM;
import com.cloud.manga.henryTing.data.TokenS;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.data.KId;
import com.cloud.manga.henryTing.data.Token;
import com.cloud.manga.henryTing.data.KIdT;
import com.cloud.manga.henryTing.data.KAddress;
import com.cloud.manga.henryTing.data.KAddressT;
import com.cloud.manga.henryTing.data.PathGet;
import com.cloud.manga.henryTing.unit.Log;

public class IdTokenS extends TokenS {
	public static Setting _setting;
	public final static Log _log = new Log("IdTokenS");
	
	IdTokenS(TokenS tokenS) {
		super(tokenS);
	}
	
	private static IdTokenS _ptr = null;
	static public IdTokenS ptr() throws SDEBase {
		return ptr(false);
	}
	static public IdTokenS ptr(boolean isReDownload) throws SDEBase {
		// _log.t("ptr 01");
		if (_ptr == null) {
			try {
				String localPath = _setting.getPathTokenS();
				_log.r("FileM.readJsonObject", "localPath", localPath);
				_ptr = new IdTokenS(
					TokenS.parseJson(
						FileM.readJsonObject(
							localPath
						)
					)
				);
			} catch (JSONException e) {
				_log.w("FileM.readJsonObject", e.toString());
			}
		}
		// _log.t("ptr 02");
		if (_ptr == null || isReDownload) {
			_log.r("ReDownload");
			_ptr = new IdTokenS(CloudM.ptr().getTokenSFolder(_setting.getNode()));
			_ptr.write();
		}
		// _log.t("ptr 03");
		return _ptr;
	}

	private void write() throws SDEBase { write(_setting.getPathTokenS()); }
	private void write(String localPath) throws SDEBase {
		_log.r("FileM.write", "localPath", localPath);
		JSONObject json = new JSONObject();
		try {
			addJson(json);
			FileM.write(localPath, json.toString());
		} catch (JSONException | java.io.IOException e1) {
			SDEBase e = SDEError.cIdTokenSWriteFile(localPath);
			_log.e("FileM.write", e.toString());
			throw e;
		}
	}
	
	public KIdT find(KId kid) throws SDEBase {
		Token token = find(kid._keyName);
		if (token == null) {
			return null;
		}
		return new KIdT(token);
	}

	public static KAddressT kAddressToT(KAddress kAddress) throws SDEBase {
		_log.p("kAddressToT");
		List<KIdT> kidt_local = new ArrayList<KIdT>();
		for (int ith=0; ith<kAddress.size(); ith++) {
			Token token = kAddress.getToken(ith);
			if (token.isLocal()) { kidt_local.add(new KIdT(token)); }
		}
		if (kidt_local.size() > 0) {
			kidt_local = ThreadWork.filterEle(
				kidt_local,
				ThreadWork.isExistFolder(PathGet.get(kidt_local.get(0)).Image(), kidt_local),
				false
			);
			if (kidt_local.size() != 0) {
				SDEBase e = SDEError.cIdTokenSkAddressToTLocalNotFound(
					new KAddress(kidt_local.toArray(new KIdT[0]))._keyName
				);
				_log.e("kidt_local is not exist", e.toString());
				throw e;
			}
		}
		
		List<KIdT> kidt_cloud = new ArrayList<KIdT>();
		boolean isNotOnce = false;
		isNotOnce = true;
		Long[] reArray = new Long[kAddress.size()];
		Token tokenAd; Token token;
		for (int ith=0; ith<kAddress.size(); ith++) {
			tokenAd = kAddress.getToken(ith);
			if (tokenAd.isLocal() || tokenAd.isCloud()) {
				reArray[ith] = tokenAd._key;
				continue;
			}
			// System.out.println("find " + kidt._keyName  + kidt._key);
			
			token = ptr().find(tokenAd._keyName);
			if (token == null && isNotOnce) {
				reArray[ith] = null;
				kidt_cloud.add(new KIdT(tokenAd));
				continue;
			}
			if (token == null) {
				isNotOnce = true;
				token = ptr(true).find(tokenAd._keyName);
				if (token == null) {
					reArray[ith] = null;
					kidt_cloud.add(new KIdT(tokenAd));
					// _log.w("kidt_cloud is not exist");
					continue;
				}
			}
			reArray[ith] = token._key;
		}
		if (kidt_cloud.size() != 0) {
			SDEBase e = SDEError.cIdTokenSkAddressToTTokenNotFound(
				new KAddress(kidt_cloud.toArray(new KIdT[0]))._keyName
			);
			_log.e("kidt_cloud is not exist", e.toString());
			throw e;
		}
		return new KAddressT(kAddress, reArray);
	}
}