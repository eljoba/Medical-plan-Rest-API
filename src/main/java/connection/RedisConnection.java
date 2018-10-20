package connection;

import redis.clients.jedis.Jedis;

public class RedisConnection {
	private static Jedis jedis;

	public static Jedis getConnection() {

		if (jedis == null) {
			Jedis jedis = new Jedis();
			System.out.println("Connection to server sucessfully");
			return jedis;
		}

		return jedis;

	}
}
