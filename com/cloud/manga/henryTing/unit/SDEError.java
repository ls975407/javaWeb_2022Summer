package com.cloud.manga.henryTing.unit;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/unit/SDEError.java
*/

public class SDEError extends SDEBase {
	public SDEError(String infor) {
		super(SDEEnum.Error, infor);
	}
	
	private static class SDEErrorFormat extends SDEError {
		public SDEErrorFormat(String infor) {
			super("Format: " + infor);
		}
	}
	private static class SDEErrorSearch extends SDEError {
		public SDEErrorSearch(String infor) {
			super("Search: " + infor);
		}
	}
	private static class SDEErrorCode extends SDEError {
		public SDEErrorCode(String infor) {
			super("Code: " + infor);
		}
	}
	private static class SDEErrorIO extends SDEError {
		public SDEErrorIO(String infor) {
			super("IO: " + infor);
		}
	}
	private static class SDEErrorSystem extends SDEError {
		public SDEErrorSystem(String infor) {
			super("System: " + infor);
		}
	}
	private static class SDEErrorCloud extends SDEError {
		public SDEErrorCloud(String infor) {
			super("Cloud: " + infor);
		}
	}
	private static class SDEErrorUser extends SDEError {
		public SDEErrorUser(String infor) {
			super("User: " + infor);
		}
	}
	// .unit.Index??
	// indexP => indexId indexCh indexPg
	static public SDEError cIndexPTypeError(int value) {
		return new SDEErrorCode(String.format("cIndexPTypeError index = %d", value));
	}
	// .data.KCh2D
	// search kch data in kch2d
	static public SDEError cKCh2DGetIndexCh(String chName, int typeIndex) {
		return new SDEErrorSearch(String.format("cKCh2DGetIndexCh chName = %s typeIndex = %d", chName, typeIndex));
	}
	static public SDEError cKCh2DGetIndexCh(String chName) {
		return new SDEErrorSearch(join_2("cKCh2DGetIndexCh", "chName", chName));
	}
	static public SDEError cKCh2DFilterExistCh(String newName, String oldName) {
		return new SDEErrorSearch(String.format("cKCh2DGetIndexCh newName = %s oldName = %s", newName, oldName));
	}
	// .tool.ThreadLocal
	static public SDEError cLocalRead(String localPath) {
		return new SDEErrorIO(join_2("cLocalRead", "localPath", localPath));
	}
	
	static public SDEError cLocalWrite(String localPath) {
		return new SDEErrorIO(join_2("cLocalWrite", "localPath", localPath));
	}
	static public SDEError cLocalIsExist(String localPath) {
		return new SDEErrorIO(join_2("cLocalIsExist", "localPath", localPath));
	}
	static public SDEError cLocalListDirectory(String localPath) {
		return new SDEErrorIO(join_2("cLocalListDirectory", "localPath", localPath));
	}
	static public SDEError cLocalListFile(String localPath) {
		return new SDEErrorIO(join_2("cLocalListFile", "localPath", localPath));
	}
	static public SDEError cUncatchLCmd(int posi) {
		return new SDEErrorCode(join_2("ThreadLocal uncatched state", "posi", Integer.valueOf(posi).toString()));
	}
	static public SDEError cThreadLocalExecution(String infor) {
		return new SDEErrorSystem(join_2("cThreadLocalExecution", "infor", infor));
	}
	static public SDEError cThreadLocalInterrupted() {
		return new SDEErrorSystem("ThreadLocal is interrupted");
	}

	// .consts.IdTokenS
	static public SDEError cIdTokenSWriteFile(String localPath) {
		return new SDEErrorIO(join_2("cIdTokenSWriteFile", "localPath", localPath));
	}
	static public SDEError cIdTokenSkAddressToTTokenNotFound(String IdkeyName) {
		return new SDEErrorSearch(join_2("cIdTokenSkAddressToTTokenNotFound", "IdkeyName", IdkeyName));
	}
	static public SDEError cIdTokenSkAddressToTLocalNotFound(String IdkeyName) {
		return new SDEErrorSearch(join_2("cIdTokenSkAddressToTLocalNotFound", "IdkeyName", IdkeyName));
	}
	
	// .consts.CloudM
	static public SDEError cCloudMInitialize() {
		return new SDEErrorCloud("cCloudMInitialize");
	}
	static public SDEError cCloudMGetRemoteFile(String cloudPath) {
		return new SDEErrorCloud(join_2("cCloudMGetRemoteFile", "cloudPath", cloudPath));
	}
	static public SDEError cCloudMGetTokenS(Long node) {
		return new SDEErrorCloud(join_2("cCloudMGetTokenS", "node", node.toString()));
	}
	
