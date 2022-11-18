package com.cloud.manga.henryTing.consts;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/consts/CloudM.java
*/

import com.cloud.manga.henryTing.data.KeyStringT;
import com.cloud.manga.henryTing.data.TokenS;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.Log;

import com.pcloud.sdk.ApiClient;
import com.pcloud.sdk.ApiError;
import com.pcloud.sdk.Authenticators;
import com.pcloud.sdk.DataSink;
import com.pcloud.sdk.PCloudSdk;
import com.pcloud.sdk.RemoteEntry;
import com.pcloud.sdk.RemoteFile;
import com.pcloud.sdk.RemoteFolder;
import com.pcloud.sdk.FileLink;
import com.pcloud.sdk.DownloadOptions;

import java.io.IOException;
import java.net.URL;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import com.cloud.manga.henryTing.data.Token;
import com.cloud.manga.henryTing.data.GetUrl;
import com.cloud.manga.henryTing.data.TokenS;
import com.cloud.manga.henryTing.data.TokenU;
import com.cloud.manga.henryTing.data.TokenUS;
import com.cloud.manga.henryTing.data.TokenSUS;

import java.nio.charset.StandardCharsets;

public class CloudM {
	
	public static Setting _setting;
	public final static Log _log = new Log("CloudM");
	private ApiClient _apiClient = null;
	
	private CloudM() throws SDEBase {
		_log.r("newClientBuilder");
		String token = _setting.getCloudToken();
		_apiClient = PCloudSdk.newClientBuilder()
			.authenticator(Authenticators.newOAuthAuthenticator(token))
			.create();
		if (_apiClient == null) {
			SDEBase e = SDEError.cCloudMInitialize();
			_log.e("_apiClient == null", e.toString());
			throw e;
		}
	}
	private static CloudM _ptr = null;
	synchronized public static CloudM ptr() throws SDEBase {
		if (_ptr == null) {
			_ptr = new CloudM();
		}
		return _ptr;
	}
	// public URL getURL(Long node) throws SDEBase {
		// try {
			// return _apiClient.createFileLink(node, DownloadOptions.DEFAULT).execute().bestUrl();
		// } catch(IOException | ApiError e) {
			// throw SDEError.cCloudMGetRemoteFile(node.toString());
		// }
	// }
	public URL getURL(RemoteFile resource) throws SDEBase {
		_log.r("getURL", resource.name());
		try {
			return resource.createFileLink().bestUrl();
		} catch(IOException | ApiError e1) {
			SDEBase e = SDEError.cCloudMGetRemoteFile(resource.name());
			_log.e("getURL", e.toString());
			throw e;
		}
	}
	
	public class Infor implements GetUrl {
		public final Long _id;
		public final  RemoteFile _remoteFile;
		Infor(RemoteFile remoteFile) {
			_id = null; _remoteFile = remoteFile;
		}
		Infor(Long v) {
			_id = Long.valueOf(v); _remoteFile = null;
		}
		public boolean isFile() {
			if (_id != null) { return true; } return false;
		}
		public boolean isDir() {
			return !isFile();
		}
		Token toToken(String name) {
			return new Token(_id, name);
		}
		TokenU toTokenU(String name) {
			return new TokenU(name, this);
		}
		public URL getUrl() throws SDEBase {
			return CloudM.this.getURL(_remoteFile);
		}
	}
	
	List<List<KeyStringT<Infor>>> getTokenS(Long node) throws SDEBase {
		_log.r("getTokenS", node.toString());
		List<RemoteEntry> remotes;
		try {
			remotes = _apiClient.listFolder(node).execute().children();
		} catch(IOException | ApiError e1) {
			SDEBase e = SDEError.cCloudMGetTokenS(node);
			_log.e("getTokenS", e.toString());
			throw e;
		}
		final int size = remotes.size();
		RemoteEntry remote; KeyStringT<Infor> keyStringT;
		List<List<KeyStringT<Infor>>> reLists = new ArrayList<>();
		reLists.add(new ArrayList<KeyStringT<Infor>>());
		reLists.add(new ArrayList<KeyStringT<Infor>>());
        for(int ith=0; ith<size; ith++) {
			remote = remotes.get(ith);
            // System.out.println(remote.name() + "\t----------");
			
			// if (remote.name().equals("002.jpg")) {
				// downloadFile("./" + remote.name(), getURL(remote.asFile()));
			// }
			
            if(remote.isFolder()) {
				keyStringT = new KeyStringT<Infor>(
					new String(remote.name().getBytes(), StandardCharsets.UTF_8), 
					// remote.name(),
					new Infor(remote.asFolder().folderId())
				);
				reLists.get(0).add(keyStringT);
			} else if (remote.isFile()){
				keyStringT = new KeyStringT<Infor>(
					new String(remote.name().getBytes(), StandardCharsets.UTF_8),  
					// remote.name(),
					new Infor(remote.asFile())
				);
				reLists.get(1).add(keyStringT);
			}
        }
		return reLists;
	}
	
	static TokenS toTokenS(List<KeyStringT<Infor>> data) {
		Token[] buf = new Token[data.size()]; int ith=0;
		for (KeyStringT<Infor> kinfor: data) {
			buf[ith++] = kinfor._value.toToken(kinfor._keyName);
		}
		return new TokenS(buf, true);
	}
	static TokenUS toTokenUS(List<KeyStringT<Infor>> data) {
		TokenU[] buf = new TokenU[data.size()]; int ith=0;
		for (KeyStringT<Infor> kinfor: data) {
			buf[ith++] = kinfor._value.toTokenU(kinfor._keyName);
		}
		return new TokenUS(buf, true);
	}
	
	public TokenSUS getTokenSAll(Long node) throws SDEBase {
		List<List<KeyStringT<Infor>>> lists = getTokenS(node);
		Collections.sort(lists.get(0)); 
		Collections.sort(lists.get(1)); 
		return new TokenSUS(
			toTokenS(lists.get(0)),
			toTokenUS(lists.get(1))
		);
	}
	public TokenS getTokenSFolder(Long node) throws SDEBase {
		List<List<KeyStringT<Infor>>> lists = getTokenS(node);
		Collections.sort(lists.get(0)); 
		return toTokenS(lists.get(0));
	}
	public TokenUS getTokenUSFile(Long node) throws SDEBase {
		List<List<KeyStringT<Infor>>> lists = getTokenS(node);
		Collections.sort(lists.get(1)); 
		return toTokenUS(lists.get(1));
	}
	
	public TokenS getTokenSFolder(Token token) throws SDEBase {
		return getTokenSFolder(token._key);
	}
	public TokenUS getTokenUSFile(Token token) throws SDEBase {
		return getTokenUSFile(token._key);
	}
}