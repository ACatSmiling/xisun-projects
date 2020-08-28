package cn.xisun.kafka.producer.properties;

import java.util.Properties;

/**
 * @author XiSun
 * @Date 2020/8/21 11:43
 * <p>
 * Kafka生产者连接信息
 */
public class ProducerProperties {
    public static Properties getProps() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.71:9092");
        // 发送所有ISR
        props.put("acks", "all");
        // 重试次数
        props.put("retries", 0);
        // 缓存大小，根据本机内存大小配置，32m
        props.put("buffer.memory", 33554432);
        // 批量发送大小，16kb
        props.put("batch.size", 16384);
        // 发送频率，1s
        props.put("linger.ms", 1000);
        // 发送端id,便于统计
        props.put("client.id", "patent-producer-asyn-1");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
}
