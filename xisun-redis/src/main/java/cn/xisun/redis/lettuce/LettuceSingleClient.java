package cn.xisun.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author XiSun
 * @Date 2020/8/12 10:47
 */
public class LettuceSingleClient {
    /**
     * <1> Redis connection是long-lived，而且线程安全。若connection lost，将会reconnect直到connection.close()函数被调用。
     * 没有timeout的Pending commands将会在重连成功后被执行。
     * <2> 所有connections从RedisClient继承默认的timeout，若timeout将抛RedisException。
     * RedisClient的默认timeout为60s，并可以为每个connection设置相应timeout。
     */
    public static RedisClient client = RedisClient.create(RedisURI.builder()
            .withHost("192.168.1.100")
            .withPort(6379)
            .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
            .build());

    private LettuceSingleClient() {
    }

    /**
     * 不指定编码解码器RedisCodec的前提下，RedisClient创建的StatefulRedisConnection实例,
     * 一般是泛型实例StatefulRedisConnection<String,String>,即：所有命令api的key和value都是String类型
     * 必要的时候可以定制编码解码器RedisCodec<K,V>
     */
    private static class StringConnection {
        private static StatefulRedisConnection<String, String> connection = client.connect();
    }

    /**
     * 指定byte编码解码器ByteArrayCodec
     */
    private static class ByteConnection {
        private static StatefulRedisConnection<byte[], byte[]> connection = client.connect(new ByteArrayCodec());
    }

    public static StatefulRedisConnection<String, String> getStringConnection() {
        StatefulRedisConnection<String, String> connection = StringConnection.connection;
        return connection;
    }

    public static StatefulRedisConnection<byte[], byte[]> getByteConnection() {
        StatefulRedisConnection<byte[], byte[]> connection = ByteConnection.connection;
        return connection;
    }
}
