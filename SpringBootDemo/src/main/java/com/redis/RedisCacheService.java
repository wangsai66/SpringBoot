package com.redis;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * redis 服务类
 * Description: 
 * All Rights Reserved.
 * @version 1.0  2019年8月17日 下午3:14:47  by 王赛(wangsai@zhihuihutong.com)
 */
@Service
@SuppressWarnings("unchecked")
public class RedisCacheService {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LogManager.getLogger(RedisCacheService.class.getName());

	@Resource
	protected RedisTemplate<String, Object> redisTemplate;
	
	
	
	/**
	 * 添加
	*
	* <p>Title: set</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:25:52</p>
	* @param key
	* @param value
	 */
	public void set(final String key, final Object value) {
		redisTemplate.execute(new RedisCallback<Object>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),
						((RedisSerializer<Object>) redisTemplate.getDefaultSerializer()).serialize(value));
				return null;
			}
		});
	}

	/**
	 * 添加
	*
	* <p>Title: set</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:26:09</p>
	* @param key
	* @param value
	* @param liveTime 单位秒
	 */
	public void set(final String key, final Object value, final long liveTime) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),
						liveTime, ((RedisSerializer<Object>) redisTemplate.getDefaultSerializer()).serialize(value));
				return null;
			}
		});
	}

	/**
	 * 
	* <p>Title: setNX  </p>
	* Description: redis setNx操作
	* @author 王长亮 wangchangliang@zhihuihutong.com
	* @date 2017年6月23日 下午5:35:57  
	* @param key
	* @param value
	* @return
	 */
	public boolean setNX(final String key, final Object value) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),
						((RedisSerializer<Object>) redisTemplate.getDefaultSerializer()).serialize(value));
			}
		});
	}

	/**
	 * 
	* <p>Title: setNX  </p>
	* Description: redis setNx方法扩展，支持设置超时时间
	* @author 王长亮 wangchangliang@zhihuihutong.com
	* @date 2017年6月23日 下午5:49:16  
	* @param key
	* @param value
	* @param liveTime 超时时间 单位 秒
	* @return
	 */
	public boolean setNX(final String key, final Object value, final long liveTime) {
		if(exists(key)){
			return false;
		}
		return redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.multi();
				connection.setNX(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),
						((RedisSerializer<Object>) redisTemplate.getDefaultSerializer()).serialize(value));
				connection.expire(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),
						liveTime);
				List<Object> exec = connection.exec();
				return (Boolean) exec.get(0);
			}
		});
	}

	/**
	 * 根据key获取value
	*
	* <p>Title: get</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:26:55</p>
	* @param key
	* @return
	 */
	public <T> T get(final String key) {
		return redisTemplate.execute(new RedisCallback<T>() {

			public T doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] value = connection
						.get(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key));
				if (value == null) {
					return null;
				}
				return (T) redisTemplate.getDefaultSerializer().deserialize(value);
			}
		});
	}

	/** 
	* <p>Title: getWithinSpecifiedTime  </p>
	* Description: 在指定时间内获取指定key值对应的value，超时返回null
	* @author 王长亮 wangchangliang@zhihuihutong.com
	* @date 2017年7月31日 下午12:04:48  
	* @param key 
	* @param timeout 超时时间 ，单位ms
	* @return
	*/
	public <T> T getWithinSpecifiedTime(final String key, final long timeout) {
		return redisTemplate.execute(new RedisCallback<T>() {

			public T doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] value = null;
				long start = System.currentTimeMillis();
				for (;;) {
					long current = System.currentTimeMillis();
					value = connection
							.get(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key));
					if (value != null || (current - start) >= timeout) {
						break;
					}
				}
				if (value == null) {
					return null;
				}
				return (T) redisTemplate.getDefaultSerializer().deserialize(value);
			}
		});
	}

	/**
	 * 判断key是否存在
	*
	* <p>Title: exists</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:27:16</p>
	* @param key
	* @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection
						.exists(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key));
			}
		});
	}

	/**
	 * 
	*
	* <p>Title: get</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:27:44</p>
	* @param key
	* @param liveTime
	* @return
	 */
	public <T> T get(final String key, final long liveTime) {
		return redisTemplate.execute(new RedisCallback<T>() {

			public T doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] bkey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				byte[] value = connection.get(bkey);
				if (value == null) {
					return null;
				}
				connection.expire(bkey, liveTime);
				return (T) redisTemplate.getDefaultSerializer().deserialize(value);
			}
		});
	}

	/**
	 * 删除
	*
	* <p>Title: del</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:27:54</p>
	* @param key
	* @return
	 */
	public long del(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {

			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.del(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key));
			}
		});
	}

	/**
	 * 更新缓存
	*
	* <p>Title: update</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:28:13</p>
	* @param key
	* @param value
	* @param liveTime
	 */
	public void update(final String key, final Object value, final long liveTime) {
		this.set(key, value, liveTime);
	}

	/**
	 * 更新缓存
	*
	* <p>Title: update</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:28:37</p>
	* @param key
	* @param value
	 */
	public void update(final String key, final Object value) {
		this.set(key, value);
	}

	/**
	 * 
	*
	* <p>Title: expire</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:28:56</p>
	* @param key 键值
	* @param liveTime 存活时间 单位 秒
	* @return
	 */
	public boolean expire(final String key, final long liveTime) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {

			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.expire(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),
						liveTime);
			}
		});
	}

	/**
	 * 
	*
	* <p>Title: hset</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:29:00</p>
	* @param key
	* @param field
	* @param value
	* @param liveTime
	 */
	public void hset(final String key, final String field, final Object value, final long liveTime) {
		redisTemplate.execute(new RedisCallback<Object>() {

			public String doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] bkey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				connection.hSet(bkey, ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(field),
						((RedisSerializer<Object>) redisTemplate.getDefaultSerializer()).serialize(value));
				if (liveTime != 0) {
					connection.expire(bkey, liveTime);
				}
				return null;
			}
		});
	}

	/**
	 * 
	*
	* <p>Title: hget</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:29:05</p>
	* @param key
	* @param field
	* @return
	 */
	public <T> T hget(final String key, final String field) {
		return redisTemplate.execute(new RedisCallback<T>() {

			public T doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] value = connection.hGet(
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(field));
				if (value == null) {
					return null;
				}
				return (T) redisTemplate.getDefaultSerializer().deserialize(value);
			}
		});
	}

	/**
	 * 
	*
	* <p>Title: hgetAll</p>
	* <p>author : xubin</p>
	* <p>date : 2017年8月11日 上午10:17:37</p>
	* @param key
	* @return Map<byte[],byte[]> 
	 */
	public Map<byte[], byte[]> hgetAll(final String key) {
		return redisTemplate.execute(new RedisCallback<Map<byte[], byte[]>>() {
			public Map<byte[], byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
				return connection
						.hGetAll(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key));
			}
		});
	}

	/**
	 * 
	*
	* <p>Title: hdel</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:29:09</p>
	* @param key
	* @param field
	* @return
	 */
	public long hdel(final String key, final String field) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.hDel(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(field));
			}
		});
	}

	/**
	 * 
	*
	* <p>Title: hexists</p>
	* <p>author : xubin</p>
	* <p>date : 2017年3月3日 下午6:29:13</p>
	* @param key
	* @param field
	* @return
	 */
	public boolean hexists(final String key, final String field) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.hExists(
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(field));
			}
		});
	}

	/**
	 * hash类型:返回 key 指定的哈希集中所有字段的名字
	 * @param key
	 * @return
	 */
	public Set<byte[]> hkeys(final String key) {
		return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			@Override
			public Set<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection
						.hKeys(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key));
			}
		});
	}

	/**
	 * hash类型:可同时对key设置多个值，hset只能一次设置一个
	 * @param key
	 * @param map
	 * @return
	 */
	public Void hmset(final String key, final Map<byte[], byte[]> map) {
		return redisTemplate.execute(new RedisCallback<Void>() {
			public Void doInRedis(RedisConnection redisConnection) throws DataAccessException {
				redisConnection.hMSet(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),
						map);
				return null;
			}
		});
	}

	/**
	 * hash类型:返回 key 指定的哈希集中指定多个字段的值。
	 * @param key
	 * @param fields
	 * @return
	 */
	public List<byte[]> hmget(final String key, final byte[]... fields) {
		return redisTemplate.execute(new RedisCallback<List<byte[]>>() {
			public List<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection
						.hMGet(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key), fields);
			}
		});
	}

	/**
	 * 
	*
	* <p>Title: setEX</p>
	* <p>author : zhaowen</p>
	* <p>date : 2017年3月3日 下午6:29:13</p>
	* @param key
	* @param field
	* @return
	 */
	public boolean setEX(final String key, final Object value, final long liveTime) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.multi();
				connection.setEx(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),
						liveTime, ((RedisSerializer<Object>) redisTemplate.getDefaultSerializer()).serialize(value));
				connection.exec();
				return true;
			}
		});
	}

	/**
	 * 
	 * <p>
	 * Title: keys
	 * </p>
	 * @author 赵文 zhaowen@zhihuihutong.com
	 * @date 2017年7月27日 上午11:37:48
	 * @param pattern
	 * @return
	 */
	public Set<byte[]> keys(final String pattern) {
		return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			@Override
			public Set<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection
						.keys(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(pattern));
			}
		});
	}

	/**
	 * 向zset中添加一条记录
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param score  分数
	 * @param value
	 * @return
	 * @version 1.0  2019年7月31日 下午5:43:23  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Boolean zadd(final String key, final double score, final String value) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
				byte[] val = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(value);
				return redisConnection.zAdd(
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key), score, val);
			}
		});
	}

	
	
	/**
	 * 返回有序集合中指定分数区间的成员列表。有序集成员按分数值递增(从小到大)次序排列。
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param begin
	 * @param end
	 * @return
	 * @version 1.0  2019年7月31日 下午5:43:01  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Set<Tuple> zRangeWithScores(final String key, final long begin, final long end) {
		return redisTemplate.execute(new RedisCallback<Set<Tuple>>() {
			@Override
			public Set<Tuple> doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.zRangeWithScores(
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key), begin, end);
			}

		});
	}

	/**
	 * 从指定zset移除数据
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param values
	 * @return
	 * @version 1.0  2019年7月31日 下午5:43:58  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Long zrem(final String key, final String... values) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] srcKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				List<byte[]> valueList = new ArrayList<>(values.length);
				for (String obj : values) {
					valueList.add(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(obj));
				}
				connection.zRem(srcKey, valueList.toArray(new byte[0][]));
				return null;

			}
		});
	}

	/**
	 * zset有序集合类型:
	 * @param     key
	 * @param offset    分数值是一个双精度的浮点型数字字符串
	 * @param bytes2    value
	 * @return
	 */
	public Boolean zadd(final String key, final Double offset, final byte[] bytes2) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection.zAdd(
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key), offset, bytes2);
			}
		});
	}

	/**
	 * zset有序集合类型:根据集合中指定的index返回成员列表
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<byte[]> zrange(final String key, final Long start, final Long end) {
		return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			public Set<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection.zRange(
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key), start, end);
			}
		});
	}

	/**
	 * zset有序集合类型:
	 * 从排序的集合中删除一个或多个成员
	 * 返回值为从有序集合中删除的成员个数，不包括不存在的成员
	 * @param key
	 * @param bytes
	 * @return
	 */
	public Long zrem(final String key, final byte[]... bytes) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
				byte[] keys = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				return redisConnection.zRem(keys, bytes);
			}
		});
	}

	/**
	 * zset有序集合类型:为有序集key的成员member的offset值加上增量increment
	 * @param key    key
	 * @param offset    增量increment
	 * @param field    集合成员
	 * @return  member成员的新offset值
	 */
	public Double zincrby(final String key, final Double offset, final String field) {
		return redisTemplate.execute(new RedisCallback<Double>() {
			public Double doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection.zIncrBy(
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key), offset,
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(field));
			}
		});
	}

	/**
	 * zset有序集合类型:找到指定区间范围的数据进行返回
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<byte[]> zrangebyscore(final String key, final Double start, final Double end) {
		return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
			public Set<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection.zRangeByScore(
						((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key), start, end);
			}
		});
	}

	/**
	 * zset有序集合类型:移除有序集key中，指定排名(rank)区间内的所有成员。
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Long zremrangebyrank(final String key, final Double start, final Double end) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
				byte[] keys = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				return redisConnection.zRemRangeByScore(keys, start, end);
			}
		});
	}

	/**
	 * zset有序集合类型:返回有序集key中，score值在min和max之间(默认包括score值等于min或max)的成员。
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Long zcount(final String key, final Double start, final Double end) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
				byte[] keys = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				return redisConnection.zCount(keys, start, end);
			}
		});
	}

	/**
	 * 用于移除列表的最后一个元素，并将该元素添加到另一个列表并返回。
	 * Description: 
	 * All Rights Reserved.
	 * @param srcName  
	 * @param dstName
	 * @return
	 * @version 1.0  2019年7月31日 下午5:45:34  by 王赛(wangsai@zhihuihutong.com)
	 */
	public <T> T rpoplpush(final String srcName, final String dstName) {
		return redisTemplate.execute(new RedisCallback<T>() {

			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] srcKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(srcName);
				byte[] dstKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(dstName);
				connection.rPopLPush(srcKey, dstKey);
				return null;
			}
		});
	}

	/**
	 * 返回列表的长度。 如果列表 key 不存在，则 key 被解释为一个空列表
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @return
	 * @version 1.0  2019年7月31日 下午5:46:49  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Long llen(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyByte = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				return connection.lLen(keyByte);
			}
		});
	}

	
	/**
	 * 从列表中取出最后一个元素，并插入到另外一个列表的头部； 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
	 * Description: 
	 * All Rights Reserved.
	 * @param srcName 
	 * @param dstName  
	 * @param timeout  超时时间
	 * @return
	 * @version 1.0  2019年7月31日 下午5:47:25  by 王赛(wangsai@zhihuihutong.com)
	 */
	public <T> T brpoplpush(final String srcName, final String dstName, final int timeout) {
		return redisTemplate.execute(new RedisCallback<T>() {

			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] srcKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(srcName);
				byte[] dstKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(dstName);
				byte[] value = connection.bRPopLPush(timeout, srcKey, dstKey);
				return (T) redisTemplate.getDefaultSerializer().deserialize(value);
			}
		});
	}

	/**
	 * 移除列表的最后一个元素，返回值为移除的元素
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @return
	 * @version 1.0  2019年7月31日 下午5:48:38  by 王赛(wangsai@zhihuihutong.com)
	 */
	public <T> T rpop(final String key) {
		return redisTemplate.execute(new RedisCallback<T>() {

			@Override
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] srcKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				byte[] value = connection.rPop(srcKey);
				return (T) redisTemplate.getDefaultSerializer().deserialize(value);
			}
		});
	}

	
	/**
	 * 将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。 当 key 存在但不是列表类型时，返回一个错误。
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param obj
	 * @version 1.0  2019年7月31日 下午5:49:01  by 王赛(wangsai@zhihuihutong.com)
	 */
	public void lpush(final String key, final Object obj) {
		redisTemplate.execute(new RedisCallback<Void>() {

			@Override
			public Void doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] srcKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				byte[] value = ((RedisSerializer<Object>) redisTemplate.getDefaultSerializer()).serialize(obj);
				connection.lPush(srcKey, value);
				return null;
			}
		});
	}

	/**
	 * 向list中添加元素
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param values
	 * @version 1.0  2019年7月31日 下午5:50:21  by 王赛(wangsai@zhihuihutong.com)
	 */
	public void lpush(final String key, final String... values) {
		checkArgument(ArrayUtils.isNotEmpty(values));
		redisTemplate.execute(new RedisCallback<Void>() {

			@Override
			public Void doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] srcKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				List<byte[]> valueList = new ArrayList<>(values.length);
				for (String obj : values) {
					valueList.add(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(obj));
				}
				connection.lPush(srcKey, valueList.toArray(new byte[0][]));
				return null;
			}
		});
	}
	
	
	/**
	 * 返回列表中指定区间内的元素
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param begin
	 * @param end
	 * @return
	 * @version 1.0  2019年7月31日 下午5:51:16  by 王赛(wangsai@zhihuihutong.com)
	 */
	public List<String> lrange(final String key, final long begin, final long end) {
		return redisTemplate.execute(new RedisCallback<List<String>>() {
			@Override
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] srcKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				List<byte[]> lRange = connection.lRange(srcKey, begin, end);
				if (CollectionUtils.isNotEmpty(lRange)) {
					return Lists.transform(lRange, new Function<byte[], String>() {
						@Override
						public String apply(byte[] input) {
							return redisTemplate.getStringSerializer().deserialize(input);
						}
					});
				}
				return null;
			}
		});
	}

	/**
	 * 将一个或多个值插入到列表的尾部(最右边)。
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param values
	 * @version 1.0  2019年7月31日 下午5:51:43  by 王赛(wangsai@zhihuihutong.com)
	 */
	public void rpush(final String key, final String... values) {
		checkArgument(ArrayUtils.isNotEmpty(values));
		redisTemplate.execute(new RedisCallback<Void>() {

			@Override
			public Void doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] srcKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				List<byte[]> valueList = new ArrayList<>(values.length);
				for (String obj : values) {
					valueList.add(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(obj));
				}
				connection.rPush(srcKey, valueList.toArray(new byte[0][]));
				return null;
			}
		});
	}

	/**
	 * 计算集合中元素的数量
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @return
	 * @version 1.0  2019年7月31日 下午5:52:07  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Long zcard(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] srcKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key);
				return connection.zCard(srcKey);
			}
		});
	}

	
	/**
	 * 移出并获取列表的最后一个元素(右侧)， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
	 * Description: 
	 * All Rights Reserved.
	 * @param timeout
	 * @param keys
	 * @return
	 * @version 1.0  2019年7月31日 下午5:52:37  by 王赛(wangsai@zhihuihutong.com)
	 */
	public List<String> brpop(final int timeout, final String... keys) {
		return redisTemplate.execute(new RedisCallback<List<String>>() {
			@Override
			public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
				List<byte[]> blist = new ArrayList<byte[]>(keys.length);
				for (int i = 0; i < keys.length; i++) {
					blist.add(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(keys[i]));
				}
				List<byte[]> poplist = connection.bRPop(timeout, blist.toArray(new byte[0][]));
				if (null != poplist && !poplist.isEmpty()) {
					List<String> result = new ArrayList<>(poplist.size());
					for (Iterator<byte[]> iterator = poplist.iterator(); iterator.hasNext();) {
						byte[] bs = (byte[]) iterator.next();
						result.add(((RedisSerializer<String>) redisTemplate.getStringSerializer()).deserialize(bs));
					}
					return result;
				}
				return null;
			}
		});
	}


	/**
	 * 将信息放到指定的频道
	 * Description: 
	 * All Rights Reserved.
	 * @param channel  频道名称 推荐命名规则 模块：操作 ，例如 USER:ADD/CAR:IN等
	 * @param message
	 * @version 1.0  2019年7月31日 下午5:53:44  by 王赛(wangsai@zhihuihutong.com)
	 */
	public void publish(final String channel, final Object message) {
		redisTemplate.execute(new RedisCallback<Void>() {

			@Override
			public Void doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] srcKey = ((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(channel);
				byte[] value = ((RedisSerializer<Object>) redisTemplate.getDefaultSerializer()).serialize(message);
				Long received = connection.publish(srcKey, value);
				logger.info(received + " clients received this message ");
				return null;
			}
		});
	}

	/**
	 * 订阅指定频道消息
	 * Description: 
	 * All Rights Reserved.
	 * @param listener
	 * @param channels
	 * @version 1.0  2019年7月31日 下午5:54:21  by 王赛(wangsai@zhihuihutong.com)
	 */
	public void subscribe(final MessageListener listener, final String... channels) {
		redisTemplate.execute(new RedisCallback<Void>() {
			@Override
			public Void doInRedis(RedisConnection connection) throws DataAccessException {
				byte[][] byteArray = new byte[channels.length][];
				for (int i = 0; i < byteArray.length; i++) {
					byteArray[i] = ((RedisSerializer<String>) redisTemplate.getStringSerializer())
							.serialize(channels[i]);
				}
				connection.subscribe(listener, byteArray);
				return null;
			}
		});
	}


	/**
	 * 将 key 中储存的数字值增一
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @return
	 * @version 1.0  2019年7月31日 下午5:55:12  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Long incr(final String key) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.incr(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key));
			}
		});
	}
	
	
	/**
	 * 将 key 中储存的数字加上指定的增量值。
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param num
	 * @return
	 * @version 1.0  2019年7月31日 下午5:55:47  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Long incrBy(final String key,final long num ) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.incrBy(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),num);
			}
		});
	}
	
	
	/**
	 * 命令将 key 中储存的数字值减一
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @return
	 * @version 1.0  2019年7月31日 下午5:56:24  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Long decr(final String key) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.decr(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key));
			}
		});
	}
	
	/**
	 * 将 key 所储存的值减去指定的减量值。
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param num
	 * @return
	 * @version 1.0  2019年7月31日 下午5:57:21  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Long decrBy(final String key,final long num) {
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.decrBy(((RedisSerializer<String>) redisTemplate.getStringSerializer()).serialize(key),num);
			}
		});
	}

	
	/**
	 * 批量向zset中添加记录
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param values
	 * @return
	 * @version 1.0  2019年7月31日 下午6:18:39  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Long zadd(final String key,final   Set<ZSetOperations.TypedTuple<Object>> typedTuples) {
		return redisTemplate.opsForZSet().add(key, typedTuples);
	}
	
	
	
	 /**
	  * 获取排行前面的数据
	  * Description: 
	  * All Rights Reserved.
	  * @param key
	  * @param topCount
	  * @return
	  * @version 1.0  2019年8月1日 下午3:31:53  by 王赛(wangsai@zhihuihutong.com)
	  */
	  public List<Object> getTopRankZSet(String key, int topCount) {
	      try {
	          Set<Object> range = redisTemplate.opsForZSet().reverseRange(key, 0, topCount);
	          return Arrays.asList(range.toArray());
	      } catch (Exception e) {
	          logger.error("redis getTopRankZSet failed, key = " + key + "| error:" + e.getMessage(), e);
	          return new ArrayList<>();
	      }
	  }
	
	
	/**
	 * 返回元素在集合的排名,有序集合是按照元素的score值由小到大排列
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param value
	 * @return
	 * @version 1.0  2019年7月31日 下午7:56:45  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Long zRank(final String key, final Object value) {
		return redisTemplate.opsForZSet().rank(key, value);
	}
	
	/**
	 * 返回该元素在集合中的score值
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @param value
	 * @return
	 * @version 1.0  2019年7月31日 下午7:59:40  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Double score(final String key,final  Object value) {
		return redisTemplate.opsForZSet().score(key, value);
	}
	
	/**
	 * 
	 * Description: 
	 * All Rights Reserved.
	 * @param key
	 * @return
	 * @version 1.0  2019年8月1日 下午4:52:50  by 王赛(wangsai@zhihuihutong.com)
	 */
	public Long zCard(final String key) {
		return redisTemplate.opsForZSet().zCard(key);
	}
}
