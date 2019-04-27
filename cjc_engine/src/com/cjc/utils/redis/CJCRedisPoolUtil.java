package com.cjc.utils.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cjc.utils.CJCExceptionUtil;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author cjc
 * @date Mar 13, 2019
 */
public class CJCRedisPoolUtil {

	private static final Logger sLog = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	//	Jedis是一个Java语言的Redis客户端，它为Java语言连接与操作Redis提供了简单易用的接口。 
	//	Jedis不是线程安全的，故不应该在多线程环境中共用一个Jedis实例。但是，也应该避免直接创建多个Jedis实例，因为这种做法会导致创建过多的socket连接，性能不高。 
	//	要保证线程安全且获得较好的性能，可以使用JedisPool。JedisPool是一个连接池，既可以保证线程安全，又可以保证了较高的效率。 
	//	可以声明一个全局的JedisPool变量来保存JedisPool对象的引用，然后在其他地方使用。要知道，JedisPool是一个线程安全的连接池。

	/**
	 * 创建redis连接池
	 * @param host
	 * @param port
	 * @param timeout
	 * @param maxIdle
	 * @param maxTotal
	 * @return
	 */
	public static JedisPool create(String host, int port, int timeout, int maxIdle, int maxTotal) {
		try {
			JedisPoolConfig config = new JedisPoolConfig();

			// 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
			config.setBlockWhenExhausted(true);

			// 设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
			config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");

			// 是否启用pool的jmx管理功能, 默认true
			config.setJmxEnabled(true);

			// MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name="
			// + "pool" + i); 默认为"pool", JMX不熟,具体不知道是干啥的...默认就好.
			config.setJmxNamePrefix("pool");

			// 是否启用后进先出, 默认true
			config.setLifo(true);

			// 最大空闲连接数
			config.setMaxIdle(maxIdle);
			// 最大连接数
			config.setMaxTotal(maxTotal);

			// 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常,小于零:阻塞不确定的时间,默认-1
			config.setMaxWaitMillis(timeout);

			// 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
			config.setMinEvictableIdleTimeMillis(1800000);

			// 最小空闲连接数, 默认0
			config.setMinIdle(0);

			// 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
			config.setNumTestsPerEvictionRun(3);

			// 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数
			// 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
			config.setSoftMinEvictableIdleTimeMillis(1800000);

			// 在获取连接的时候检查有效性, 默认false
			config.setTestOnBorrow(false);

			// 在空闲时检查有效性, 默认false
			config.setTestWhileIdle(false);

			// 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
			config.setTimeBetweenEvictionRunsMillis(-1);

			return new JedisPool(config, host, port, timeout);
		} catch (Exception e) {
			CJCExceptionUtil.log(sLog, e);
			return null;
		}
	}
}