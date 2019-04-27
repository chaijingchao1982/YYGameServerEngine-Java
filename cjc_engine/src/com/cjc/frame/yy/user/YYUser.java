package com.cjc.frame.yy.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.internal.StringUtil;

/**
 * user base class
 * @author chaijingchao
 * @date 2018-4
 */
public abstract class YYUser {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	public static final AttributeKey<YYUser> CHANNEL_ATTR_KEY_USER = AttributeKey.valueOf("user");

	/** the channel context for communication（volatile is required due to multi-threaded broadcasts） */
	protected volatile ChannelHandlerContext mChc;

	/** user's unique id */
	protected String mJid;

	/** user's online and offline time stamp*/
	protected volatile long mOnlineTimeStampMsec = -1;
	protected volatile long mOfflineTimeStampMsec = -1;

	/** user's online and offline logic lock object*/
	protected Object mOnlineLock = new Object();

	protected void init(String jid) {
		assert (!StringUtil.isNullOrEmpty(jid)) : "jid is null";
		this.mJid = jid;
	}

	public ChannelHandlerContext getChannelHandlerContext() {
		return mChc;
	}

	public String getJid() {
		return mJid;
	}

	public long getOfflineTimeStamp() {
		return mOfflineTimeStampMsec;
	}

	public void setOfflineTimeStamp(long timeStamp) {
		this.mOfflineTimeStampMsec = timeStamp;
	}

	public boolean isOnline() {
		synchronized (mOnlineLock) {
			return (this.mChc != null);
		}
	}

	/**
	 * 设置时间戳为现在上线
	 * @param chc
	 */
	public final void setOnlineTimeStamp2Now() {
		synchronized (mOnlineLock) {
			mOnlineTimeStampMsec = System.currentTimeMillis();
			mOfflineTimeStampMsec = -1;
		}
	}

	/**
	 * 设置时间戳为现在下线
	 * @param chc
	 */
	public final void setOfflineTimeStamp2Now() {
		synchronized (mOnlineLock) {
			mOnlineTimeStampMsec = -1;
			mOfflineTimeStampMsec = System.currentTimeMillis();
		}
	}

	/**
	 * called after handshake success
	 * @param chc
	 */
	public void online(ChannelHandlerContext chc) {
		assert (chc != null) : "chc is null";

		synchronized (mOnlineLock) {
			mOnlineTimeStampMsec = System.currentTimeMillis();
			mOfflineTimeStampMsec = -1;

			if (mChc != null) {
				//still not offline (old link is dead situation)
				sLog.warn("old mChc is not null");
				if (mChc == chc) {
					sLog.warn("mChc==chc ={}", chc);
					return;
				} else {
					//close the old context/tcp
					mChc.close();
				}
			}

			//set Attribute
			Attribute<YYUser> attr = chc.channel().attr(CHANNEL_ATTR_KEY_USER);
			if (attr.get() != null) {
				//unexpected, it should be null (is this chc reuse)
				sLog.warn("attr.get() != null");
			}
			attr.set(this);

			this.mChc = chc;
		}
	}

	/**
	 * called when link is broken
	 * @param chc
	 * @return
	 */
	public void offline(ChannelHandlerContext chc) {
		synchronized (mOnlineLock) {
			if (chc == null) {
				chc = mChc;
			}

			if (mChc != chc) {
				chcErr(chc);
				return;
			}

			if (mChc != null) {
				Attribute<YYUser> attr = mChc.channel().attr(CHANNEL_ATTR_KEY_USER);
				attr.set(null);

				mChc.close();
				mChc = null;
			}

			//update the time stamp
			mOnlineTimeStampMsec = -1;
			mOfflineTimeStampMsec = System.currentTimeMillis();
		}
	}

	private void chcErr(ChannelHandlerContext chc) {
		sLog.error("mChc != chc");

		if (chc != null) {
			Attribute<YYUser> attr = chc.channel().attr(CHANNEL_ATTR_KEY_USER);
			attr.set(null);

			chc.close();
		}
	}
}
