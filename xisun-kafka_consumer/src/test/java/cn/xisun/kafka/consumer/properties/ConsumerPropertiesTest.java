package cn.xisun.kafka.consumer.properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author XiSun
 * @Date 2020/8/24 11:23
 */
class ConsumerPropertiesTest {
    @Test
    void getProps() {
        Properties props = ConsumerProperties.getProps();
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            List<String> topics = new ArrayList<>();
            topics.add("patent_message_log");
            // 可以订阅多个topic
            consumer.subscribe(topics);
            // 拉取任务超时时间
            for (; ; ) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord consumerRecord : records) {
                    System.out.println("partition:" + consumerRecord.partition());
                    System.out.println("offset:" + consumerRecord.offset());
                    System.out.println("key:" + consumerRecord.key());
                    System.out.println("value:" + consumerRecord.value());
                }

                // 指定特定的partition和偏移量
                Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
                // 指定topic 和partition
                TopicPartition topicPartition = new TopicPartition("patent_message_log", 0);
                // 指定offset
                OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(8413);
                // 可以提交多个topic
                offsets.put(topicPartition, offsetAndMetadata);
                // 提交offset
                consumer.commitSync(offsets);
            }
        }
    }
}