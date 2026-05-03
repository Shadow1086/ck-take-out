package com.ck.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Package: com.ck.it
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/5/3 18:19
 */
//@SpringBootTest
public class SpringDataRedisTest {
//	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void testRedisTemplate() {
		System.out.println(redisTemplate);
		ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
		HashOperations<String,Object,Object> hashOperations = redisTemplate.opsForHash();
		SetOperations<String,Object> setOperations = redisTemplate.opsForSet();
		ListOperations<String,Object> listOperations = redisTemplate.opsForList();
		ZSetOperations<String,Object> zSetOperations = redisTemplate.opsForZSet();
	}

	/**
	 * 操作字符串类型的数据
	 *
	 */
	@Test
	public void testString() {
		// set  get   setex   setnx

		ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
		valueOperations.set("city", "北京");
		String city = (String) valueOperations.get("city");
		System.out.println(city);

		valueOperations.set("code", "2738", 3, TimeUnit.MINUTES);

		valueOperations.setIfAbsent("lock", "1");
		valueOperations.setIfAbsent("lock", "2");
	}

	/**
	 * 操作哈希类型的数据
	 *
	 */
	@Test
	public void testHas() {
		/// hset    hget    hdel    hkeys   hvals
		HashOperations<String,Object,Object> hashOperations = redisTemplate.opsForHash();
		hashOperations.put("person", "name", "小明");
		hashOperations.put("person", "age", "20");
		String name = (String) hashOperations.get("person", "name");
		System.out.println(name);

		Set person = hashOperations.keys("person");
		System.out.println(person);

		System.out.println(hashOperations.values("person"));

		hashOperations.delete("person", "age");
	}

	/**
	 * 操作列表类型数据
	 *
	 */
	@Test
	public void testList() {
		ListOperations<String,Object> listOperations = redisTemplate.opsForList();

		listOperations.leftPushAll("mylist", "a", "b", "c");
		listOperations.leftPush("mylist", "d");

		System.out.println(listOperations.range("mylist", 0, -1));

		listOperations.rightPop("mylist");

		System.out.println(listOperations.size("mylist"));
	}

	@Test
	public void testSet() {
		// sadd smembers    scard   sinter  sunion  srem
		SetOperations<String ,Object> setOperations = redisTemplate.opsForSet();

		setOperations.add("set1", "a", "b", "c", "d");
		setOperations.add("set1", "a", "b", "x", "z");

		System.out.println(setOperations.members("set1"));

		System.out.println(setOperations.size("set1"));

		System.out.println(setOperations.intersect("set1", "set2"));

		System.out.println(setOperations.intersect("set1", "set2"));

		setOperations.remove("set1", "a", "b");
	}

	@Test
	public void testZSet() {
		// zadd     zrange  zincrby zrem
		ZSetOperations<String,Object> zSetOperations = redisTemplate.opsForZSet();
		zSetOperations.add("zset1", "a", 10);
		zSetOperations.add("zset1", "b", 12);
		zSetOperations.add("zset1", "c", 9);

		System.out.println(zSetOperations.range("zset1", 0, -1));

		zSetOperations.incrementScore("zset1", "c", 10);

		zSetOperations.remove("zset1", "a", "b");
	}

	@Test
	public void testCommon() {
		Set keys = redisTemplate.keys("*");
		System.out.println("keys = " + keys);

		Boolean name = redisTemplate.hasKey("name");
		Boolean set1 = redisTemplate.hasKey("set1");

		for (Object key : keys) {
			DataType type = redisTemplate.type(key);
			System.out.println(type.name());
		}
		redisTemplate.delete("mylist");
	}
}
