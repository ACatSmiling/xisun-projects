package cn.xisun.kafka.consumer.properties;

import java.util.Properties;

/**
 * @author XiSun
 * @Date 2020/8/24 11:17
 * <p>
 * Kafka消费者连接信息
 */
public class ConsumerProperties {
    public static Properties getProps() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.71:9092");
        // 所属消费者组
        props.put("group.id", "test_3");
        // 如果其超时
        props.put("session.timeout.ms", 30000);
        // 开启自动提交
        props.put("enable.auto.commit", "true");
        // 自动提交时间
        props.put("auto.commit.interval.ms", "1000");
        // 从最早的offset开始拉取，latest:从最近的offset开始消费
        props.put("auto.offset.reset", "earliest");
        // 发送端id,便于统计
        props.put("client.id", "producer-syn-1");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }
}
