package com.cloud.manga.henryTing.tool;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/tool/ThreadLocal.java
*/
import com.cloud.manga.henryTing.unit.Log;
import com.cloud.manga.henryTing.unit.ResultEn;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.SDEStop;
import com.cloud.manga.henryTing.unit.ExceptionJoin;
import com.cloud.manga.henryTing.tool.FileM;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class ThreadLocal {
	public final static Log _log = new Log("ThreadLocal");
	enum EJobL {
		read, write, isexist, listdirectory, listfile
	}
	
	static class InforLocalJob {
		final EJobL _type;
		final byte[] _bytes;
		final Boolean _isexist;
		final String _localPath;
		final String[] _dirName;
		private InforLocalJob(EJobL type, byte[] bytes, Boolean isexist, String localPath, String[] dirName) {
			_type = type;
			_bytes = bytes;
			_isexist = isexist;
			_localPath = localPath;
			_dirName = dirName;
		}
		static InforLocalJob cWrite(String localPath, byte[] bytes) {
			return new InforLocalJob(EJobL.write, bytes, true, localPath, null);
		}
		static InforLocalJob cWriteResult() {
			return new InforLocalJob(EJobL.write, null, null, null, null);
		}
		static InforLocalJob cRead(String localPath) {
			return new InforLocalJob(EJobL.read, null, true, localPath, null);
		}
		static InforLocalJob cReadResult(byte[] bytes) {
			return new InforLocalJob(EJobL.read, bytes, true, null, null);
		}
		static InforLocalJob cIsExist(String localPath) {
			return new InforLocalJob(EJobL.isexist, null, true, localPath, null);
		}
		static InforLocalJob cIsExistResult(Boolean isexist) {
			return new InforLocalJob(EJobL.isexist, null, isexist, null, null);
		}
		static InforLocalJob cDirName(String localPath) {
			return new InforLocalJob(EJobL.listdirectory, null, null, localPath, null);
		}
		static InforLocalJob cDirNameResult(String[] dirName) {
			return new InforLocalJob(EJobL.listdirectory, null, null, null, dirName);
		}
		static InforLocalJob cFileName(String localPath) {
			return new InforLocalJob(EJobL.listfile, null, null, localPath, null);
		}
		static InforLocalJob cFileNameResult(String[] dirName) {
			return new InforLocalJob(EJobL.listfile, null, null, null, dirName);
		}
	}
	
	private static final ExecutorService _LRoutine = Executors.newSingleThreadExecutor();
	private static volatile boolean _lockState = false;
	
	public static void shutdown() throws SDEBase {
		_log.p("shutdown");
		forceSaveCashes();
		// _LRoutine.shutdown();
	}
	
	public static void forceSaveCashes() throws SDEBase {
		_log.r("forceSaveCashes");
		SDEBase e =  _byteList.clearAndGet();
		if (e != null) {
			throw e;
		}
	}
	
	private static volatile ByteList _byteList = new ByteList();
	private static class ByteList {
		private final List<byte[]> _bytesA = new ArrayList<byte[]>();
		private final List<String> _keyStrings = new ArrayList<String>();
		synchronized void append(String keyString, byte[] bytes) {
			_bytesA.add(bytes);
			_keyStrings.add(keyString);
		}
		int size() { return _bytesA.size(); }
		synchronized byte[] find(String keyString) {
			for (int ith=0; ith<size(); ith++) {
				if (_keyStrings.get(ith).equals(keyString)) {
					return _bytesA.get(ith);
				}
			}
			return null;
		}
		void forceClear() {
			_bytesA.clear();
			_keyStrings.clear();
		}
		synchronized SDEBase clearAndGet() {
			ResultEn<InforLocalJob> result;
			for (int ith=0; ith<size(); ith++) {
				result = ThreadLocal.getInforLocalJob(InforLocalJob.cWrite(_keyStrings.get(ith), _bytesA.get(ith)));
				if (result._exception != null) {
					return result._exception;
				}
			}
			_bytesA.clear();
			_keyStrings.clear();
			return null;
		}
	}
	public static SDEBase write(String localPath, String content) {
		return write(localPath, FileM.string2Bytes(content));
	}
	public static SDEBase write(String localPath, byte[] bytes) {
		return write(localPath, bytes, _LRoutine);
	}
	public static SDEBase write(String localPath, byte[] bytes, ExecutorService service) {
		try {
			return run(InforLocalJob.cWrite(localPath, bytes), service).get()._exception;
		} catch(InterruptedException e) {
			return SDEError.cThreadLocalInterrupted();
		} catch(ExecutionException e) {
			return SDEError.cThreadLocalExecution(e.getCause().toString());
		}
	}
	public static ResultEn<byte[]> read(String localPath) {
		return read(localPath, _LRoutine);
	}
	public static ResultEn<byte[]> read(String localPath, ExecutorService service) {
		ResultEn<InforLocalJob> infor;
		try {
			infor = run(InforLocalJob.cRead(localPath), service).get();
		} catch(InterruptedException e) {
			return new ResultEn<byte[]>(SDEError.cThreadLocalInterrupted());
		} catch(ExecutionException e) {
			return new ResultEn<byte[]>(SDEError.cThreadLocalExecution(e.getCause().toString()));
		}
		// System.out.println(FileM.bytes2String(infor._result._bytes));
		if (infor.isSuccess()) {
			return new ResultEn<byte[]>(infor._result._bytes);
		} else {
			return new ResultEn<byte[]>(infor._exception);
		}
	}
	public static ResultEn<Boolean> isExist(String localPath) {
		return isExist(localPath, _LRoutine);
	}
	public static ResultEn<Boolean> isExist(String localPath, ExecutorService service){
		ResultEn<InforLocalJob> infor;
		try {
			infor = run(InforLocalJob.cIsExist(localPath), service).get();
		} catch(InterruptedException e) {
			return new ResultEn<Boolean>(SDEError.cThreadLocalInterrupted());
		} catch(ExecutionException e) {
			return new ResultEn<Boolean>(SDEError.cThreadLocalExecution(e.getCause().toString()));
		}
		if (infor.isSuccess()) {
			return new ResultEn<Boolean>(Boolean.valueOf(infor._result._isexist));
		} else {
			return new ResultEn<Boolean>(infor._exception);
		}
	}
	public static ResultEn<String[]> listDirectory(String localPath) {
		return listDirectory(localPath, _LRoutine);
	}
	public static ResultEn<String[]> listDirectory(String localPath, ExecutorService service){
		ResultEn<InforLocalJob> infor;
		try {
			infor = run(InforLocalJob.cDirName(localPath), service).get();
		} catch(InterruptedException e) {
			return new ResultEn<String[]>(SDEError.cThreadLocalInterrupted());
		} catch(ExecutionException e) {
			return new ResultEn<String[]>(SDEError.cThreadLocalExecution(e.getCause().toString()));
		}
		if (infor.isSuccess()) {
			return new ResultEn<String[]>(infor._result._dirName);
		} else {
			return new ResultEn<String[]>(infor._exception);
		}
	}
	public static ResultEn<String[]> listFile(String localPath) {
		return listFile(localPath, _LRoutine);
	}
	public static ResultEn<String[]> listFile(String localPath, ExecutorService service){
		ResultEn<InforLocalJob> infor;
		try {
			infor = run(InforLocalJob.cFileName(localPath), service).get();
		} catch(InterruptedException e) {
			return new ResultEn<String[]>(SDEError.cThreadLocalInterrupted());
		} catch(ExecutionException e) {
			return new ResultEn<String[]>(SDEError.cThreadLocalExecution(e.getCause().toString()));
		}
		if (infor.isSuccess()) {
			return new ResultEn<String[]>(infor._result._dirName);
		} else {
			return new ResultEn<String[]>(infor._exception);
		}
	}
	
	public static boolean isLocked() {
		return _lockState;
	}
	public static synchronized void setLock() {
		_lockState = true;
	}
	public static synchronized void releaseLock() {
		_lockState = false;
	}
	public static synchronized Future<ResultEn<InforLocalJob>> run(
		final InforLocalJob inforLocalJob, ExecutorService service
	) {
		return service.submit( new Callable<ResultEn<InforLocalJob>>() { public ResultEn<InforLocalJob> call() {
			EJobL type = inforLocalJob._type;	
			if (_lockState) {
				switch(type) {
					case write:
						_byteList.append(inforLocalJob._localPath, inforLocalJob._bytes);
						break;
					default: 
						break;
				}
				return new ResultEn<InforLocalJob>(SDEStop.cThreadLocal());
			}
			SDEBase exception = null;
			// System.out.println("mark 01");
			exception = _byteList.clearAndGet();
			// System.out.println("mark 02");
			if (exception != null) {
				return new ResultEn<InforLocalJob>(exception);
			}
			return getInforLocalJob(inforLocalJob);
		}});
	}
	
	private static ResultEn<InforLocalJob> getInforLocalJob(InforLocalJob inforLocalJob) {
		SDEBase exception = null;
		String localPath = inforLocalJob._localPath;	
		EJobL type = inforLocalJob._type;
		InforLocalJob reInfor = null;			
		try {
			switch(type) {
				case read:    
				byte[] bytes = _byteList.find(localPath);
				if (bytes == null) {
					// System.out.println("mark 01");
					// System.out.println("localPath = " + localPath);
					bytes = FileM.read(localPath);
					// System.out.println("mark 02");
					// System.out.println(FileM.bytes2String(bytes));
				}
				reInfor = InforLocalJob.cReadResult(bytes);
				break;
				case write:   FileM.write(localPath, inforLocalJob._bytes);  reInfor = InforLocalJob.cWriteResult();   break;
				case isexist: reInfor = InforLocalJob.cIsExistResult(Boolean.valueOf(FileM.isExist(localPath))); break;
				case listdirectory: 
				// FileM.mkdirs(localPath);
				FileM.mkdirForFolderByLoop(localPath);
				reInfor = InforLocalJob.cDirNameResult(FileM.listDirectory(localPath)); break;
				case listfile: 
				// FileM.mkdirs(localPath);
				FileM.mkdirForFolderByLoop(localPath);
				reInfor = InforLocalJob.cFileNameResult(
					// FileM.listFile(localPath, _setting.getLegalImageExtension())
					FileM.listFile(localPath)
				); break;
				default: break;
			}
		} catch (IOException e) {
			switch(type) {
				case read:    exception = SDEError.cLocalRead(localPath + " " + e.toString()); break;
				case write:   exception = SDEError.cLocalWrite(localPath + " " + e.toString()); break;
				case isexist: exception = SDEError.cLocalIsExist(localPath + " " + e.toString()); break;
				case listdirectory: exception = SDEError.cLocalListDirectory(localPath + " " + e.toString()); break;
				case listfile: exception = SDEError.cLocalListFile(localPath + " " + e.toString()); break;
				default: break;
			}
			if (exception != null) {
				return new ResultEn<InforLocalJob>(
					exception
				);
			}
			return new ResultEn<InforLocalJob>(
				SDEError.cUncatchLCmd(0)
			);
		}
		return new ResultEn<InforLocalJob>(reInfor);
	}
	
}