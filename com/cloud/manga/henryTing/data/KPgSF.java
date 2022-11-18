package com.cloud.manga.henryTing.data;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/data/KPgSF.java
*/

public class KPgSF extends TokenUS {
	public final KCh _kch;
	private final Boolean[] _isExists;
	public KPgSF(KCh kch, TokenUS tokenUS) {
		super(tokenUS);
		_kch = kch;
		_isExists = new Boolean[size()];
		for (int ith=0; ith<_isExists.length; ith++) {
			_isExists[ith] = null;
		}
	}
	public KPgSF(KCh kch, TokenUS tokenUS, boolean isExist) {
		super(tokenUS);
		_kch = kch;
		_isExists = new Boolean[size()];
		for (int ith=0; ith<_isExists.length; ith++) {
			_isExists[ith] = isExist;
		}
	}
	
	public KPgSF(KCh kch, TokenUS tokenUS, TokenUS isExists) {
		super(tokenUS);
		_kch = kch;
		_isExists = new Boolean[size()];
		TokenU tokenU, tokenUExist;
		int jth = 0;
		final int size_ = isExists.size();
		for (int ith=0; ith<_isExists.length; ith++) {
			tokenU = get(ith);
			if (jth < size_) {
				tokenUExist = isExists.get(jth);
				if (tokenU.equals(tokenUExist)) {
					jth++;
					_isExists[ith] = Boolean.valueOf(true);
					continue;
				} 
			}
			_isExists[ith] = Boolean.valueOf(false);
		}
	}
	public KPg getKPg(int index) { return new KPg(_kch, _tokenUs[index], _isExists[index]); }
	public boolean update(KPg kpg) {
		if (!_kch.equals(kpg._kch)) {
			System.out.println(String.format("kch not the same %s %s", _kch._keyName, kpg._kch._keyName));
			return false;
		}
		int index = super.findIndex(kpg._tokenU._keyName);
		if (index < 0) {
			System.out.println(String.format("index < 0 %s", kpg._tokenU._keyName));
			return false;
		}
		_isExists[index] = kpg._isExist;
		return true;
	}
}