	// .consts.ThreadWork
	static public SDEError cThreadWorkWriteAddJson(String error) {
		return new SDEErrorSystem(join_2("cThreadWorkWriteAddJson", "error", error));
	}
	static public SDEError cThreadWorkUrlIsNull(String method) {
		return new SDEErrorCode(join_2("cThreadWorkUrlIsNull", "method", method));
	}
	static public SDEError cThreadWorkReadKCh2DFile(String localPath) {
		return new SDEErrorIO(join_2("cThreadWorkReadKCh2DFile", "localPath", localPath));
	}
	static public SDEError cThreadWorkReadJson(String localPath) {
		return new SDEErrorFormat(join_2("cThreadWorkReadJson", "localPath", localPath));
	}
	static public SDEError cThreadWorkGetImageIsLocalNotExist(String localPath) {
		return new SDEErrorCode(join_2("cThreadWorkGetImageIsLocalNotExist", "localPath", localPath));
	}
	static public SDEError cThreadWorkGetDefaultKCh(String localPath) {
		return new SDEErrorCode(join_2("cThreadWorkGetDefaultKCh", "localPath", localPath));
	}
	static public SDEError cThreadWorkIdFolderLongNull(String keyName) {
		return new SDEErrorCode(join_2("cThreadWorkIdFolderLongNull", "keyName", keyName));
	}
	static public SDEError cThreadWorkGetKPgSFTExistNull(String localPath) {
		return new SDEErrorUser(join_2("cThreadWorkGetKPgSFTExistNull", "localPath", localPath));
	}
	
	// .consts.FrameM
	// static public SDEError cFrameMNoDefaultImage(String frameName) {
		// return new SDEErrorCode(join_2("cFrameMNoDefaultImage", "frameName", frameName));
	// }
	static public SDEError cFrameMTypeNotIni(String frameName) {
		return new SDEErrorUser(join_2("cFrameMTypeNotIni", "frameName", frameName));
	}
	
	// .holder.Holder
	static public SDEError cHolderFailToUpdateKPgS(String kpgKeyName) {
		return new SDEErrorCode(join_2("cHolderFailToUpdateKPgS", "kpgKeyName", kpgKeyName));
	}		
	static public SDEError cHolderFailToGetIndexP(String kpgKeyName) {
		return new SDEErrorSearch(join_2("cHolderFailToGetIndexP", "kpgKeyName", kpgKeyName));
	}	
	static public SDEError cHolderRecordNotMatch(String content) {
		return new SDEErrorUser(join_2("cHolderRecordNotMatch", "content", content));
	}
	// .holder.HolderM
	static public SDEError cHolderMNextMark(String holderName) {
		return new SDEErrorCode(join_2("cHolderMNextMark", "holderName", holderName));
	}
	static public SDEError cHolderMFrameTypeIni(String frameName) {
		return new SDEErrorUser(join_2("cHolderMFrameTypeIni", "frameName", frameName));
	}
	// .thread.TouchDelayer
	static public SDEError cTouchDelayerInterrupted() {
		return new SDEErrorSystem("TouchDelayer is Interrupted");
	}
	static public SDEError cTouchDelayerClose() {
		return new SDEErrorSystem("TouchDelayer receive close signal");
	}
	// .thread.ThreadImp 
	static public SDEError cThreadImpExecution(String method, String infor) {
		return new SDEErrorSystem(String.format("cThreadImpExecution method = %s infor = %s", method, infor));
	}
	static public SDEError cThreadImpInterrupted(String method) {
		return new SDEErrorSystem(join_2("cThreadImpInterrupted", "method", method));
	}
	static public SDEError cThreadImpPrepareImageKpgsIsLocal(String kpgKeyName) {
		return new SDEErrorSystem(join_2("cThreadImpInterrupted", "kpgKeyName", kpgKeyName));
	}
	static public SDEError cIniIdFoldersSizeZero() {
		return new SDEErrorCode("cIniIdFoldersSizeZero");
	}
	
