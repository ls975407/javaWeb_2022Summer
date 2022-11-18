package com.cloud.manga.henryTing.thread;
/*
javac -encoding utf8  ./com/cloud/manga/henryTing/thread/ThreadImp.java
*/
import com.cloud.manga.henryTing.unit.Log;
import com.cloud.manga.henryTing.unit.ResultEn;
import com.cloud.manga.henryTing.unit.SDEBase;
import com.cloud.manga.henryTing.unit.SDEStop;
import com.cloud.manga.henryTing.unit.SDEError;
import com.cloud.manga.henryTing.unit.SDEHandle;
import com.cloud.manga.henryTing.unit.ExceptionJoin;
import com.cloud.manga.henryTing.unit.IndexP;
import com.cloud.manga.henryTing.holder.IterUnit;
import com.cloud.manga.henryTing.consts.ThreadWork;
import com.cloud.manga.henryTing.tool.ThreadLocal;
import com.cloud.manga.henryTing.data.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class ThreadImp {
	public final static Log _log = new Log("ThreadImp");
	private ExecutorService _CRoutine = null; 
	
	public static Setting _setting;
	private final RangePre _rangePre = new RangePre();
	private final int _numThread;
	private ThreadImp(int num) {
		_log.r("newFixedThreadPool", Integer.valueOf(num).toString());
		_numThread = num;
		_CRoutine = Executors.newFixedThreadPool(num);		
	}
	private static ThreadImp _ptr = null;
	synchronized public static ThreadImp ptr() {
		if (_ptr == null) {
			_ptr = new ThreadImp(_setting.getNumThread());
		} else {
			int num = _setting.getNumThread();
			if (_ptr._numThread != num){
				_ptr = new ThreadImp(num);
			}
		}
		return _ptr;
	}
	
	private class ICounter {
		int count = 0;
		void add() { count++; }
		int get() { return count; }
	}
	public void iniIdFolders(final KIdTS kidts) throws SDEBase {
		if (kidts.size() < 1) {
			throw SDEError.cIniIdFoldersSizeZero();
		}
		final ExecutorService service = _CRoutine;
		final int threadNum = _numThread;
		final KIdTS kidts_need = ThreadWork.filterExistIdFolder(kidts);
		final int kidts_need_size = kidts_need.size();
		final int size = kidts.size();
		final ICounter ith_need = new ICounter();
		final ICounter ith_total = new ICounter();
		List<Future<SDEBase>> futures = new ArrayList<Future<SDEBase>>();
		_log.p("iniIdFolders submit");
		for (int kth=0 ;kth<threadNum; kth++) {
			futures.add(service.submit( new Callable<SDEBase>() { public SDEBase call() {
				while (true) {
					Boolean _isNeed;
					KIdT _kidt_work; 
					synchronized (this) {
						if (ith_total.get() >= size) { return SDEStop.cThreadImpIniIdFolders(); }
						KIdT _kidt = kidts.get(ith_total.get());
						KIdT _kidt_need = _kidt;
						int ith = ith_need.get();
						if (ith < kidts_need_size) {
							_kidt_need = kidts_need.get(ith);
						}
						_isNeed = _kidt.equals(_kidt_need);
						if (_isNeed) {
							_kidt_work = _kidt_need;
							ith_total.add(); ith_need.add();
						} else {
							_kidt_work = _kidt;
							ith_total.add();
						}
					}
					try {
						if (_isNeed) {
							ThreadWork.iniIdFolder(_kidt_work);
						} else {
							ThreadWork.updateIdFolder(_kidt_work);
						}
					} catch (SDEBase e) {
						System.out.println(e.toString());
						return e;
					}
					// System.out.println("mark 05");
					// System.out.println(String.format("finish %s", _kidt_work._keyName));
				}
			}}));
		}
		_log.t("iniIdFolders join start");
		ExceptionJoin joiner = new ExceptionJoin();
		for (int kth=0 ;kth<threadNum; kth++) {
			try {
				joiner.append(futures.get(kth).get());
			} catch(InterruptedException e) {
				joiner.append(SDEError.cThreadImpInterrupted("iniIdFolders"));
			} catch(ExecutionException e) {
				joiner.append(SDEError.cThreadImpExecution("iniIdFolders", e.toString()));
			}
		}
		_log.t("iniIdFolders join end");
		joiner.throwException();
	}
	
	private static class RangePre {
		private int _rangeNextPg() { return ThreadImp._setting.getRangeNextPg(); }
		private int _rangePrevPg() { return ThreadImp._setting.getRangePrevPg(); }
		private int _rangeNextCh() { return ThreadImp._setting.getRangeNextCh(); }
		private int _rangePrevCh() { return ThreadImp._setting.getRangePrevCh(); }
		
		IndexP next(IndexP indexP, IterUnit iterUnit, int indexDirect, int[] icounters) throws SDEBase {
			final int ith = indexDirect;
			switch(indexDirect) {
				case 0:
				if (icounters[ith] >= _rangeNextPg()) { return null; }
				indexP = iterUnit.pnextPage(indexP);
				if (indexP == null) { icounters[ith] = _rangeNextPg();}
				break;
				case 1:
				if (icounters[ith] >= _rangePrevPg()) { return null; }
				indexP = iterUnit.pprevPage(indexP);
				if (indexP == null) { icounters[ith] = _rangePrevPg();}
				break;
				case 2:
				if (icounters[ith] >= _rangeNextCh()) { return null; }
				indexP = iterUnit.pnextChapter(indexP);
				if (indexP == null) { icounters[ith] = _rangeNextCh();}
				break;
				case 3:
				if (icounters[ith] >= _rangePrevCh()) { return null; }
				indexP = iterUnit.pprevChapter(indexP);
				if (indexP == null) { icounters[ith] = _rangePrevCh();}
				break;
				default:
				return null;
			}
			icounters[ith]++;
			return indexP;
		}
	}
	
	private Future<SDEBase> futurePCNode = null;
	private final ExecutorService _Routine = Executors.newSingleThreadExecutor();

	public void prepareImage(final IndexP indexPStd, final IterUnit iterUnit) {
		if (iterUnit.isLocal()) { return; }
		joinPrepareImage();
		_log.p("_prepareImage submit");
		futurePCNode = _Routine.submit( new Callable<SDEBase>() { public SDEBase call() {
			return _prepareImage(indexPStd, iterUnit);
		}});
	}
	private SDEBase _prepareImage(IndexP indexPStd, IterUnit iterUnit) {
		final ExecutorService service = _CRoutine;
		final int size = 4;
		List<Future<SDEBase>> futurePC = new ArrayList<Future<SDEBase>>();
		for (int ith=0; ith<size; ith++) { futurePC.add(null); }
		IndexP[] indexPs = new IndexP[]{indexPStd, indexPStd, indexPStd, indexPStd};
		KPgS[] kpgs = new KPgS[]{null, null, null, null};
		boolean isZero, isStop, resetCode;
		ExceptionJoin joiner = new ExceptionJoin();
		SDEBase result;
		
		final int[] icounters = new int[]{0,0,0,0};
		for(int levelCount=0; levelCount<1000; levelCount++) {
			if (ThreadWork.isLocked()){ return joiner.get(); }
			isZero = true;
			
			for (int ith=0; ith<size; ith++) {
				try { 
					do {
						indexPs[ith] = _rangePre.next(indexPs[ith], iterUnit, ith, icounters);
						if (indexPs[ith] == null) { kpgs[ith] = null; break; }
						kpgs[ith] = iterUnit.getKPgS(indexPs[ith]);
					} while(kpgs[ith] != null && kpgs[ith].isExist()); 
				} catch (SDEBase e) { return e; }
				if (kpgs[ith] == null) { continue; }
				if (!isZero) { _log.t("_prepareImage inner submit"); }
				isZero = false;
				final KPgS kpgs_pass = kpgs[ith];
				// for (KPg kpg: kpgs_pass) {System.out.println(kpg._keyName); }
				if (kpgs_pass.toKPg().isLocal()) {
					return SDEError.cThreadImpPrepareImageKpgsIsLocal(kpgs_pass.toKPg()._keyName);
				}
				
				futurePC.set(ith, service.submit( new Callable<SDEBase>() { 
					public SDEBase call() {
						try {
							ThreadWork.prepareImage(kpgs_pass);
							return SDEHandle.cNone();
						} catch (SDEBase e) { return e; }
					}
				}));
			}
			if (isZero) { return joiner.get(); }
			_log.t("_prepareImage inner join");
			joiner = new ExceptionJoin();
			isStop = false;
			for (int ith=0; ith<size; ith++) {
				if (kpgs[ith] == null) { continue; }
				result = null; resetCode = false;
				try {
					result = futurePC.get(ith).get();
				} catch(InterruptedException e) {
					joiner.append(SDEError.cThreadImpInterrupted("_prepareImage"));
					resetCode = true;
				} catch(ExecutionException e) {
					joiner.append(SDEError.cThreadImpExecution("_prepareImage", e.toString()));
					resetCode = true;
				}
				if (result != null) {
					switch(result._type) {
						case Error:
						joiner.append(result);
						resetCode = true;
						break;
						case Stop:
						isStop = true;
						if (kpgs[ith].isChange()) {
							iterUnit.saveKPgS(indexPs[ith], kpgs[ith]);
						}
						break;
						case Handle:
						try {
							handleSDE(iterUnit, result, indexPs[ith], kpgs[ith]);
						} catch (SDEError e) {
							joiner.append(e);
						}
						break;
					}
				}
				if (resetCode) {
					try {
						iterUnit.resetKPgS(indexPs[ith], kpgs[ith]);
					} catch (SDEError e) {
						joiner.append(e);
					}
				}
			}
			if (isStop) { return joiner.get(); }
			if (joiner.isNotSuccess()) { break; }
		}
		return joiner.get();
	}
	
	public void handleSDE(IterUnit iterUnit, SDEBase sdebase, IndexP indexP, KPgS kpgs) throws SDEBase {
		switch(sdebase._h_type) {
			case none: case download:
			// System.out.println("handleSDE none");
			if (kpgs.isChange()) {
				// System.out.println("kpgs.isChange()");
				iterUnit.saveKPgS(indexP, kpgs);
			}
			break;
			// case download:
			// // iterUnit.clearKPgS(indexP, kpgs);
			// break;
			default:
			break;
		}
	}
	
	public void askJoinPrepareImage() {
		SDEBase eReture = null;
		if (futurePCNode != null) {
			_log.t("askJoinPrepareImage");
			ThreadLocal.setLock();
		}
	}
	public SDEBase joinPrepareImage() {
		SDEBase eReture = null;
		if (futurePCNode != null) {
			_log.t("joinPrepareImage start");
			ThreadLocal.setLock();
			try {
				eReture = futurePCNode.get();
			} catch(InterruptedException e) {
				eReture = SDEError.cThreadImpInterrupted("joinPrepareImage");
			} catch(ExecutionException e) {
				eReture = SDEError.cThreadImpExecution("joinPrepareImage", e.toString());
			}
			futurePCNode = null;
			ThreadLocal.releaseLock();
			_log.t("joinPrepareImage end");
		}
		return eReture;
	}
	
	public void shutdown() {
		_log.p("shutdown");
		joinPrepareImage();
//		_CRoutine.shutdown();
	}
	
	public void synKAddress(TokenS tokenS, String localPath) throws SDEBase {
		synKAddress(tokenS, localPath, null);
	}
	public void synKAddress(TokenS tokenS, final String localPath, final KIdT kidt) throws SDEBase {
		_log.p("synKAddress submit");
		final boolean synId = kidt == null;
		{
			List<Token> list_ = ThreadWork.filterEle(
				tokenS.asList(),
				ThreadWork.isExistFile(localPath, tokenS.asList(), ".json"),
				false
			);
			// System.out.println(list_.size()); 
			// System.out.println(localPath_work); 
			// System.out.println(tokenS.asList().toString()); 
			// if(true) return;
			if (list_.size() == 0) { return; }
			tokenS = new TokenS(list_.toArray(new Token[0]), true);
		}
		final ExecutorService service = _CRoutine;
		final int threadNum = _numThread;
		final TokenS tokenS_work = tokenS;
		final int size = tokenS.size();
		final ICounter ith_total = new ICounter();
		List<Future<SDEBase>> futures = new ArrayList<Future<SDEBase>>();
		for (int kth=0 ;kth<threadNum; kth++) {
			futures.add(service.submit( new Callable<SDEBase>() { public SDEBase call() {
				Token token = null;
				while (true) {
					synchronized (this) {
						if (ith_total.get() >= size) { return SDEStop.cThreadImpIniIdFolders(); }
						token = tokenS_work.get(ith_total.get());
						ith_total.add();
					}
					if (synId) {
						ThreadWork.write(
							String.format("%s/%s.json", localPath, token._keyName),
							new KAddress(new KIdT(token))
						);
					} else {
						ThreadWork.write(
							String.format("%s/%s.json", localPath, token._keyName),
							new KAddress(new KIdT[]{kidt}, new KCh(kidt, token._keyName))
						);
					}
				}
			}}));
		}
		_log.p("synKAddress join");
		ExceptionJoin joiner = new ExceptionJoin();
		for (int kth=0 ;kth<threadNum; kth++) {
			try {
				joiner.append(futures.get(kth).get());
			} catch(InterruptedException e) {
				joiner.append(SDEError.cThreadImpInterrupted("synKAddress"));
			} catch(ExecutionException e) {
				joiner.append(SDEError.cThreadImpExecution("synKAddress", e.toString()));
			}
		}
		joiner.throwException();
	}
}