package com.cjc.frame.yy.user;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.cjc.frame.yy.msg.YYTcpWriterImpl;
import com.google.protobuf.GeneratedMessageV3;

/**
 * user manager base class
 * @author chaijingchao
 * @date 2018-3
 * @param <T>
 */
public class YYUserMgr<T extends YYUser> {

	/** key:jid, value:user */
	protected ConcurrentHashMap<String, T> mUserMap = new ConcurrentHashMap<>();

	@Autowired
	private YYTcpWriterImpl mWriter;

	public boolean contains(String jid, T user) {
		T old = getUser(jid);
		return (user == old);
	}

	public ConcurrentHashMap<String, T> getUserMap() {
		return mUserMap;
	}

	public T getUser(String jid) {
		return mUserMap.get(jid);
	}

	public T addUser(String userId, T user) {
		return mUserMap.put(userId, user);
	}

	public T removeUser(String userId) {
		return mUserMap.remove(userId);
	}

	public int getUserCount() {
		return mUserMap.size();
	}

	public void broadcastToAllUsers(GeneratedMessageV3 msg) {
		assert (msg != null) : "msg is null";

		Iterator<Entry<String, T>> iterator = mUserMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, T> entry = iterator.next();
			T user = entry.getValue();
			mWriter.writeFlush(user.getChannelHandlerContext(), msg);
		}
	}
}