	// .main.KAddressM
	static public SDEError cKAddressMOpenChKIdTOutOfState(String kidName) {
		return new SDEErrorUser(join_2("cKAddressMOpenChKIdTOutOfState", "kidName", kidName));
	}
	static public SDEError cKAddressMOpenLocalId(String localPath) {
		return new SDEErrorUser(join_2("cKAddressMOpenLocalId", "localPath", localPath));
	}
	static public SDEError cKAddressMLocalNameNull(String localPath) {
		return new SDEErrorUser(join_2("cKAddressMLocalNameNull", "localPath", localPath));
	}
	static public SDEError cKAddressMReadExtensionFail(String localPath) {
		return new SDEErrorUser(join_2("cKAddressMReadExtensionFail", "localPath", localPath));
	}
	static public SDEError cKAddressMToJSONObject(String content) {
		return new SDEErrorFormat(join_2("cKAddressMToJSONObject", "content", content));
	}
	static public SDEError cKAddressMParseJson(String content) {
		return new SDEErrorFormat(join_2("cKAddressMParseJson", "content", content));
	}
	static public SDEError cKAddressMInputLocalRandom() {
		return new SDEErrorFormat(join_2("cKAddressMInputLocalRandom", "reason", "size=0 no resource"));
	}
	static public SDEError cKAddressMInputCloudFileIndex(int bound, int index) {
		return new SDEErrorSearch(String.format("cKAddressMInputCloudFileIndex bound = %d index = %d", bound, index));
	}
	static public SDEError cKAddressMInputCloudFileFormat(String fileName) {
		return new SDEErrorFormat(join_2("cKAddressMInputCloudFileFormat", "fileName", fileName));
	}
	static public SDEError cKAddressMOpenAFile() {
		return new SDEErrorSystem(join_2("cKAddressMOpenAFile", "reason", "you didn't choose any file"));
	}
	static public SDEError cKAddressMOpenCloudFolder() {
		return new SDEErrorSearch(join_2("cKAddressMOpenCloudFolder", "reason", "size=0 no resource"));
	}
	
	// main.MangaBase
	static public SDEError cMangaBaseExecution(String method, String infor) {
		return new SDEErrorSystem(String.format("cMangaBaseExecution method = %s infor = %s", method, infor));
	}
	static public SDEError cMangaBaseInterrupted(String method) {
		return new SDEErrorSystem(join_2("cMangaBaseInterrupted", "method", method));
	}
	static public SDEError cMangaBaseFrameDefaultFrameNull() {
		return new SDEErrorUser(join_2("cMangaBaseFrameDefaultFrameNull", "reason", "cannot find default frame"));
	}
	static public SDEError cMangaBasePathDir(String localPath) {
		return new SDEErrorSystem(join_2("cMangaBasePathDir", "localPath", localPath));
	}
	static public SDEError cMangaBasePathBase(String localPath) {
		return new SDEErrorSystem(join_2("cMangaBasePathBase", "localPath", localPath));
	}
	static public SDEError cMangaBaseOpenAFile() {
		return new SDEErrorSystem(join_2("cMangaBaseOpenAFile", "reason", "you didn't choose any file"));
	}
	static public SDEError cMangaBaseReadFormat(String fileName) {
		return new SDEErrorFormat(join_2("cMangaBaseReadFormat", "fileName", fileName));
	}
	static public SDEError cMangaBaseFrameTypeNotIni(String frameName) {
		return new SDEErrorUser(join_2("cMangaBaseFrameTypeNotIni", "frameName", frameName));
	}
	static public SDEError cMangaMainNoDefaultFrame() {
		return new SDEErrorUser(join_2("cMangaMainNoDefaultFrame", "reason", "fail to find frame key value"));
	}
	// main.SettingAd
	static public SDEError cSettingAdParseJson(String reason) {
		return new SDEErrorFormat(join_2("cSettingAdParseJson", "reason", reason));
	}
	// main.MangaMain
	static public SDEError cMangaMainActionMNotSupport(String actionName) {
		return new SDEErrorUser(join_2("cMangaMainActionMNotSupport", "actionName", actionName));
	}
	static public SDEError cMangaMainCannotAccessDirName(String localPath) {
		return new SDEErrorSystem(join_2("cMangaMainCannotAccessDirName", "localPath", localPath));
	}
	static public SDEError cMangaMainHandleIndexPNull(String cmdEnum) {
		return new SDEErrorCode(join_2("cMangaMainHandleIndexPNull", "cmdEnum", cmdEnum));
	}
	static public SDEError cMangaBaseIniIndexP(String frameM) {
		return new SDEErrorUser(join_2("cMangaBaseIniIndexP", "frameM", frameM));
	}
	static public SDEError cMangaBaseOpenAFile(String dirWork) {
		return new SDEErrorUser(join_2("cMangaBaseOpenAFile", "dirWork", dirWork));
	}
	static public SDEError cMangaBaseFileExtension(String filename) {
		return new SDEErrorUser(join_2("cMangaBaseFileExtension", "filename", filename));
	}
}