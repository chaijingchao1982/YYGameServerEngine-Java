package com.cjc.utils.observer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author CJC
 * @date 2014 0901
 */
public class CJCObserverMgr {

	public static CJCObserverMgr sInstance = new CJCObserverMgr();

	/** msg code-> observer list */
	private Hashtable<Integer, List<ICJCObserver>> mObservers = new Hashtable<Integer, List<ICJCObserver>>();

	public static CJCObserverMgr getInstance() {
		return sInstance;
	}

	public CJCObserverMgr() {
	}

	private void addObserver(ICJCObserver observer, Integer msgCode) {
		List<ICJCObserver> obs = mObservers.get(msgCode);
		if (obs == null) {
			obs = new ArrayList<ICJCObserver>();
			mObservers.put(msgCode, obs);
		}

		if (obs.indexOf(observer) == -1) {
			//avoid repeat observer
			obs.add(observer);
		}
	}

	public void addObservers(ICJCObserver observer, Integer... msgCodes) {
		for (Integer code : msgCodes) {
			addObserver(observer, code);
		}
	}

	public boolean removeObserver(ICJCObserver observer, Integer... msgCodes) {
		if (null == observer) {
			return false;
		}

		for (Integer code : msgCodes) {
			List<ICJCObserver> obs = mObservers.get(code);
			if (obs == null) {
				continue;
			}

			while (obs.contains(observer)) {
				obs.remove(observer);
			}
		}
		return true;
	}

	public void clearAllObservers() {
		mObservers.clear();
	}

	public boolean notify(int msgCode) {
		return notify(msgCode, null);
	}

	public boolean notifyWithData(int msgCode, Object data) {
		return notify(msgCode, null, data);
	}

	public boolean notifyWithSender(int msgCode, Object sender) {
		return notify(msgCode, sender);
	}

	public boolean notify(int msgCode, Object sender, Object... data) {
		List<ICJCObserver> obs = mObservers.get(msgCode);
		if (obs == null) {
			return false;
		}

		boolean ret = false;
		for (ICJCObserver ob : obs) {
			if (/* sender != null && */ ob == sender) {
				// 不通知sender自己
				continue;
			}
			ob.obNotify(sender, msgCode, data);
			ret = true;
		}
		return ret;
	}
}
